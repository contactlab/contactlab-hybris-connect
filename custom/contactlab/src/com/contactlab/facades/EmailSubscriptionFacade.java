package com.contactlab.facades;

import com.contactlab.data.SubscriptionData;


/**
 * @author Techedge Group
 * 
 */
public interface EmailSubscriptionFacade
{

    void subscribeUser(SubscriptionData subscriptionData);

    void subscribeCurrentUser();

    void unsubscribeCurrentUser();

    void unsubscribeByCustomerId(String customerID);

    boolean isCurrentUserSubscribed();

}
