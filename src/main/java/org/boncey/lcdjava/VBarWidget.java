package org.boncey.lcdjava;

/**
 * Vertical Bar Widget.
 * <p>Displays a Vertical Bar on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: VBarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class VBarWidget extends BarWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: VBarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected VBarWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_VBAR;
    }

    /**
     * Construct a new VBarWidget.
     * @param screen the Screen that owns the Widget.
     * @param x the x position.
     * @param y the y position.
     * @param length the widget length.
     * @return a new VBarWidget.
     */
    public static VBarWidget construct(Screen screen, int x, int y, int length)
    {
        VBarWidget widget = null;

        try
        {
            widget = (VBarWidget)screen.constructWidget(Widget.WIDGET_VBAR);
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
