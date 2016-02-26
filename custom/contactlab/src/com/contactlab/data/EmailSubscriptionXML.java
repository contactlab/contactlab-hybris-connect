package com.contactlab.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.contactlab.converters.adapters.CampaignSubscriptionStatusAdapter;


/**
 * @author Techedge Group
 * 
 */
@XmlRootElement(name = "RECORD")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailSubscriptionXML
{
    @XmlAttribute(name = "ACTION")
    private final static String action = "U";

    @XmlElement(name = "entity_id", required = true)
    private int entityId; // Unique Id

    @XmlElement(name = "unsubscribe_id", required = true)
    private String unsubscribeId; // Unique Id

    @XmlElement(name = "prefix")
    private String prefix; //

    @XmlElement(name = "firstname")
    private String firstName; //

    @XmlElement(name = "middlename")
    private String middleName; //

    @XmlElement(name = "lastname")
    private String lastName; //

    @XmlElement(name = "suffix")
    private String suffix; //

    @XmlElement(name = "dob")
    private String dayOfBirth; //

    @XmlElement(name = "gender")
    private String gender; //

    @XmlElement(name = "email", required = true)
    private String email; //

    @XmlElement(name = "created_at", required = true)
    private String createdAt; //

    @XmlElement(name = "is_customer", required = true)
    private int isCustomer; //

    @XmlElement(name = "billing_country_id")
    private String billingCountryId; //

    @XmlElement(name = "billing_region")
    private String billingRegion; //

    @XmlElement(name = "billing_postcode")
    private String billingPostcode; //

    @XmlElement(name = "billing_city")
    private String billingCity; //

    @XmlElement(name = "billing_street")
    private String billingStreet; //

    @XmlElement(name = "billing_telephone")
    private String billingTelephone; //

    @XmlElement(name = "billing_fax")
    private String billingFax; //

    @XmlElement(name = "billing_company")
    private String billingCompany; //

    @XmlElement(name = "shipping_country_id")
    private String shippingCountryId; //

    @XmlElement(name = "shipping_region")
    private String shippingRegion; //

    @XmlElement(name = "shipping_postcode")
    private String shippingPostcode; //

    @XmlElement(name = "shipping_city")
    private String shippingCity; //

    @XmlElement(name = "shipping_street")
    private String shippingStreet; //

    @XmlElement(name = "shipping_telephone")
    private String shippingTelephone; //

    @XmlElement(name = "shipping_fax")
    private String shippingFax; //

    @XmlElement(name = "shipping_company")
    private String shippingCompany; //

    @XmlElement(name = "website_id")
    private int websiteId; //

    @XmlElement(name = "website_name")
    private String websiteName; //

    @XmlElement(name = "group_id")
    private int groupId; //

    @XmlElement(name = "group_name")
    private String groupName; //

    @XmlElement(name = "store_id")
    private int storeId; //

    @XmlElement(name = "store_name")
    private String storeName; //

    @XmlElement(name = "lang")
    private String lang; //

    @XmlElement(name = "LAST_ORDER_DATE")
    private String lastOrderDate; //

    @XmlElement(name = "LAST_ORDER_AMOUNT")
    private Double lastOrderAmount; //

    @XmlElement(name = "LAST_ORDER_PRODUCTS")
    private int lastOrderProducts; //

    @XmlElement(name = "TOTAL_ORDERS_AMOUNT")
    private Double totalOrdersAmount; //

    @XmlElement(name = "TOTAL_ORDERS_PRODUCTS")
    private int totalOrdersProducts; //

    @XmlElement(name = "TOTAL_ORDERS_COUNT")
    private int totalOrdersCount; //

    @XmlElement(name = "AVG_ORDERS_AMOUNT")
    private Double avgOrdersAmount; //

    @XmlElement(name = "AVG_ORDERS_PRODUCTS")
    private Double avgOrdersProducts; //

    @XmlElement(name = "PERIOD1_AMOUNT")
    private Double period1Amount; //

    @XmlElement(name = "PERIOD1_ORDERS")
    private int period1Orders; //

    @XmlElement(name = "PERIOD1_PRODUCTS")
    private int period1Products; //

    @XmlElement(name = "PERIOD2_AMOUNT")
    private Double period2Amount; //

    @XmlElement(name = "PERIOD2_PRODUCTS")
    private int period2Products; //

    @XmlElement(name = "PERIOD2_ORDERS")
    private int period2Orders; //

    @XmlElement(name = "CST_1")
    private String customField1; //

    @XmlElement(name = "CST_2")
    private String customField2; //

    @XmlElement(name = "CST_3")
    private String customField3; //

    @XmlElement(name = "CST_4")
    private String customField4; //

    @XmlElement(name = "CST_5")
    private String customField5; //

    @XmlElement(name = "CST_6")
    private String customField6; //

    @XmlElement(name = "CST_7")
    private String customField7; //

    @XmlElement(name = "CST_8")
    private String customField8; //

    @XmlElement(name = "CST_9")
    private String customField9; //

    @XmlElement(name = "CST_10")
    private String customField10; //

    @XmlAnyElement
    @XmlJavaTypeAdapter(CampaignSubscriptionStatusAdapter.class)
    private List<CampaignSubscriptionStatus> campaignSubscriptionStatuses;

    public static String getAction()
    {
        return action;
    }

    public List<CampaignSubscriptionStatus> getCampaignSubscriptionStatuses()
    {
        return campaignSubscriptionStatuses;
    }

    public void setCampaignSubscriptionStatuses(final List<CampaignSubscriptionStatus> campaignSubscriptionStatuses)
    {
        this.campaignSubscriptionStatuses = campaignSubscriptionStatuses;
    }

    public int getEntityId()
    {
        return entityId;
    }

    public void setEntityId(final int entityId)
    {
        this.entityId = entityId;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(final String prefix)
    {
        this.prefix = prefix;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(final String middleName)
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(final String suffix)
    {
        this.suffix = suffix;
    }

    public String getDayOfBirth()
    {
        return dayOfBirth;
    }

    public void setDayOfBirth(final String dayOfBirth)
    {
        this.dayOfBirth = dayOfBirth;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(final String gender)
    {
        this.gender = gender;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt)
    {
        this.createdAt = createdAt;
    }

    public int getIsCustomer()
    {
        return isCustomer;
    }

    public void setIsCustomer(final int isCustomer)
    {
        this.isCustomer = isCustomer;
    }

    public String getBillingCountryId()
    {
        return billingCountryId;
    }

    public void setBillingCountryId(final String billingCountryId)
    {
        this.billingCountryId = billingCountryId;
    }

    public String getBillingRegion()
    {
        return billingRegion;
    }

    public void setBillingRegion(final String billingRegion)
    {
        this.billingRegion = billingRegion;
    }

    public String getBillingPostcode()
    {
        return billingPostcode;
    }

    public void setBillingPostcode(final String billingPostcode)
    {
        this.billingPostcode = billingPostcode;
    }

    public String getBillingCity()
    {
        return billingCity;
    }

    public void setBillingCity(final String billingCity)
    {
        this.billingCity = billingCity;
    }

    public String getBillingStreet()
    {
        return billingStreet;
    }

    public void setBillingStreet(final String billingStreet)
    {
        this.billingStreet = billingStreet;
    }

    public String getBillingTelephone()
    {
        return billingTelephone;
    }

    public void setBillingTelephone(final String billingTelephone)
    {
        this.billingTelephone = billingTelephone;
    }

    public String getBillingFax()
    {
        return billingFax;
    }

    public void setBillingFax(final String billingFax)
    {
        this.billingFax = billingFax;
    }

    public String getBillingCompany()
    {
        return billingCompany;
    }

    public void setBillingCompany(final String billingCompany)
    {
        this.billingCompany = billingCompany;
    }

    public String getShippingCountryId()
    {
        return shippingCountryId;
    }

    public void setShippingCountryId(final String shippingCountryId)
    {
        this.shippingCountryId = shippingCountryId;
    }

    public String getShippingRegion()
    {
        return shippingRegion;
    }

    public void setShippingRegion(final String shippingRegion)
    {
        this.shippingRegion = shippingRegion;
    }

    public String getShippingPostcode()
    {
        return shippingPostcode;
    }

    public void setShippingPostcode(final String shippingPostcode)
    {
        this.shippingPostcode = shippingPostcode;
    }

    public String getShippingCity()
    {
        return shippingCity;
    }

    public void setShippingCity(final String shippingCity)
    {
        this.shippingCity = shippingCity;
    }

    public String getShippingStreet()
    {
        return shippingStreet;
    }

    public void setShippingStreet(final String shippingStreet)
    {
        this.shippingStreet = shippingStreet;
    }

    public String getShippingTelephone()
    {
        return shippingTelephone;
    }

    public void setShippingTelephone(final String shippingTelephone)
    {
        this.shippingTelephone = shippingTelephone;
    }

    public String getShippingFax()
    {
        return shippingFax;
    }

    public void setShippingFax(final String shippingFax)
    {
        this.shippingFax = shippingFax;
    }

    public String getShippingCompany()
    {
        return shippingCompany;
    }

    public void setShippingCompany(final String shippingCompany)
    {
        this.shippingCompany = shippingCompany;
    }

    public int getWebsiteId()
    {
        return websiteId;
    }

    public void setWebsiteId(final int websiteId)
    {
        this.websiteId = websiteId;
    }

    public String getWebsiteName()
    {
        return websiteName;
    }

    public void setWebsiteName(final String websiteName)
    {
        this.websiteName = websiteName;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(final int groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(final String groupName)
    {
        this.groupName = groupName;
    }

    public int getStoreId()
    {
        return storeId;
    }

    public void setStoreId(final int storeId)
    {
        this.storeId = storeId;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(final String storeName)
    {
        this.storeName = storeName;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(final String lang)
    {
        this.lang = lang;
    }

    public String getLastOrderDate()
    {
        return lastOrderDate;
    }

    public void setLastOrderDate(final String lastOrderDate)
    {
        this.lastOrderDate = lastOrderDate;
    }

    public Double getLastOrderAmount()
    {
        return lastOrderAmount;
    }

    public void setLastOrderAmount(final Double lastOrderAmount)
    {
        this.lastOrderAmount = lastOrderAmount;
    }

    public int getLastOrderProducts()
    {
        return lastOrderProducts;
    }

    public void setLastOrderProducts(final int lastOrderProducts)
    {
        this.lastOrderProducts = lastOrderProducts;
    }

    public Double getTotalOrdersAmount()
    {
        return totalOrdersAmount;
    }

    public void setTotalOrdersAmount(final Double totalOrdersAmount)
    {
        this.totalOrdersAmount = totalOrdersAmount;
    }

    public int getTotalOrdersProducts()
    {
        return totalOrdersProducts;
    }

    public void setTotalOrdersProducts(final int totalOrdersProducts)
    {
        this.totalOrdersProducts = totalOrdersProducts;
    }

    public int getTotalOrdersCount()
    {
        return totalOrdersCount;
    }

    public void setTotalOrdersCount(final int totalOrdersCount)
    {
        this.totalOrdersCount = totalOrdersCount;
    }

    public Double getAvgOrdersAmount()
    {
        return avgOrdersAmount;
    }

    public void setAvgOrdersAmount(final Double avgOrdersAmount)
    {
        this.avgOrdersAmount = avgOrdersAmount;
    }

    public Double getAvgOrdersProducts()
    {
        return avgOrdersProducts;
    }

    public void setAvgOrdersProducts(final Double avgOrdersProducts)
    {
        this.avgOrdersProducts = avgOrdersProducts;
    }

    public Double getPeriod1Amount()
    {
        return period1Amount;
    }

    public void setPeriod1Amount(final Double period1Amount)
    {
        this.period1Amount = period1Amount;
    }

    public int getPeriod1Orders()
    {
        return period1Orders;
    }

    public void setPeriod1Orders(final int period1Orders)
    {
        this.period1Orders = period1Orders;
    }

    public int getPeriod1Products()
    {
        return period1Products;
    }

    public void setPeriod1Products(final int period1Products)
    {
        this.period1Products = period1Products;
    }

    public Double getPeriod2Amount()
    {
        return period2Amount;
    }

    public void setPeriod2Amount(final Double period2Amount)
    {
        this.period2Amount = period2Amount;
    }

    public int getPeriod2Products()
    {
        return period2Products;
    }

    public void setPeriod2Products(final int period2Products)
    {
        this.period2Products = period2Products;
    }

    public int getPeriod2Orders()
    {
        return period2Orders;
    }

    public void setPeriod2Orders(final int period2Orders)
    {
        this.period2Orders = period2Orders;
    }

    public String getCustomField1()
    {
        return customField1;
    }

    public void setCustomField1(final String customField1)
    {
        this.customField1 = customField1;
    }

    public String getCustomField2()
    {
        return customField2;
    }

    public void setCustomField2(final String customField2)
    {
        this.customField2 = customField2;
    }

    public String getCustomField3()
    {
        return customField3;
    }

    public void setCustomField3(final String customField3)
    {
        this.customField3 = customField3;
    }

    public String getCustomField4()
    {
        return customField4;
    }

    public void setCustomField4(final String customField4)
    {
        this.customField4 = customField4;
    }

    public String getCustomField5()
    {
        return customField5;
    }

    public void setCustomField5(final String customField5)
    {
        this.customField5 = customField5;
    }

    public String getCustomField6()
    {
        return customField6;
    }

    public void setCustomField6(final String customField6)
    {
        this.customField6 = customField6;
    }

    public String getCustomField7()
    {
        return customField7;
    }

    public void setCustomField7(final String customField7)
    {
        this.customField7 = customField7;
    }

    public String getCustomField8()
    {
        return customField8;
    }

    public void setCustomField8(final String customField8)
    {
        this.customField8 = customField8;
    }

    public String getCustomField9()
    {
        return customField9;
    }

    public void setCustomField9(final String customField9)
    {
        this.customField9 = customField9;
    }

    public String getCustomField10()
    {
        return customField10;
    }

    public void setCustomField10(final String customField10)
    {
        this.customField10 = customField10;
    }

    public String getUnsubscribeId()
    {
        return unsubscribeId;
    }

    public void setUnsubscribeId(final String unsubscribeId)
    {
        this.unsubscribeId = unsubscribeId;
    }
}
