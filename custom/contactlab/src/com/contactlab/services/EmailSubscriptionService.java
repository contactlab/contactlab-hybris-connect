package com.contactlab.services;

import de.hybris.platform.core.model.user.CustomerModel;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 * 
 */
public interface EmailSubscriptionService
{

    CustomerModel findCustomerByCustomerID(String customerId);

    void subscribeCustomer(CustomerModel customer);

    void unsubscribe(EmailSubscriptionModel subscription, boolean force);

    void unsubscribeCustomer(CustomerModel customer);

    void subscribeCustomer(String title, String firstName, String middleName, String lastName, String email, String birthDate,
            String gender, String address, String city, String postalCode, String district, String region, String country,
            String phone, String cellPhone, String fax, String company);

    boolean isCustomerSubscribed(CustomerModel customer);

    EmailSubscriptionModel findEmailSubscriptionByID(String customerID);
}
