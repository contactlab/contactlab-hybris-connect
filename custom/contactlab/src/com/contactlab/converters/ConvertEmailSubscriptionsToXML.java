package com.contactlab.converters;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.data.CampaignSubscriptionStatus;
import com.contactlab.data.EmailSubscriptionXML;
import com.contactlab.data.EmailSubscriptionsXML;
import com.contactlab.data.OrderStatisticsData;
import com.contactlab.enums.CampaignType;
import com.contactlab.model.CampaignModel;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.CampaignService;
import com.contactlab.services.OrderStatisticsService;


/**
 * Class that converts a list of objects to a list of Strings using the Object's annotations.
 */
public class ConvertEmailSubscriptionsToXML implements Converter<List<EmailSubscriptionModel>, String>
{
    private static final Logger LOG = Logger.getLogger(ConvertEmailSubscriptionsToXML.class);

    private CampaignService campaignService;

    private OrderStatisticsService orderStatisticsService;

    private ConfigurationService configurationService;

    private String dateFormatPattern;

    @Override
    public String convert(final List<EmailSubscriptionModel> source, final String target) throws ConversionException
    {
        return convert(source);
    }

    @Override
    public String convert(final List<EmailSubscriptionModel> source) throws ConversionException
    {
        String retValue = "";
        if (source != null && (!source.isEmpty()))
        {
            try
            {
                retValue += this.getConvertObjectsToXML(source);
            }
            catch (final JAXBException e)
            {
                LOG.error("Error while converting email subscriptions to XML", e);
                throw new ConversionException(e.getMessage());
            }
        }
        return retValue;
    }

    protected String getConvertObjectsToXML(final List<EmailSubscriptionModel> mails) throws JAXBException
    {
        final EmailSubscriptionsXML emailSubscriptionsXML = getEmailSubscriptionsToConvert(mails);

        final StringBuffer retValue = new StringBuffer();
        final StringWriter writer = new StringWriter();

        // create JAXB context and initializing Marshaller
        final JAXBContext jaxbContext = JAXBContext.newInstance(EmailSubscriptionsXML.class);
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // for getting nice formatted output
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        jaxbMarshaller.marshal(emailSubscriptionsXML, writer);
        retValue.append(writer.getBuffer().toString());

        // return xmlFile;
        return retValue.toString();
    }

    private EmailSubscriptionsXML getEmailSubscriptionsToConvert(final List<EmailSubscriptionModel> mails)
    {
        final ArrayList<EmailSubscriptionXML> emailSubscriptionXMLList = new ArrayList<EmailSubscriptionXML>();

        final List<CampaignModel> marketingCampaigns = getCampaignService().getCampaignsByType(CampaignType.MARKETING);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getDateFormatPattern());

        final Date endDatePeriods = new Date();
        final Date startDatePriod1 = getEndDateByPeriod(1);
        final Date startDatePriod2 = getEndDateByPeriod(2);

