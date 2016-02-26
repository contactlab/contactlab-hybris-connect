package com.contactlab.converters.adapters;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.contactlab.data.CampaignSubscriptionStatus;


/**
 * @author Techedge Group
 *
 */
public class CampaignSubscriptionStatusAdapter extends XmlAdapter<JAXBElement, CampaignSubscriptionStatus>
{
    private static final Logger LOG = Logger.getLogger(CampaignSubscriptionStatusAdapter.class);

    @Override
    public CampaignSubscriptionStatus unmarshal(final JAXBElement v) throws Exception
    {
        // Unmarshal function is not required
        throw new OperationNotSupportedException();
    }

    @Override
    public JAXBElement marshal(final CampaignSubscriptionStatus campaignSubscriptionStatus) throws Exception
    {
        if (null == campaignSubscriptionStatus)
        {
            return null;
        }

        // 1. Build the JAXBElement to wrap the instance of Parameter.
        final QName rootElement = new QName(campaignSubscriptionStatus.getFieldTag());
        final Integer value = Integer.valueOf(campaignSubscriptionStatus.getStatus());
        final JAXBElement jaxbElement = new JAXBElement(rootElement, Integer.class, value);

        // 2. Return the element to be added to the XML
        return jaxbElement;
    }
}
