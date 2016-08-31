package org.lcdproc.lcdjava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * RingMenuItem.
 * <p>Item to display a text and a status indicator.
 * The status can be one of the strings specified for the item. 
 * @author StefanKrupop
 */
public class RingMenuItem extends AbstractMenuItem 
{
	private int _value;
	private List<String> _items;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected RingMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _items = new ArrayList<String>();
        _value = 0;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MENUITEM_RING;
    }
    
    public void setItems(List<String> items) {
    	_items = items;
    	_value = 0;
    	update();
    }

    /**
     * Sets current value of the ring
     * @param value current value 
     */
    public void setValue(String value) {
    	for (int i = 0; i < _items.size(); ++i) {
    		if (value.equals(_items.get(i))) {
    	    	_value = i;    			
    		}
    	}
    	update();
    }
    
    /**
     * Sets current value of the ring but does not send the change to LCDd
     * @param value current value
     */
    public void setValueNoUpdate(int value) {
    	if (value >= 0 && value < _items.size()) {
    		_value = value;
    	}
    }
    
    /**
     * Gets the current value of the ring
     * @return value of the ring
     */
    public String getValue() {
    	return _items.get(_value);
    }
        
    @Override
    public String getData() {
    	StringBuilder strb = new StringBuilder();
    	strb.append(super.getData());
    	strb.append(" -value ");
    	strb.append(_value);
    	strb.append(" -strings \"");
    	boolean first = true;
    	for (String item : _items) {
    		if (!first) {
    			strb.append('\t');
    		}
    		strb.append(item);
    		first = false;
    	}
    	strb.append('"');
    	
    	return strb.toString();
    }

    /** 
     * Construct a new RingMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param items the items in the ring
     * @return a new RingMenuItem.
     */
    public static RingMenuItem construct(Submenu menu, String text, String... items)
    {
    	return construct(menu, text, Arrays.asList(items));
    }
    
    /** 
     * Construct a new RingMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param items the items in the ring
     * @return a new RingMenuItem.
     */
    public static RingMenuItem construct(Submenu menu, String text, List<String> items)
    {
        RingMenuItem menuItem = null;

        try
        {
            menuItem = (RingMenuItem)menu.constructMenuItem(MENUITEM_RING);
            menuItem.setItems(items);
            menuItem.setText(text);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
    }
}
