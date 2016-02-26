package com.contactlab.listeners;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class ContactlabPlaceOrderEventListener extends AbstractEventListener<OrderPlacedEvent>
{
    private static final Logger LOG = Logger.getLogger(ContactlabPlaceOrderEventListener.class);

    private ModelService modelService;

    @Override
    protected void onEvent(final OrderPlacedEvent orderPlacedEvent)
    {
        final UserModel userModel = orderPlacedEvent.getProcess().getOrder().getUser();

        if (userModel instanceof CustomerModel)
        {
            final CustomerModel customerModel = (CustomerModel) userModel;

            final Collection<EmailSubscriptionModel> customerSubscriptions = customerModel.getEmailSubscriptions();

            if (!customerSubscriptions.isEmpty())
            {
                // It is expected only one subscription for customer
                final EmailSubscriptionModel emailSubscriptionModel = customerSubscriptions.iterator().next();

                emailSubscriptionModel.setToBeExported(Boolean.TRUE);

                getModelService().save(emailSubscriptionModel);
            }
        }
    }

    protected ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}
