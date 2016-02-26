/**
 * 
 */
package com.contactlab.addon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contactlab.addon.controllers.ContactlabaddonControllerConstants;
import com.contactlab.addon.form.EmailSubscriptionForm;
import com.contactlab.addon.form.validation.SubscriptionValidator;
import com.contactlab.data.CampaignData;
import com.contactlab.facades.ContactlabEmailSubscriptionFacade;


/**
 * @author Techedge Group
 * 
 */

@Controller
@RequestMapping("/emailmarketing/subscriptionconfirm")
public class ContactLabSubscriptionConfirmController extends AbstractAddOnPageController
{

	private static final Logger LOG = Logger.getLogger(ContactLabSubscriptionConfirmController.class);


	@RequestMapping(method = RequestMethod.GET)
	public String show(final Model model) throws CMSItemNotFoundException
	{
		LOG.debug("Subscription successful");
		
		storeCmsPageInModel(model, getContentPageForLabelOrId("contactlabSubscriptionConfirm"));
		return ContactlabaddonControllerConstants.Views.Pages.Emailmarketing.SubscriptionConfirmPage;
	}


}
