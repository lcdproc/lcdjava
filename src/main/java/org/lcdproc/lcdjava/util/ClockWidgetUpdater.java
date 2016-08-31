package org.lcdproc.lcdjava.util;

import org.lcdproc.lcdjava.StringWidget;
import org.lcdproc.lcdjava.Widget;

import java.text.DateFormat;
import java.util.Date;

/**
 * StringWidget that updates with the current time.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: ClockWidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class ClockWidgetUpdater implements WidgetUpdater
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: ClockWidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The StringWidget we will be updating.
     */
    private StringWidget _widget;

    /**
     * Store the time as a String so we only write it when it has changed.
     */
    private String _timeString;

    /**
     * Public constructor.
     * @param widget the Widget with which to display the clock.
     */
    public ClockWidgetUpdater(StringWidget widget)
    {
        _widget = widget;
    }

    /**
     * Update the clock.
     * @param timer the WidgetTimer.
     */
    public void updateWidget(WidgetTimer timer)
    {
        DateFormat fmt = DateFormat.getTimeInstance(DateFormat.SHORT);
        String time = fmt.format(new Date());
        if (!time.equals(_timeString))
        {
            _widget.setText(time);
            _timeString = time;
        }
    }

    /**
     * Retrurn the wrapped Widget.
     * @return the Widget.
     */
    public Widget getWidget()
    {
        return _widget;
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Widget = " + _widget;
    }
}
