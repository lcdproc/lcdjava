package org.boncey.lcdjava.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.boncey.lcdjava.Screen;

/**
 * A Runnable class to display a Screen for a specified time period.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: ScreenThread.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class ScreenThread implements Runnable
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: ScreenThread.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The screen we are displaying.
     */
    private Screen _screen;

    /**
     * How long to display the screen for.
     */
    private int _timeout;

    /**
     * The priority to set when displaying.
     */
    private String _priority;

    /**
     * Flag that tracks if we are displaying or not.
     */
    private boolean _displaying;

    /**
     * Flag that tracks if we are alive or not.
     */
    private boolean _alive = true;

    /**
     * Flag that tracks whether we should die after the first timeout or not.
     */
    private boolean _dieOnTimeout;

    /**
     * A List of TimeoutHook objects that want to be notified when the thread
     * times out.
     */
    private List<TimeoutHook> _timeoutHooks;

    /**
     * How many times to count down before calling stop.
     */
    private int _step;

    /**
     * How often to poll for changes.
     */
    private static final int POLL = 100;

    /**
     * Public constructor.
     * @param screen the Screen to monitor.
     * @param timeout how long to display for once woken up (in milliseconds).
     * @param priority the priority to set when awake.
     * @param dieOnTimeout whether we should die after the first timeout or not.
     */
    public ScreenThread(Screen screen,
                        int timeout,
                        String priority,
                        boolean dieOnTimeout)
    {
        _screen = screen;
        _timeout = timeout;
        _priority = priority;
        _dieOnTimeout = dieOnTimeout;

        _timeoutHooks = new ArrayList<>();
    }

    /**
     * Public constructor.
     * @param screen the Screen to monitor.
     * @param timeout how long to display for once woken up (in milliseconds).
     * @param priority the priority to set when awake.
     */
    public ScreenThread(Screen screen, int timeout, String priority)
    {
        this(screen, timeout, priority, false);
    }

    /**
     * Add a TimeoutHook object.
     * @param hook the TimeoutHook.
     */
    public void addTimeoutHook(TimeoutHook hook)
    {
        _timeoutHooks.add(hook);
    }

    /**
     * Remove a TimeoutHook object.
     * @param hook the TimeoutHook.
     * @return <code>true</code> if the TimeoutHook was removed,
     * <code>false</code> otherwise.
     */
    public boolean removeTimeoutHook(TimeoutHook hook)
    {
        return _timeoutHooks.remove(hook);
    }

    /**
     * Switch to the specified priority for the specified time period.
     */
    public void run()
    {
        while (_alive)
        {
            synchronized (this)
            {
                try
                {
                    Thread.sleep(POLL);
                    if (_displaying)
                    {
                        if (_step > 0)
                        {
                            _step--;
                            if (!_priority.equals(_screen.getPriority()))
                            {
                                start();
                            }
                        }
                        else
                        {
                            stop();
                            timeout();
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    // Do nothing
                }
            }
        }
    }

    /**
     * Switch to the specified priority (display the screen).
     */
    private void start()
    {
        _screen.setPriority(_priority);
    }

    /**
     * Set counter to zero then hide the screen.
     * <p>This triggers a timeout and any TimeoutHooks are notified.
     */
    private void stop()
    {
        _step = 0;
        hide();
    }

    /**
     * Switch to the hidden priority (hide the screen).
     * <p>Explicitly hide the screen without triggering a timeout.
     */
    public void hide()
    {
        _displaying = false;
        _screen.setPriority(Screen.PRIORITY_HIDDEN);
    }

    /**
     * Tell this thread to die gracefully.
     */
    public void destroy()
    {
        _alive = false;
    }

    /**
     * Are we displaying now?
     * @return <code>true</code> if displaying, <code>false</code> otherwise.
     */
    public boolean isDisplaying()
    {
        return _displaying;
    }

    /**
     * Tell the screen to display for the specified timeout.
     */
    public void display()
    {
        _step = ((int)_timeout / POLL);
        _displaying = true;
    }

    /**
     * Get the Screen we are wrapping.
     * @return the Screen we are wrapping.
     */
    public Screen getScreen()
    {
        return _screen;
    }

    /**
     * Call timeout for each TimeoutHook.
     */
    private void timeout()
    {
        for (Iterator i = _timeoutHooks.iterator(); i.hasNext();)
        {
            TimeoutHook hook = (TimeoutHook)i.next();
            hook.timeout(this);
        }

        if (_dieOnTimeout)
        {
            destroy();
        }
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Displaying = " + _displaying +
               "; priority = " + _priority +
               "; timeout = " + _timeout +
               "; screen = " + _screen;
    }

    /**
     * Determine if this ScreenThread is equal to the passed in Object.
     * <p>They are considered equal if the passed in Object is not null and is a
     * ScreenThread object that has the same Screen, priority and timeout value
     * as this ScreenThread.
     * @param object the Object to compare.
     * @return <code>true</code> if the ScreenThread is equal,
     * <code>false</code> otherwise.
     */
    public boolean equals(Object object)
    {
        boolean equal = false;

        if (object instanceof ScreenThread)
        {
            ScreenThread s = (ScreenThread)object;

            equal = (_screen != null && _screen.equals(s._screen)) &&
                    (_priority != null && _priority.equals(s._priority)) &&
                    _timeout == s._timeout;
        }

        return equal;
    }

    /**
     * Return the hash code for this ScreenThread.
     * <p>The hash is generated from a String representation of the Screen,
     * priority and timeout value.
     * @return the hash code of this ScreenThread.
     */
    public int hashCode()
    {
        return String.valueOf(_screen + _priority + _timeout).hashCode();
    }
}
