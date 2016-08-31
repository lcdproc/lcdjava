package org.boncey.lcdjava.util;

import org.boncey.lcdjava.Widget;

/**
 * A Runnable class to update a Widget on a specified time period.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: WidgetTimer.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class WidgetTimer implements Runnable
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: WidgetTimer.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The class that holds a Widget for updating.
     */
    private WidgetUpdater _updater;

    /**
     * Flag that tracks if we are alive or not.
     */
    private boolean _alive = true;

    /**
     * How long to display the widget for (in milliseconds).
     */
    private int _timeout;

    /**
     * Public constructor.
     * @param updater the WidgetUpdater that will update when displaying.
     * @param timeout how long to display the widget for (in milliseconds).
     */
    public WidgetTimer(WidgetUpdater updater, int timeout)
    {
        _updater = updater;
        _timeout = timeout;
    }

    /**
     * Switch to the specified priority for the specified time period.
     */
    public void run()
    {
        while (_alive)
        {
            try
            {
                _updater.updateWidget(this);
                Thread.sleep(_timeout);
            }
            catch (InterruptedException e)
            {
                // Do nothing
            }
        }
    }

    /**
     * Return the wrapped Widget.
     * @return the Widget.
     */
    public Widget getWidget()
    {
        return _updater.getWidget();
    }

    /**
     * Alter the timeout value and wake up the thread.
     * @param timeout the new timeout value.
     */
    public synchronized void setTimeout(int timeout)
    {
        _timeout = timeout;
        notify();
    }

    /**
     * Tell this thread to die gracefully.
     */
    public synchronized void destroy()
    {
        _alive = false;
        _timeout = 0;
        notify();
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Updater = " + _updater + "; Timeout = " + _timeout;
    }
}
