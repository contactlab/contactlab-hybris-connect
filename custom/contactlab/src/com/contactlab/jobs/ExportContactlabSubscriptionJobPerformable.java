package com.contactlab.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.converters.ConvertEmailSubscriptionsToXML;
import com.contactlab.daos.EmailSubscriptionDao;
import com.contactlab.services.impl.ContactlabEmailSubscriptionExportService;


/**
 * @author Techedge Group
 * 
 */
public class ExportContactlabSubscriptionJobPerformable extends AbstractJobPerformable<CronJobModel>
{

    private static final Logger LOG = Logger.getLogger(ExportContactlabSubscriptionJobPerformable.class);

    private EmailSubscriptionDao emailSubscriptionDao;

    private ContactlabEmailSubscriptionExportService contactlabEmailSubscriptionExportService;

    private ConvertEmailSubscriptionsToXML convertObjectsToXML;

    @Override
    public boolean isAbortable()
    {
        return true;
    }

    @Override
    public boolean isPerformable()
    {
        return true;
    }

    @Override
    public PerformResult perform(final CronJobModel cronJob)
    {
        final CronJobResult cronJobResult = CronJobResult.SUCCESS;

        // try {
        //
        // LOG.info("Email Subscription CronJob - START");
        //
        // final List<EmailSubscriptionModel> emailSubscriptionsToUpdate = emailSubscriptionDao
        // .findEmailSubscriptionsToUpdate();
        // if (emailSubscriptionsToUpdate != null && !emailSubscriptionsToUpdate.isEmpty()) {
        // LOG.info("Updating " + emailSubscriptionsToUpdate.size() + " Email Subscripton(s)");
        //
        // final boolean success = convertObjectsToXML.getConvertObjectsToXML(emailSubscriptionsToUpdate);
        //
        // if (!success) {
        // LOG.error(convertObjectsToXML + " of " + emailSubscriptionsToUpdate.size()
        // + " not successfully generated XML");
        // cronJobResult = CronJobResult.ERROR;
        // }
        // final int subscriptionsUpdated = contactlabEmailSubscriptionExportService
        // .updateSubscriptions(emailSubscriptionsToUpdate);
        //
        // if (subscriptionsUpdated == emailSubscriptionsToUpdate.size()) {
        // LOG.info(emailSubscriptionsToUpdate.size() + " successfully updated");
        // } else {
        // LOG.error(subscriptionsUpdated + " of " + emailSubscriptionsToUpdate.size()
        // + " successfully updated");
        // cronJobResult = CronJobResult.ERROR;
        // }
        // } else {
        // LOG.info("No Email Subscripton to update");
        // }
        //
        // checkAbort(cronJob);
        // } catch (final AbortCronJobException e) {
        // // LOG.error("AbortCronJobException: " + e.getMessage(), e);
        // LOG.warn(e.getMessage());
        //
        // return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        // } catch (final Exception e) {
        // LOG.error("Exception: " + e.getMessage(), e);
        //
        // return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        // }

        LOG.info("Email Subscription CronJob - END");

        return new PerformResult(cronJobResult, CronJobStatus.FINISHED);
    }

    private void checkAbort(final CronJobModel cronJob) throws AbortCronJobException
    {
        if (clearAbortRequestedIfNeeded(cronJob))
        {
            throw new AbortCronJobException("CronJob aborted by user");
        }
    }

    protected EmailSubscriptionDao getEmailSubscriptionDao()
    {
        return emailSubscriptionDao;
    }

    @Required
    public void setEmailSubscriptionDao(final EmailSubscriptionDao emailSubscriptionDao)
    {
        this.emailSubscriptionDao = emailSubscriptionDao;
    }

    public ContactlabEmailSubscriptionExportService getContactlabEmailSubscriptionExportService()
    {
        return contactlabEmailSubscriptionExportService;
    }

    public void setContactlabEmailSubscriptionExportService(
            final ContactlabEmailSubscriptionExportService contactlabEmailSubscriptionExportService)
    {
        this.contactlabEmailSubscriptionExportService = contactlabEmailSubscriptionExportService;
    }

    /**
     * @return the convertObjectsToXML
     */
    public ConvertEmailSubscriptionsToXML getConvertObjectsToXML()
    {
        return convertObjectsToXML;
    }

    /**
     * @param convertObjectsToXML
     *            the convertObjectsToXML to set
     */
    public void setConvertObjectsToXML(final ConvertEmailSubscriptionsToXML convertObjectsToXML)
    {
        this.convertObjectsToXML = convertObjectsToXML;
    }
}
