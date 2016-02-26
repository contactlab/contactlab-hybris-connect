/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.contactlab.addon.form.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.contactlab.addon.form.EmailSubscriptionForm;


/**
 * Validator for email subscription forms.
 */
@Component("subscriptionValidator")
public class SubscriptionValidator implements Validator
{
	
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return EmailSubscriptionForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final EmailSubscriptionForm subscriptionForm = (EmailSubscriptionForm) object;


		final String firstName = subscriptionForm.getFirstName();
		final String lastName = subscriptionForm.getLastName();
		final String email = subscriptionForm.getEmail();
		final List<String> selectedCampaigns = subscriptionForm.getSelectedCampaigns();
		final boolean privacy = subscriptionForm.isPrivacy();
		
		/*
		 * Fields actually unused
		 */
		//final String emailConfirmation = subscriptionForm.getEmailConfirmation();
		// final String title = profileForm.getTitleCode();
		// final String middleName = profileForm.getMiddleName();
		// final String birthDate = profileForm.getBirthDate();
		// final String gender = profileForm.getGender();
		// final String address = profileForm.getAddress();
		// final String city = profileForm.getCity();
		//	final String postalCode = profileForm.getPostalCode();
		//	final String district = profileForm.getDistrict();
		//	final String region = profileForm.getRegion();
		//	final String country = profileForm.getCountry();
		//	final String phone = profileForm.getPhone();
		// final String cellPhone = profileForm.getCellPhone();
		//	final String fax = profileForm.getFax();
		//	final String company = profileForm.getCompany();

		if (!privacy)
		{
			errors.rejectValue("privacy", "newsletter.privacy.invalid");
		}

		if (StringUtils.isBlank(firstName))
		{
			errors.rejectValue("firstName", "newsletter.firstName.invalid");
		}
		else if (StringUtils.length(firstName) > 255)
		{
			errors.rejectValue("firstName", "newsletter.firstName.invalid");
		}

		if (StringUtils.isBlank(lastName))
		{
			errors.rejectValue("lastName", "newsletter.lastName.invalid");
		}
		else if (StringUtils.length(lastName) > 255)
		{
			errors.rejectValue("lastName", "newsletter.lastName.invalid");
		}

		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "newsletter.email.invalid");
		}
		else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "newsletter.email.invalid");
		}

		if (selectedCampaigns == null || selectedCampaigns.isEmpty())
		{
			errors.rejectValue("selectedCampaigns", "newsletter.selectedCampaigns.invalid");
		}

	}
	
	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
