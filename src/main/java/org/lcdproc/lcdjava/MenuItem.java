package org.lcdproc.lcdjava;


/**
 * Interface for menu items.
 * @author Stefan Krupop
 */
public interface MenuItem
{
    /** 
     * The name of the action item.
     */
    public static final String MENUITEM_ACTION = "action";

    /** 
     * The name of the checkbox item.
     */
    public static final String MENUITEM_CHECKBOX = "checkbox";

    /** 
     * The name of the ring item.
     */
    public static final String MENUITEM_RING = "ring";

    /** 
     * The name of the slider item.
     */
    public static final String MENUITEM_SLIDER = "slider";

    /** 
     * The name of the numeric item.
     */
    public static final String MENUITEM_NUMERIC = "numeric";

    /** 
     * The name of the alpha item.
     */
    public static final String MENUITEM_ALPHA = "alpha";

    /** 
     * The name of the IP item.
     */
    public static final String MENUITEM_IP = "ip";

    /** 
     * The name of the menu item.
     */
    public static final String MENUITEM_MENU = "menu";

    /** 
     * Get the menu id.
     * @return the menu id.
     */
    public String getID();

    /** 
     * Get the menu type.
     * @return the menu type.
     */
    public String getType();

    /** 
     * Return the data this item needs to update itself.
     * @return the data to update this item.
     */
    public String getData();

    /** 
     * Add this item to the Menu.
     * @return whether or not the item was added successfully.
     */
    public boolean activate();

    /** 
     * Remove this item.
     */
    public void remove();
}
