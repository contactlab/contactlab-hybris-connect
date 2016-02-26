package com.contactlab.services.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import com.contactlab.enums.EmailMessageType;


public class DefaultContactlabEmailGenerationService extends DefaultEmailGenerationService
{
    @Override
    public EmailMessageModel generate(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
            throws RuntimeException
    {
        final EmailMessageModel emailMessageModel = super.generate(businessProcessModel, emailPageModel);

        if (!EmailMessageType.DEFAULT.equals(emailPageModel.getEmailMessageType()))
        {
            emailMessageModel.setEmailMessageType(emailPageModel.getEmailMessageType());
            emailMessageModel.setCampaign(emailPageModel.getCampaign());
        }

        getModelService().save(emailMessageModel);

        return emailMessageModel;
    }
}
