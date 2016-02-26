package com.contactlab.core;

/**
 * @author Techedge Group
 * 
 */
public abstract class Activity<T>
{

    public T invoke()
    {
        invokeNoResult();
        return null;
    }

    public void invokeNoResult()
    {
        //
    }
}
