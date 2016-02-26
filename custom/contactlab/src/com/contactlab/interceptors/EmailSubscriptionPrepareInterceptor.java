package com.contactlab.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.time.TimeService;

import org.springframework.beans.factory.annotation.Required;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class EmailSubscriptionPrepareInterceptor implements PrepareInterceptor
{

    // private static final Logger LOG = Logger.getLogger(EmailSubscriptionContactAddressPrepareInterceptor.class);

    private TimeService timeService;

    @Override
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        // Works only for EmailSubscriptionModel
        if (model instanceof EmailSubscriptionModel)
        {

            final EmailSubscriptionModel emailSub = (EmailSubscriptionModel) model;

            if (emailSub.getFirstOptInTime() == null && emailSub.getOptIn())
            {

                emailSub.setFirstOptInTime(timeService.getCurrentTime());
            }
        }
        else
        {
            throw new InterceptorException("Model is not instance of AddressModel");
        }
    }

    protected TimeService getTimeService()
    {
        return timeService;
    }

    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }

}
