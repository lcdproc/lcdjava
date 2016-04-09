package org.boncey.lcdjava;

import org.apache.logging.log4j.Logger;

/**
 * Title Widget.
 * <p>Displays a title on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: TitleWidget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public class TitleWidget extends AbstractWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: TitleWidget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /**
     * The Widget text.
     */
    private String _text;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected TitleWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_TITLE;
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
        return "\"" + stripQuotes(_text) + "\"";
    }

    /**
     * Construct a new TitleWidget.
     * @param screen the Screen that owns the Widget.
     * @param text the widget text.
     * @return a new TitleWidget.
     */
    public static TitleWidget construct(Screen screen, String text)
    {
        TitleWidget widget = null;

        try
        {
            widget = (TitleWidget)screen.constructWidget(Widget.WIDGET_TITLE);
            widget.setText(text);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }

}

