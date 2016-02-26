package com.contactlab.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;

import com.contactlab.constants.ContactlabConstants;


@SuppressWarnings("PMD")
public class ContactlabManager extends GeneratedContactlabManager
{
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ContactlabManager.class.getName());

    public static final ContactlabManager getInstance()
    {
        final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (ContactlabManager) em.getExtension(ContactlabConstants.EXTENSIONNAME);
    }

}
