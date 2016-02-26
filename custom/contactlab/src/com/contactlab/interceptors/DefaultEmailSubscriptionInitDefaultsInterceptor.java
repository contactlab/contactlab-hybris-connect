package com.contactlab.interceptors;

import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.log4j.Logger;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailSubscriptionInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailSubscriptionInitDefaultsInterceptor.class);

    @Override
    public void onInitDefaults(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailSubscriptionModel)
        {
            final EmailSubscriptionModel emailSubscription = (EmailSubscriptionModel) model;

            emailSubscription.setToBeExported(Boolean.TRUE);
            emailSubscription.setOptIn(Boolean.FALSE);
        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailSubscriptionModel");
        }
    }
}
