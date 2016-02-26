package com.contactlab.services;

import java.util.List;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public interface EmailSubscriptionExportService
{

    int addSubscriptions(List<EmailSubscriptionModel> emailSubscriptionList);

    int removeSubscriptions(List<EmailSubscriptionModel> emailSubscriptionList);

    int updateSubscriptions(List<EmailSubscriptionModel> emailSubscriptionList);

}
