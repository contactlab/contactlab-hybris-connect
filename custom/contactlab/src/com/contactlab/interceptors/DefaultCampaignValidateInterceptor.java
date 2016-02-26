package com.contactlab.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.log4j.Logger;

import com.contactlab.model.CampaignModel;
import com.contactlab.services.CampaignService;


/**
 * @author Techedge Group
 * 
 */
public class DefaultCampaignValidateInterceptor implements ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultCampaignValidateInterceptor.class);

    private CampaignService campaignService;

    @Override
    public void onValidate(final Object model, final InterceptorContext interceptorContext) throws InterceptorException
    {
        if (model instanceof CampaignModel)
        {
            final CampaignModel campaignModel = (CampaignModel) model;
            if (interceptorContext.isModified(campaignModel, "defaultOptIn"))
            {
                if (Boolean.TRUE.equals(campaignModel.getDefaultOptIn()))
                {
                    if (getCampaignService().isDefaultCampaignAlreadyPresent(campaignModel.getCampaignType()))
                    {
                        throw new InterceptorException("Another Campaign is set as default opt in!");
                    }
                }
            }

            if (getCampaignService().isWebFormCodeAlreadyPresent(campaignModel))
            {
                throw new InterceptorException("Web Form Code already in use!");
            }
            if (getCampaignService().isSubscriptionFieldAlreadyPresent(campaignModel))
            {
                throw new InterceptorException("Subscription Field already in use!");
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of CampaignModel ");
        }
    }

    public CampaignService getCampaignService()
    {
        return campaignService;
    }

    public void setCampaignService(final CampaignService campaignService)
    {
        this.campaignService = campaignService;
    }
}
