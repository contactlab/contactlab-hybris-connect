package com.contactlab.facades;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.converters.CampaignConverter;
import com.contactlab.data.CampaignData;
import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.services.CampaignService;
import com.contactlab.services.ContactlabEmailSubscriptionService;
import com.google.common.collect.Lists;


/**
 * @author Techedge Group
 *
 */
public class DefaultContactlabEmailSubscriptionFacade extends DefaultEmailSubscriptionFacade
        implements ContactlabEmailSubscriptionFacade
{

    protected static final Logger LOG = Logger.getLogger(DefaultContactlabEmailSubscriptionFacade.class);

    private CampaignService campaignService;

    private CampaignConverter campaignConverter;

    private ContactlabEmailSubscriptionService contactlabEmailSubscriptionService;

    @Override
    public List<CampaignData> getMarketingCampaigns()
    {
        final List<CampaignModel> campaignModels = getCampaignService().getCampaignsByType(CampaignType.MARKETING);

        final List<CampaignData> campaignsData = Converters.convertAll(campaignModels, getCampaignConverter());

        return campaignsData;
    }

    @Override
    public boolean unsubscribeFromCampaign(final String webFormCode, final String emailSubscriptionPkAsString)
    {
        try
        {
            final long emailSubscriptionPk = Long.parseLong(emailSubscriptionPkAsString);

            final CampaignModel campaignModel = getCampaignService().getCampaignByWebformCode(webFormCode);
            if (getContactlabEmailSubscriptionService().unsubscribeEmailSubscriptionFromCampaign(emailSubscriptionPk,
                    campaignModel))
            {
                return true;
            }
        }
        catch (final NumberFormatException nfe)
        {
            LOG.error("Email Subscription PK is not valid", nfe);
        }

        return false;
    }

    @Override
    public boolean subscribeGuestCustomer(final String title, final String firstName, final String middleName,
            final String lastName, final String email, final String birthDate, final String gender, final String address,
            final String city, final String postalCode, final String district, final String region, final String country,
            final String phone, final String cellPhone, final String fax, final String company,
            final List<String> subscribedCampaignIDs)
    {

        final List<CampaignModel> subscribedCampaigns = Lists.newArrayList();
        for (final String campaignID : subscribedCampaignIDs)
        {
            final CampaignModel campaign = getCampaignService().getCampaignByID(campaignID);
            if (campaign != null)
            {
                subscribedCampaigns.add(campaign);
                LOG.info("Campaign found for id " + campaignID);
            }
            else
            {
                LOG.info("No campaign found for id " + campaignID);
            }
        }

        getContactlabEmailSubscriptionService().subscribeCustomer(title, firstName, middleName, lastName, email, birthDate,
                gender, address, city, postalCode, district, region, country, phone, cellPhone, fax, company,
                subscribedCampaigns);
        return true;

    }

    @Override
    public boolean subscribeCustomer(final List<String> subscribedCampaignIDs)
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        final List<CampaignModel> subscribedCampaigns = Lists.newArrayList();

        if (subscribedCampaignIDs != null && !subscribedCampaignIDs.isEmpty())
        {
            for (final String campaignID : subscribedCampaignIDs)
            {
                final CampaignModel campaign = getCampaignService().getCampaignByID(campaignID);
                if (campaign != null)
                {
                    subscribedCampaigns.add(campaign);
                }
            }
        }

        if (currentUser instanceof CustomerModel)
        {
            getContactlabEmailSubscriptionService().subscribeCustomer((CustomerModel) currentUser, subscribedCampaigns);
            return true;
        }

        LOG.error("Current user is not a Customer");
        return false;
    }

    @Override
    public List<CampaignData> getSubscribedCampaigns()
    {
        final List<CampaignModel> sourceList = Lists.newArrayList();
        final UserModel customer = getUserService().getCurrentUser();

        if (customer != null && customer instanceof CustomerModel && !getUserService().isAnonymousUser(customer))
        {
            sourceList.addAll(getContactlabEmailSubscriptionService().getSubscribedCampaigns((CustomerModel) customer));
        }

        return Converters.convertAll(sourceList, getCampaignConverter());
    }

    protected CampaignConverter getCampaignConverter()
    {
        return campaignConverter;
    }

    @Required
    public void setCampaignConverter(final CampaignConverter campaignConverter)
    {
        this.campaignConverter = campaignConverter;
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

    protected ContactlabEmailSubscriptionService getContactlabEmailSubscriptionService()
    {
        return contactlabEmailSubscriptionService;
    }

    @Required
    public void setContactlabEmailSubscriptionService(final ContactlabEmailSubscriptionService contactlabEmailSubscriptionService)
    {
        this.contactlabEmailSubscriptionService = contactlabEmailSubscriptionService;
    }

}
