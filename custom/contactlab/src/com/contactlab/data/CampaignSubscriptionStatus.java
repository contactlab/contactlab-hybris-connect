package com.contactlab.data;

/**
 * @author Techedge Group
 * 
 */
public class CampaignSubscriptionStatus
{

    private String fieldTag;

    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(final int status)
    {
        this.status = status;
    }

    public String getFieldTag()
    {
        return fieldTag;
    }

    public void setFieldTag(final String fieldTag)
    {
        this.fieldTag = fieldTag;
    }
}
