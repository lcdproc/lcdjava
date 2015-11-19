package org.boncey.lcdjava;

import java.util.HashMap;
import java.util.Map;


/**
 * Submenu.
 * <p>Displays the submenus name with tailing submenu marker
 * @author StefanKrupop
 */
public class Submenu extends AbstractMenuItem 
{
    /** 
     * The LCD that owns this Submenu.
     */
    private LCD _lcd;
    
    /** 
     * A Map of MenuItems added to this Submenu.
     */
    private Map<String, MenuItem> _menuItems;
    
    /** 
     * The count of menu items we have created.
     */
    private int _menuItemCounter;
    
    /**
     * Constructor.
     * @param lcd the LCD server object.
     * @param id the of the submenu.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected Submenu(LCD lcd, String id, Submenu menu)
    {
        super(id, menu);
        _lcd = lcd;
        _menuItems = new HashMap<String, MenuItem>();
    }

	public Submenu(LCD lcd) {
		this(lcd, "", null);
		_menu = this;
	}

	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MenuItem.MENUITEM_MENU;
    }

    /** 
     * Construct a new Submenu.
     * @param menu the Submenu that owns the submenu.
     * @param text the menu text.
     * @return a new Submenu.
     */
    public static Submenu construct(Submenu menu, String text)
    {
        Submenu menuItem = null;

        try
        {
            menuItem = (Submenu)menu.constructMenuItem(MenuItem.MENUITEM_MENU);
            menuItem.setText(text);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
    }

    /** 
     * Create a MenuItem for this Submenu.
     * @param type the menu item type, see {@link MenuItem} for a list of types.
     * @return the created MenuItem.
     * @throws LCDException if the menu item type is not recognized.
     */
    public MenuItem constructMenuItem(String type)
        throws LCDException
    {
        MenuItem item = null;

        synchronized (this) {
	        if (type.equals(MenuItem.MENUITEM_MENU))
	        {
	            item = new Submenu(_lcd, getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_ACTION))
	        {
	            item = new ActionMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_CHECKBOX))
	        {
	        	item = new CheckboxMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_RING))
	        {
	        	item = new RingMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_SLIDER))
	        {
	        	item = new SliderMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_NUMERIC))
	        {
	        	item = new NumericMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_ALPHA))
	        {
	        	item = new AlphaMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else if (type.equals(MenuItem.MENUITEM_IP))
	        {
	        	item = new IpMenuItem(getID() + "_" + Integer.toString(_menuItemCounter), this);
	        }
	        else
	        {
	            throw new LCDException(
	                    "Unable to create a menu item of type " + type);
	        }
	        
	        _menuItemCounter++;
        }

        return item;
    }

    /** 
     * Add a MenuItem to this Submenu.
     * @param item the MenuItem to add.
     * @return whether or not the menu item was added successfully.
     */
    protected boolean addItem(MenuItem item)
    {
        boolean success = false;

        String itemId = item.getID();
        synchronized (this) {
	        if (!_menuItems.containsKey(itemId))
	        {
	            _menuItems.put(itemId, item);
	            _lcd.write(LCD.CMD_MENU_ADD + "\"" + _id + "\"" + " " + itemId +
	                       " " + item.getType() + " \"\"");
	            success = updateItem(item);
	        }
        }

        return success;
    }
	
    /** 
     * Update a MenuItem on this Submenu.
     * @param item the MenuItem to update.
     * @return whether or not the MenuItem was updated successfully.
     */
    protected boolean updateItem(MenuItem item)
    {
        boolean success = false;

        String itemId = item.getID();
        synchronized (this) {
	        if (_menuItems.containsKey(itemId))
	        {
	            _lcd.write(LCD.CMD_MENU_SET + "\"" + _id + "\"" + " " + itemId +
	                       " " + item.getData());
	            success = true;
	        }
        }

        return success;
    }
    
    /** 
     * Delete a MenuItem from this Submenu.
     * @param item the MenuItem to delete.
     */
    protected void removeItem(MenuItem item)
    {
        String itemId = item.getID();
        synchronized (this) {
	        if (_menuItems.containsKey(itemId))
	        {
	            _lcd.write(LCD.CMD_MENU_DEL + "\"" + _id + "\"" + " " + itemId);
	            _menuItems.remove(itemId);
	        }
        }
    }

    /**
     * Return menu item with the given id
     * @param id the id of the menu item
     * @return the MenuItem or null when the ID does not exist
     */
	public MenuItem getMenuItem(String id) {
		return _menuItems.get(id);
	}
    
    /**
     * Sets this menu as the current main menu
     */
	public void setAsMainMenu() {
        synchronized (this) {
            _lcd.write(LCD.CMD_MENU_SET_MAIN + "\"" + _id + "\"");
        }
	}
}

