package org.boncey.lcdjava;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract parent class for Widgets.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: AbstractWidget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public abstract class AbstractWidget implements Widget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: AbstractWidget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /**
     * The Widget id.
     */
    private int _id;

    /**
     * The Screen this Widget belongs to.
     */
    private Screen _screen;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected AbstractWidget(int id, Screen screen)
    {
        _id = id;
        _screen = screen;
    }

    /**
     * Get the Widget id.
     * @return the Widget id.
     */
    public int getId()
    {
        return _id;
    }

    /**
     * Add this Widget to the Screen that constructed us.
     * @return whether or not the widget was added successfully.
     */
    public boolean activate()
    {
        return _screen.addWidget(this);
    }

    /**
     * Remove this Widget from the Screen that constructed us.
     */
    public void remove()
    {
        _screen.removeWidget(this);
    }

    /**
     * Strip any quotes from the provided text.
     * @param text the text to strip quotes from.
     * @return the text with any quotes removed.
     */
    protected String stripQuotes(String text)
    {
        StringBuffer ret = new StringBuffer();
        if (text != null)
        {
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++)
            {
                if (chars[i] != '\"')
                {
                    ret.append(chars[i]);
                }
            }
        }
        return (text == null) ? null : ret.toString();
    }

    /**
     * Update this Widget's state.
     */
    protected void update()
    {
        _screen.updateWidget(this);
    }

    /**
     * Return a String representing this Widget.
     * @return a String representing this Widget.
     */
    public String toString()
    {
        return "Type = " + getType() +
               "; screen id = " + _screen.getId() +
               "; id = " + getId() +
               "; data = " + getData();
    }
}
