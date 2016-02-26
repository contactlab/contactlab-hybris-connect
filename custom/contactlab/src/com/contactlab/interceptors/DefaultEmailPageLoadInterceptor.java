package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;

import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailPageLoadInterceptor implements LoadInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailPageLoadInterceptor.class);

    @Override
    public void onLoad(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailPageModel)
        {
            final EmailPageModel emailPage = (EmailPageModel) model;
            final EmailMessageType emailType = emailPage.getEmailMessageType();
            if (emailType == null)
            {
                emailPage.setEmailMessageType(EmailMessageType.DEFAULT);
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailPageModel ");
        }
    }
}
