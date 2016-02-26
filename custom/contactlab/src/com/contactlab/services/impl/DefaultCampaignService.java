package com.contactlab.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.daos.CampaignDao;
import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.services.CampaignService;


/**
 * @author Techedge Group
 * 
 */
public class DefaultCampaignService implements CampaignService
{
    private static final Logger LOG = Logger.getLogger(DefaultCampaignService.class);

    private CampaignDao campaignDao;

    @Override
    public boolean isDefaultCampaignAlreadyPresent(final CampaignType campaignType)
    {
        final List<CampaignModel> defaultCampaignModels = getCampaignDao().getDefaultCampaign(campaignType);

        if (defaultCampaignModels.size() > 0)
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean isWebFormCodeAlreadyPresent(final CampaignModel campaignModel)
    {
        final List<CampaignModel> campaignModels = getCampaignDao().getCampaignByWebFormCode(campaignModel.getWebFormCode());

        for (final CampaignModel model : campaignModels)
        {
            // It is expected that the list contains only one item
            if (!model.equals(campaignModel))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isSubscriptionFieldAlreadyPresent(final CampaignModel campaignModel)
    {
        final List<CampaignModel> campaignModels = getCampaignDao()
                .getCampaignBySubscriptionField(campaignModel.getSubscriptionField(), campaignModel.getCampaignType());

        for (final CampaignModel model : campaignModels)
        {
            // It is expected that the list contains only one item
            if (!model.equals(campaignModel))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public CampaignModel getDefaultCampaign(final CampaignType campaignType)
    {
        final List<CampaignModel> defaultCampaignModels = getCampaignDao().getDefaultCampaign(campaignType);

        return (defaultCampaignModels.size() > 0) ? defaultCampaignModels.get(0) : null;
    }

    @Override
    public CampaignModel getCampaignByWebformCode(final String webFormCode)
    {
        final List<CampaignModel> campaignModels = getCampaignDao().getCampaignByWebFormCode(webFormCode);

        return (campaignModels.size() > 0) ? campaignModels.get(0) : null;
    }

    @Override
    public CampaignModel getCampaignByID(final String campaignID)
    {
        final List<CampaignModel> campaignModels = getCampaignDao().getCampaignByID(campaignID);

        return (campaignModels.size() > 0) ? campaignModels.get(0) : null;
    }

    @Override
    public List<CampaignModel> getCampaignsByType(final CampaignType campaignType)
    {
        return getCampaignDao().getCampaignsByType(campaignType);
    }

    protected CampaignDao getCampaignDao()
    {
        return campaignDao;
    }

    @Required
    public void setCampaignDao(final CampaignDao campaignDao)
    {
        this.campaignDao = campaignDao;
    }
}
