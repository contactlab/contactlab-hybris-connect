package com.contactlab.daos;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author openmind
 */
public class DefaultContactlabCartDao implements ContactlabCartDao
{

    private static Logger log = LoggerFactory.getLogger(DefaultContactlabCartDao.class);

    private FlexibleSearchService flexibleSearchService;

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }

    public List<CartModel> searchAbandonedCarts(List<BaseStoreModel> stores, Integer hours, Integer notificationNumber,
            Boolean exportGuest)
    {

        String FIND_CONTACTLAB_ABANDONED_CART = "SELECT {c:pk} " + "FROM {Cart as c "
                + "JOIN Customer as u ON {c:user} = {u.pk} " + "} " + "WHERE {u:uid} <> ?user "
                + (exportGuest.booleanValue() ? "" : "AND {u:type} IS NULL ") + "AND {c:store} in ( ?store ) "
                + "AND {c:totalprice} > ?totalprice " + "AND ("
                + "((({c:contactlabAbandonedCartCounter} IS NULL AND ?notificationNumber = 1) OR {c:contactlabAbandonedCartCounter} = ?notificationNumber - 1) AND (({c:contactlabAbandonedCartDate} IS NULL AND {c:modifiedtime} < ?modifiedtime) OR ({c:contactlabAbandonedCartDate} < ?modifiedtime))) "
                + "OR ({c:contactlabAbandonedCartDate} IS NOT NULL AND {c:modifiedtime} > {c:contactlabAbandonedCartDate}) "
                + ") " + "ORDER BY {c:contactlabAbandonedCartDate} ASC, {c:modifiedtime} ASC";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -hours.intValue());

        Map<String, Object> params = new HashMap<>();
        params.put("store", stores);
        params.put("user", "anonymous");
        params.put("modifiedtime", calendar.getTime());
        params.put("notificationNumber", notificationNumber);
        params.put("totalprice", Double.valueOf(0d));

        return doSearch(FIND_CONTACTLAB_ABANDONED_CART, params, CartModel.class);
    }

    protected <T> List<T> doSearch(String query, Map<String, Object> params, Class<T> resultClass)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        if (params != null)
        {
            fQuery.addQueryParameters(params);
        }

        fQuery.setResultClassList(Collections.singletonList(resultClass));
        SearchResult<T> searchResult = flexibleSearchService.search(fQuery);
        return searchResult.getResult();
    }

}
