package com.contactlab.jobs;

import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.contactlab.model.ContactlabAbandonedCartCronJobModel;
import com.contactlab.services.ContactlabCartService;
import com.contactlab.services.SFTPService;
import com.contactlab.xmldelivery.Auth;
import com.contactlab.xmldelivery.Campaign;
import com.contactlab.xmldelivery.Contactlab;
import com.contactlab.xmldelivery.DeliveryType;
import com.contactlab.xmldelivery.Email;
import com.contactlab.xmldelivery.EmailHeaders;
import com.contactlab.xmldelivery.MailFrom;
import com.contactlab.xmldelivery.MessageEmailGroup;
import com.contactlab.xmldelivery.NewRecipients;
import com.contactlab.xmldelivery.Recipients;

/**
 * @author openmind
 */
public class ContactlabAbandonedCartJob extends AbstractJobPerformable<ContactlabAbandonedCartCronJobModel>
        implements InitializingBean
{

    private static Logger log = LoggerFactory.getLogger(ContactlabAbandonedCartJob.class);

    private FastDateFormat DATE_TIME_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    private ContactlabCartService contactlabCartService;

    private RendererService rendererService;

    private SFTPService sFTPService;

    private String authSuid;
    private String authKey;
    private String deliveryType;
    private String encoding;

    private String localDirectory;

    private String sftpHost;
    private int sftpPort;
    private String sftpUsername;
    private String sftpPassword;
    private String sftpDirectory;

    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
    private String siteBaseUrl;

    // not autowired
    private JAXBContext context;

    @Override
    public PerformResult perform(ContactlabAbandonedCartCronJobModel cronjob)
    {
        boolean success = true;
        try
        {

            File localdir = new File(this.localDirectory);
            localdir.mkdirs();

            String fileNamePrefix = new StringBuffer("abandoned-cart-").append(formatDateTime(new Date())).toString();

            log.info("Exporting abandoned carts to {}", localdir.getAbsolutePath());

            Collection<BaseStoreModel> stores = cronjob.getStores();

            if (!stores.isEmpty())
            {
                RendererTemplateModel htmlTemplate = cronjob.getHtmlTemplate();

                List<CartModel> carts = contactlabCartService.getAbandonedCarts((List) stores, cronjob.getDelay(),
                        cronjob.getNotificationNumber(), cronjob.getExportGuest());

                if (!carts.isEmpty())
                {
                    File csvFile = new File(this.localDirectory, fileNamePrefix + ".csv");

                    success = appendCsvRow(csvFile, this.encoding, true, csvHeader());

                    if (!success)
                    {
                        throw new Throwable("Cannot append csv file header: Abort job");
                    }

                    Map<String, Object> params = new HashMap<String, Object>();

                    int addedCarts = 0;
                    for (CartModel cart : carts)
                    {
                        if (cart.getContactlabAbandonedCartDate() != null
                                && cart.getContactlabAbandonedCartDate().before(cart.getModifiedtime()))
                        {
                            cart.setContactlabAbandonedCartCounter(null);
                            cart.setContactlabAbandonedCartDate(null);
                            modelService.save(cart);
                            log.info("Reset contactlab send counter for cart {}", cart.getCode());
                        }
                        else
                        {
                            params.put("user", cart.getUser());

                            CartModel recalculatedCart = contactlabCartService.recalculateCart(cart);

                            if (recalculatedCart != null)
                            {
                                CartData cartData = contactlabCartService.getCartData(recalculatedCart);
                                log.info("cartData: {}", cartData);

                                List<ProductData> productReferences = contactlabCartService
                                        .getCrosssellingProducts(recalculatedCart, 3);

                                BaseSiteModel baseSite = cart.getSite();
                                params.put("siteBaseUrl", this.siteBaseUrl);
                                params.put("siteuid", baseSite.getUid());
                                params.put("cart", cartData);
                                params.put("guid", cartData.getGuid());
                                params.put("productReferences", productReferences);

                                addSiteSpecificUrls(baseSite, params);

                                addContextAttributes(params);

                                StringWriter htmlSnippet = new StringWriter();

                                rendererService.render(htmlTemplate, params, htmlSnippet);

                                appendCsvRow(csvFile, this.encoding, true, csvLineData(cart, htmlSnippet.toString()));

                                success = writeFile(new File(localdir, fileNamePrefix + "-" + cart.getCode() + ".html"),
                                        htmlSnippet.toString(), this.encoding, false);

                                if (!success)
                                {
                                    log.warn("Skipped csv line for cart {}", cartData.getCode());
                                }
                                else
                                {
                                    addedCarts++;
                                    log.info("Exported abandoned cart email for cart {}", cartData.getCode());
                                }

                                cart.setContactlabAbandonedCartCounter(cronjob.getNotificationNumber());

                                cart.setContactlabAbandonedCartDate(new Date());
                            }

                            modelService.save(cart);
                        }
                    }

                    if (addedCarts > 0)
                    {
                        Contactlab xmlDeliveryFile = buildXmlDeliveryObject(cronjob, fileNamePrefix + ".csv");

                        File xmlFile = new File(localdir, fileNamePrefix + ".xml");

                        writeFile(xmlFile, objectToXml(xmlDeliveryFile), this.encoding, false);

                        File okFile = new File(localdir, fileNamePrefix + ".xml.ok");
                        okFile.createNewFile();

                        File[] files =
                        { csvFile, xmlFile, okFile };

                        for (File file : files)
                        {
                            success = sFTPService.outboundTransfer(this.sftpHost, this.sftpPort, this.sftpUsername,
                                    this.sftpPassword, this.sftpDirectory, file, "contactlab");

                            if (!success)
                            {
                                log.error("Unable to uplod file {}", file.getAbsolutePath());
                                throw new Throwable("Uploading error");
                            }

                        }

                    }
                    else
                    {
                        log.info("No abandoned cart added to contactlab quee");
                    }
                }
                else
                {
                    log.info("No abandoned cart found");
                }

            }
        }
        catch (Throwable e)
        {
            log.error("Exception in job execution", e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private void addSiteSpecificUrls(BaseSiteModel baseSite, Map<String, Object> params)
    {
        params.put(AbstractEmailContext.BASE_SITE, baseSite);

        params.put(AbstractEmailContext.BASE_URL,
                siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, null, false, ""));
        params.put(AbstractEmailContext.BASE_THEME_URL,
                siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, false, ""));
        params.put(AbstractEmailContext.SECURE_BASE_URL,
                siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, null, true, ""));
        params.put(AbstractEmailContext.MEDIA_BASE_URL,
                siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, false));
        params.put(AbstractEmailContext.MEDIA_SECURE_BASE_URL,
                siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, true));
        params.put(AbstractEmailContext.THEME, baseSite.getTheme() != null ? baseSite.getTheme().getCode() : null);
    }

    protected void addContextAttributes(Map<String, Object> params)
    {
        // hook for extensions
    }

    private String formatDateTime(Date date)
    {
        if (date == null)
        {
            return StringUtils.EMPTY;
        }
        else
        {
            return DATE_TIME_FORMAT.format(date);
        }
    }

    private Map<Integer, String> csvHeader()
    {
        Map<Integer, String> header = new TreeMap<Integer, String>();
        header.put(Integer.valueOf(0), "customerName");
        header.put(Integer.valueOf(1), "customerEmail");
        header.put(Integer.valueOf(2), "orderCode");
        header.put(Integer.valueOf(3), "htmlSnippet");
        return header;
    }

    private Map<Integer, String> csvLineData(CartModel cart, String htmlSnippet)
    {
        Map<Integer, String> header = new TreeMap<Integer, String>();
        if (StringUtils.equals(cart.getUser().getName(), "guest"))
        {
            header.put(Integer.valueOf(0), "");
            header.put(Integer.valueOf(1),
                    stripNewLine(StringUtils.substringAfter(((CustomerModel) cart.getUser()).getUid(), "|")));
        }
        else
        {
            header.put(Integer.valueOf(0), stripNewLine(cart.getUser().getName()));
            header.put(Integer.valueOf(1), stripNewLine(((CustomerModel) cart.getUser()).getUid()));
        }
        header.put(Integer.valueOf(2), stripNewLine(cart.getCode()));
        header.put(Integer.valueOf(3), stripNewLine(htmlSnippet));
        return header;
    }

    private Contactlab buildXmlDeliveryObject(ContactlabAbandonedCartCronJobModel cronjob, String csvFileName)
    {
        Contactlab xmlDeliveryFile = new Contactlab();
        Auth auth = new Auth();
        auth.setSuid(this.authSuid);
        auth.setKey(this.authKey);
        xmlDeliveryFile.setAuth(auth);
        Campaign campaign = new Campaign();
        Email email = new Email();
        Recipients recipients = new Recipients();
        NewRecipients newRecipients = new NewRecipients();
        newRecipients.setCsvFile(csvFileName);
        recipients.setRetry("");
        recipients.setNewRecipients(newRecipients);
        email.setNewsletter(cronjob.getEmailGroup());
        email.setRecipients(recipients);
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setMethod(this.deliveryType);
        email.setDelivery(deliveryType);
        MessageEmailGroup message = new MessageEmailGroup();
        message.setEncoding(this.encoding);
        EmailHeaders headers = new EmailHeaders();
        headers.setSubject(cronjob.getEmailSubject());
        MailFrom mailFrom = new MailFrom();
        mailFrom.setName(cronjob.getEmailFromName());
        mailFrom.setAddress(cronjob.getEmailFromAddress());
        headers.setMailFrom(mailFrom);
        headers.setReplyTo(cronjob.getEmailReplyTo());
        message.setHeaders(headers);
        message.setTemplateFromCampaignId(cronjob.getEmailTemplateId());
        email.setMessage(message);
        campaign.setEmail(email);
        xmlDeliveryFile.setCampaign(campaign);

        return xmlDeliveryFile;
    }

    public void afterPropertiesSet() throws Exception
    {
        context = JAXBContext.newInstance(Contactlab.class);
    }

    private String objectToXml(Contactlab obj)
    {
        try
        {
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();
            m.marshal(obj, sw);
            return sw.toString();
        }
        catch (JAXBException e)
        {
            log.error("Unable to marshall object {} - {}:{}", obj, ClassUtils.getShortClassName(e.getClass()),
                    e.getMessage(), e);
        }

        return null;
    }

    protected boolean appendCsvRow(File file, String encoding, boolean append, Map linedata)
    {
        CSVWriter writer;
        try
        {
            writer = new CSVWriter(file, encoding, append);
            writer.setFieldseparator(',');
            writer.setTextseparator('\"');
            writer.write(linedata);
            writer.closeQuietly();
        }
        catch (IOException e)
        {
            log.error("Error while append csv row", e);
            return false;
        }
        return true;
    }

    protected boolean writeFile(File file, String data, String encoding, boolean append)
    {
        try
        {
            FileUtils.writeStringToFile(file, data, encoding, append);
        }
        catch (IOException e)
        {
            log.info("Error occurred while creating file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    protected String stripNewLine(String html)
    {
        String str = StringUtils.defaultIfEmpty(
                StringUtils.trim(StringUtils.replace(StringUtils.replace(html, "\n", ""), "\r", "")), "");
        return str;
    }

    public void setContactlabCartService(ContactlabCartService contactlabCartService)
    {
        this.contactlabCartService = contactlabCartService;
    }

    public void setRendererService(RendererService rendererService)
    {
        this.rendererService = rendererService;
    }

    public void setAuthSuid(String authSuid)
    {
        this.authSuid = authSuid;
    }

    public void setAuthKey(String authKey)
    {
        this.authKey = authKey;
    }

    public void setDeliveryType(String deliveryType)
    {
        this.deliveryType = deliveryType;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setLocalDirectory(String localDirectory)
    {
        this.localDirectory = localDirectory;
    }

    public void setsFTPService(SFTPService sFTPService)
    {
        this.sFTPService = sFTPService;
    }

    public void setSftpHost(String sftpHost)
    {
        this.sftpHost = sftpHost;
    }

    public void setSftpPort(int sftpPort)
    {
        this.sftpPort = sftpPort;
    }

    public void setSftpUsername(String sftpUsername)
    {
        this.sftpUsername = sftpUsername;
    }

    public void setSftpPassword(String sftpPassword)
    {
        this.sftpPassword = sftpPassword;
    }

    public void setSftpDirectory(String sftpDirectory)
    {
        this.sftpDirectory = sftpDirectory;
    }

    public void setSiteBaseUrlResolutionService(SiteBaseUrlResolutionService siteBaseUrlResolutionService)
    {
        this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
    }

    public void setSiteBaseUrl(String siteBaseUrl)
    {
        this.siteBaseUrl = siteBaseUrl;
    }

}
