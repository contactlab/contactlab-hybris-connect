package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailMessageInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailMessageInitDefaultsInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor#onInitDefaults(java.lang.Object,
     * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
     */
    @Override
    public void onInitDefaults(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailMessageModel)
        {
            final EmailMessageModel emailMessage = (EmailMessageModel) model;

            emailMessage.setEmailMessageType(EmailMessageType.DEFAULT);

        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailMessageModel ");
        }
    }
}
