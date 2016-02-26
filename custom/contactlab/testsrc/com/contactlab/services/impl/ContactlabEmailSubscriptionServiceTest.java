package com.contactlab.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.contactlab.daos.EmailSubscriptionDao;
import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.CampaignService;
import com.contactlab.webservices.delegate.ContactlabWebserviceDelegate;


/**
 * @author contactlab
 *
 */
@UnitTest
public class ContactlabEmailSubscriptionServiceTest
{
    final static Logger LOG = Logger.getLogger(ContactlabEmailSubscriptionServiceTest.class);

    private ModelService modelService;

    private DefaultContactlabEmailSubscriptionService fakeService;

    private ContactlabWebserviceDelegate contactlabWebserviceDelegate;

    private CampaignService campaignService;

    private EmailSubscriptionDao emailSubscriptionDao;

    private CampaignModel defaultCampaign;

    private FlexibleSearchService flexibleSearchService;

    private DefaultEmailSubscriptionService emailSubscriptionService;

    private CustomerModel customerMock;

    @Before
    public void setup()
    {
        modelService = mock(ModelService.class);
        contactlabWebserviceDelegate = mock(ContactlabWebserviceDelegate.class);
        campaignService = mock(CampaignService.class);
        emailSubscriptionDao = mock(EmailSubscriptionDao.class);
        emailSubscriptionService = mock(DefaultEmailSubscriptionService.class);
        flexibleSearchService = mock(FlexibleSearchService.class);
        fakeService = new DefaultContactlabEmailSubscriptionService();
        fakeService.setModelService(modelService);
        fakeService.setContactlabWebserviceDelegate(contactlabWebserviceDelegate);
        fakeService.setCampaignService(campaignService);
        fakeService.setEmailSubscriptionDao(emailSubscriptionDao);
        fakeService.setFlexibleSearchService(flexibleSearchService);
        defaultCampaign = getDefaultCampaign();
        customerMock = mock(CustomerModel.class);
        Mockito.when(campaignService.getDefaultCampaign(Mockito.any(CampaignType.class))).thenReturn(defaultCampaign);
    }

    @Test
    public void testSetOptIn()
    {
        final EmailSubscriptionModel emailSubscription = getNewEmailSubscription();
        // before calling method emailSubscription must not contain defaultCampaign
        assertTrue(!emailSubscription.getCampaigns().contains(defaultCampaign));
        fakeService.setOptIn(emailSubscription, true);
        // after calling method with optIn=true emailSubscription must contain defaultCampaign
        assertTrue(emailSubscription.getCampaigns().contains(defaultCampaign));
        fakeService.setOptIn(emailSubscription, false);
        // after calling method with optIn=false emailSubscription must not contain defaultCampaign
        assertTrue(!emailSubscription.getCampaigns().contains(defaultCampaign));
    }

    @Test
    public void testIsCustomerSubscribed()
    {
        // create customer with a subscription
        final Collection<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        final CustomerModel customer = new CustomerModel();
        final EmailSubscriptionModel emailSubscription = getNewEmailSubscription();
        final CampaignModel campaign = emailSubscription.getCampaigns().iterator().next();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        // customer must be subscribed
        assertTrue(fakeService.isCustomerSubscribed(customer, campaign));
        // set another emailSubscription to customer (default campaign)
        emailSubscriptions.clear();
        emailSubscriptions.add(getDefaultEmailSubscription());
        customer.setEmailSubscriptions(emailSubscriptions);
        // customer must be subscribed (default campaign)
        assertTrue(fakeService.isCustomerSubscribed(customer)); // default campaign
    }

    @Test
    public void testUnsubscribeEmailSubscriptionFromCampaign()
    {
        // create emailSubscription
        final EmailSubscriptionModel emailSubscription = getNewEmailSubscription();
        final CampaignModel campaign = emailSubscription.getCampaigns().iterator().next();
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        Mockito.when(emailSubscriptionDao.getEmailSubscriptionByPK(Mockito.any(PK.class))).thenReturn(emailSubscriptions);
        final long subscriptionPK = 123;
        // before calling method emailSubscription must contain campaign
        assertTrue(emailSubscription.getCampaigns().contains(campaign));
        fakeService.unsubscribeEmailSubscriptionFromCampaign(subscriptionPK, campaign);
        // after calling method emailSubscription must not contain campaign
        assertTrue(!emailSubscription.getCampaigns().contains(campaign));
    }

