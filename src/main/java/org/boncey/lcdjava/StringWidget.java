package org.boncey.lcdjava;

import org.apache.logging.log4j.Logger;

/**
 * String Widget.
 * <p>Displays a String on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: StringWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class StringWidget extends PositionalWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: StringWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The Widget text.
     */
    private String _text;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected StringWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_STRING;
    }

    /**
     * Set the Widget text.
     * @param text the Widget text.
     */
    public void setText(String text)
    {
        _text = text;
        update();
    }

    /**
     * Get the Widget text.
     * @return the Widget text.
     */
    public String getText()
    {
        return _text;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return super.getData() + "\"" + stripQuotes(_text) + "\"";
    }

    /**
     * Construct a new StringWidget.
     * @param screen the Screen that owns the Widget.
     * @param x the x position.
     * @param y the y position.
     * @param text the widget text.
     * @return a new StringWidget.
     */
    public static StringWidget construct(Screen screen,
                                         int x,
                                         int y,
                                         String text)
    {
        StringWidget widget = null;

        try
        {
            widget = (StringWidget)screen.constructWidget(Widget.WIDGET_STRING);
            widget.setX(x);
            widget.setY(y);
            widget.setText(text);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }
}

