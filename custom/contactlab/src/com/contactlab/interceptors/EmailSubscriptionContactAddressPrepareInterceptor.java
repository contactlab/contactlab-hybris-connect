package com.contactlab.interceptors;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class EmailSubscriptionContactAddressPrepareInterceptor implements PrepareInterceptor
{

    // private static final Logger LOG = Logger.getLogger(EmailSubscriptionContactAddressPrepareInterceptor.class);

    @Override
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        // Works only for AddressModel
        if (model instanceof AddressModel)
        {

            final AddressModel address = (AddressModel) model;

            if (ctx.isModified(address, AddressModel.EMAIL))
            {

                if (address.getOwner() instanceof CustomerModel)
                {

                    final CustomerModel customer = (CustomerModel) address.getOwner();
                    if (address.equals(customer.getDefaultShipmentAddress()))
                    {
                        if (customer.getEmailSubscriptions() != null && !customer.getEmailSubscriptions().isEmpty())
                        {

                            EmailSubscriptionModel emailSubscription = customer.getEmailSubscriptions().iterator().next();
                            if (emailSubscription != null && !emailSubscription.getCurrentSubscribedEmail().trim()
                                    .equals(customer.getDefaultShipmentAddress().getEmail().trim()))
                            {
                                emailSubscription
                                        .setCurrentSubscribedEmail(customer.getDefaultShipmentAddress().getEmail().trim());

                                ctx.getModelService().save(emailSubscription);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            throw new InterceptorException("Model is not instance of AddressModel");
        }
    }

}
