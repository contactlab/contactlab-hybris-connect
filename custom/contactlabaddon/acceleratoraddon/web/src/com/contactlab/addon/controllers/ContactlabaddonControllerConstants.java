/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.contactlab.addon.controllers;


/**
 */
public interface ContactlabaddonControllerConstants
{
	// implement here controller constants used by this extension
	final String ADDON_PREFIX = "addon:/contactlabaddon/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{

		interface Pages
		{

			interface Emailmarketing
			{
				String UnsubscriptionResultPage = ADDON_PREFIX + "pages/contactlabUnsubscriptionResult";
				String SubscriptionConfirmPage = ADDON_PREFIX + "pages/contactlabSubscriptionConfirm";
				String SubscriptionLightbox = ADDON_PREFIX + "fragments/contactlabSubscriptionLightbox";
			}

			interface Account
			{
				String AccountNewsletterPage = ADDON_PREFIX + "pages/account/accountNewsletterPage";
				String AccountNewsletterEditPage = ADDON_PREFIX + "pages/account/accountNewsletterEditPage";
			}
		}
	}
}
