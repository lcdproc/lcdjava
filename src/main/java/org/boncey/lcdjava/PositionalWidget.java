package org.boncey.lcdjava;

import org.apache.logging.log4j.Logger;

/**
 * Abstract Widget that deals with x and y dimensions.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: PositionalWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public abstract class PositionalWidget extends AbstractWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: PositionalWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The x position.
     */
    private int _x = 1;

    /**
     * The y position.
     */
    private int _y = 1;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected PositionalWidget(int id, Screen screen)
    {
        super(id, screen);
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
     * Set the y position.
     * @param y the y position.
     */
    public void setY(int y)
    {
        _y = y;
    }

    /**
     * Get the y position.
     * @return the y position.
     */
    public int getY()
    {
        return _y;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return getX() + " " + getY() + " ";
    }
}
