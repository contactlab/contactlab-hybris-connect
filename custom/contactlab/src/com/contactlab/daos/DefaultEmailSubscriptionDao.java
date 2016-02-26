package com.contactlab.daos;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 *
 */
public class DefaultEmailSubscriptionDao implements EmailSubscriptionDao
{

    private static final Logger LOG = Logger.getLogger(DefaultEmailSubscriptionDao.class);

    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<EmailSubscriptionModel> findEmailSubscriptionsToUpdate()
    {
        LOG.info("findEmailSubscriptionsToUpdate");

        final String query = "SELECT {es.pk} FROM { EmailSubscription AS es " + "LEFT JOIN Customer AS c ON {es.customer}={c.pk} "
                + "LEFT JOIN Address AS dsa ON {c.defaultShipmentAddress}={dsa.pk} "
                + "LEFT JOIN Address AS dpa ON {c.defaultPaymentAddress}={dpa.pk} } " + "WHERE {es.toBeExported} = true "
                + "OR ( ({es.lastExportTime} < {c.modifiedTime}) OR ({es.lastExportTime} < {dsa.modifiedTime}) OR ({es.lastExportTime} < {dpa.modifiedTime}) ) "
                + "OR ( {es.exportedEmail} != {es.currentSubscribedEmail} )";

        return getFlexibleSearchService().<EmailSubscriptionModel> search(query).getResult();
    }

    @Override
    public List<EmailSubscriptionModel> findEmailSubscriptionsToAdd()
    {
        return ListUtils.EMPTY_LIST;
    }

    @Override
    public List<EmailSubscriptionModel> findEmailSubscriptionsToRemove()
    {
        return ListUtils.EMPTY_LIST;
    }

    @Override
    public List<EmailSubscriptionModel> findEmailSubscriptionsFull()
    {
        LOG.info("findEmailSubscriptionsFull");

        String query = "select {es.pk} from { EmailSubscription as es " + "left join Customer as c on {es.customer}={c.pk} "
                + "left join Address as a on {c.defaultShipmentAddress}={a.pk} } " + "where {es.optIn} = 1 ";

        LOG.info("Query: " + query);

        return flexibleSearchService.<EmailSubscriptionModel> search(query).getResult();
    }

    @Override
    public List<EmailSubscriptionModel> getEmailSubscriptionByExternalId(int emailSubscriptionExternalId)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("select {es.pk} from { EmailSubscription as es} "
                + "where {es.externalUserId} = ?" + EmailSubscriptionModel.EXTERNALUSERID);
        query.addQueryParameter(EmailSubscriptionModel.EXTERNALUSERID, emailSubscriptionExternalId);

        LOG.info("Query: " + query.getQuery());

        return flexibleSearchService.<EmailSubscriptionModel> search(query).getResult();
    }

    @Override
    public List<EmailSubscriptionModel> getEmailSubscriptionByPK(PK emailSubscriptionPK)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("select {es.pk} from { EmailSubscription as es} " + "where {es."
                + EmailSubscriptionModel.PK + "} = ?" + EmailSubscriptionModel.PK);
        query.addQueryParameter(EmailSubscriptionModel.PK, emailSubscriptionPK);

        LOG.info("Query: " + query.getQuery());

        return flexibleSearchService.<EmailSubscriptionModel> search(query).getResult();
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }

    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }

}
