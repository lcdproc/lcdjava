package org.boncey.lcdjava;

import org.apache.log4j.Logger;

/**
 * Horizontal Bar Widget.
 * <p>Displays a Horizontal Bar on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: HBarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class HBarWidget extends BarWidget 
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: HBarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * Logger for log4j.
     */
    private static Logger _log = Logger.getLogger(LCD.class);

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected HBarWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /** 
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_HBAR;
    }

    /** 
     * Construct a new HBarWidget.
     * @param screen the Screen that owns the Widget.
     * @param x the x position.
     * @param y the y position.
     * @param length the widget length.
     * @return a new HBarWidget.
     */
    public static HBarWidget construct(Screen screen, int x, int y, int length)
    {
        HBarWidget widget = null;

        try
        {
            widget = (HBarWidget)screen.constructWidget(Widget.WIDGET_HBAR);
            widget.setX(x);
            widget.setY(y);
            widget.setLength(length);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }
}
