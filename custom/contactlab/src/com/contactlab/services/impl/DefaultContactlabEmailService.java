package com.contactlab.services.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.contactlab.enums.EmailMessageType;
import com.contactlab.utils.ContactlabMailUtils;


/**
 * @author Techedge Group
 * 
 */
public class DefaultContactlabEmailService extends DefaultEmailService
{
    protected static final Logger LOG = Logger.getLogger(DefaultContactlabEmailService.class);

    @Override
    public boolean send(final EmailMessageModel message)
    {
        if (message == null)
        {
            throw new IllegalArgumentException("message must not be null");
        }

        if (message.getEmailMessageType() != null)
        {
            if (EmailMessageType.DEFAULT.equals(message.getEmailMessageType()))
            {
                super.send(message);
            }
            else
            {
                final boolean sendEnabled = getConfigurationService().getConfiguration()
                        .getBoolean(EMAILSERVICE_SEND_ENABLED_CONFIG_KEY, true);
                if (sendEnabled)
                {
                    try
                    {
                        final HtmlEmail email = (HtmlEmail) ContactlabMailUtils.getPreConfiguredEmail();
                        // final HtmlEmail email = getPerConfiguredEmail();
                        email.setCharset("UTF-8");

                        final List<EmailAddressModel> toAddresses = message.getToAddresses();

                        if (CollectionUtils.isNotEmpty(toAddresses))
                        {
                            email.setTo(getAddresses(toAddresses));
                        }
                        else
                        {
                            throw new IllegalArgumentException("message has no To addresses");
                        }

                        final List<EmailAddressModel> ccAddresses = message.getCcAddresses();
                        if (ccAddresses != null && !ccAddresses.isEmpty())
                        {
                            email.setCc(getAddresses(ccAddresses));
                        }

                        final List<EmailAddressModel> bccAddresses = message.getBccAddresses();
                        if (bccAddresses != null && !bccAddresses.isEmpty())
                        {
                            email.setBcc(getAddresses(bccAddresses));
                        }

                        final EmailAddressModel fromAddress = message.getFromAddress();
                        email.setFrom(fromAddress.getEmailAddress(), nullifyEmpty(fromAddress.getDisplayName()));

                        // Add the reply to if specified
                        final String replyToAddress = message.getReplyToAddress();
                        if (replyToAddress != null && !replyToAddress.isEmpty())
                        {
                            email.setReplyTo(Collections.singletonList(createInternetAddress(replyToAddress, null)));
                        }

                        email.setSubject(message.getSubject());
                        email.setHtmlMsg(getBody(message));

                        // AGGIUNTA HEADER
                        final Map<String, String> headersMap = new HashMap<String, String>();
                        headersMap.put("X-Clab-SmartRelay-DeliveryId", String.valueOf(message.getCampaign().getCampaignId()));
                        headersMap.put("X-Clab-SmartRelay-DeliveryLabel", message.getCampaign().getCampaignName());
                        email.setHeaders(headersMap);

                        // To support plain text parts use email.setTextMsg()

                        final List<EmailAttachmentModel> attachments = message.getAttachments();
                        if (attachments != null && !attachments.isEmpty())
                        {
                            for (final EmailAttachmentModel attachment : attachments)
                            {
                                try
                                {
                                    final DataSource dataSource = new ByteArrayDataSource(
                                            getMediaService().getDataFromMedia(attachment), attachment.getMime());
                                    email.attach(dataSource, attachment.getRealFileName(), attachment.getAltText());
                                }
                                catch (final EmailException ex)
                                {
                                    LOG.error("Failed to load attachment data into data source [" + attachment + "]", ex);
                                    return false;
                                }
                            }
                        }

                        // Important to log all emails sent out
                        LOG.info("Sending Email [" + message.getPk() + "] To [" + convertToStrings(toAddresses) + "] From ["
                                + fromAddress.getEmailAddress() + "] Subject [" + email.getSubject() + "]");

                        // Send the email and capture the message ID
                        final String messageID = email.send();

                        message.setSent(true);
                        message.setSentMessageID(messageID);
                        message.setSentDate(new Date());
                        getModelService().save(message);

                        return true;
                    }
                    catch (final EmailException e)
                    {
                        LOG.warn("Could not send e-mail pk [" + message.getPk() + "] subject [" + message.getSubject()
                                + "] cause: " + e.getMessage());
                        if (LOG.isDebugEnabled())
                        {
                            LOG.debug(e);
                        }
                    }
                }
                else
                {
                    LOG.warn("Could not send e-mail pk [" + message.getPk() + "] subject [" + message.getSubject() + "]");
                    LOG.info("Email sending has been disabled. Check the config property 'emailservice.send.enabled'");
                    return true;
                }
            }
        }
        return true;
    }
}
