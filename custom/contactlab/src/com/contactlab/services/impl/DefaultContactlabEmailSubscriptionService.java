package com.contactlab.services.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.contactlab.daos.EmailSubscriptionDao;
import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.CampaignService;
import com.contactlab.services.ContactlabEmailSubscriptionService;
import com.contactlab.webservices.delegate.ContactlabWebserviceDelegate;
import com.contactlab.webservices.exception.ContactlabWebserviceException;


/**
 * @author Techedge Group
 *
 */
public class DefaultContactlabEmailSubscriptionService extends DefaultEmailSubscriptionService
        implements ContactlabEmailSubscriptionService
{
    protected static final Logger LOG = Logger.getLogger(DefaultContactlabEmailSubscriptionService.class);

    private ContactlabWebserviceDelegate contactlabWebserviceDelegate;

    private CampaignService campaignService;

    private EmailSubscriptionDao emailSubscriptionDao;

    @Override
    protected void setOptIn(final EmailSubscriptionModel emailSubscriptionModel, final boolean subscriptionStatus)
    {
        final CampaignModel defautlCampaignModel = getCampaignService().getDefaultCampaign(CampaignType.MARKETING);

        final Collection<CampaignModel> subscribedCampaigns = new ArrayList<CampaignModel>(emailSubscriptionModel.getCampaigns());

        if ((subscriptionStatus) && (!subscribedCampaigns.contains(defautlCampaignModel)))
        {
            try
            {
                if (subscribedCampaigns.add(defautlCampaignModel))
                {
                    emailSubscriptionModel.setCampaigns(subscribedCampaigns);
                    emailSubscriptionModel.setToBeExported(Boolean.TRUE);
                }
            }
            catch (final Exception e)
            {
                LOG.error("Generic exception while unsubscribing default campaign", e);
            }
        }
        else if ((!subscriptionStatus) && (subscribedCampaigns.contains(defautlCampaignModel)))
        {
            try
            {
                sendUnsubscriptionToContactlab(emailSubscriptionModel, defautlCampaignModel);

                if (subscribedCampaigns.remove(defautlCampaignModel))
                {
                    emailSubscriptionModel.setCampaigns(subscribedCampaigns);
                    emailSubscriptionModel.setToBeExported(Boolean.TRUE);
                }
            }
            catch (final ContactlabWebserviceException e)
            {
                LOG.error("Error while unsubscribing default campaing [" + defautlCampaignModel.getCampaignId() + " - "
                        + defautlCampaignModel.getCampaignName() + " - " + defautlCampaignModel.getWebFormCode()
                        + "] for email subscription [" + emailSubscriptionModel.getExternalUserId() + " - "
                        + emailSubscriptionModel.getExportedEmail() + "]", e);
            }
            catch (final Exception e)
            {
                LOG.error("Generic exception while unsubscribing default campaign", e);
            }
        }
    }

    @Override
    public boolean isCustomerSubscribed(final CustomerModel customerModel)
    {
        final CampaignModel defaultCampaign = getCampaignService().getDefaultCampaign(CampaignType.MARKETING);

        return isCustomerSubscribed(customerModel, defaultCampaign);
    }

    @Override
    public boolean unsubscribeEmailSubscriptionFromCampaign(final long emailSubscriptionExternalPkAsLong,
            final CampaignModel campaignModel)
    {
        final PK emailSubscriptionExternalPk = PK.fromLong(emailSubscriptionExternalPkAsLong);
        final List<EmailSubscriptionModel> emailSubscriptionModels = getEmailSubscriptionDao()
                .getEmailSubscriptionByPK(emailSubscriptionExternalPk);

        if (!emailSubscriptionModels.isEmpty())
        {
            final EmailSubscriptionModel emailSubscriptionModel = emailSubscriptionModels.get(0);
            final Collection<CampaignModel> subscribedCampaigns = emailSubscriptionModel.getCampaigns();

            if (!subscribedCampaigns.isEmpty() && subscribedCampaigns.contains(campaignModel))
            {
                try
                {
                    sendUnsubscriptionToContactlab(emailSubscriptionModel, campaignModel);
                    final Collection<CampaignModel> newSubscribedCampaigns = new ArrayList<CampaignModel>(subscribedCampaigns);
                    newSubscribedCampaigns.remove(campaignModel);
                    emailSubscriptionModel.setCampaigns(newSubscribedCampaigns);
                    emailSubscriptionModel.setToBeExported(Boolean.TRUE);
                    getModelService().save(emailSubscriptionModel);

                    return true;
                }
                catch (final Exception e)
                {
                    LOG.error(
                            "Error while unsubscribing [" + emailSubscriptionModel.getExportedEmail() + "] from campaign ["
                                    + campaignModel.getCampaignId().intValue() + " - " + campaignModel.getCampaignName() + "]",
                            e);
                }
            }
        }

        return false;
    }

    @Override
    public boolean isCustomerSubscribed(final CustomerModel customerModel, final CampaignModel campaignModel)
    {
        final Collection<EmailSubscriptionModel> subscriptions = customerModel.getEmailSubscriptions();

        if (CollectionUtils.isNotEmpty(subscriptions))
        {
            final EmailSubscriptionModel emailSubscription = subscriptions.iterator().next();
            final Collection<CampaignModel> subscribedCampaigns = emailSubscription.getCampaigns();

            if (CollectionUtils.isNotEmpty(subscribedCampaigns))
            {
                return subscribedCampaigns.contains(campaignModel);
            }
        }

        return false;
    }

    @Override
    public Collection<CampaignModel> getSubscribedCampaigns(final CustomerModel customerModel)
    {
        final Collection<EmailSubscriptionModel> subscriptions = customerModel.getEmailSubscriptions();

        if (CollectionUtils.isNotEmpty(subscriptions))
        {
            final EmailSubscriptionModel emailSubscription = subscriptions.iterator().next();
            final Collection<CampaignModel> subscribedCampaigns = emailSubscription.getCampaigns();

            if (CollectionUtils.isNotEmpty(subscribedCampaigns))
            {
                return subscribedCampaigns;
            }
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public void subscribeCustomer(final CustomerModel customerModel, final List<CampaignModel> subscribedCampaigns)
    {
        tryToCreateEmailSubscription(customerModel, subscribedCampaigns);
    }

    @Override
    public void subscribeCustomer(final String title, final String firstName, final String middleName, final String lastName,
            final String email, final String birthDate, final String gender, final String address, final String city,
            final String postalCode, final String district, final String region, final String country, final String phone,
            final String cellPhone, final String fax, final String company, final List<CampaignModel> subscribedCampaigns)
    {
        Assert.notNull(firstName, "Parameter firstname cannot be null");
        Assert.notNull(lastName, "Parameter lastname cannot be null");
        Assert.notNull(email, "Parameter email cannot be null");

        tryToCreateEmailSubscription(title, firstName, middleName, lastName, email, birthDate, gender, address, city, postalCode,
                district, region, country, phone, cellPhone, fax, company, subscribedCampaigns);
    }

    protected void tryToCreateEmailSubscription(final CustomerModel customerModel, final List<CampaignModel> subscribedCampaigns)
    {

        EmailSubscriptionModel emailSubscription = findEmailSubscriptionByEmail(customerModel.getContactEmail().trim());

        if (emailSubscription == null)
        {
            emailSubscription = getModelService().create(EmailSubscriptionModel.class);
            emailSubscription.setCurrentSubscribedEmail(customerModel.getContactEmail().trim());
        }

        emailSubscription.setCustomer(customerModel);

        emailSubscription = updateSubscriptions(emailSubscription, subscribedCampaigns);

        getModelService().save(emailSubscription);
    }

    protected void tryToCreateEmailSubscription(final String title, final String firstName, final String middleName,
            final String lastName, final String email, final String birthDate, final String gender, final String address,
            final String city, final String postalCode, final String district, final String region, final String country,
            final String phone, final String cellPhone, final String fax, final String company,
            final List<CampaignModel> subscribedCampaigns)
    {

        EmailSubscriptionModel emailSubscription = findEmailSubscriptionByEmail(email.trim());

        if (emailSubscription == null)
        {
            emailSubscription = getModelService().create(EmailSubscriptionModel.class);

            emailSubscription.setCurrentSubscribedEmail(email.trim());
            emailSubscription.setExportedTitle(title);
            emailSubscription.setExportedFirstname(firstName);
            emailSubscription.setExportedMiddleName(middleName);
            emailSubscription.setExportedLastname(lastName);

            emailSubscription.setExportedGender(gender);

            emailSubscription.setExportedBillingAddress(address);
            emailSubscription.setExportedBillingCity(city);
            emailSubscription.setExportedBillingPostalCode(postalCode);
            emailSubscription.setExportedBillingDistrict(district);
            emailSubscription.setExportedBillingRegion(region);
            emailSubscription.setExportedBillingCountry(country);
            emailSubscription.setExportedBillingPhone(phone);
            emailSubscription.setExportedBillingCellPhone(cellPhone);
            emailSubscription.setExportedBillingFax(fax);
            emailSubscription.setExportedBillingCompany(company);

            if (StringUtils.isNotBlank(birthDate))
            {
                try
                {
                    final Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).parse(birthDate);
                    emailSubscription.setExportedBirthDate(date);
                }
                catch (final ParseException e)
                {
                    LOG.error("Error parsing birthdate", e);
                }
            }
        }

        emailSubscription = updateSubscriptions(emailSubscription, subscribedCampaigns);
        getModelService().save(emailSubscription);
    }

    private EmailSubscriptionModel updateSubscriptions(final EmailSubscriptionModel emailSubscriptionModel,
            final List<CampaignModel> subscribedCampaignModels)
    {

        boolean changedSubscriptions = false;
        final Collection<CampaignModel> actualSubscribedCampaigns = new ArrayList<CampaignModel>();
        if (emailSubscriptionModel.getCampaigns() != null)
        {
            actualSubscribedCampaigns.addAll(emailSubscriptionModel.getCampaigns());
        }

        // Check for new subscriptions
        for (final CampaignModel campaignModel : subscribedCampaignModels)
        {
            if (!actualSubscribedCampaigns.contains(campaignModel))
            {
                actualSubscribedCampaigns.add(campaignModel);
                changedSubscriptions = true;
            }
        }

        // Check for unsubscriptions
        for (final CampaignModel campaignModel : actualSubscribedCampaigns)
        {
            if (!subscribedCampaignModels.contains(campaignModel))
            {
                try
                {
                    sendUnsubscriptionToContactlab(emailSubscriptionModel, campaignModel);
                    actualSubscribedCampaigns.remove(campaignModel);
                    changedSubscriptions = true;
                }
                catch (final ContactlabWebserviceException e)
                {
                    LOG.error(
                            "Error while unsubscribing [" + emailSubscriptionModel.getExportedEmail() + "] from campaign ["
                                    + campaignModel.getCampaignId().intValue() + " - " + campaignModel.getCampaignName() + "]",
                            e);
                }
            }
        }

        if (changedSubscriptions)
        {
            emailSubscriptionModel.setCampaigns(actualSubscribedCampaigns);
            emailSubscriptionModel.setToBeExported(Boolean.TRUE);
            emailSubscriptionModel.setOptIn(Boolean.TRUE);
            getModelService().save(emailSubscriptionModel);
        }

        return emailSubscriptionModel;
    }

    private void sendUnsubscriptionToContactlab(final EmailSubscriptionModel emailSubscription,
            final CampaignModel campaignToBeUnsubscribed) throws ContactlabWebserviceException
    {

        getContactlabWebserviceDelegate().unsubscribeSubscriberFromCampaign(emailSubscription.getExternalUserId().intValue(),
                campaignToBeUnsubscribed.getWebFormCode());
    }

    protected ContactlabWebserviceDelegate getContactlabWebserviceDelegate()
    {
        return contactlabWebserviceDelegate;
    }

    @Required
    public void setContactlabWebserviceDelegate(final ContactlabWebserviceDelegate contactlabWebserviceDelegate)
    {
        this.contactlabWebserviceDelegate = contactlabWebserviceDelegate;
    }

    protected CampaignService getCampaignService()
    {
        return campaignService;
    }

    @Required
    public void setCampaignService(final CampaignService campaignService)
    {
        this.campaignService = campaignService;
    }

    protected EmailSubscriptionDao getEmailSubscriptionDao()
    {
        return emailSubscriptionDao;
    }

    @Required
    public void setEmailSubscriptionDao(final EmailSubscriptionDao emailSubscriptionDao)
    {
        this.emailSubscriptionDao = emailSubscriptionDao;
    }
}
