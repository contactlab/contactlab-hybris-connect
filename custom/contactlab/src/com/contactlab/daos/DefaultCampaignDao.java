package com.contactlab.daos;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;


/**
 * @author Techedge Group
 * 
 */
public class DefaultCampaignDao implements CampaignDao
{
    private static final Logger LOG = Logger.getLogger(DefaultCampaignDao.class);

    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<CampaignModel> getCampaignsByType(final CampaignType campaignType)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("select {c.pk} from { Campaign as c } where {c."
                + CampaignModel.CAMPAIGNTYPE + "} = ?" + CampaignModel.CAMPAIGNTYPE);
        query.addQueryParameter(CampaignModel.CAMPAIGNTYPE, campaignType);

        return getFlexibleSearchService().<CampaignModel> search(query).getResult();
    }

    @Override
    public List<CampaignModel> getDefaultCampaign(final CampaignType campaignType)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "select {c.pk} from { Campaign as c } where {c." + CampaignModel.DEFAULTOPTIN + "} = true and {c."
                        + CampaignModel.CAMPAIGNTYPE + "} = ?" + CampaignModel.CAMPAIGNTYPE);
        query.addQueryParameter(CampaignModel.CAMPAIGNTYPE, campaignType);

        return getFlexibleSearchService().<CampaignModel> search(query).getResult();
    }

    @Override
    public List<CampaignModel> getCampaignByWebFormCode(final String webFormCode)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("select {c.pk} from { Campaign as c } where {c."
                + CampaignModel.WEBFORMCODE + "} = ?" + CampaignModel.WEBFORMCODE);
        query.addQueryParameter(CampaignModel.WEBFORMCODE, webFormCode);

        return getFlexibleSearchService().<CampaignModel> search(query).getResult();
    }

    @Override
    public List<CampaignModel> getCampaignByID(final String campaignID)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(
                "select {c.pk} from { Campaign as c } where {c." + CampaignModel.CAMPAIGNID + "} = ?" + CampaignModel.CAMPAIGNID);
        query.addQueryParameter(CampaignModel.CAMPAIGNID, campaignID);

        return getFlexibleSearchService().<CampaignModel> search(query).getResult();
    }

    @Override
    public List<CampaignModel> getCampaignBySubscriptionField(final String subscriptionField, final CampaignType campaignType)
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("select {c.pk} from { Campaign as c } where {c."
                + CampaignModel.SUBSCRIPTIONFIELD + "} = ?" + CampaignModel.SUBSCRIPTIONFIELD + " and {c."
                + CampaignModel.CAMPAIGNTYPE + "} = ?" + CampaignModel.CAMPAIGNTYPE);
        query.addQueryParameter(CampaignModel.SUBSCRIPTIONFIELD, subscriptionField);
        query.addQueryParameter(CampaignModel.CAMPAIGNTYPE, campaignType);

        return getFlexibleSearchService().<CampaignModel> search(query).getResult();
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
