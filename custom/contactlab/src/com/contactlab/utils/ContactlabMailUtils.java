package com.contactlab.utils;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.constants.ContactlabConstants;


/**
 * @author Techedge Group
 * 
 */

public class ContactlabMailUtils
{
    private static final Logger LOG = Logger.getLogger(ContactlabMailUtils.class);

    private static ConfigurationService configurationService;

    public static Email getPreConfiguredEmail() throws EmailException
    {
        try
        {
            final Email email = MailUtils.getPreConfiguredEmail();

            final int smtpport = Config.getInt(ContactlabConstants.CONTACTLAB_SMARTRELAY_PORT, 21);
            final String aHostName = Config.getString(ContactlabConstants.CONTACTLAB_SMARTRELAY_ADDRESS, StringUtils.EMPTY);
            if (StringUtils.isEmpty(aHostName))
            {
                throw new EmailException("The SMTP host is not set");
            }
            final boolean useTLS = Boolean.FALSE.booleanValue();
            email.setSmtpPort(smtpport); // contactlab.smartrelay.port
            email.setHostName(aHostName);// contactlab.smartrelay.address
            email.setTLS(useTLS);

            return email;

        }
        catch (final EmailException e)
        {
            throw new EmailException("ContactlabMailUtils -getPreConfiguredEmail - exception " + e.getMessage());
        }

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