        for (final EmailSubscriptionModel emailSubscription : mails)
        {
            final Collection<CampaignModel> subscribedCampaigns = emailSubscription.getCampaigns();

            final EmailSubscriptionXML emailSubscriptionXML = new EmailSubscriptionXML();

            // Set parameters
            emailSubscriptionXML.setEntityId(emailSubscription.getExternalUserId().intValue());
            emailSubscriptionXML.setUnsubscribeId(emailSubscription.getPk().getLongValueAsString());
            emailSubscriptionXML.setFirstName(emailSubscription.getFirstname());
            emailSubscriptionXML.setMiddleName(emailSubscription.getMiddlename());
            emailSubscriptionXML.setLastName(emailSubscription.getLastname());
            emailSubscriptionXML.setEmail(emailSubscription.getCurrentSubscribedEmail());
            String gender = "";
            if (emailSubscription.getGender() != null)
            {
                gender = emailSubscription.getGender().toString();
            }
            emailSubscriptionXML.setGender(gender);
            if (emailSubscription.getBirthDate() != null)
            {
                emailSubscriptionXML.setDayOfBirth(simpleDateFormat.format(emailSubscription.getBirthDate()));
            }

            if (emailSubscription.getCreationtime() != null)
            {
                emailSubscriptionXML.setCreatedAt(simpleDateFormat.format(emailSubscription.getCreationtime()));
            }
            int isCustomer = 0;
            String lang = "";
            final CustomerModel customerModel = emailSubscription.getCustomer();
            if (customerModel != null)
            {
                isCustomer = 1;
                lang = customerModel.getSessionLanguage().getName();
            }
            emailSubscriptionXML.setIsCustomer(isCustomer);
            emailSubscriptionXML.setLang(lang);

            // Billing Address
            emailSubscriptionXML.setBillingStreet(emailSubscription.getBillingAddress());
            emailSubscriptionXML.setBillingCity(emailSubscription.getBillingCity());
            emailSubscriptionXML.setBillingPostcode(emailSubscription.getBillingPostalcode());
            String billingRegion = "";
            if (StringUtils.isNotBlank(emailSubscription.getBillingRegion()))
            {
                billingRegion = emailSubscription.getBillingRegion();
            }
            else if (StringUtils.isNotBlank(emailSubscription.getBillingDistrict()))
            {
                billingRegion = emailSubscription.getBillingDistrict();
            }
            emailSubscriptionXML.setBillingRegion(billingRegion);
            String billingCountryIsoCode = "";
            if (emailSubscription.getBillingCountry() != null)
            {
                billingCountryIsoCode = emailSubscription.getBillingCountry().getIsocode();
            }
            emailSubscriptionXML.setBillingCountryId(billingCountryIsoCode);
            String billingTelephone = "";
            if (StringUtils.isNotBlank(emailSubscription.getBillingPhone()))
            {
                billingTelephone = emailSubscription.getBillingPhone();
            }
            else if (StringUtils.isNotBlank(emailSubscription.getBillingCellphone()))
            {
                billingTelephone = emailSubscription.getBillingCellphone();
            }
            emailSubscriptionXML.setBillingTelephone(billingTelephone);
            emailSubscriptionXML.setBillingFax(emailSubscription.getBillingFax());
            emailSubscriptionXML.setBillingCompany(emailSubscription.getBillingCompany());

            // Shipping Address
            emailSubscriptionXML.setShippingStreet(emailSubscription.getShippingAddress());
            emailSubscriptionXML.setShippingCity(emailSubscription.getShippingCity());
            emailSubscriptionXML.setShippingPostcode(emailSubscription.getShippingPostalcode());
            String shippingRegion = "";
            if (StringUtils.isNotBlank(emailSubscription.getShippingRegion()))
            {
                shippingRegion = emailSubscription.getShippingRegion();
            }
            else if (StringUtils.isNotBlank(emailSubscription.getShippingDistrict()))
            {
                shippingRegion = emailSubscription.getShippingDistrict();
            }
            emailSubscriptionXML.setShippingRegion(shippingRegion);
            String shippingCountryIsoCode = "";
            if (emailSubscription.getShippingCountry() != null)
            {
                shippingCountryIsoCode = emailSubscription.getShippingCountry().getIsocode();
            }
            emailSubscriptionXML.setShippingCountryId(shippingCountryIsoCode);
            String shippingTelephone = "";
            if (StringUtils.isNotBlank(emailSubscription.getShippingPhone()))
            {
                shippingTelephone = emailSubscription.getShippingPhone();
            }
            else if (StringUtils.isNotBlank(emailSubscription.getShippingCellphone()))
            {
                shippingTelephone = emailSubscription.getShippingCellphone();
            }
            emailSubscriptionXML.setShippingTelephone(shippingTelephone);
            emailSubscriptionXML.setShippingFax(emailSubscription.getShippingFax());
            emailSubscriptionXML.setShippingCompany(emailSubscription.getShippingCompany());

            // Aggregated data are set only if the subscription is associated to a Customer with orders
            if ((customerModel != null) && (!customerModel.getOrders().isEmpty()))
            {
                final OrderStatisticsData lastCustomerOrder = getOrderStatisticsService()
                        .getCustomerLastOrderModel(customerModel);

                emailSubscriptionXML.setLastOrderAmount(lastCustomerOrder.getValue());
                emailSubscription.setExportedLastOrderValue(lastCustomerOrder.getValue());
                if (lastCustomerOrder.getDate() != null)
                {
                    emailSubscriptionXML.setLastOrderDate(simpleDateFormat.format(lastCustomerOrder.getDate()));
                    emailSubscription.setExportedLastOrderDate(lastCustomerOrder.getDate());
                }
                emailSubscriptionXML.setLastOrderProducts(lastCustomerOrder.getItems().intValue());
                emailSubscription.setExportedLastOrderItemsCount(Integer.valueOf(lastCustomerOrder.getItems().intValue()));

                final OrderStatisticsData totalOrderStatistics = getOrderStatisticsService()
                        .getCustomerTotalOrdersStatistics(customerModel);
                emailSubscriptionXML.setTotalOrdersAmount(totalOrderStatistics.getValue());
                emailSubscription.setExportedTotalOrderValue(totalOrderStatistics.getValue());
                emailSubscriptionXML.setTotalOrdersProducts(totalOrderStatistics.getItems().intValue());
                emailSubscription.setExportedTotalOrderItemsCount(Integer.valueOf(totalOrderStatistics.getItems().intValue()));
                emailSubscriptionXML.setTotalOrdersCount(totalOrderStatistics.getCount().intValue());
                emailSubscription.setExportedTotalOrderCount(Integer.valueOf(totalOrderStatistics.getCount().intValue()));

                final OrderStatisticsData avgOrderStatistics = getOrderStatisticsService()
                        .getCustomerAvgOrderStatistics(customerModel);
                emailSubscriptionXML.setAvgOrdersAmount(avgOrderStatistics.getValue());
                emailSubscription.setExportedAverageOrderValue(avgOrderStatistics.getValue());
                emailSubscriptionXML.setAvgOrdersProducts(avgOrderStatistics.getItems());
                emailSubscription.setExportedAverageNumberOfItemsPerOrder(avgOrderStatistics.getItems());

                final OrderStatisticsData period1OrderStatistics = getOrderStatisticsService()
                        .getCustomerOrdersStatisticsOnAPeriod(customerModel, startDatePriod1, endDatePeriods);
                emailSubscriptionXML.setPeriod1Amount(period1OrderStatistics.getValue());
                emailSubscription.setExportedCustomPeriod1OrderValue(period1OrderStatistics.getValue());
                emailSubscriptionXML.setPeriod1Orders(period1OrderStatistics.getCount().intValue());
                emailSubscription
                        .setExportedCustomPeriod1OrderCount(Integer.valueOf(period1OrderStatistics.getCount().intValue()));
                emailSubscriptionXML.setPeriod1Products(period1OrderStatistics.getItems().intValue());
                emailSubscription
                        .setExportedCustomPeriod1OrderItemsCount(Integer.valueOf(period1OrderStatistics.getItems().intValue()));
                final OrderStatisticsData period2OrderStatistics = getOrderStatisticsService()
                        .getCustomerOrdersStatisticsOnAPeriod(customerModel, startDatePriod2, endDatePeriods);
                emailSubscriptionXML.setPeriod2Amount(period2OrderStatistics.getValue());
                emailSubscription.setExportedCustomPeriod2OrderValue(period2OrderStatistics.getValue());
                emailSubscriptionXML.setPeriod2Orders(period2OrderStatistics.getCount().intValue());
                emailSubscription
                        .setExportedCustomPeriod2OrderCount(Integer.valueOf(period2OrderStatistics.getCount().intValue()));
                emailSubscriptionXML.setPeriod2Products(period2OrderStatistics.getItems().intValue());
                emailSubscription
                        .setExportedCustomPeriod2OrderItemsCount(Integer.valueOf(period2OrderStatistics.getItems().intValue()));
            }

            emailSubscriptionXML.setCustomField1(emailSubscription.getCustomField1());
            emailSubscriptionXML.setCustomField2(emailSubscription.getCustomField2());
            emailSubscriptionXML.setCustomField3(emailSubscription.getCustomField3());
            emailSubscriptionXML.setCustomField4(emailSubscription.getCustomField4());
            emailSubscriptionXML.setCustomField5(emailSubscription.getCustomField5());
            emailSubscriptionXML.setCustomField6(emailSubscription.getCustomField6());
            emailSubscriptionXML.setCustomField7(emailSubscription.getCustomField7());
            emailSubscriptionXML.setCustomField8(emailSubscription.getCustomField8());
            emailSubscriptionXML.setCustomField9(emailSubscription.getCustomField9());
            emailSubscriptionXML.setCustomField10(emailSubscription.getCustomField10());

            final ArrayList<CampaignSubscriptionStatus> campaignSubscriptions = new ArrayList<CampaignSubscriptionStatus>();
            for (final CampaignModel marketingCampaign : marketingCampaigns)
            {
                // Set campaigns
                final CampaignSubscriptionStatus campaignSubscriptionStatus = new CampaignSubscriptionStatus();
                campaignSubscriptionStatus.setFieldTag(marketingCampaign.getSubscriptionField());
                int subscriptionStatus = -1;
                if (!CollectionUtils.isEmpty(subscribedCampaigns) && (subscribedCampaigns.contains(marketingCampaign)))
                {
                    subscriptionStatus = 1;
                }
                campaignSubscriptionStatus.setStatus(subscriptionStatus);

                campaignSubscriptions.add(campaignSubscriptionStatus);
            }
            emailSubscriptionXML.setCampaignSubscriptionStatuses(campaignSubscriptions);

            emailSubscriptionXMLList.add(emailSubscriptionXML);
        }

        final EmailSubscriptionsXML emailSubscriptionsXML = new EmailSubscriptionsXML();
        emailSubscriptionsXML.setEmailSubscriptions(emailSubscriptionXMLList);

        return emailSubscriptionsXML;
    }

    private Date getEndDateByPeriod(final int periodNumber)
    {
        final int periodIntervalDays = getConfigurationService().getConfiguration()
                .getInt("contactlab.customerperiod" + periodNumber + ".days");

        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.DATE, (-periodIntervalDays));

        return cal.getTime();
    }

    protected CampaignService getCampaignService()
    {
        return campaignService;
    }

    @Required
    public void setCampaignService(final CampaignService campaignService)
    {
        this.campaignService = campaignService;
    }

    protected String getDateFormatPattern()
    {
        return dateFormatPattern;
    }

    @Required
    public void setDateFormatPattern(final String dateFormatPattern)
    {
        this.dateFormatPattern = dateFormatPattern;
    }

    protected OrderStatisticsService getOrderStatisticsService()
    {
        return orderStatisticsService;
    }

    @Required
    public void setOrderStatisticsService(final OrderStatisticsService orderStatisticsService)
    {
        this.orderStatisticsService = orderStatisticsService;
    }

    protected ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    @Required
    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
