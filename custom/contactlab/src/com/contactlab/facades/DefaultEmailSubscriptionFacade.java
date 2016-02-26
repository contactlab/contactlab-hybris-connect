package com.contactlab.facades;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.contactlab.core.Activity;
import com.contactlab.core.SessionLock;
import com.contactlab.data.SubscriptionData;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.EmailSubscriptionService;


/**
 * @author Techedge Group
 * 
 */
public class DefaultEmailSubscriptionFacade implements EmailSubscriptionFacade
{
    protected static final Logger LOG = Logger.getLogger(DefaultEmailSubscriptionFacade.class);

    private EmailSubscriptionService emailSubscriptionService;

    private UserService userService;

    @Override
    public void subscribeUser(final SubscriptionData subscriptionData)
    {
        Assert.notNull(subscriptionData, "Parameter subscriptionData cannot be null.");

        final String title = subscriptionData.getTitle();
        final String firstName = subscriptionData.getFirstName();
        final String middleName = subscriptionData.getMiddleName();
        final String lastName = subscriptionData.getLastName();
        final String email = subscriptionData.getEmail();
        final String birthDate = subscriptionData.getBirthDate();

        final String gender = subscriptionData.getGender();

        final String address = subscriptionData.getBillingAddress();
        final String city = subscriptionData.getBillingCity();
        final String postalCode = subscriptionData.getBillingPostalCode();
        final String district = subscriptionData.getBillingDistrict();
        final String region = subscriptionData.getBillingRegion();
        final String country = subscriptionData.getBillingCountry();
        final String phone = subscriptionData.getBillingPhone();
        final String cellPhone = subscriptionData.getBillingCellPhone();
        final String fax = subscriptionData.getBillingFax();
        final String company = subscriptionData.getBillingCompany();

        SessionLock.doSynchronized(new Activity<Void>()
        {

            @Override
            public void invokeNoResult()
            {
                getEmailSubscriptionService().subscribeCustomer(title, firstName, middleName, lastName, email, birthDate, gender,
                        address, city, postalCode, district, region, country, phone, cellPhone, fax, company);
            }

        });

    }

    @Override
    public void subscribeCurrentUser()
    {

        final UserModel currentUser = getUserService().getCurrentUser();
        final boolean isAnonymous = getUserService().isAnonymousUser(currentUser);

        if (!isAnonymous)
        {
            if (currentUser instanceof CustomerModel)
            {
                SessionLock.doSynchronized(new Activity<Void>()
                {

                    @Override
                    public void invokeNoResult()
                    {
                        getEmailSubscriptionService().subscribeCustomer((CustomerModel) currentUser);
                    }

                });
            }
        }
    }

    @Override
    public void unsubscribeCurrentUser()
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        final boolean isAnonymous = getUserService().isAnonymousUser(currentUser);

        if (!isAnonymous)
        {
            if (currentUser instanceof CustomerModel)
            {

                SessionLock.doSynchronized(new Activity<Void>()
                {

                    @Override
                    public void invokeNoResult()
                    {
                        getEmailSubscriptionService().unsubscribeCustomer((CustomerModel) currentUser);
                    }

                });
            }
        }
    }

    @Override
    public void unsubscribeByCustomerId(final String customerID)
    {
        final CustomerModel customerModel = getEmailSubscriptionService().findCustomerByCustomerID(customerID);
        if (customerModel != null)
        {
            getEmailSubscriptionService().unsubscribeCustomer(customerModel);
        }
        else
        {
            // remove subscription
            EmailSubscriptionModel subscription = getEmailSubscriptionService().findEmailSubscriptionByID(customerID);
            if (subscription != null)
            {

                getEmailSubscriptionService().unsubscribe(subscription, true);
            }
            else
            {
                LOG.info("Error unsubscribing user " + customerID + ", EmailSubscription not found!");
            }
        }

    }

    @Override
    public boolean isCurrentUserSubscribed()
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        final boolean isAnonymous = getUserService().isAnonymousUser(currentUser);

        if (!isAnonymous)
        {
            if (currentUser instanceof CustomerModel)
            {
                return getEmailSubscriptionService().isCustomerSubscribed((CustomerModel) currentUser);
            }

        }
        return false;
    }

    protected EmailSubscriptionService getEmailSubscriptionService()
    {
        return emailSubscriptionService;
    }

    @Required
    public void setEmailSubscriptionService(final EmailSubscriptionService emailSubscriptionService)
    {
        this.emailSubscriptionService = emailSubscriptionService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }

}
