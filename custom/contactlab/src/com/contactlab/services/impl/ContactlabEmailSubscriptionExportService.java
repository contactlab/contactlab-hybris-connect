package com.contactlab.services.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.contactlab.converters.ConvertEmailSubscriptionsToXML;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.EmailSubscriptionExportService;


/**
 * @author Techedge Group
 *
 */
public class ContactlabEmailSubscriptionExportService implements EmailSubscriptionExportService
{

    private static final Logger LOG = Logger.getLogger(ContactlabEmailSubscriptionExportService.class);

    private ConfigurationService configurationService;

    private ModelService modelService;

    private TimeService timeService;

    private ConvertEmailSubscriptionsToXML convertEmailSubscriptionsToXML;

    private MessageChannel sftpChannel;

    private String fileNamePrefix;

    private String fileExtension;

    private String fileExportDateFormat;

    private String localFolder;

    @Override
    public int updateSubscriptions(final List<EmailSubscriptionModel> emailSubscriptionList)
    {

        int errors = 0;

        try
        {
            final String xmlFileContent = getConvertEmailSubscriptionsToXML().convert(emailSubscriptionList);

            final String filePath = getFileFullPath();

            // Upload exported subscriptions first
            uploadFile(xmlFileContent, filePath);

            // When finished upload the "semaphore" (an empty file with the same filename and one more extension
            // ".ready")
            uploadFile("", filePath + ".ready");

            errors = saveUpdatedSubscriptions(emailSubscriptionList);
        }
        catch (final JAXBException e)
        {
            LOG.error("Error while converting email subscriptions for export", e);
            return 0;
        }
        catch (final Exception e)
        {
            LOG.error("Error while exporting email subscriptions", e);
            return 0;
        }

        return (emailSubscriptionList.size() - errors);
    }

    @Override
    public int addSubscriptions(final List<EmailSubscriptionModel> emailSubscriptionList)
    {
        // YTODO Auto-generated method stub
        return 0;
    }

    @Override
    public int removeSubscriptions(final List<EmailSubscriptionModel> emailSubscriptionList)
    {
        // YTODO Auto-generated method stub
        return 0;
    }

    protected int saveUpdatedSubscriptions(final List<EmailSubscriptionModel> emailSubscriptionList)
    {
        int errors = 0;

        for (final EmailSubscriptionModel emailSubscription : emailSubscriptionList)
        {
            try
            {
                emailSubscription.setExportedEmail(emailSubscription.getCurrentSubscribedEmail());
                emailSubscription.setExportedFirstname(emailSubscription.getFirstname());
                emailSubscription.setExportedLastname(emailSubscription.getLastname());
                emailSubscription.setExportedBirthDate(emailSubscription.getBirthDate());
                emailSubscription.setExportedMiddleName(emailSubscription.getMiddlename());
                if (emailSubscription.getTitle() != null)
                {
                    emailSubscription.setExportedTitle(emailSubscription.getTitle().getCode());
                }
                if (emailSubscription.getGender() != null)
                {
                    emailSubscription.setExportedGender(emailSubscription.getGender().getCode());
                }

                // Dynamic Attribute
                emailSubscription.setExportedCustomField1(emailSubscription.getCustomField1());
                emailSubscription.setExportedCustomField2(emailSubscription.getCustomField2());
                emailSubscription.setExportedCustomField3(emailSubscription.getCustomField3());
                emailSubscription.setExportedCustomField4(emailSubscription.getCustomField4());
                emailSubscription.setExportedCustomField5(emailSubscription.getCustomField5());
                emailSubscription.setExportedCustomField6(emailSubscription.getCustomField6());
                emailSubscription.setExportedCustomField7(emailSubscription.getCustomField7());
                emailSubscription.setExportedCustomField8(emailSubscription.getCustomField8());
                emailSubscription.setExportedCustomField9(emailSubscription.getCustomField9());
                emailSubscription.setExportedCustomField10(emailSubscription.getCustomField10());

                // BillingAddress
                emailSubscription.setExportedBillingAddress(emailSubscription.getBillingAddress());
                emailSubscription.setExportedBillingCity(emailSubscription.getBillingCity());
                emailSubscription.setExportedBillingPostalCode(emailSubscription.getBillingPostalcode());
                if (emailSubscription.getBillingCountry() != null)
                {
                    emailSubscription.setExportedBillingCountry(emailSubscription.getBillingCountry().getName());
                }
                emailSubscription.setExportedBillingPhone(emailSubscription.getBillingPhone());
                emailSubscription.setExportedBillingCellPhone(emailSubscription.getBillingCellphone());
                emailSubscription.setExportedBillingFax(emailSubscription.getBillingFax());
                emailSubscription.setExportedBillingCompany(emailSubscription.getBillingCompany());

                // ShippingAddress
                emailSubscription.setExportedShippingAddress(emailSubscription.getShippingAddress());
                emailSubscription.setExportedShippingCity(emailSubscription.getShippingCity());
                emailSubscription.setExportedShippingPostalCode(emailSubscription.getShippingPostalcode());
                if (emailSubscription.getShippingCountry() != null)
                {
                    emailSubscription.setExportedShippingCountry(emailSubscription.getShippingCountry().getName());
                }
                emailSubscription.setExportedShippingPhone(emailSubscription.getShippingPhone());
                emailSubscription.setExportedShippingCellPhone(emailSubscription.getShippingCellphone());
                emailSubscription.setExportedShippingFax(emailSubscription.getShippingFax());
                emailSubscription.setExportedShippingCompany(emailSubscription.getShippingCompany());

                emailSubscription.setLastExportTime(getTimeService().getCurrentTime());
                emailSubscription.setToBeExported(Boolean.FALSE);

                modelService.save(emailSubscription);
            }
            catch (final Exception e)
            {
                LOG.error("Update Subscription failed for ExternalUserId " + emailSubscription.getExternalUserId());
                LOG.error("ContactLab Exception: " + e.getMessage(), e);
                errors++;
            }
        }

        return errors;
    }

