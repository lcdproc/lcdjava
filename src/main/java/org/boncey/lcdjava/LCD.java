package org.boncey.lcdjava;

import java.awt.event.ActionEvent;
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

import org.boncey.lcdjava.CheckboxMenuItem.CheckboxValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger _log = LoggerFactory.getLogger(LCD.class);

    /**
     * How often to poll for changes.
     */
    private static final int POLL = 100;

    /**
     * How many times to poll the server before giving up.
     */
    private static final int POLL_REPEAT = 30;

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
    private static final String CMD_CLIENT_SET = "client_set -name ";

    /**
     * The command to send to the server to add a Screen.
     */
    private static final String CMD_SCREEN_ADD = "screen_add ";

    /**
     * The command to send to the server to set (modify) a Screen.
     */
    private static final String CMD_SCREEN_SET = "screen_set ";

    /**
     * The command to send to the server to remove a Screen.
     */
    private static final String CMD_SCREEN_DEL = "screen_del ";

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
     * The command to send to the server to add a menu item.
     */
    public static final String CMD_MENU_ADD = "menu_add_item ";

    /**
     * The command to send to the server to remove a menu item.
     */
    public static final String CMD_MENU_DEL = "menu_del_item ";

    /**
     * The command to send to the server to set (modify) a MenuItem.
     */
    public static final String CMD_MENU_SET = "menu_set_item ";

    /**
     * The command to send to the server to set a menu as main menu
     */
    public static final String CMD_MENU_SET_MAIN = "menu_set_main ";

    /**
     * The response from LCDd that indicates an action menu item was selected.
     */
    private static final String EVENT_SELECT = "select";

    /**
     * The response from LCDd that indicates an menu item was updated.
     */
    private static final String EVENT_UPDATE = "update";

    /**
     * The response from LCDd that indicates a slider was moved to the right.
     */
    private static final String EVENT_PLUS = "plus";

    /**
     * The response from LCDd that indicates a slider was moved to the left.
     */
    private static final String EVENT_MINUS = "minus";

    /**
     * The protocol version that we know how to deal with.
     */
    private static final String PROTOCOL_VERSION = "0.3";

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
    private final Map<Integer, Screen> _screens;

    /**
     * The count of screens we have created.
     * <p>This is used internally to ensure Screens have unique ids.
     */
    private int _screenCounter;

    /**
     * The root of the client's menu
     */
    private final Submenu _rootMenu;

    /**
     * Public constructor.
     * @param host the LCDd host.
     * @param port the LCDd port.
     * @throws LCDException if there was a problem connecting to the server.
     */
    public LCD(String host, int port)
        throws LCDException
    {
    	this(host, port, "lcdjava/1.0");
    }

    /**
     * Public constructor.
     * @param host the LCDd host.
     * @param port the LCDd port.
     * @param clientName the name of this client as shown to the user
     * @throws LCDException if there was a problem connecting to the server.
     */
    public LCD(String host, int port, String clientName)
        throws LCDException
    {
        _screens = new HashMap<>();
        _rootMenu = new Submenu(this);

        try
        {
            String response = connect(host, port, clientName);
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
        _log.debug("Connected");
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
        Screen screen = _screens.get(screenId);
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
     * The user interacted with a menu
     * @param menuId the id of the menu item.
     * @param eventType the type of the event that occurred
     * @param value the value returned or <code>null</code>when not available
     */
    @Override
	public void menuAction(String menuId, String eventType, String value) {
		String menuParts[] = menuId.split("_");
		StringBuilder id = new StringBuilder();
		MenuItem menu = _rootMenu;
		for (String part : menuParts) {
			if (!"".equals(part)) {
				id.append('_');
				id.append(part);
				if (menu instanceof Submenu) {
					menu = ((Submenu)menu).getMenuItem(id.toString());
				} else {
					break;
				}
				if (menu == null) {
					break;
				}
			}
		}
		if (menu != null) {
            switch (eventType) {
                case LCD.EVENT_SELECT:
                    if (menu instanceof ActionMenuItem) {
                        ((ActionMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    }
                    break;
                case LCD.EVENT_UPDATE:
                    if (menu instanceof CheckboxMenuItem) {
                        if ("on".equals(value)) {
                            ((CheckboxMenuItem) menu).setValueNoUpdate(CheckboxValue.On);
                        } else if ("off".equals(value)) {
                            ((CheckboxMenuItem) menu).setValueNoUpdate(CheckboxValue.Off);
                        } else if ("gray".equals(value)) {
                            ((CheckboxMenuItem) menu).setValueNoUpdate(CheckboxValue.Gray);
                        }
                        ((CheckboxMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    } else if (menu instanceof RingMenuItem) {
                        ((RingMenuItem) menu).setValueNoUpdate(Integer.parseInt(value));
                        ((RingMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    } else if (menu instanceof NumericMenuItem) {
                        ((NumericMenuItem) menu).setValueNoUpdate(Integer.parseInt(value));
                        ((NumericMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    } else if (menu instanceof AlphaMenuItem) {
                        ((AlphaMenuItem) menu).setValueNoUpdate(value);
                        ((AlphaMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    } else if (menu instanceof IpMenuItem) {
                        ((IpMenuItem) menu).setValueNoUpdate(value);
                        ((IpMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                    }
                    break;
                case LCD.EVENT_PLUS:
                case LCD.EVENT_MINUS:
                    if (menu instanceof SliderMenuItem) {
                        int v = Integer.parseInt(value);
                        if (v != ((SliderMenuItem) menu).getValue()) {
                            ((SliderMenuItem) menu).setValueNoUpdate(v);
                            ((NumericMenuItem) menu).notifyActionPerformed(new ActionEvent(menu, ActionEvent.ACTION_PERFORMED, menu.getID()));
                        }
                    }
                    break;
            }
		}
	}

    /**
     * Shut down the server, terminating any threads.
     * @throws LCDException in case of a network problem.
     */
    public void shutdown()
        throws LCDException
    {
        _log.debug("Shutdown requested");
        if (_poller != null)
        {
            _poller.interrupt();
            _log.debug("Waiting for LCDSocketPoller to terminate...");
            try {
                _poller.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try
        {
            _log.debug("Closing socket");
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
     * @param name the Screen name.
     * @return the newly constructed Screen.
     */
    public synchronized Screen constructScreen(String name)
    {
        return constructScreen(name, Screen.PRIORITY_HIDDEN);
    }

    /**
     * Construct a new (unactivated) Screen.
     * @param name the Screen name.
     * @param priority the screen priority.
     * @return the newly constructed Screen.
     */
    public synchronized Screen constructScreen(String name, String priority)
    {
        return constructScreen(name, priority, false);
    }

    /**
     * Construct and optionally activate a new Screen.
     * @param name the Screen name.
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
        Integer id = screen.getId();
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
        Integer id = screen.getId();
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
        return _screens.remove(screenId);
    }

	public Submenu getRootMenu() {
		return _rootMenu;
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
     * @param clientName the name of this client as shown to the user
     * @return the response from the server.
     * @throws IOException in case of a network problem.
     */
    private String connect(String host, int port, String clientName)
        throws IOException
    {
        _socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    _socket.getInputStream()));
        _out = new BufferedWriter(new OutputStreamWriter(
                    _socket.getOutputStream()));
        _poller = new LCDSocketPoller(in, this);
        _poller.start();

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
        write(CMD_CLIENT_SET + clientName);

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
            throw new LCDException(e);
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
