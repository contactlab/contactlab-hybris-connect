package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailPageInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailPageInitDefaultsInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor#onInitDefaults(java.lang.Object,
     * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
     */
    @Override
    public void onInitDefaults(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailPageModel)
        {
            final EmailPageModel emailPage = (EmailPageModel) model;

            emailPage.setEmailMessageType(EmailMessageType.DEFAULT);

        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailMessageModel ");
        }
    }
}
