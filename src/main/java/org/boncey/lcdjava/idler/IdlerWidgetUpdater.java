package org.boncey.lcdjava.idler;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.boncey.lcdjava.LCD;
import org.boncey.lcdjava.ScrollerWidget;
import org.boncey.lcdjava.util.WidgetTimer;
import org.boncey.lcdjava.util.WidgetUpdater;
import org.boncey.lcdjava.Widget;

/**
 * ScrollerWidget that updates a Widget with the text of the provided idler.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: IdlerWidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class IdlerWidgetUpdater implements WidgetUpdater
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: IdlerWidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The ScrollerWidget we will be updating with the idler content.
     */
    private ScrollerWidget _content;

    /**
     * The ScrollerWidget we will be updating with the idler name.
     */
    private ScrollerWidget _name;

    /**
     * The list of Idler objects to update.
     */
    private List _idlers;

    /**
     * The index of the last Idler displayed.
     */
    private int _index = -1;

    /**
     * Store the currently displayed text so we don't update if it hasn't
     * changed (only happens if we have one idler or two identical ones in a
     * row).
     */
    private String _displayedText;

    /**
     * Public constructor.
     * @param content the Widget to display each Idler upon.
     * @param name the Widget to display the Idler name upon.
     * @param idlers the List of Idlers to display.
     */
    public IdlerWidgetUpdater(ScrollerWidget content,
                              ScrollerWidget name,
                              List idlers)
    {
        _content = content;
        _name = name;
        _idlers = idlers;
    }

    /**
     * Update the Widget by showing the next Idler.
     * @param timer the WidgetTimer.
     */
    public void updateWidget(WidgetTimer timer)
    {
        int size = _idlers.size();
        _index++;
        if (_index >= size)
        {
            _index = 0;
        }
        Idler idler = (Idler)_idlers.get(_index);
        if (idler.isValid())
        {
            String text = idler.getDisplayText();
            int timeout =
                (int)(text.length() * (_content.getSpeed() * LCD.FRAME));
            if (!text.equals(_displayedText))
            {
                _content.setText(text);
                _name.setText(idler.getName());
                _displayedText = text;
            }
            // This may have been reset if another Idler failed
            timer.setTimeout(timeout * IdlerLoader.ONE_SECOND);
        }
        else
        {
            // Invalid Idler, set timeout to one second and try the next one
            timer.setTimeout(1);
        }
    }

    /**
     * Retrurn the wrapped Widget.
     * @return the Widget.
     */
    public Widget getWidget()
    {
        return _content;
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Widget = " + _content +
               "; Name = " + _name;
    }
}
