package com.contactlab.daos;

import de.hybris.platform.core.PK;

import java.util.List;

import com.contactlab.model.EmailSubscriptionModel;


//import com.contactlab.model.EmailSubscriptionModel;

/**
 * @author Techedge Group
 * 
 */
public interface EmailSubscriptionDao
{

    List<EmailSubscriptionModel> findEmailSubscriptionsToAdd();

    List<EmailSubscriptionModel> findEmailSubscriptionsToUpdate();

    List<EmailSubscriptionModel> findEmailSubscriptionsToRemove();

    List<EmailSubscriptionModel> findEmailSubscriptionsFull();

    List<EmailSubscriptionModel> getEmailSubscriptionByExternalId(int emailSubscriptionExternalId);

    List<EmailSubscriptionModel> getEmailSubscriptionByPK(PK emailSubscriptionPK);
}
