package org.boncey.lcdjava.idler;

/**
 * Interface for anything that wants to display <i>idle information</i>
 * on an LCD display.
 * <p><i>idle information</i> is defined as a single line of scrolling text that
 * can be displayed.
 * <p>Implementations of Idler need to provide a constructor with the following
 * signature.
 * <code>Idler(Element config, String name)</code>
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: Idler.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public interface Idler
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: Idler.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /** 
     * Get the text to display.
     * @return the text to display.
     */
    public String getDisplayText();

    /** 
     * Destroy this Idler.
     */
    public void destroy();

    /** 
     * Is there any text to display?
     * @return <code>true</code> if there is anything to display,
     * <code>false</code> otherwise.
     */
    public boolean isValid();

    /** 
     * Get the name to display.
     * @return the name to display.
     */
    public String getName();
}
