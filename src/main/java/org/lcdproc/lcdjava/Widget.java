package org.lcdproc.lcdjava;

/**
 * Interface for Widgets.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: Widget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $
 */
public interface Widget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: Widget.java,v 1.3 2008-07-06 15:38:34 boncey Exp $";

    /** 
     * The name of the string Widget.
     */
    public static final String WIDGET_STRING = "string";

    /** 
     * The name of the hbar Widget.
     */
    public static final String WIDGET_HBAR = "hbar";

    /** 
     * The name of the vbar Widget.
     */
    public static final String WIDGET_VBAR = "vbar";

    /** 
     * The name of the num Widget.
     */
    public static final String WIDGET_NUM = "num";

    /** 
     * The name of the title Widget.
     */
    public static final String WIDGET_TITLE = "title";

    /** 
     * The name of the scroller Widget.
     */
    public static final String WIDGET_SCROLLER = "scroller";

    /** 
     * The name of the icon Widget.
     */
    public static final String WIDGET_ICON = "icon";

    /** 
     * Get the Widget id.
     * @return the Widget id.
     */
    public int getId();

    /** 
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType();

    /** 
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData();

    /** 
     * Add this Widget to the Screen that constructed us.
     * @return whether or not the widget was added successfully.
     */
    public boolean activate();

    /** 
     * Remove this Widget from the Screen that constructed us.
     */
    public void remove();
}
