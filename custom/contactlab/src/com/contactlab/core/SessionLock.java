package com.contactlab.core;

import de.hybris.platform.jalo.JaloSession;

import org.apache.log4j.Logger;


/**
 * @author Techedge Group
 * 
 */
public class SessionLock
{

    protected static final Logger LOG = Logger.getLogger(SessionLock.class);

    public static <T> T doSynchronized(final Activity<T> activity)
    {
        final Object lock = getCurrentLock();
        if (lock != null)
        {
            synchronized (lock)
            {
                return activity.invoke();
            }
        }
        else
        {
            return activity.invoke();
        }
    }

    private static Object getCurrentLock()
    {
        return ((CoreJaloSession) JaloSession.getCurrentSession()).getAttribute(CoreJaloSession.SESSION_LOCK);
    }
}
