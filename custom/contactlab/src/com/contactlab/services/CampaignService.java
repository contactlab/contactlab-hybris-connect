package com.contactlab.services;

import java.util.List;

import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;


/**
 * <<<<<<< HEAD
 * 
 * @author contactlabgroup.com =======
 * @author Techedge Group >>>>>>> 82eeef1e7a698ef82aa31292135efa6db031f7b6
 * 
 */
public interface CampaignService
{

    public boolean isDefaultCampaignAlreadyPresent(final CampaignType campaignType);

    public boolean isWebFormCodeAlreadyPresent(final CampaignModel campaignModel);

    public boolean isSubscriptionFieldAlreadyPresent(final CampaignModel campaignModel);

    public CampaignModel getDefaultCampaign(final CampaignType campaignType);

    public CampaignModel getCampaignByWebformCode(final String webFormCode);

    public CampaignModel getCampaignByID(final String campaignID);

    public List<CampaignModel> getCampaignsByType(final CampaignType campaignType);

}
