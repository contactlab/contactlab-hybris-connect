package com.contactlab.services.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.EmailSubscriptionService;


/**
 * @author Techedge Group
 *
 */
public class DefaultEmailSubscriptionService implements EmailSubscriptionService
{
    protected static final Logger LOG = Logger.getLogger(DefaultEmailSubscriptionService.class);

    final static String FIND_CUSTOMER_BY_CUSTOMERID = "SELECT {c:pk} FROM {Customer AS c} WHERE {c:customerid} = ?customerId";

    final static String FIND_EMAIL_SUBSCRIPTION_BY_EMAIL = "SELECT {pk} FROM {EmailSubscription} WHERE lower({currentSubscribedEmail}) = lower(?email)";

    final static String FIND_EMAILSUBSCRIPTION_BY_ID = "SELECT {c:pk} FROM {EMailSubscription AS c} WHERE lower({c:currentSubscribedEmail}) = lower(?customerId)";

    private UserService userService;

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;

    @Override
    public void subscribeCustomer(final CustomerModel customer)
    {
        if (customer != null)
        {
            internalSubscribeCustomer(customer, true);
        }
    }

    @Override
    public void unsubscribeCustomer(final CustomerModel customer)
    {
        if (customer != null)
        {
            internalSubscribeCustomer(customer, false);
        }
    }

    @Override
    public void unsubscribe(EmailSubscriptionModel emailSubscription, boolean force)
    {
        setOptIn(emailSubscription, false);
        if (force)
        {
            //
        }
        getModelService().save(emailSubscription);
    }

    @Override
    public void subscribeCustomer(final String title, final String firstName, final String middleName, final String lastName,
            final String email, final String birthDate, final String gender, final String address, String city,
            final String postalCode, final String district, final String region, final String country, final String phone,
            final String cellPhone, final String fax, final String company)
    {
        Assert.notNull(firstName, "Parameter firstname cannot be null");
        Assert.notNull(lastName, "Parameter lastname cannot be null");
        Assert.notNull(email, "Parameter email cannot be null");

        tryToCreateEmailSubscription(title, firstName, middleName, lastName, email, birthDate, gender, address, city, postalCode,
                district, region, country, phone, cellPhone, fax, company);
    }

    @Override
    public EmailSubscriptionModel findEmailSubscriptionByID(final String customerId)
    {
        ServicesUtil.validateParameterNotNull(customerId, "there is no customerId here!");

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_EMAILSUBSCRIPTION_BY_ID);
        fQuery.addQueryParameter("customerId", customerId);
        fQuery.setResultClassList(Collections.singletonList(CustomerModel.class));
        final SearchResult<EmailSubscriptionModel> result = getFlexibleSearchService().<EmailSubscriptionModel> search(fQuery);

        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    @Override
    public CustomerModel findCustomerByCustomerID(final String customerId)
    {
        ServicesUtil.validateParameterNotNull(customerId, "there is no customerId here!");

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_CUSTOMER_BY_CUSTOMERID);
        fQuery.addQueryParameter("customerId", customerId);
        fQuery.setResultClassList(Collections.singletonList(CustomerModel.class));
        final SearchResult<CustomerModel> result = getFlexibleSearchService().<CustomerModel> search(fQuery);

        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    protected EmailSubscriptionModel findEmailSubscriptionByEmail(final String email)
    {
        ServicesUtil.validateParameterNotNull(email, "there is no email here!");

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_EMAIL_SUBSCRIPTION_BY_EMAIL);
        fQuery.addQueryParameter("email", email.trim());
        fQuery.setResultClassList(Collections.singletonList(EmailSubscriptionModel.class));
        final SearchResult<EmailSubscriptionModel> result = getFlexibleSearchService().<EmailSubscriptionModel> search(fQuery);

        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    protected void internalSubscribeCustomer(final CustomerModel customerModel, final boolean subscription)
    {
        final Collection<EmailSubscriptionModel> subscriptions = customerModel.getEmailSubscriptions();

        if (subscriptions != null && !subscriptions.isEmpty())
        {
            EmailSubscriptionModel emailSubscription = subscriptions.iterator().next();
            setOptIn(emailSubscription, subscription);

            getModelService().save(emailSubscription);
        }
        else
        {
            tryToCreateEmailSubscription(customerModel, subscription);
        }
    }

    protected void tryToCreateEmailSubscription(final String title, final String firstName, final String middleName,
            final String lastName, final String email, final String birthDate, final String gender, final String address,
            String city, final String postalCode, final String district, final String region, final String country,
            final String phone, final String cellPhone, final String fax, final String company)
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
                    Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).parse(birthDate);
                    emailSubscription.setExportedBirthDate(date);
                }
                catch (ParseException e)
                {
                    LOG.error("Error parsing birthdate", e);
                }
            }
        }

        setOptIn(emailSubscription, true);
        getModelService().save(emailSubscription);
    }

    protected void tryToCreateEmailSubscription(final CustomerModel customerModel, final boolean subscription)
    {

        EmailSubscriptionModel emailSubscription = findEmailSubscriptionByEmail(customerModel.getContactEmail().trim());

        if (emailSubscription == null)
        {
            emailSubscription = getModelService().create(EmailSubscriptionModel.class);
            emailSubscription.setCurrentSubscribedEmail(customerModel.getContactEmail().trim());
        }

        emailSubscription.setCustomer(customerModel);
        setOptIn(emailSubscription, subscription);

        getModelService().save(emailSubscription);
    }

    protected void setOptIn(EmailSubscriptionModel emailSubscriptionModel, boolean subscriptionStatus)
    {
        emailSubscriptionModel.setOptIn(subscriptionStatus);
    }

    @Override
    public boolean isCustomerSubscribed(CustomerModel customerModel)
    {
        final Collection<EmailSubscriptionModel> subscriptions = customerModel.getEmailSubscriptions();

        if (subscriptions != null && !subscriptions.isEmpty())
        {
            EmailSubscriptionModel emailSubscription = subscriptions.iterator().next();
            return emailSubscription.getOptIn() != null ? emailSubscription.getOptIn().booleanValue() : false;
        }
        else
        {
            EmailSubscriptionModel emailSubscription = findEmailSubscriptionByEmail(customerModel.getContactEmail());
            if (emailSubscription != null)
            {
                emailSubscription.setCustomer(customerModel);
                getModelService().save(emailSubscription);

                return emailSubscription.getOptIn() != null ? emailSubscription.getOptIn().booleanValue() : false;
            }
        }

        return false;
    }

    protected UserService getUserService()
    {
        return userService;
    }

    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }

    protected ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }

    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
