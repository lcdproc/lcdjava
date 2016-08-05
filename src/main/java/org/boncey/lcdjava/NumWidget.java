package org.boncey.lcdjava;

/**
 * Number Widget.
 * <p>Displays a number on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: NumWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class NumWidget extends AbstractWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: NumWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The Widget number.
     */
    private int _number;

    /**
     * The x position.
     */
    private int _x = 1;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected NumWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_NUM;
    }

    /**
     * Set the x position.
     * @param x the x position.
     */
    public void setX(int x)
    {
        _x = x;
    }

    /**
     * Get the x position.
     * @return the x position.
     */
    public int getX()
    {
        return _x;
    }

    /**
     * Set the Widget number.
     * @param number the Widget number.
     */
    public void setNumber(int number)
    {
        _number = number;
        update();
    }

    /**
     * Get the Widget number.
     * @return the Widget number.
     */
    public int getNumber()
    {
        return _number;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return _x + " " + _number;
    }

    /**
     * Construct a new NumWidget.
     * @param screen the Screen that owns the Widget.
     * @param x the x position.
     * @param number the number.
     * @return a new NumWidget.
     */
    public static NumWidget construct(Screen screen, int x, int number)
    {
        NumWidget widget = null;

        try
        {
            widget = (NumWidget)screen.constructWidget(Widget.WIDGET_NUM);
            widget.setX(x);
            widget.setNumber(number);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }

}