    private void uploadFile(final String content, final String path) throws Exception
    {
        final File fileToBeExported = new File(path);
        FileUtils.writeStringToFile(fileToBeExported, content);

        final Message<File> message = MessageBuilder.withPayload(fileToBeExported).build();
        getSftpChannel().send(message);
    }

    private String getFileFullPath()
    {
        final Date now = getTimeService().getCurrentTime();
        final SimpleDateFormat sdf = new SimpleDateFormat(getFileExportDateFormat());

        String path = getLocalFolder();
        if (!path.endsWith("/"))
        {
            path += "/";
        }

        return path + getFileNamePrefix() + sdf.format(now) + getFileExtension();
    }

    protected String getFileNamePrefix()
    {
        return fileNamePrefix;
    }

    @Required
    public void setFileNamePrefix(final String fileNamePrefix)
    {
        this.fileNamePrefix = fileNamePrefix;
    }

    protected String getFileExtension()
    {
        return fileExtension;
    }

    @Required
    public void setFileExtension(final String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    protected String getFileExportDateFormat()
    {
        return fileExportDateFormat;
    }

    @Required
    public void setFileExportDateFormat(final String fileExportDateFormat)
    {
        this.fileExportDateFormat = fileExportDateFormat;
    }

    protected String getLocalFolder()
    {
        return localFolder;
    }

    @Required
    public void setLocalFolder(final String localFolder)
    {
        this.localFolder = localFolder;
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

    protected ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    protected TimeService getTimeService()
    {
        return timeService;
    }

    @Required
    public void setTimeService(final TimeService timeService)
    {
        this.timeService = timeService;
    }

    protected ConvertEmailSubscriptionsToXML getConvertEmailSubscriptionsToXML()
    {
        return convertEmailSubscriptionsToXML;
    }

    @Required
    public void setConvertEmailSubscriptionsToXML(final ConvertEmailSubscriptionsToXML convertEmailSubscriptionsToXML)
    {
        this.convertEmailSubscriptionsToXML = convertEmailSubscriptionsToXML;
    }

    protected MessageChannel getSftpChannel()
    {
        return sftpChannel;
    }

    @Required
    public void setSftpChannel(final MessageChannel sftpChannel)
    {
        this.sftpChannel = sftpChannel;
    }
}
