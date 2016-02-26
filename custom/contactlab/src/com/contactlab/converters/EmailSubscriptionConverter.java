package com.contactlab.converters;

import de.hybris.platform.converters.impl.AbstractConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.contactlab.data.SubscriptionData;
import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public class EmailSubscriptionConverter extends AbstractConverter<EmailSubscriptionModel, SubscriptionData>
{

    private String birthDateFormat;

    private String optInDateFormat;

    private Map<String, String> attributes;

    @Override
    protected SubscriptionData createTarget()
    {
        return new SubscriptionData();
    }

    @Override
    public void populate(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        Assert.notNull(source, "Parameter source cannot be null");

        addEmail(source, target);
        addFirtNameAndLastName(source, target);
        addBirthDate(source, target);

        addContactNumbers(source, target);
        addAddresses(source, target);
    }

    protected void addEmail(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        target.setEmail(source.getCurrentSubscribedEmail());
    }

    protected void addFirtNameAndLastName(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        final String middleName = StringUtils.isNotBlank(source.getMiddlename()) ? source.getMiddlename().trim() : "";

        target.setFirstName(source.getFirstname());
        target.setMiddleName(middleName);
        target.setLastName(source.getLastname());
    }

    protected void addContactNumbers(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        final String billingMobileNumber = StringUtils.isNotBlank(source.getExportedBillingCellPhone())
                ? source.getExportedBillingCellPhone().trim() : "";
        final String billingPhoneNumber = StringUtils.isNotBlank(source.getExportedBillingPhone())
                ? source.getExportedBillingPhone().trim() : "";
        final String billingFaxNumber = StringUtils.isNotBlank(source.getExportedBillingFax())
                ? source.getExportedBillingFax().trim() : "";

        final String shippingMobileNumber = StringUtils.isNotBlank(source.getExportedShippingCellPhone())
                ? source.getExportedShippingCellPhone().trim() : "";
        final String shippingPhoneNumber = StringUtils.isNotBlank(source.getExportedShippingPhone())
                ? source.getExportedShippingPhone().trim() : "";
        final String shippingFaxNumber = StringUtils.isNotBlank(source.getExportedShippingFax())
                ? source.getExportedShippingFax().trim() : "";

        target.setBillingCellPhone(billingMobileNumber);
        target.setBillingPhone(billingPhoneNumber);
        target.setBillingFax(billingFaxNumber);

        target.setShippingCellPhone(shippingMobileNumber);
        target.setShippingPhone(shippingPhoneNumber);
        target.setShippingFax(shippingFaxNumber);
    }

    protected void addBirthDate(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        final Date date = source.getBirthDate();
        if (date != null)
        {
            final String formattedDate = getFormattedDate(date, birthDateFormat);
            if (!formattedDate.isEmpty())
            {
                target.setBirthDate(formattedDate);
            }
        }
    }

    protected void addAddresses(final EmailSubscriptionModel source, final SubscriptionData target)
    {
        final String billingCountryCode = StringUtils.isNotBlank(source.getExportedBillingCountry())
                ? source.getExportedBillingCountry().trim() : "";
        final String billingAddress = StringUtils.isNotBlank(source.getExportedBillingAddress())
                ? source.getExportedBillingAddress().trim() : "";
        final String billingCity = StringUtils.isNotBlank(source.getExportedBillingCity())
                ? source.getExportedBillingCity().trim() : "";
        final String billingPostalCode = StringUtils.isNotBlank(source.getExportedBillingPostalCode())
                ? source.getExportedBillingPostalCode().trim() : "";
        final String billingDistrict = StringUtils.isNotBlank(source.getExportedBillingDistrict())
                ? source.getExportedBillingDistrict().trim() : "";
        final String billingRegion = StringUtils.isNotBlank(source.getExportedBillingRegion())
                ? source.getExportedBillingRegion().trim() : "";

        final String shippingCountryCode = StringUtils.isNotBlank(source.getExportedShippingCountry())
                ? source.getExportedShippingCountry().trim() : "";
        final String shippingAddress = StringUtils.isNotBlank(source.getExportedShippingAddress())
                ? source.getExportedShippingAddress().trim() : "";
        final String shippingCity = StringUtils.isNotBlank(source.getExportedShippingCity())
                ? source.getExportedShippingCity().trim() : "";
        final String shippingPostalCode = StringUtils.isNotBlank(source.getExportedShippingPostalCode())
                ? source.getExportedShippingPostalCode().trim() : "";
        final String shippingDistrict = StringUtils.isNotBlank(source.getExportedShippingDistrict())
                ? source.getExportedShippingDistrict().trim() : "";
        final String shippingRegion = StringUtils.isNotBlank(source.getExportedShippingRegion())
                ? source.getExportedShippingRegion().trim() : "";

        target.setBillingAddress(billingAddress);
        target.setBillingPostalCode(billingPostalCode);
        target.setBillingCity(billingCity);
        target.setBillingDistrict(billingDistrict);
        target.setBillingRegion(billingRegion);
        target.setBillingCountry(billingCountryCode);

        target.setShippingAddress(shippingAddress);
        target.setShippingPostalCode(shippingPostalCode);
        target.setShippingCity(shippingCity);
        target.setShippingDistrict(shippingDistrict);
        target.setShippingRegion(shippingRegion);
        target.setShippingCountry(shippingCountryCode);
    }

    protected String getFormattedDate(final Date date, final String format)
    {
        if (date != null && format != null && !format.isEmpty())
        {
            return new SimpleDateFormat(format).format(date);
        }
        else
        {
            return "";
        }
    }

    protected String getBirthDateFormat()
    {
        return birthDateFormat;
    }

    @Required
    public void setBirthDateFormat(String birthDateFormat)
    {
        this.birthDateFormat = birthDateFormat;
    }

    protected String getOptInDateFormat()
    {
        return optInDateFormat;
    }

    @Required
    public void setOptInDateFormat(String optInDateFormat)
    {
        this.optInDateFormat = optInDateFormat;
    }
}