    @Test
    public void testSubscribeCustomer1()
    {
        // create customer with a subscription
        final CustomerModel customer = getCustomerMock();
        final EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription();
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        // create a list of campaigns
        final List<CampaignModel> campaigns = (List<CampaignModel>) emailSubscription.getCampaigns();
        final SearchResult<EmailSubscriptionModel> searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions,
                1, 1, 0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        // mock customer because "contactEmail" is a dynamic attribute
        Mockito.when(customer.getContactEmail()).thenReturn("test@test.de");
        // before method call customer must not be subscribed
        assertNull(emailSubscription.getCustomer());
        fakeService.subscribeCustomer(customer, campaigns);
        // after method call customer must be subscribed
        assertEquals(emailSubscription.getCustomer(), customer);
    }

    @Test
    public void testSubscribeCustomer2()
    {
        // create emailsubscription
        final EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription();
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        // final List<CampaignModel> campaigns = (List<CampaignModel>) emailSubscription.getCampaigns();
        final List<CampaignModel> campaigns = new ArrayList<CampaignModel>();
        campaigns.add(getNewCampaign());

        final SearchResult<EmailSubscriptionModel> searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions,
                1, 1, 0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        // before adding/removing a campaign getToBeExported must be null
        assertNull(emailSubscription.getToBeExported());
        fakeService.subscribeCustomer("test", "test", "test", "test", "test@test.it", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test", campaigns);
        // after adding/removing a campaign getToBeExported must be true
        assertTrue(emailSubscription.getToBeExported().booleanValue());
    }

    @Test
    public void testGetSubscribedCampaigns()
    {
        // create model and emailSubscription
        final CustomerModel customer = getCustomer();
        final EmailSubscriptionModel emailSubscription = getNewEmailSubscription();
        final Collection<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        final Collection<CampaignModel> collection = fakeService.getSubscribedCampaigns(customer);
        final CampaignModel campaign = emailSubscription.getCampaigns().iterator().next();
        // method must return the subscribed campaign
        assertEquals(campaign, collection.iterator().next());
    }

    private CampaignModel getDefaultCampaign()
    {
        final CampaignModel campaign = new CampaignModel();
        campaign.setCampaignName("defaultCampaignName");
        campaign.setCampaignId(Integer.valueOf(1));
        campaign.setCampaignType(CampaignType.MARKETING);
        campaign.setDefaultOptIn(Boolean.valueOf(true));
        campaign.setWebFormCode("webFormCode1");
        campaign.setSubscriptionField("subscriptionField");
        return campaign;
    }

    private CampaignModel getNewCampaign()
    {
        final CampaignModel campaign = new CampaignModel();
        campaign.setCampaignName("campaignName");
        campaign.setCampaignId(Integer.valueOf(1));
        campaign.setCampaignType(CampaignType.MARKETING);
        campaign.setDefaultOptIn(Boolean.valueOf(true));
        campaign.setWebFormCode("webFormCode2");
        campaign.setSubscriptionField("subscriptionField");
        return campaign;
    }

    private EmailSubscriptionModel getNewEmailSubscription()
    {
        final EmailSubscriptionModel emailSubscription = new EmailSubscriptionModel();
        final Collection<CampaignModel> campaigns = new ArrayList<CampaignModel>();
        campaigns.add(getNewCampaign());
        emailSubscription.setCampaigns(campaigns);
        emailSubscription.setExternalUserId(Integer.valueOf(1));
        emailSubscription.setExportedEmail("test@test.de");
        return emailSubscription;
    }

    private EmailSubscriptionModel getDefaultEmailSubscription()
    {
        final EmailSubscriptionModel emailSubscription = new EmailSubscriptionModel();
        final Collection<CampaignModel> campaigns = new ArrayList<CampaignModel>();
        campaigns.add(defaultCampaign);
        emailSubscription.setCampaigns(campaigns);
        emailSubscription.setExternalUserId(Integer.valueOf(1));
        return emailSubscription;
    }

    private CustomerModel getCustomer()
    {
        final CustomerModel customer = new CustomerModel();
        customer.setName("name");
        customer.setUid("customerUid");
        return customer;
    }

    private CustomerModel getCustomerMock()
    {
        customerMock.setName("name");
        customerMock.setUid("customerUid");
        return customerMock;
    }

    private EmailSubscriptionModel getEmptyEmailSubscription()
    {
        final EmailSubscriptionModel emailSubscription = new EmailSubscriptionModel();
        emailSubscription.setExternalUserId(Integer.valueOf(1));
        return emailSubscription;
    }

    private void addCampaignToSubscription(final CampaignModel campaign, final EmailSubscriptionModel emailSubscription)
    {
        final Collection<CampaignModel> presentCampaigns = emailSubscription.getCampaigns();
        presentCampaigns.add(campaign);
        emailSubscription.setCampaigns(presentCampaigns);
    }
}
