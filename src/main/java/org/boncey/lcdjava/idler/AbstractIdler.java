package org.boncey.lcdjava.idler;

import org.apache.logging.log4j.Logger;
import org.boncey.lcdjava.LCDException;
import org.w3c.dom.Element;

/**
 * Display a static string that is configured from the XML config.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: AbstractIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public abstract class AbstractIdler implements Idler
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: AbstractIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The Idler name.
     */
    private String _name;

    /**
     * Idler is being destroyed, do nothing.
     */
    public void destroy()
    {
    }

    /**
     * Public constructor, read the text from the config.
     * @param config the XML element for this Idler.
     * @param name the name of the Idler.
     * @throws LCDException if there was a problem.
     */
    public AbstractIdler(Element config, String name)
        throws LCDException
    {
        _name = name;
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Name = " + _name;
    }

    /**
     * Get the name to display.
     * @return the name to display.
     */
    public String getName()
    {
        return _name;
    }
}
