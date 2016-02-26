/**
 * 
 */
package com.contactlab.addon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.yacceleratorstorefront.controllers.ControllerConstants;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.contactlab.addon.controllers.ContactlabaddonControllerConstants;
import com.contactlab.addon.form.EmailSubscriptionForm;
import com.contactlab.data.CampaignData;
import com.contactlab.facades.ContactlabEmailSubscriptionFacade;
import com.google.common.collect.Lists;


/**
 * @author Techedge Group
 * 
 */

@Controller
@RequestMapping("/my-account/newsletter")
public class ContactLabAccountPageController extends AbstractAddOnPageController
{

	// Internal Redirects
	private static final String REDIRECT_TO_PROFILE_PAGE = REDIRECT_PREFIX + "/my-account/newsletter";

	// CMS Pages
	private static final String NEWSLETTER_CMS_PAGE = "newsletter";
	private static final String UPDATE_NEWSLETTER_CMS_PAGE = "update-newsletter";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "emailSubscriptionFacade")
	private ContactlabEmailSubscriptionFacade contactlabEmailSubscriptionFacade;


	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String newsletter(final Model model) throws CMSItemNotFoundException
	{
		final List<CampaignData> campaigns = contactlabEmailSubscriptionFacade.getSubscribedCampaigns();
		final List<CampaignData> allCampaigns = contactlabEmailSubscriptionFacade.getMarketingCampaigns();
		model.addAttribute("campaigns", campaigns);
		model.addAttribute("allCampaigns", allCampaigns);

		storeCmsPageInModel(model, getContentPageForLabelOrId(NEWSLETTER_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NEWSLETTER_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.newsletter"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editNewsletter(final Model model) throws CMSItemNotFoundException
	{
		final List<CampaignData> subscribedCampaigns = contactlabEmailSubscriptionFacade.getSubscribedCampaigns();

		final List<String> selectedCampaigns = Lists.newArrayList();
		for (final CampaignData campaign : subscribedCampaigns)
		{
			selectedCampaigns.add(String.valueOf(campaign.getId()));
		}

		final List<CampaignData> campaigns = contactlabEmailSubscriptionFacade.getMarketingCampaigns();
		model.addAttribute("campaigns", campaigns);

		final EmailSubscriptionForm emailSubscriptionForm = new EmailSubscriptionForm();
		emailSubscriptionForm.setSelectedCampaigns(selectedCampaigns);
		model.addAttribute("emailSubscriptionForm", emailSubscriptionForm);


		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_NEWSLETTER_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_NEWSLETTER_CMS_PAGE));

		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.newsletter"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateNewsletter(final EmailSubscriptionForm emailSubscriptionForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{

		String returnAction = ContactlabaddonControllerConstants.Views.Pages.Account.AccountNewsletterEditPage;

		final List<String> subscribedCampaignIDs = emailSubscriptionForm.getSelectedCampaigns();

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
		}
		else
		{

			final boolean subscribed = contactlabEmailSubscriptionFacade.subscribeCustomer(subscribedCampaignIDs);
			if (subscribed)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.newsletter.confirmationUpdated",null);
				returnAction = REDIRECT_TO_PROFILE_PAGE;
			}
			else
			{
				GlobalMessages.addErrorMessage(model, "form.global.error");
			}

		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(NEWSLETTER_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NEWSLETTER_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.newsletter"));
		return returnAction;
	}

}
