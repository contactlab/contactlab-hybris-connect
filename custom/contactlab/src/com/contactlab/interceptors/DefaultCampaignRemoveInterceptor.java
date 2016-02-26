package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.contactlab.model.CampaignModel;
import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 */
public class DefaultCampaignRemoveInterceptor implements RemoveInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultCampaignRemoveInterceptor.class);

    @Override
    public void onRemove(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        LOG.info("REMOVE CampaignModel");
        if (model instanceof CampaignModel)
        {
            final CampaignModel campaign = (CampaignModel) model;
            final Collection<EmailMessageModel> emailMessages = campaign.getEmailMessages();
            final Collection<EmailPageModel> emailPages = campaign.getEmailPages();
            if ((CollectionUtils.isNotEmpty(emailPages)) && (CollectionUtils.isNotEmpty(emailMessages)))
            {
                throw new InterceptorException("EmailMessage associate a this campaign");
            }
            else
            {
                final Collection<EmailSubscriptionModel> emailSubscription = campaign.getEmailSubscriptions();
                if (emailSubscription != null && emailSubscription.size() > 0)
                {
                    throw new InterceptorException("EmailSubscriptionModel associate a this campaign");
                }
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of CampaignModel");
        }
    }
}
