
package com.contactlab.emailmarketing.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.impl.DefaultEmailSubscriptionService;


/**
 * @author contactlab
 *
 */
@UnitTest
public class EmailSubscriptionServiceTest
{
    final static Logger LOG = Logger.getLogger(EmailSubscriptionServiceTest.class);

    private DefaultEmailSubscriptionService fakeService;

    private UserService userService;

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;

    private CampaignModel defaultCampaign;

    @Before
    public void setup()
    {
        fakeService = new DefaultEmailSubscriptionService();
        userService = mock(UserService.class);
        modelService = mock(ModelService.class);
        flexibleSearchService = mock(FlexibleSearchService.class);
        fakeService.setUserService(userService);
        fakeService.setModelService(modelService);
        fakeService.setFlexibleSearchService(flexibleSearchService);
        defaultCampaign = getDefaultCampaign();
    }

    @Test
    public void testSubscribeCustomer1()
    {
        // create customer with a subscription
        CustomerModel customer = getCustomer();
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(false);
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        // before subscription optIn must be false
        assertTrue(!customer.getEmailSubscriptions().iterator().next().getOptIn());
        fakeService.subscribeCustomer(customer);
        // after subscription optIn must be true
        assertTrue(customer.getEmailSubscriptions().iterator().next().getOptIn());
    }

    @Test
    public void testUnsubscribeCustomer()
    {
        // create customer with a subscription
        CustomerModel customer = getCustomer();
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(true);
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        // before unsubscription optIn must be true
        assertTrue(customer.getEmailSubscriptions().iterator().next().getOptIn());
        fakeService.unsubscribeCustomer(customer);
        // after unsubscription optIn must be false
        assertTrue(!customer.getEmailSubscriptions().iterator().next().getOptIn());
    }

    @Test
    public void testUnsubscribe()
    {
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(true);
        // before unsubscription optIn must be true
        assertTrue(emailSubscription.getOptIn());
        fakeService.unsubscribe(emailSubscription, false);
        // after unsubscription optIn must be false
        assertTrue(!emailSubscription.getOptIn());
    }

    @Test
    public void testSubscribeCustomer2()
    {
        // create emailsubscription
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(false);
        final List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        SearchResult<EmailSubscriptionModel> searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions, 1, 1,
                0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        // before subscription optIn must be false
        assertTrue(!emailSubscription.getOptIn());
        fakeService.subscribeCustomer("test", "test", "test", "test", "test@test.it", "test", "test", "test", "test", "test",
                "test", "test", "test", "test", "test", "test", "test");
        // after subscription optIn must be true
        assertTrue(emailSubscription.getOptIn());
    }

    @Test
    public void testFindEmailSubscriptionByID()
    {
        // create customer with a subscription
        CustomerModel customer = getCustomer();
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(true);
        List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        // calling method must return 'emailSubscription'
        SearchResult<EmailSubscriptionModel> searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions, 1, 1,
                0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        assertEquals(fakeService.findEmailSubscriptionByID("test"), emailSubscription);
        // calling method must return null
        emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions, 1, 1, 0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        assertNull(fakeService.findEmailSubscriptionByID("test"));
    }

    @Test
    public void testIsCustomerSubscribed()
    {
        // create customer with a subscription
        CustomerModel customer = getCustomer();
        EmailSubscriptionModel emailSubscription = getDefaultEmailSubscription(true);
        List<EmailSubscriptionModel> emailSubscriptions = new ArrayList<EmailSubscriptionModel>();
        emailSubscriptions.add(emailSubscription);
        customer.setEmailSubscriptions(emailSubscriptions);
        SearchResult<EmailSubscriptionModel> searchResult = new SearchResultImpl<EmailSubscriptionModel>(emailSubscriptions, 1, 1,
                0);
        Mockito.when(flexibleSearchService.<EmailSubscriptionModel> search(Mockito.any(FlexibleSearchQuery.class)))
                .thenReturn(searchResult);
        // customer must be subscribed
        assertTrue(fakeService.isCustomerSubscribed(customer));
        emailSubscriptions.clear();
        emailSubscriptions.add(getDefaultEmailSubscription(false));
        customer.setEmailSubscriptions(emailSubscriptions);
        // customer must be unsubscribed
        assertTrue(!fakeService.isCustomerSubscribed(customer));
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

    private EmailSubscriptionModel getDefaultEmailSubscription(boolean optin)
    {
        final EmailSubscriptionModel emailSubscription = new EmailSubscriptionModel();
        final Collection<CampaignModel> campaigns = new ArrayList<CampaignModel>();
        campaigns.add(defaultCampaign);
        emailSubscription.setCampaigns(campaigns);
        emailSubscription.setCurrentSubscribedEmail("test@test.it");
        emailSubscription.setExternalUserId(Integer.valueOf(1));
        emailSubscription.setOptIn(optin);
        return emailSubscription;
    }

    private CustomerModel getCustomer()
    {
        final CustomerModel customer = new CustomerModel();
        customer.setName("name");
        customer.setUid("customerUid");
        return customer;
    }

}
