package org.boncey.lcdjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Thread that listens for data on the LCD socket.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: LCDSocketPoller.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public class LCDSocketPoller implements Runnable
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: LCDSocketPoller.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /**
     * Logger for log4j.
     */
    private static Logger _log = Logger.getLogger(LCDSocketPoller.class);

    /** 
     * How often to poll for changes.
     */
    private static final int POLL = 100;

    /**
     * The Pattern that matches ignore/listen events.
     */
    private static final Pattern IGNORE_STATUS = Pattern.compile(
            "(ignore|listen)\\s+(\\d+).*");

    /** 
     * The Reader to read data from.
     */
    private BufferedReader _in;

    /** 
     * The last line of data received.
     * <p>Will be null if no data was received.
     */
    private String _lastLine;

    /** 
     * Flag that tracks if we are alive or not.
     */
    private boolean _alive = true;

    /** 
     * Flag that tracks if we are polling or not.
     */
    private boolean _polling = true;

    /** 
     * The listener to notify of listen/ignore events.
     */
    private LCDListener _listener;

    /**
     * Public constructor.
     * @param in the BufferedReader that will recieve data from the server.
     * @param listener the LCDListener that gets notified of screens being
     * listened to or ignored.
     */
    public LCDSocketPoller(BufferedReader in, LCDListener listener)
    {
        _in = in;
        _listener = listener;
    }

    /** 
     * Poll the socket every 100 milliseconds.
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
                    if (_in.ready())
                    {
                        _lastLine = _in.readLine();
                        Matcher m = IGNORE_STATUS.matcher(_lastLine);
                        if (_lastLine.startsWith(LCD.RESPONSE_ERROR))
                        {
                            _log.warn("Got a response of " + _lastLine +
                                    " from server");
                        }
                        else if (m.matches())
                        {
                            if (_listener != null)
                            {
                                boolean listen = (LCD.RESPONSE_LISTEN.equals(
                                            m.group(1)));
                                try
                                {
                                    int screenId = Integer.parseInt(m.group(2));
                                    _listener.setListenStatus(screenId, listen);
                                }
                                catch (NumberFormatException e)
                                {
                                    // Ignore
                                }
                            }
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    // Do nothing
                }
                catch (IOException e)
                {
                    _log.error("Caught IOException", e);
                }
            }
        }

        _polling = false;
    }

    /** 
     * Get the last line received <i>non-blocking</i>.
     * <p>Calling this clears the last line received.
     * @return the last line received.
     */
    public synchronized String getLastLine()
    {
        String ret = _lastLine;
        _lastLine = null;
        return ret;
    }

    /** 
     * Tell this thread to die gracefully.
     */
    public void shutdown()
    {
        _alive = false;
    }

    /** 
     * Is the server still polling for data.
     * <p>Call this after shutdown to see if we are still polling.
     * @return <code>true</code> if still polling, <code>false</code> otherwise.
     */
    public boolean isPolling()
    {
        return _polling;
    }
}
