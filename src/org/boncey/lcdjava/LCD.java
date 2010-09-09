package org.boncey.lcdjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Class to control access to an LCDproc LCDd daemon.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: LCD.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public class LCD implements LCDListener
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: LCD.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /**
     * Logger for log4j.
     */
    private static Logger _log = Logger.getLogger(LCD.class);

    /** 
     * How often to poll for changes.
     */
    private static final int POLL = 100;

    /** 
     * How many times to poll the server before giving up.
     */
    public static final int POLL_REPEAT = 30;

    /** 
     * The pattern for matching a grouping of one or more numbers and .
     * characters.
     */
    private static final String PATTERN_NUMERIC = "([\\d\\.]+)";

    /** 
     * The pattern for matching the server version.
     * <p>All numeric for stable, text for CVS version.
     */
    private static final String PATTERN_VERSION = "([\\w\\-\\d\\.]+)";

    /**
     * The pattern for matching and grouping the init string returned by LCDd.
     */
    private static final String PATTERN_INIT =
        "connect LCDproc " + PATTERN_VERSION +
        " protocol " + PATTERN_NUMERIC +
        " lcd wid " + PATTERN_NUMERIC +
        " hgt " + PATTERN_NUMERIC +
        " cellwid " + PATTERN_NUMERIC +
        " cellhgt " + PATTERN_NUMERIC;

    /** 
     * The response from LCDd that indicates it didn't understand our command.
     */
    public static final String RESPONSE_ERROR = "huh?";

    /** 
     * The response from LCDd that indicates it is listening to a Screen.
     */
    public static final String RESPONSE_IGNORE = "ignore";

    /** 
     * The response from LCDd that indicates it is ignoring a Screen.
     */
    public static final String RESPONSE_LISTEN = "listen";

    /** 
     * The command to send to the server to initiate communication.
     */
    private static final String CMD_INIT = "hello";

    /** 
     * The command to send to the server to identify ourselves.
     */
    public static final String CMD_CLIENT_SET = "client_set -name lcdjava/1.0";

    /** 
     * The command to send to the server to add a Screen.
     */
    public static final String CMD_SCREEN_ADD = "screen_add ";

    /** 
     * The command to send to the server to set (modify) a Screen.
     */
    public static final String CMD_SCREEN_SET = "screen_set ";

    /** 
     * The command to send to the server to remove a Screen.
     */
    public static final String CMD_SCREEN_DEL = "screen_del ";

    /** 
     * The command to send to the server to add a Widget.
     */
    public static final String CMD_WIDGET_ADD = "widget_add ";

    /** 
     * The command to send to the server to set (modify) a Widget.
     */
    public static final String CMD_WIDGET_SET = "widget_set ";

    /** 
     * The command to send to the server to remove a Widget.
     */
    public static final String CMD_WIDGET_DEL = "widget_del ";

    /** 
     * The protocol version that we know how to deal with.
     */
    public static final String PROTOCOL_VERSION = "0.3";

    /** 
     * This is the maximum data that can be sent at once.
     * This is calculated from the following code in lcdproc-0.4.5/server/sock.c
     * #define MAXMSG 8192
     * ...
     * else if (nbytes &gt; (MAXMSG - (MAXMSG / 8))
     * {
     *     sock_send_string
     *         (filedes, "huh? Too much data received... quiet down!\n");
     * }
     */
    private static final int MAX_DATA_LENGTH = 7168;

    /** 
     * The measurement of a <i>LCD frame</i>, documented as one eight of a
     * second.
     */
    public static final float FRAME = 0.125f;

    /** 
     * The Socket used to talk and listen to the LCDd server.
     */
    private Socket _socket;

    /** 
     * The Writer we send all data to the server with.
     */
    private BufferedWriter _out;

    /** 
     * Thread that listens for responses from the server in a non-blocking
     * manner.
     */
    private LCDSocketPoller _poller;

    /** 
     * The software version of LCDd.
     */
    private String _version;
    
    /** 
     * The protocol version.
     */
    private String _protocolVersion;
    
    /** 
     * The width in characters of the LCD device.
     */
    private int _width;
    
    /** 
     * The height in characters of the LCD device.
     */
    private int _height;
    
    /** 
     * The cell width of the LCD device.
     */
    private int _cellWidth;
    
    /** 
     * The cell height of the LCD device.
     */
    private int _cellHeight;

    /** 
     * The Map of Screens, indexed by the Screen id.
     */
    private Map _screens;

    /** 
     * The count of screens we have created.
     * <p>This is used internally to ensure Screens have unique ids.
     */
    private int _screenCounter;

    /**
     * Public constructor.
     * @param host the LCDd host.
     * @param port the LCDd port.
     * @throws LCDException if there was a problem connecting to the server.
     */
    public LCD(String host, int port)
        throws LCDException
    {
        _screens = new HashMap();

        try
        {
            String response = connect(host, port);
            if (response == null)
            {
                throw new LCDException(
                        "Unable to get a response from the server at " +
                        host + ":" + port);
            }
            else if (response.startsWith(RESPONSE_ERROR))
            {
                throw new LCDException(
                        "Received error response from the server at " +
                        host + ":" + port);
            }

            parseInitString(response);
        }
        catch (IOException e)
        {
            shutdown();
            throw new LCDException(e);
        }
        catch (LCDException e)
        {
            shutdown();
            throw e;
        }

    }

    /**
     * Get the version.
     * @return the version.
     */
    public String getVersion()
    {
        return _version;
    }
    

    /**
     * Get the protocolVersion.
     * @return the protocolVersion.
     */
    public String getProtocolVersion()
    {
        return _protocolVersion;
    }
    

    /**
     * Get the width.
     * @return the width.
     */
    public int getWidth()
    {
        return _width;
    }
    

    /**
     * Get the height.
     * @return the height.
     */
    public int getHeight()
    {
        return _height;
    }
    

    /**
     * Get the cellWidth.
     * @return the cellWidth.
     */
    public int getCellWidth()
    {
        return _cellWidth;
    }
    

    /**
     * Get the cellHeight.
     * @return the cellHeight.
     */
    public int getCellHeight()
    {
        return _cellHeight;
    }

    /** 
     * The listen/ignore state of a Screen has been notified to us by LCDd.
     * @param screenId the id of the Screen.
     * @param listening <code>true</code> if the server is listening to us,
     * <code>false</code> if not.
     */
    public void setListenStatus(int screenId, boolean listening)
    {
        Screen screen = (Screen)_screens.get(new Integer(screenId));
        if (screen != null)
        {
            screen.setListening(listening);
        }
        else
        {
            _log.warn("Receiving signal " +
                    (listening ? "listen" : "ignore") +
                    " about screen id " +
                    screenId + " that we don't know anything about");
        }
    }

    /** 
     * Shut down the server, terminating any threads.
     * @throws LCDException in case of a network problem.
     */
    public void shutdown()
        throws LCDException
    {
        if (_poller != null)
        {
            _poller.shutdown();
            while (_poller.isPolling())
            {
                // loop until LCDSocketPoller has stopped polling
            }
        }

        try
        {
            if (_socket != null && !_socket.isClosed())
            {
                _socket.close();
            }
        }
        catch (IOException e)
        {
            throw new LCDException(e);
        }
    }

    /** 
     * Construct a new (unactivated) Screen.
     * @param name the Screeen name.
     * @return the newly constructed Screen.
     */
    public synchronized Screen constructScreen(String name)
    {
        return constructScreen(name, Screen.PRIORITY_HIDDEN);
    }

    /** 
     * Construct a new (unactivated) Screen.
     * @param name the Screeen name.
     * @param priority the screen priority.
     * @return the newly constructed Screen.
     */
    public synchronized Screen constructScreen(String name, String priority)
    {
        return constructScreen(name, priority, false);
    }

    /** 
     * Construct and optionally activate a new Screen.
     * @param name the Screeen name.
     * @param priority the screen priority.
     * @param activate <code>true</code> to activate this Screen immediately,
     * <code>false</code> to leave in unactivated.
     * @return the newly constructed Screen.
     */
    public synchronized Screen constructScreen(String name,
                                               String priority,
                                               boolean activate)
    {
        Screen screen = new Screen(this, _screenCounter, name);
        _screenCounter++;

        screen.setPriority(priority);

        if (activate)
        {
            screen.activate();
        }

        return screen;
    }

    /** 
     * Add this screen to the LCD server.
     * <p>There is no need to call this, call {@link Screen#activate()} instead.
     * @param screen the Screen to add.
     */
    protected synchronized void addScreen(Screen screen)
    {
        Integer id = new Integer(screen.getId());
        if (!_screens.containsKey(id))
        {
            _screens.put(id, screen);
            write(LCD.CMD_SCREEN_ADD + id);
            updateScreen(screen);
        }
    }

    /** 
     * Update this screen to the LCD server.
     * @param screen the Screen to update.
     */
    protected synchronized void updateScreen(Screen screen)
    {
        Integer id = new Integer(screen.getId());
        if (_screens.containsKey(id))
        {
            write(LCD.CMD_SCREEN_SET + screen.getData());
        }
    }

    /** 
     * Remove a Screen from the server.
     * @param screen the Screen to remove.
     * @return the removed Screen, or null if not removed.
     */
    protected synchronized Screen removeScreen(Screen screen)
    {
        return removeScreen(screen.getId());
    }

    /** 
     * Remove a Screen from the server.
     * @param screenId the Screen id to remove.
     * @return the removed Screen, or null if not removed.
     */
    protected synchronized Screen removeScreen(int screenId)
    {
        write(CMD_SCREEN_DEL + screenId);
        return (Screen)_screens.remove(new Integer(screenId));
    }

    /** 
     * Parse the init string and split up into groups.
     * @param init the init string.
     * @throws LCDException if the protocol version is one we don't know about.
     */
    private void parseInitString(String init)
        throws LCDException
    {
        Pattern p = Pattern.compile(PATTERN_INIT);
        Matcher m = p.matcher(init);
        if (m.matches())
        {
            _version = m.group(1);
            _protocolVersion = m.group(2);
            _width = Integer.parseInt(m.group(3));
            _height = Integer.parseInt(m.group(4));
            _cellWidth = Integer.parseInt(m.group(5));
            _cellHeight = Integer.parseInt(m.group(6));

            if (!PROTOCOL_VERSION.equals(_protocolVersion))
            {
                throw new LCDException("Incorrect protocol version, " +
                        "we only understand version " + PROTOCOL_VERSION);
            }
        }
        else
        {
            throw new LCDException(
                "Unable to parse parameters from LCDd response '" + init + "'");
        }
    }

    /** 
     * Connect to the LCDd server.
     * @param host the hostname to connect to.
     * @param port the port to connect to.
     * @return the response from the server.
     * @throws IOException in case of a network problem.
     */
    private String connect(String host, int port)
        throws IOException
    {
        _socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    _socket.getInputStream()));
        _out = new BufferedWriter(new OutputStreamWriter(
                    _socket.getOutputStream()));
        _poller = new LCDSocketPoller(in, this);
        Thread t = new Thread(_poller);
        t.start();

        write(CMD_INIT);
        String response = null;

        for (int i = 0; i < POLL_REPEAT && response == null; i++)
        {
            synchronized (this)
            {
                try
                {
                    Thread.sleep(POLL);
                    response = _poller.getLastLine();
                }
                catch (InterruptedException e)
                {
                    // Do nothing
                }
            }
        }
        write(CMD_CLIENT_SET);

        return response;
    }

    /** 
     * Write the data to the server.
     * @param text the text to write.
     */
    protected void write(String text)
    {
        try
        {
            String tmpString = text;
            if (text.length() >= MAX_DATA_LENGTH)
            {
                _log.error("Text too long: " + text.length() + ", truncating");
                tmpString = text.substring(0, MAX_DATA_LENGTH - 1);
            }
            _out.write(tmpString);
            _out.newLine();
            _out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Version = " + _version +
            "; protocol version = " + _protocolVersion +
            "; width = " + _width +
            "; height = " + _height +
            "; cell width = " + _cellWidth +
            "; cell height = " + _cellHeight;

    }
}
