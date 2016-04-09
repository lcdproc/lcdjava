package org.boncey.lcdjava.idler;

import org.apache.logging.log4j.Logger;
import org.boncey.lcdjava.LCDException;
import org.w3c.dom.Element;

/**
 * Display a static string that is configured from the XML config.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: StaticTextIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class StaticTextIdler extends AbstractIdler
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: StaticTextIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The text to display.
     */
    private String _text;

    /**
     * Public constructor, read the text from the config.
     * @param config the XML element for this Idler.
     * @param name the name of the Idler.
     * @throws LCDException if there was a problem.
     */
    public StaticTextIdler(Element config, String name)
        throws LCDException
    {
        super(config, name);

        _text = config.getAttribute("text");
    }

    /**
     * Get the text to display.
     * @return the text to display.
     */
    public String getDisplayText()
    {
        return _text;
    }

    /**
     * Idler is being destroyed, do nothing.
     */
    public void destroy()
    {
    }

    /**
     * Is there any text to display?
     * @return <code>true</code> if there is anything to display,
     * <code>false</code> otherwise.
     */
    public boolean isValid()
    {
        return true;
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Text = " + _text;
    }

}
