package com.contactlab.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Techedge Group
 * 
 */
@XmlRootElement(name = "dataroot")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailSubscriptionsXML
{
    @XmlElement(name = "RECORD")
    private List<EmailSubscriptionXML> emailSubscriptions;

    public List<EmailSubscriptionXML> getEmailSubscriptions()
    {
        return emailSubscriptions;
    }

    public void setEmailSubscriptions(final List<EmailSubscriptionXML> emailSubscriptions)
    {
        this.emailSubscriptions = emailSubscriptions;
    }
}
