package org.boncey.lcdjava;

import java.util.HashMap;
import java.util.Map;

/**
  A Screen that can contain Widgets.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: Screen.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public class Screen
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: Screen.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /**
     * Set heartbeat to 'Heart icon'.
     */
    public static final String HEARTBEAT_ON = "on";

    /**
     * Set heartbeat to 'Heart icon'.
     */
    public static final String HEARTBEAT_HEART = "heart";

    /**
     * Set heartbeat to 'User's choice'.
     */
    public static final String HEARTBEAT_NORMAL = "normal";

    /**
     * Set heartbeat to 'User's choice'.
     */
    public static final String HEARTBEAT_DEFAULT = "default";

    /**
     * Set heartbeat to 'None'.
     */
    public static final String HEARTBEAT_OFF = "off";

    /**
     * Set heartbeat to 'None'.
     */
    public static final String HEARTBEAT_NONE = "none";

    /**
     * Set heartbeat to 'Rotating slash'.
     */
    public static final String HEARTBEAT_SLASH = "slash";

    /**
     * The LCD that owns this Screen.
     */
    private LCD _lcd;

    /**
     * The id of the Screen.
     */
    private int _id;

    /**
     * The name of the Screen.
     */
    private String _name;

    /**
     * The priority of the Screen.
     */
    private String _priority;

    /**
     * The duration of the Screen.
     */
    private int _duration;

    /**
     * The width of the Screen.
     */
    private int _width;

    /**
     * The height of the Screen.
     */
    private int _height;

    /**
     * A Map of Widgets added to this Screen.
     */
    private Map<Integer, Widget> _widgets;

    /**
     * The count of widgets we have created.
     */
    private int _widgetCounter;

    /**
     * Whether or not the server is listening to this screen.
     */
    private boolean _listening;

    /**
     * The heartbeat type.
     */
    private String _heartbeat = HEARTBEAT_OFF;

    /**
     * The priority to set a Screen to "hidden" level.
     */
    public static final String PRIORITY_HIDDEN = "hidden";

    /**
     * The priority to set a Screen to "background" level.
     */
    public static final String PRIORITY_BACKGROUND = "background";

    /**
     * The priority to set a Screen to "info" level.
     */
    public static final String PRIORITY_INFO = "info";

    /**
     * The priority to set a Screen to "foreground" level.
     */
    public static final String PRIORITY_FOREGROUND = "foreground";

    /**
     * The priority to set a Screen to "alert" level.
     */
    public static final String PRIORITY_ALERT = "alert";

    /**
     * Protected constructor.
     * <p>There is no need to call this contructor, call
     * {@link LCD#constructScreen(String)} instead.
     * @param lcd the LCD server object.
     * @param id the Screen id.
     * @param name the Screen name.
     */
    protected Screen(LCD lcd, int id, String name)
    {
        _lcd = lcd;
        _id = id;
        _name = name;
        _widgets = new HashMap<>();
    }

    /**
     * Protected constructor.
     * <p>There is no need to call this contructor, call
     * {@link LCD#constructScreen(String)} instead.
     * @param lcd the LCD server object.
     * @param id the Screen id.
     */
    protected Screen(LCD lcd, int id)
    {
        this(lcd, id, null);
    }

    /**
     * Get the screen id.
     * @return the screen id.
     */
    public int getId()
    {
        return _id;
    }

    /**
     * Set the name.
     * @param name the name.
     */
    public void setName(String name)
    {
        _name = name;
        update();
    }

    /**
     * Get the name.
     * @return the name.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * Set the priority.
     * @param priority the priority.
     */
    public void setPriority(String priority)
    {
        _priority = priority;
        update();
    }

    /**
     * Get the priority.
     * @return the priority.
     */
    public String getPriority()
    {
        return _priority;
    }


    /**
     * Set the duration.
     * @param duration the duration.
     */
    public void setDuration(int duration)
    {
        _duration = duration;
        update();
    }

    /**
     * Get the duration.
     * @return the duration.
     */
    public int getDuration()
    {
        return _duration;
    }


    /**
     * Set the width.
     * @param width the width.
     */
    public void setWidth(int width)
    {
        _width = width;
        update();
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
     * Set the height.
     * @param height the height.
     */
    public void setHeight(int height)
    {
        _height = height;
        update();
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
     * Set the heartbeat.
     * @param heartbeat the heartbeat.
     */
    public void setHeartbeat(String heartbeat)
    {
        _heartbeat = heartbeat;
        update();
    }

    /**
     * Get the heartbeat.
     * @return the heartbeat.
     */
    public String getHeartbeat()
    {
        return _heartbeat;
    }

    /**
     * Set if the server is listening to us.
     * @param listening <code>true</code> if the server is listening to us,
     * <code>false</code> if not.
     */
    public void setListening(boolean listening)
    {
        _listening = listening;
    }

    /**
     * Is the server listening to us?
     * @return <code>true</code> if the server is listening to us,
     * <code>false</code> if not.
     */
    public boolean isListening()
    {
        return _listening;
    }

    /**
     * Update this Screen's state to the server.
     */
    private void update()
    {
        _lcd.updateScreen(this);
    }

    /**
     * Activate this Screen.
     */
    public void activate()
    {
        _lcd.addScreen(this);
    }

    /**
     * Remove this Screen.
     */
    public void remove()
    {
        _lcd.removeScreen(this);
    }

    /**
     * Get this Screen's data in a format for writing to the LCDd server.
     * @return this Screen's state.
     */
    public String getData()
    {
        return _id +
               " -priority " + _priority +
               " -name \"" + _name + "\"" +
               " -duration " + _duration +
               " -wid " + _width +
               " -hgt " + _height +
               " -heartbeat " + _heartbeat;
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return " priority " + _priority +
               " name \"" + _name + "\"" +
               " duration " + _duration +
               " wid " + _width +
               " hgt " + _height +
               " heartbeat " + _heartbeat;
    }

    /**
     * Add a Widget to this Screen.
     * @param widget the Widget to add.
     * @return whether or not the widget was added successfully.
     */
    protected synchronized boolean addWidget(Widget widget)
    {
        boolean success = false;

        Integer widgetId = new Integer(widget.getId());
        if (!_widgets.containsKey(widgetId))
        {
            _widgets.put(widgetId, widget);
            _lcd.write(LCD.CMD_WIDGET_ADD + _id + " " + widgetId +
                       " " + widget.getType());
            success = updateWidget(widget);
        }

        return success;
    }

    /**
     * Update a Widget to this Screen.
     * @param widget the Widget to update.
     * @return whether or not the widget was updated successfully.
     */
    protected synchronized boolean updateWidget(Widget widget)
    {
        boolean success = false;

        Integer widgetId = new Integer(widget.getId());
        if (_widgets.containsKey(widgetId))
        {
            _lcd.write(LCD.CMD_WIDGET_SET + _id + " " + widgetId +
                       " " + widget.getData());
            success = true;
        }

        return success;
    }

    /**
     * Delete a Widget from this Screen.
     * @param widget the Widget to delete.
     */
    protected synchronized void removeWidget(Widget widget)
    {
        Integer widgetId = new Integer(widget.getId());
        if (_widgets.containsKey(widgetId))
        {
            _lcd.write(LCD.CMD_WIDGET_DEL + _id + " " + widgetId);
            _widgets.remove(widgetId);
        }
    }

    /**
     * Create a Widget for this screen.
     * @param type the Widget type, see {@link Widget} for a list of types.
     * @return the created Widget.
     * @throws LCDException if the Widget type is not recognised.
     */
    public synchronized Widget constructWidget(String type)
        throws LCDException
    {
        Widget widget;

        if (type.equals(Widget.WIDGET_TITLE))
        {
            widget = new TitleWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_STRING))
        {
            widget = new StringWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_HBAR))
        {
            widget = new HBarWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_VBAR))
        {
            widget = new VBarWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_NUM))
        {
            widget = new NumWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_SCROLLER))
        {
            widget = new ScrollerWidget(_widgetCounter, this);
        }
        else if (type.equals(Widget.WIDGET_ICON))
        {
            widget = new IconWidget(_widgetCounter, this);
        }
        else
        {
            throw new LCDException(
                    "Unable to create a Widget of type " + type);
        }

        _widgetCounter++;

        return widget;
    }

    /**
     * Determine if this Screen is equal to the passed in Object.
     * <p>They are considered equal if the passed in Object is not null and is a
     * Screen object that has the same id as this Screen.
     * <p>Note: Only the id is used for comparison, all other fields are
     * ignored.
     * @param object the Object to compare.
     * @return <code>true</code> if the Screen is equal, <code>false</code>
     * otherwise.
     */
    public boolean equals(Object object)
    {
        boolean equal = false;

        if (object instanceof Screen)
        {
            Screen s = (Screen)object;

            equal = _id == s._id;
        }

        return equal;
    }

    /**
     * Return the hash code for this Screen, simply the id of the Screen.
     * @return the id of the Screen.
     */
    public int hashCode()
    {
        return _id;
    }
}
