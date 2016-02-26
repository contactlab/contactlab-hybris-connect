package com.contactlab.systemsetup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.contactlab.constants.ContactlabConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 *
 * @author Techedge Group
 *
 */
@SystemSetup(extension = ContactlabConstants.EXTENSIONNAME)
public class ContactlabSystemSetup extends AbstractSystemSetup
{

    private static final Logger LOG = Logger.getLogger(ContactlabSystemSetup.class);

    public static final String IMPORT_IMPEX = "importImpex";

    @Override
    @SystemSetupParameterMethod
    public List<SystemSetupParameter> getInitializationOptions()
    {
        final List<SystemSetupParameter> systemSetupParams = new ArrayList<SystemSetupParameter>();

        return systemSetupParams;
    }

    @SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
    public void createEssentialData(final SystemSetupContext context) throws Exception
    {
        LOG.info("Import Email Marketing Project Data - Start");

        importImpexFile(context, "/contactlab/import/jobs-emailmarketing.impex");

        LOG.info("Import Email Marketing Project Data - End");

    }

}
