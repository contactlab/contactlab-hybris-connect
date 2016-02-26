package com.contactlab.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import com.contactlab.data.ExportProductData;
import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 *
 */
public class ExportProductConverter extends AbstractPopulatingConverter<EmailSubscriptionModel, ExportProductData>
{

    @Override
    protected ExportProductData createTarget()
    {
        return new ExportProductData();
    }

    @Override
    public void populate(final EmailSubscriptionModel source, final ExportProductData target)
    {
        //
    }

}
