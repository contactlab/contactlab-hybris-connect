package com.contactlab.webservices.delegate;

import com.contactlab.webservices.exception.ContactlabWebserviceException;

/**
 * 
 */

/**
 *  @author Techedge Group
 * 
 */
public interface ContactlabWebserviceDelegate {
    public void unsubscribeSubscriberFromCampaign(final int userId, final String campaignWebFormCode)
            throws ContactlabWebserviceException;
}
