package org.boncey.lcdjava;




/**
 * CheckboxnMenuItem.
 * <p>Item to display a slectable status indicator
 * @author StefanKrupop
 */
public class CheckboxMenuItem extends AbstractMenuItem 
{
	public enum CheckboxValue {
		/**
		 * Off
		 */
		Off("off"),
		/**
		 * On
		 */
		On("on"),
		/**
		 * Grayed out
		 */
		Gray("gray");
		
		public String val;
		
		CheckboxValue(String val) {
			this.val = val;
		}
	}
	
	private CheckboxValue _value;
	private boolean _allowGray;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected CheckboxMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _allowGray = false;
        _value = CheckboxValue.Off;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MenuItem.MENUITEM_CHECKBOX;
    }

    /**
     * Sets if a grayed checkbox is allowed
     * @param allowGray whether grayed checkbox is allowed
     */
    public void setAllowGray(boolean allowGray) {
    	_allowGray = allowGray;
    	update();
    }

    /**
     * Sets current value of a checkbox
     * @param value current status 
     */
    public void setValue(CheckboxValue value) {
    	_value = value;
    	update();
    }
    
    /**
     * Sets current value of a checkbox but does not send the change to LCDd
     * @param value current status
     */
    public void setValueNoUpdate(CheckboxValue value) {
    	_value = value;
    }
    
    /**
     * Gets the current status of the checkbox
     * @return status of the checkbox
     */
    public CheckboxValue getValue() {
    	return _value;
    }
    
    /**
     * Gets the current status of the checkbox
     * @return whether the checkbox is On
     */
    public boolean isSelected() {
    	return _value.equals(CheckboxValue.On);
    }
    
    @Override
    public String getData() {
    	return super.getData() + " -value " + _value.val + " -allow_gray " + (_allowGray ? "true" : "false");
    }
    
    /** 
     * Construct a new CheckboxMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @return a new ActionMenuItem.
     */
    public static CheckboxMenuItem construct(Submenu menu, String text)
    {
    	return construct(menu, text, false);
    }
    
    /** 
     * Construct a new ActionMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param allowGray whether the "Gray" state is allowed
     * @return a new ActionMenuItem.
     */
    public static CheckboxMenuItem construct(Submenu menu, String text, boolean allowGray)
    {
        CheckboxMenuItem menuItem = null;

        try
        {
            menuItem = (CheckboxMenuItem)menu.constructMenuItem(MenuItem.MENUITEM_CHECKBOX);
            menuItem.setAllowGray(allowGray);
            menuItem.setText(text);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
    }
}

