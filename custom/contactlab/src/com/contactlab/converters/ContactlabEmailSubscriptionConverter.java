package com.contactlab.converters;

import de.hybris.platform.converters.impl.AbstractConverter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

import com.contactlab.data.ContactlabEmailSubscriptionData;
import com.contactlab.model.EmailSubscriptionModel;


/**
 * <<<<<<< HEAD
 * 
 * @author contactlabgroup.com =======
 * @author Techedge Group >>>>>>> 82eeef1e7a698ef82aa31292135efa6db031f7b6
 * 
 */
public class ContactlabEmailSubscriptionConverter
        extends AbstractConverter<EmailSubscriptionModel, ContactlabEmailSubscriptionData>
{

    private String dateFormat;

    @Override
    protected ContactlabEmailSubscriptionData createTarget()
    {
        return new ContactlabEmailSubscriptionData();
    }

    @Override
    public void populate(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        addExportedLastOrder(source, target);
        addExportedTotalOrder(source, target);
        addExportedAverage(source, target);
        addExportedCustomPeriod1(source, target);
        addExportedCustomPeriod2(source, target);
        addExportedCustomField(source, target);
    }

    private void addExportedCustomField(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        target.setExportedCustomField1(source.getCustomField1());
        target.setExportedCustomField2(source.getCustomField2());
        target.setExportedCustomField3(source.getCustomField3());
        target.setExportedCustomField4(source.getCustomField4());
        target.setExportedCustomField5(source.getCustomField5());
        target.setExportedCustomField6(source.getCustomField6());
        target.setExportedCustomField7(source.getCustomField7());
        target.setExportedCustomField8(source.getCustomField8());
        target.setExportedCustomField9(source.getCustomField9());
        target.setExportedCustomField10(source.getCustomField10());
    }

    /**
     * @param source
     * @param target
     */
    private void addExportedCustomPeriod1(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        final Double value = source.getExportedCustomPeriod1OrderValue();
        if (value != null)
        {
            target.setExportedCustomPeriod1OrderValue(value);
        }
        final Integer orderCount = source.getExportedCustomPeriod1OrderCount();
        if (orderCount != null)
        {
            target.setExportedCustomPeriod1OrderCount(orderCount);
        }
        final Integer itemsCount = source.getExportedCustomPeriod1OrderItemsCount();
        if (itemsCount != null)
        {
            target.setExportedCustomPeriod1OrderItemsCount(itemsCount);
        }
    }

    private void addExportedCustomPeriod2(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        final Double value = source.getExportedCustomPeriod2OrderValue();
        if (value != null)
        {
            target.setExportedCustomPeriod2OrderValue(value);
        }
        final Integer orderCount = source.getExportedCustomPeriod2OrderCount();
        if (orderCount != null)
        {
            target.setExportedCustomPeriod2OrderCount(orderCount);
        }
        final Integer itemsCount = source.getExportedCustomPeriod2OrderItemsCount();
        if (itemsCount != null)
        {
            target.setExportedCustomPeriod2OrderItemsCount(itemsCount);
        }
    }

    private void addExportedAverage(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        final Double average = source.getExportedAverageOrderValue();
        if (average != null)
        {
            target.setExportedAverageOrderValue(average);
        }
        final Double itemsPerOrder = source.getExportedAverageNumberOfItemsPerOrder();
        if (itemsPerOrder != null)
        {
            target.setExportedAverageNumberOfItemsPerOrder(itemsPerOrder);
        }
    }

    private void addExportedLastOrder(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        final Date date = source.getExportedLastOrderDate();
        if (date != null)
        {
            target.setExportedLastOrderDate(date);
        }
        final Double value = source.getExportedLastOrderValue();
        if (value != null)
        {
            target.setExportedLastOrderValue(value);
        }
        final Integer itemsCount = source.getExportedLastOrderItemsCount();
        if (itemsCount != null)
        {
            target.setExportedLastOrderItemsCount(itemsCount);
        }
    }

    private void addExportedTotalOrder(final EmailSubscriptionModel source, final ContactlabEmailSubscriptionData target)
    {
        final Double value = source.getExportedTotalOrderValue();
        if (value != null)
        {
            target.setExportedTotalOrderValue(value);
        }
        final Integer itemsCount = source.getExportedTotalOrderItemsCount();
        if (itemsCount != null)
        {
            target.setExportedTotalOrderItemsCount(itemsCount);
        }
        final Integer count = source.getExportedTotalOrderCount();
        if (count != null)
        {
            target.setExportedTotalOrderCount(count);
        }
    }

    public String getDateFormat()
    {
        return dateFormat;
    }

    @Required
    public void setDateFormat(final String dateFormat)
    {
        this.dateFormat = dateFormat;
    }

}
