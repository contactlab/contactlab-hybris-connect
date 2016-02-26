/**
 * 
 */
package com.contactlab.addon.form;

import java.util.List;


/**
 * @author Techedge Group
 * 
 */
public class EmailSubscriptionForm
{

	private String titleCode;
	private String firstName;
	private String lastName;
	private String email;
	private String emailConfirmation;
	private String middleName;
	private String birthDate;
	private String gender;
	private String address;
	private String city;
	private String postalCode;
	private String district;
	private String region;
	private String country;
	private String phone;
	private String cellPhone;
	private String fax;
	private String company;
	private List<String> selectedCampaigns;

	private boolean privacy;


	public String getTitleCode()
	{
		return titleCode;
	}

	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getEmailConfirmation()
	{
		return emailConfirmation;
	}

	public void setEmailConfirmation(final String emailConfirmation)
	{
		this.emailConfirmation = emailConfirmation;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(final String middleName)
	{
		this.middleName = middleName;
	}

	public String getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(final String birthDate)
	{
		this.birthDate = birthDate;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(final String gender)
	{
		this.gender = gender;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(final String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(final String district)
	{
		this.district = district;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(final String region)
	{
		this.region = region;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(final String country)
	{
		this.country = country;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	public String getCellPhone()
	{
		return cellPhone;
	}

	public void setCellPhone(final String cellPhone)
	{
		this.cellPhone = cellPhone;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(final String fax)
	{
		this.fax = fax;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(final String company)
	{
		this.company = company;
	}

	public List<String> getSelectedCampaigns()
	{
		return selectedCampaigns;
	}

	public void setSelectedCampaigns(final List<String> selectedCampaigns)
	{
		this.selectedCampaigns = selectedCampaigns;
	}

	public boolean isPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(final boolean privacy)
	{
		this.privacy = privacy;
	}


}
