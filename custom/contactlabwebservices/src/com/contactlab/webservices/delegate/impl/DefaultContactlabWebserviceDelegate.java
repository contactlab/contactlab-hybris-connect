/**
 * 
 */
package com.contactlab.webservices.delegate.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.api.ws.ClabService;
import com.contactlab.api.ws.ClabService_Service;
import com.contactlab.api.ws.domain.AuthToken;
import com.contactlab.webservices.constants.ContactlabwebservicesConstants;
import com.contactlab.webservices.delegate.ContactlabWebserviceDelegate;
import com.contactlab.webservices.exception.ContactlabConfigurationPropertyException;
import com.contactlab.webservices.exception.ContactlabWebserviceException;

/**
 *  @author Techedge Group
 * 
 */
public class DefaultContactlabWebserviceDelegate implements ContactlabWebserviceDelegate {
    private final Logger LOG = Logger.getLogger(DefaultContactlabWebserviceDelegate.class);

    private ConfigurationService configurationService;

    private ClabService wsPort;

    private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;

    @Override
    public void unsubscribeSubscriberFromCampaign(final int userId, final String campaignWebFormCode)
            throws ContactlabWebserviceException {
        // Authenticate and get token
        final AuthToken authToken = authenticate();

        // Unsubscription
        getPort().modifySubscriberSubscriptionStatus(authToken, campaignWebFormCode, userId, false);

        // Invalidate token
        invalidateToken(authToken);
    }

    protected AuthToken authenticate() throws ContactlabWebserviceException {
        try {
            // Authentication: retrieve APIKEY and UID from extension's properties
            final String apiKey = getConfigurationProperty(ContactlabwebservicesConstants.APIKEY);
            final String uid = getConfigurationProperty(ContactlabwebservicesConstants.UID);

            final AuthToken authToken = getPort().borrowToken(apiKey, uid);

            // Return token
            return authToken;
        } catch (final ContactlabConfigurationPropertyException ccpe) {
            LOG.error(ccpe.getMessage(), ccpe);
        }

        throw new ContactlabWebserviceException("Error getting authorization token by Contactlab SOAP API");
    }

    protected void invalidateToken(final AuthToken authToken) throws ContactlabWebserviceException {
        if (!getPort().invalidateToken(authToken)) {
            throw new ContactlabWebserviceException("Error invalidating autharization token on Contactlab SOAP API");
        }
    }

    /**
     * @return the port
     */
    protected ClabService getPort() throws ContactlabWebserviceException {
        if (this.wsPort == null) {
            try {
                // Create the service
                final ClabService_Service service = new ClabService_Service();
                // Add a Port to the service and specify the SOAP 1.2 binding
                service.addPort(ClabService_Service.ClabServicePort, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING,
                        getConfigurationProperty(ContactlabwebservicesConstants.ENDPOINT_URL));
                // Access the port
                this.wsPort = service.getClabServicePort();
            } catch (final ContactlabConfigurationPropertyException e) {
                throw new ContactlabWebserviceException(e.getMessage(), e);
            }
        }

        return this.wsPort;
    }

    private String getConfigurationProperty(final String propertyKey) throws ContactlabConfigurationPropertyException {
        final String property = getConfigurationService().getConfiguration().getString(
                ContactlabwebservicesConstants.EXTENSIONNAME.toLowerCase() + "." + propertyKey);

        if (StringUtils.isEmpty(property)) {
            throw new ContactlabConfigurationPropertyException("The ["
                    + ContactlabwebservicesConstants.EXTENSIONNAME.toLowerCase() + "." + propertyKey
                    + "] property is not set.");
        }

        return property;
    }

    protected ConfigurationService getConfigurationService() {
        return this.configurationService;
    }

    @Required
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
