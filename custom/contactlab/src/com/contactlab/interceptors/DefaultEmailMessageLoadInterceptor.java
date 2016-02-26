package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;

import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailMessageLoadInterceptor implements LoadInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailMessageLoadInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see de.hybris.platform.servicelayer.interceptor.LoadInterceptor#onLoad(java.lang.Object,
     * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
     */
    @Override
    public void onLoad(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailMessageModel)
        {
            final EmailMessageModel emailMessage = (EmailMessageModel) model;
            final EmailMessageType emailType = emailMessage.getEmailMessageType();
            if (emailType == null)
            {
                emailMessage.setEmailMessageType(EmailMessageType.DEFAULT);
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailMessageModel ");
        }
    }
}
