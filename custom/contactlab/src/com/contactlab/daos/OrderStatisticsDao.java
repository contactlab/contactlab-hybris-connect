package com.contactlab.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;


/**
 * @author Techedge Group
 * 
 */
public interface OrderStatisticsDao
{

    List<Object> getCustomerLastOrderModel(CustomerModel customerModel);

    List<Double> getCustomerOrdersStatisticsOnAPeriod(CustomerModel customerModel, Date start, Date end);

    List<Double> getCustomerTotalOrdersStatistics(CustomerModel customerModel);

    List<Double> getCustomerAvgOrderStatistics(CustomerModel customerModel);
}
