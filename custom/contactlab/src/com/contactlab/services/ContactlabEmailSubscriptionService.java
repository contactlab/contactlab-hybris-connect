package com.contactlab.services;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Collection;
import java.util.List;

import com.contactlab.model.CampaignModel;


/**
 * @author Techedge Group
 * 
 */
public interface ContactlabEmailSubscriptionService
{
    void subscribeCustomer(CustomerModel customerModel, List<CampaignModel> subscribedCampaigns);

    void subscribeCustomer(String title, String firstName, String middleName, String lastName, String email, String birthDate,
            String gender, String address, String city, String postalCode, String district, String region, String country,
            String phone, String cellPhone, String fax, String company, List<CampaignModel> subscribedCampaigns);

    boolean isCustomerSubscribed(CustomerModel customerModel, CampaignModel campaignModel);

    Collection<CampaignModel> getSubscribedCampaigns(CustomerModel customerModel);

    boolean unsubscribeEmailSubscriptionFromCampaign(long emailSubscriptionPk, CampaignModel campaignModel);
}
