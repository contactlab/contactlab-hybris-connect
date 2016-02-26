package com.contactlab.core;

import de.hybris.platform.jalo.JaloSession;

import java.io.Serializable;


/**
 * @author Techedge Group
 * 
 */
public class CoreJaloSession extends JaloSession
{

    public static final String SESSION_LOCK = "session.lock";

    /**
     * Session-based synch object, to prevent cart deadlocks (see @link{https://jira.hybris.com/browse/SUP-9012})
     */
    @Override
    protected void init()
    {
        super.init();
        setAttribute(SESSION_LOCK, new LockObject());
    }

    private static class LockObject implements Serializable
    {
        //
    }
}
