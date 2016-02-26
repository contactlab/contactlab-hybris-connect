package com.contactlab.services;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;

import com.contactlab.data.OrderStatisticsData;


/**
 * @author Techedge Group
 * 
 */
public interface OrderStatisticsService
{
    OrderStatisticsData getCustomerLastOrderModel(CustomerModel customerModel);

    OrderStatisticsData getCustomerOrdersStatisticsOnAPeriod(CustomerModel customerModel, Date start, Date end);

    OrderStatisticsData getCustomerTotalOrdersStatistics(CustomerModel customerModel);

    OrderStatisticsData getCustomerAvgOrderStatistics(CustomerModel customerModel);
}
