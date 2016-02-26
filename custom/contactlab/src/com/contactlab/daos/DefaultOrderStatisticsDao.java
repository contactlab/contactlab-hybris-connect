package com.contactlab.daos;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * @author Techedge Group
 * 
 */
public class DefaultOrderStatisticsDao implements OrderStatisticsDao
{
    private static final Logger LOG = Logger.getLogger(DefaultOrderStatisticsDao.class);

    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<Object> getCustomerLastOrderModel(final CustomerModel customerModel)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT (CASE WHEN {o." + OrderModel.TOTALPRICE
                + "} IS NULL THEN 0 ELSE {o." + OrderModel.TOTALPRICE + "} END) AS totalValue, {o." + OrderModel.CREATIONTIME
                + "} AS date, sum({oe." + OrderEntryModel.QUANTITY + "}) AS totalValue  FROM {" + OrderModel._TYPECODE
                + " AS o LEFT JOIN " + OrderEntryModel._TYPECODE + " AS oe ON {oe." + OrderEntryModel.ORDER + "} = {o."
                + OrderModel.PK + "}} WHERE {o." + OrderModel.PK + "} IN ({{ SELECT max({ord." + OrderModel.PK + "}) FROM {"
                + OrderModel._TYPECODE + " AS ord} WHERE {ord." + OrderModel.USER + "} = ?" + OrderModel.USER
                + " }}) GROUP BY {o." + OrderModel.PK + "}");
        query.addQueryParameter(OrderModel.USER, customerModel);
        query.setResultClassList(Arrays.asList(Double.class, Date.class, Double.class));

        LOG.info(query.getQuery());

        final List<List<Object>> result = getFlexibleSearchService().<List<Object>> search(query).getResult();

        return (!result.isEmpty()) ? result.get(0) : Lists.<Object> newArrayList();
    }

    @Override
    public List<Double> getCustomerOrdersStatisticsOnAPeriod(final CustomerModel customerModel, final Date start, final Date end)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "SELECT sum(totorderentries.value) AS totalValue, count(totorderentries.orderPk) AS totalCount, sum(totorderentries.quantities) AS totQty FROM ({{ SELECT (CASE WHEN {o."
                        + OrderModel.TOTALPRICE + "} IS NULL THEN 0 ELSE {o." + OrderModel.TOTALPRICE + "} END) AS value, {o."
                        + OrderModel.PK + "} AS orderPk, sum({oe." + OrderEntryModel.QUANTITY + "}) AS quantities FROM {"
                        + OrderModel._TYPECODE + " AS o LEFT JOIN " + OrderEntryModel._TYPECODE + " AS oe ON {oe."
                        + OrderEntryModel.ORDER + "} = {o." + OrderModel.PK + "}} WHERE {o." + OrderModel.USER + "} = ?"
                        + OrderModel.USER + " AND {o." + OrderModel.CREATIONTIME + "} >= ?startDate AND {o."
                        + OrderModel.CREATIONTIME + "} < ?endDate GROUP BY {o." + OrderModel.PK + "} }}) AS totorderentries");
        query.addQueryParameter(OrderModel.USER, customerModel);
        query.addQueryParameter("startDate", start);
        query.addQueryParameter("endDate", end);
        query.setResultClassList(Arrays.asList(Double.class, Double.class, Double.class));

        LOG.info(query.getQuery());

        final List<List<Double>> result = getFlexibleSearchService().<List<Double>> search(query).getResult();

        return (!result.isEmpty()) ? result.get(0) : Lists.<Double> newArrayList();
    }

    @Override
    public List<Double> getCustomerTotalOrdersStatistics(final CustomerModel customerModel)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "SELECT sum(totalorders.value) AS totalValue, count(totalorders.orderPk) AS totalCount, sum(totalorders.quantities) AS totQty FROM ({{ SELECT (CASE WHEN {o."
                        + OrderModel.TOTALPRICE + "} IS NULL THEN 0 ELSE {o." + OrderModel.TOTALPRICE + "} END) AS value, {o."
                        + OrderModel.PK + "} AS orderPk, sum({oe." + OrderEntryModel.QUANTITY + "}) AS quantities FROM {"
                        + OrderModel._TYPECODE + " AS o LEFT JOIN " + OrderEntryModel._TYPECODE + " AS oe ON {oe."
                        + OrderEntryModel.ORDER + "} = {o." + OrderModel.PK + "}} WHERE {o." + OrderModel.USER + "} = ?"
                        + OrderModel.USER + " GROUP BY {o." + OrderModel.PK + "} }}) AS totalorders");
        query.addQueryParameter(OrderModel.USER, customerModel);
        query.setResultClassList(Arrays.asList(Double.class, Double.class, Double.class));

        LOG.info(query.getQuery());

        final List<List<Double>> result = getFlexibleSearchService().<List<Double>> search(query).getResult();

        return (!result.isEmpty()) ? result.get(0) : Lists.<Double> newArrayList();
    }

    @Override
    public List<Double> getCustomerAvgOrderStatistics(final CustomerModel customerModel)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "SELECT avg(totorderentries.totvalue) AS avgValue, avg(totorderentries.totquantities) AS avgItemsQty FROM ({{ SELECT (CASE WHEN {o."
                        + OrderModel.TOTALPRICE + "} IS NULL THEN 0 ELSE {o." + OrderModel.TOTALPRICE
                        + "} END) AS totvalue, sum({oe." + OrderEntryModel.QUANTITY + "}) AS totquantities FROM {"
                        + OrderModel._TYPECODE + " AS o LEFT JOIN " + OrderEntryModel._TYPECODE + " AS oe ON {oe."
                        + OrderEntryModel.ORDER + "} = {o." + OrderModel.PK + "}} WHERE {o." + OrderModel.USER + "} = ?"
                        + OrderModel.USER + " GROUP BY {o." + OrderModel.PK + "} }}) AS totorderentries");
        query.addQueryParameter(OrderModel.USER, customerModel);
        query.setResultClassList(Arrays.asList(Double.class, Double.class));

        LOG.info(query.getQuery());

        final List<List<Double>> result = getFlexibleSearchService().<List<Double>> search(query).getResult();

        return (!result.isEmpty()) ? result.get(0) : Lists.<Double> newArrayList();
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }

    @Required
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
