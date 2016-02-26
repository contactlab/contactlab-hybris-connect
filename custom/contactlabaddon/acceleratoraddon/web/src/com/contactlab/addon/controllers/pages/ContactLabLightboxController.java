/**
 * 
 */
package com.contactlab.addon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;

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
@RequestMapping("/emailmarketing/subscribe")
public class ContactLabLightboxController extends AbstractAddOnPageController
{

	private static final Logger LOG = Logger.getLogger(ContactLabLightboxController.class);

	private static final String EMAILMARKETING_SUBSCRIBE_SHOW = "/show";
	public static final String SUBSCRIBE_LIGTHBOX_CMS_PAGE = "contactlabUnsubscriptionResult";

	@Resource(name = "subscriptionValidator")
	private SubscriptionValidator subscriptionValidator;

	@Resource(name = "defaultContactlabEmailSubscriptionFacade")
	private ContactlabEmailSubscriptionFacade contactlabEmailSubscriptionFacade;


	@RequestMapping(method = RequestMethod.GET)
	public String show(final Model model)
	{
		LOG.debug("Show subscribe lightbox.");
		addCampaigns(model);

		final EmailSubscriptionForm emailsubscriptionForm = new EmailSubscriptionForm();
		model.addAttribute("emailSubscriptionForm", emailsubscriptionForm);

		return ContactlabaddonControllerConstants.Views.Pages.Emailmarketing.SubscriptionLightbox;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String subscribe(@ModelAttribute("emailSubscriptionForm") final EmailSubscriptionForm emailSubscriptionForm,
			final BindingResult bindingResult, final Model model)
	{

		LOG.debug("Add subscription");
		subscriptionValidator.validate(emailSubscriptionForm, bindingResult);

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			addCampaigns(model);
			model.addAttribute("subscriptionCompleted", Boolean.FALSE);
		}
		else
		{
			final String firstName = emailSubscriptionForm.getFirstName();
			final String lastName = emailSubscriptionForm.getLastName();
			final String email = emailSubscriptionForm.getEmail();
			final List<String> selectedCampaigns = emailSubscriptionForm.getSelectedCampaigns();

			LOG.debug("Creating email subscription...");
			LOG.debug("Name: " + firstName);
			LOG.debug("Surname: " + lastName);
			LOG.debug("Email: " + email);

			final StringBuilder builder = new StringBuilder();
			builder.append("Selected Campaigns: ");

			for (final String string : selectedCampaigns)
			{
				builder.append(string);
				builder.append(" - ");
			}
			LOG.debug(builder.toString());

			final String title = null;
			final String middleName = null;
			final String birthDate = null;
			final String gender = null;
			final String address = null;
			final String city = null;
			final String postalCode = null;
			final String district = null;
			final String region = null;
			final String country = null;
			final String phone = null;
			final String cellPhone = null;
			final String fax = null;
			final String company = null;

			contactlabEmailSubscriptionFacade.subscribeGuestCustomer(title, firstName, middleName, lastName, email, birthDate,
					gender, address, city, postalCode, district, region, country, phone, cellPhone, fax, company, selectedCampaigns);

			model.addAttribute("subscriptionCompleted", Boolean.TRUE);
		}
		return ContactlabaddonControllerConstants.Views.Pages.Emailmarketing.SubscriptionLightbox;
	}

	private void addCampaigns(final Model model)
	{
		final List<CampaignData> campaigns = contactlabEmailSubscriptionFacade.getMarketingCampaigns();
		model.addAttribute("campaigns", campaigns);
	}

}
