package com.contactlab.data;

import java.util.List;


/**
 * @author Techedge Group
 *
 */
public class ExportProductData
{

    private String code;

    private String name;

    private String description;

    private String pictureUrl;

    private String categoryName;

    private String brandName;

    private List<String> priceLsit;

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the pictureUrl
     */
    public String getPictureUrl()
    {
        return pictureUrl;
    }

    /**
     * @param pictureUrl
     *            the pictureUrl to set
     */
    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName()
    {
        return categoryName;
    }

    /**
     * @param categoryName
     *            the categoryName to set
     */
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    /**
     * @return the brandName
     */
    public String getBrandName()
    {
        return brandName;
    }

    /**
     * @param brandName
     *            the brandName to set
     */
    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    /**
     * @return the priceLsit
     */
    public List<String> getPriceLsit()
    {
        return priceLsit;
    }

    /**
     * @param priceLsit
     *            the priceLsit to set
     */
    public void setPriceLsit(List<String> priceLsit)
    {
        this.priceLsit = priceLsit;
    }

}
