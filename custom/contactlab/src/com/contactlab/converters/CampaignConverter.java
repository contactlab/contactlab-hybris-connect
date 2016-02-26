package com.contactlab.converters;

import de.hybris.platform.converters.impl.AbstractConverter;

import org.springframework.util.Assert;

import com.contactlab.data.CampaignData;
import com.contactlab.model.CampaignModel;


/**
 * <<<<<<< HEAD
 * 
 * @author contactlabgroup.com =======
 * @author Techedge Group >>>>>>> 82eeef1e7a698ef82aa31292135efa6db031f7b6
 * 
 */
public class CampaignConverter extends AbstractConverter<CampaignModel, CampaignData>
{
    @Override
    protected CampaignData createTarget()
    {
        return new CampaignData();
    }

    @Override
    public void populate(final CampaignModel source, final CampaignData target)
    {
        Assert.notNull(source, "Parameter CampaignModel cannot be null!");

        target.setId(source.getCampaignId().intValue());
        target.setLabel(source.getCampaignName());
        target.setWebFormCode(source.getWebFormCode());
    }
}
