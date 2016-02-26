package com.contactlab.facades;

import java.util.List;

import com.contactlab.data.CampaignData;


/**
 * @author Techedge Group
 * 
 */
public interface ContactlabEmailSubscriptionFacade
{
    public List<CampaignData> getMarketingCampaigns();

    public boolean subscribeGuestCustomer(final String title, final String firstName, final String middleName,
            final String lastName, final String email, final String birthDate, final String gender, final String address,
            final String city, final String postalCode, final String district, final String region, final String country,
            final String phone, final String cellPhone, final String fax, final String company,
            final List<String> subscribedCampaignIDs);

    boolean subscribeCustomer(final List<String> subscribedCampaignIDs);

    List<CampaignData> getSubscribedCampaigns();

    public boolean unsubscribeFromCampaign(String webFormCode, String emailSubscriptionPk);
}
