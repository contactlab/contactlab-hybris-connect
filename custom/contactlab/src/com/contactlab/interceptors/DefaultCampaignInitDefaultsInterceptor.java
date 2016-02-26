package com.contactlab.interceptors;

import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.services.CampaignService;


/**
 * @author Techedge Group
 * 
 */
public class DefaultCampaignInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultCampaignInitDefaultsInterceptor.class);

    private CampaignService campaignService;

    @Override
    public void onInitDefaults(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof CampaignModel)
        {
            final CampaignModel campaign = (CampaignModel) model;

            final boolean marketingDefaultCampaignPresent = campaignService
                    .isDefaultCampaignAlreadyPresent(CampaignType.MARKETING);
            if (!marketingDefaultCampaignPresent)
            {
                campaign.setDefaultOptIn(Boolean.TRUE);
            }

        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailMessageModel ");
        }
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
}
