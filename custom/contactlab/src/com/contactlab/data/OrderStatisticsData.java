package com.contactlab.data;

import java.util.Date;


/**
 * @author Techedge Group
 * 
 */
public class OrderStatisticsData
{
    private Date date;

    private Double value;

    private Double items;

    private Integer count;

    public Double getValue()
    {
        return value;
    }

    public void setValue(final Double value)
    {
        this.value = value;
    }

    public Double getItems()
    {
        return items;
    }

    public void setItems(final Double items)
    {
        this.items = items;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(final Integer count)
    {
        this.count = count;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(final Date date)
    {
        this.date = date;
    }
}
