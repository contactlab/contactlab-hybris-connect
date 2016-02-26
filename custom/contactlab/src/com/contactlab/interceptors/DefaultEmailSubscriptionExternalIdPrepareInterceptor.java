package com.contactlab.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import org.springframework.beans.factory.annotation.Required;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailSubscriptionExternalIdPrepareInterceptor implements PrepareInterceptor
{
    private PersistentKeyGenerator emailSubscriptionExternalIdGenerator;

    @Override
    public void onPrepare(final Object model, final InterceptorContext interceptorContext) throws InterceptorException
    {
        if (model instanceof EmailSubscriptionModel)
        {
            final EmailSubscriptionModel emailSubscription = (EmailSubscriptionModel) model;

            if (interceptorContext.isNew(emailSubscription))
            {
                emailSubscription
                        .setExternalUserId(Integer.valueOf((String) getEmailSubscriptionExternalIdGenerator().generate()));
            }
        }
        else
        {
            throw new InterceptorException("Model is not Type of EmailSubscriptionModel ");
        }
    }

    protected PersistentKeyGenerator getEmailSubscriptionExternalIdGenerator()
    {
        return emailSubscriptionExternalIdGenerator;
    }

    @Required
    public void setEmailSubscriptionExternalIdGenerator(final PersistentKeyGenerator emailSubscriptionExternalIdGenerator)
    {
        this.emailSubscriptionExternalIdGenerator = emailSubscriptionExternalIdGenerator;
    }

}
