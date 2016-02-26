package com.contactlab.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.contactlab.daos.EmailSubscriptionDao;
import com.contactlab.model.EmailSubscriptionModel;
import com.contactlab.services.EmailSubscriptionExportService;


/**
 * @author Techedge Group
 * 
 */
public class ExportEmailSubscriptionJobPerformable extends AbstractJobPerformable<CronJobModel>
{

    private static final Logger LOG = Logger.getLogger(ExportEmailSubscriptionJobPerformable.class);

    private EmailSubscriptionDao emailSubscriptionDao;

    private EmailSubscriptionExportService emailSubscriptionExportService;

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
    public PerformResult perform(CronJobModel cronJob)
    {
        CronJobResult cronJobResult = CronJobResult.SUCCESS;

        try
        {

            LOG.info("Email Subscription CronJob - START");

            List<EmailSubscriptionModel> emailSubscriptionsToRemove = emailSubscriptionDao.findEmailSubscriptionsToRemove();
            if (emailSubscriptionsToRemove != null && !emailSubscriptionsToRemove.isEmpty())
            {
                LOG.info("Removing " + emailSubscriptionsToRemove.size() + " Email Subscripton(s)");

                int subscriptionsRemoved = emailSubscriptionExportService.removeSubscriptions(emailSubscriptionsToRemove);

                if (subscriptionsRemoved == emailSubscriptionsToRemove.size())
                {
                    LOG.info(emailSubscriptionsToRemove.size() + " successfully removed");
                }
                else
                {
                    LOG.error(subscriptionsRemoved + " of " + emailSubscriptionsToRemove.size() + " successfully removed");
                    cronJobResult = CronJobResult.ERROR;
                }
            }
            else
            {
                LOG.info("No Email Subscripton to remove");
            }

            checkAbort(cronJob);

            List<EmailSubscriptionModel> emailSubscriptionsToUpdate = emailSubscriptionDao.findEmailSubscriptionsToUpdate();
            if (emailSubscriptionsToUpdate != null && !emailSubscriptionsToUpdate.isEmpty())
            {
                LOG.info("Updating " + emailSubscriptionsToUpdate.size() + " Email Subscripton(s)");

                int subscriptionsUpdated = emailSubscriptionExportService.updateSubscriptions(emailSubscriptionsToUpdate);

                if (subscriptionsUpdated == emailSubscriptionsToUpdate.size())
                {
                    LOG.info(emailSubscriptionsToUpdate.size() + " successfully updated");
                }
                else
                {
                    LOG.error(subscriptionsUpdated + " of " + emailSubscriptionsToUpdate.size() + " successfully updated");
                    cronJobResult = CronJobResult.ERROR;
                }
            }
            else
            {
                LOG.info("No Email Subscripton to update");
            }

            checkAbort(cronJob);

            List<EmailSubscriptionModel> emailSubscriptionsToAdd = emailSubscriptionDao.findEmailSubscriptionsToAdd();
            if (emailSubscriptionsToAdd != null && !emailSubscriptionsToAdd.isEmpty())
            {
                LOG.info("Adding " + emailSubscriptionsToAdd.size() + " Email Subscripton(s)");

                int subscriptionsAdded = emailSubscriptionExportService.addSubscriptions(emailSubscriptionsToAdd);

                if (subscriptionsAdded == emailSubscriptionsToAdd.size())
                {
                    LOG.info(emailSubscriptionsToAdd.size() + " successfully added");
                }
                else
                {
                    LOG.error(subscriptionsAdded + " of " + emailSubscriptionsToAdd.size() + " successfully added");
                    cronJobResult = CronJobResult.ERROR;
                }
            }
            else
            {
                LOG.info("No Email Subscripton to add");
            }

        }
        catch (AbortCronJobException e)
        {
            // LOG.error("AbortCronJobException: " + e.getMessage(), e);
            LOG.warn(e.getMessage());

            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        catch (Exception e)
        {
            LOG.error("Exception: " + e.getMessage(), e);

            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }

        LOG.info("Email Subscription CronJob - END");

        return new PerformResult(cronJobResult, CronJobStatus.FINISHED);
    }

    private void checkAbort(CronJobModel cronJob) throws AbortCronJobException
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
    public void setEmailSubscriptionDao(EmailSubscriptionDao emailSubscriptionDao)
    {
        this.emailSubscriptionDao = emailSubscriptionDao;
    }

    protected EmailSubscriptionExportService getEmailSubscriptionExportService()
    {
        return emailSubscriptionExportService;
    }

    @Required
    public void setEmailSubscriptionExportService(EmailSubscriptionExportService emailSubscriptionExportService)
    {
        this.emailSubscriptionExportService = emailSubscriptionExportService;
    }

}
