package org.lcdproc.lcdjava;




/**
 * IpMenuItem.
 * <p>Allows the user to input an IP number (v4 or v6). When selected, a 
 * screen comes up that shows an IP number that can be edited - digit by digit 
 * - via left/right (switch digit) and up/down keys (increase/decrease). 
 * @author StefanKrupop
 */
public class IpMenuItem extends AbstractMenuItem 
{
	private String _value;
	private boolean _v6;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected IpMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _value = "";
        _v6 = false;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MenuItem.MENUITEM_IP;
    }
    
    /**
     * Sets current value of the entry
     * @param value current value 
     */
    public void setValue(String value) {
    	_value = value;    			
    	update();
    }
    
    /**
     * Sets current value of the entry but does not send the change to LCDd
     * @param value current value
     */
    public void setValueNoUpdate(String value) {
   		_value = value;
    }
    
    /**
     * Gets the currently set ip
     * @return value IP in string format
     */
    public String getValue() {
    	return _value;
    }

    /**
     * Sets if this entry is for v6 IP addresses
     * @param isV6 current value
     */
    public void setV6(boolean isV6) {
    	_v6 = isV6;    			
    	update();
    }

    @Override
    public String getData() {
    	StringBuilder strb = new StringBuilder();
    	strb.append(super.getData());
    	strb.append(" -value \"");
    	strb.append(_value);
    	strb.append("\" -v6 ");
    	strb.append(_v6 ? "true" : "false");
    	
    	return strb.toString();
    }

    /** 
     * Construct a new IpMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param current the current IP
     * @return a new IpMenuItem.
     */
    public static IpMenuItem construct(Submenu menu, String text, String current)
    {
        IpMenuItem menuItem = null;

        try
        {
            menuItem = (IpMenuItem)menu.constructMenuItem(MenuItem.MENUITEM_IP);
            menuItem.setValue(current);
            menuItem.setText(text);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
    }
}
