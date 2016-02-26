package com.contactlab.interceptors;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailPageValidateInterceptor implements ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(DefaultEmailPageValidateInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
     * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
     */
    @Override
    public void onValidate(final Object model, final InterceptorContext interceptorcontext) throws InterceptorException
    {
        if (model instanceof EmailPageModel)
        {
            final EmailPageModel emailPage = (EmailPageModel) model;
            final EmailMessageType emailType = emailPage.getEmailMessageType();
            if (emailType != null)
            {
                if (emailPage.getEmailMessageType().equals(EmailMessageType.CONTACTLAB_SMARTRELAY))
                {
                    if (emailPage.getCampaign() == null)
                    {
                        throw new InterceptorException("Model CampaignModel is NULL");
                    }
                }
            }
            else
            {
                throw new InterceptorException("EmailMessageType is NULL");
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailMessageModel ");
        }
    }

}
