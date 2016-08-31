package org.lcdproc.lcdjava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * ActionMenuItem.
 * <p>Item to trigger an action
 * @author StefanKrupop
 */
public class ActionMenuItem extends AbstractMenuItem 
{
	public enum MenuResult {
		/**
		 * Menu stays as it is
		 */
		None("none"),
		/**
		 * Menu closes and returns to a higher level
		 */
		Close("close"),
		/**
		 * Quits the menu completely
		 */
		Quit("quit");
		
		public String val;
		
		MenuResult(String val) {
			this.val = val;
		}
	}
	
	private MenuResult _menuResult;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected ActionMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _menuResult = MenuResult.None;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MENUITEM_ACTION;
    }

    /**
     * Sets what to do with the menu when this action is selected
     * @param result the MenuResult
     */
    public void setMenuResult(MenuResult result) {
    	_menuResult = result;
    	update();
    }
    
    @Override
    public String getData() {
    	return super.getData() + " -menu_result " + _menuResult.val;
    }
    
    /** 
     * Construct a new ActionMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @return a new ActionMenuItem.
     */
    public static ActionMenuItem construct(Submenu menu, String text)
    {
    	return construct(menu, text, MenuResult.None);
    }
    
    /** 
     * Construct a new ActionMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param result the MenuResult defining what to do when action is selected
     * @return a new ActionMenuItem.
     */
    public static ActionMenuItem construct(Submenu menu, String text, MenuResult result)
    {
        ActionMenuItem menuItem = null;

        try
        {
            menuItem = (ActionMenuItem)menu.constructMenuItem(MENUITEM_ACTION);
            menuItem.setMenuResult(result);
            menuItem.setText(text);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
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
}

