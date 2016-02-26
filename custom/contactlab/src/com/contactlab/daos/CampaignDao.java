package com.contactlab.daos;

import java.util.List;

import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;


/**
 * @author Techedge Group
 */
public interface CampaignDao
{

    public List<CampaignModel> getCampaignsByType(final CampaignType campaignType);

    public List<CampaignModel> getDefaultCampaign(final CampaignType campaignType);

    public List<CampaignModel> getCampaignByWebFormCode(final String webFormCode);

    public List<CampaignModel> getCampaignByID(final String campaignID);

    public List<CampaignModel> getCampaignBySubscriptionField(final String subscriptionField, final CampaignType campaignType);

}
