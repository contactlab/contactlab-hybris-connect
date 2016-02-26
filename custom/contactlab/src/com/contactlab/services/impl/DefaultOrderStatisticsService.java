package com.contactlab.services.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.contactlab.daos.OrderStatisticsDao;
import com.contactlab.data.OrderStatisticsData;
import com.contactlab.services.OrderStatisticsService;


/**
 * @author Techedge Group
 * 
 */
public class DefaultOrderStatisticsService implements OrderStatisticsService
{
    private OrderStatisticsDao orderStatisticsDao;

    @Override
    public OrderStatisticsData getCustomerLastOrderModel(final CustomerModel customerModel)
    {
        final List<Object> lastOrderStatistics = getOrderStatisticsDao().getCustomerLastOrderModel(customerModel);

        final OrderStatisticsData orderStatisticsData = new OrderStatisticsData();
        if (!lastOrderStatistics.isEmpty())
        {
            orderStatisticsData.setValue((Double) lastOrderStatistics.get(0));
            orderStatisticsData.setDate((Date) lastOrderStatistics.get(1));
            orderStatisticsData.setItems((Double) lastOrderStatistics.get(2));
        }

        return orderStatisticsData;
    }

    @Override
    public OrderStatisticsData getCustomerOrdersStatisticsOnAPeriod(final CustomerModel customerModel, final Date start,
            final Date end)
    {
        final List<Double> orderStatistics = getOrderStatisticsDao().getCustomerOrdersStatisticsOnAPeriod(customerModel, start,
                end);

        final OrderStatisticsData orderStatisticsData = new OrderStatisticsData();
        if (!orderStatistics.isEmpty())
        {
            orderStatisticsData.setValue(orderStatistics.get(0));
            orderStatisticsData.setCount(new Integer(orderStatistics.get(1).intValue()));
            orderStatisticsData.setItems(orderStatistics.get(2));
        }

        return orderStatisticsData;
    }

    @Override
    public OrderStatisticsData getCustomerTotalOrdersStatistics(final CustomerModel customerModel)
    {
        final List<Double> orderStatistics = getOrderStatisticsDao().getCustomerTotalOrdersStatistics(customerModel);

        final OrderStatisticsData orderStatisticsData = new OrderStatisticsData();
        if (!orderStatistics.isEmpty())
        {
            orderStatisticsData.setValue(orderStatistics.get(0));
            orderStatisticsData.setCount(new Integer(orderStatistics.get(1).intValue()));
            orderStatisticsData.setItems(orderStatistics.get(2));
        }

        return orderStatisticsData;
    }

    @Override
    public OrderStatisticsData getCustomerAvgOrderStatistics(final CustomerModel customerModel)
    {
        final List<Double> orderStatistics = getOrderStatisticsDao().getCustomerAvgOrderStatistics(customerModel);

        final OrderStatisticsData orderStatisticsData = new OrderStatisticsData();
        if (!orderStatistics.isEmpty())
        {
            orderStatisticsData.setValue(orderStatistics.get(0));
            orderStatisticsData.setItems(orderStatistics.get(1));
        }

        return orderStatisticsData;
    }

    protected OrderStatisticsDao getOrderStatisticsDao()
    {
        return orderStatisticsDao;
    }

    @Required
    public void setOrderStatisticsDao(final OrderStatisticsDao orderStatisticsDao)
    {
        this.orderStatisticsDao = orderStatisticsDao;
    }

}
