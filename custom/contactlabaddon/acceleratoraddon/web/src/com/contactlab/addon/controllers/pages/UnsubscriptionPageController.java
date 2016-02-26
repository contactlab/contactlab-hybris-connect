/**
 * 
 */
package com.contactlab.addon.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contactlab.facades.ContactlabEmailSubscriptionFacade;

import com.contactlab.addon.controllers.ContactlabaddonControllerConstants;

/**
 *  @author Techedge Group
 * 
 */
@Controller
//@Scope("tenant")
@RequestMapping("/emailmarketing/unsubscribe")
public class UnsubscriptionPageController extends AbstractAddOnPageController
{
	private static final Logger LOG = Logger.getLogger(UnsubscriptionPageController.class);
	public static final String EMAILMARKETING_UNSUBSCRIBE_PAGE = "/emailmarketing/unsubscribe";
	public static final String EMAILMARKETING_UNSUBSCRIBE_ERROR = "/error";
	public static final String EMAILMARKETING_UNSUBSCRIBE_SUCCESS = "/success";

	public static final String UNSUBSCRIPTION_RESULT_CMS_PAGE = "contactlabUnsubscriptionResult";

	@Resource(name = "defaultContactlabEmailSubscriptionFacade")
	private ContactlabEmailSubscriptionFacade contactlabEmailSubscriptionFacade;

	@RequestMapping(value = "/wfc/{webFormCode}/es/{emailSubscriptionExternalId}", method = RequestMethod.GET)
	public String unsubscribe(@PathVariable("webFormCode") final String webFormCode,
			@PathVariable("emailSubscriptionExternalId") final String emailSubscriptionPk, final Model model)
			throws CMSItemNotFoundException
	{
		LOG.debug("Trying to unsubscribe [" + emailSubscriptionPk + "] for [" + webFormCode + "]");
		if (getContactlabEmailSubscriptionFacade().unsubscribeFromCampaign(webFormCode, emailSubscriptionPk))
		{
			return REDIRECT_PREFIX + EMAILMARKETING_UNSUBSCRIBE_PAGE + EMAILMARKETING_UNSUBSCRIBE_SUCCESS;
		}

		return REDIRECT_PREFIX + EMAILMARKETING_UNSUBSCRIBE_PAGE + EMAILMARKETING_UNSUBSCRIBE_ERROR;
	}

	@RequestMapping(value = EMAILMARKETING_UNSUBSCRIBE_ERROR, method = RequestMethod.GET)
	public String error(final Model model) throws CMSItemNotFoundException
	{
		LOG.debug("NOT unsubscribed");
		model.addAttribute("unsubscriptionResultMessage", "The unsubscription could not be executed!");

		storeCmsPageInModel(model, getContentPageForLabelOrId(UNSUBSCRIPTION_RESULT_CMS_PAGE));
		return ContactlabaddonControllerConstants.Views.Pages.Emailmarketing.UnsubscriptionResultPage;
	}

	@RequestMapping(value = EMAILMARKETING_UNSUBSCRIBE_SUCCESS, method = RequestMethod.GET)
	public String unsubscribed(final Model model) throws CMSItemNotFoundException
	{
		LOG.debug("Subscribing errors!");
		model.addAttribute("unsubscriptionResultMessage", "Successfully unsubscribed!");

		storeCmsPageInModel(model, getContentPageForLabelOrId(UNSUBSCRIPTION_RESULT_CMS_PAGE));
		return ContactlabaddonControllerConstants.Views.Pages.Emailmarketing.UnsubscriptionResultPage;
	}

	protected ContactlabEmailSubscriptionFacade getContactlabEmailSubscriptionFacade()
	{
		return contactlabEmailSubscriptionFacade;
	}
}
