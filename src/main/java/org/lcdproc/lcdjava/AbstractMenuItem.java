package org.boncey.lcdjava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.EventListenerList;


/**
 * Abstract parent class for menu items.
 * @author Stefan Krupop
 */
public abstract class AbstractMenuItem implements MenuItem
{
    /** 
     * The item id.
     */
    protected final String _id;
    
    /** 
     * The item text.
     */
    private String _text;

    /** 
     * The menu this item belongs to.
     */
    protected Submenu _menu;
    
    /** 
     * The event listeners connected to this item
     */
    protected EventListenerList _listeners;

    /**
     * Constructor.
     * @param id the of the item.
     * @param menu the menu to add this item to
     */
    protected AbstractMenuItem(String id, Submenu menu)
    {
        _id = id;
        _menu = menu;
        _listeners = new EventListenerList();
    }

    /** 
     * Get the item id.
     * @return the item id.
     */
    public String getID()
    {
        return _id;
    }
    
    /**
     * Set the menu item text.
     * @param text the menu item text.
     */
    public void setText(String text)
    {
        _text = text;
        update();
    }

    /** 
     * Get the menu item text.
     * @return the menu item text.
     */
    public String getText()
    {
        return _text;
    }

    /** 
     * Add this MenuItem to the Submenu that constructed us.
     * @return whether or not the menu item was added successfully.
     */
    public boolean activate()
    {
        return _menu.addItem(this);
    }

    /** 
     * Remove this MenuItem from the Screen that constructed us.
     */
    public void remove()
    {
        _menu.removeItem(this);
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
     * Update this MenuItems's state.
     */
    protected void update()
    {
        _menu.updateItem(this);
    }
    
    /** 
     * Return the data this menu item needs to update itself.
     * @return the data to update this menu item.
     */
    public String getData()
    {
        return "-text \"" + stripQuotes(_text) + "\"";
    }

    /**
     * Adds an ActionListener to the menu item.
     * @param listener the ActionListener to be added
     */
	public void addActionListener(ActionListener listener) {
		_listeners.add(ActionListener.class, listener);
	}

	/**
	 * Removes an ActionListener from the menu item.
	 * @param listener the ActionListener to be removed
	 */
	public void removeActionListener(ActionListener listener) {
		_listeners.remove(ActionListener.class, listener);
	}
    
    public void notifyActionPerformed(ActionEvent e) {
		for (ActionListener l : _listeners.getListeners(ActionListener.class)) {
			l.actionPerformed(e);
		}
    }
    
    /**
     * Return a String representing this MenuItem.
     * @return a String representing this MenuItem.
     */
    public String toString()
    {
        return "Type = " + getType() +
               "; menu id = " + _menu.getID() +
               "; id = " + getID() +
               "; data = " + getData();
    }
}
