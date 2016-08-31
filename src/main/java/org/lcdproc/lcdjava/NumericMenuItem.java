package org.boncey.lcdjava;




/**
 * NumericMenuItem.
 * <p>Allows the user to input an integer value. Is visible as a text. When 
 * selected, a screen comes up that shows the current numeric value, that you 
 * can edit with the cursor keys and Enter. The number is ended by selecting 
 * a 'null' input digit. After that the menu returns. 
 * @author StefanKrupop
 */
public class NumericMenuItem extends AbstractMenuItem 
{
	private int _value;
	private int _minvalue;
	private int _maxvalue;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected NumericMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _value = 0;
        _minvalue = 0;
        _maxvalue = 100;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MenuItem.MENUITEM_NUMERIC;
    }
    
    /**
     * Sets current value of the entry
     * @param value current value 
     */
    public void setValue(int value) {
    	if (value >= _minvalue && value <= _maxvalue) {
    		_value = value;    			
    		update();
    	}
    }
    
    /**
     * Sets current value of the entry but does not send the change to LCDd
     * @param value current value
     */
    public void setValueNoUpdate(int value) {
    	if (value >= _minvalue && value <= _maxvalue) {
    		_value = value;    			
    	}
    }
    
    /**
     * Gets the current value of the entry
     * @return value of the entry
     */
    public int getValue() {
    	return _value;
    }
    
    /**
     * Sets minimum value of the entry
     * @param value min value 
     */
    public void setMinValue(int value) {
    	_minvalue = value;
    	if (_value < _minvalue) {
    		_value = _minvalue;
    	}
    	if (_minvalue <= _maxvalue) {
    		update();
    	}
    }
    
    /**
     * Sets maximum value of the entry
     * @param value max value 
     */
    public void setMaxValue(int value) {
    	_maxvalue = value;
    	if (_value > _maxvalue) {
    		_value = _maxvalue;
    	}
    	if (_maxvalue >= _minvalue) {
    		update();
    	}
    }

    @Override
    public String getData() {
    	StringBuilder strb = new StringBuilder();
    	strb.append(super.getData());
    	strb.append(" -value ");
    	strb.append(_value);
    	strb.append(" -minvalue ");
    	strb.append(_minvalue);
    	strb.append(" -maxvalue ");
    	strb.append(_maxvalue);
    	return strb.toString();
    }

    /** 
     * Construct a new NumericMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @return a new NumericMenuItem.
     */
    public static NumericMenuItem construct(Submenu menu, String text)
    {
    	return construct(menu, text, 0, 100);
	}
    
    /** 
     * Construct a new NumericMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param minValue the minimum value of the entry
     * @param maxValue the maximum value of the entry
     * @return a new NumericMenuItem.
     */
    public static NumericMenuItem construct(Submenu menu, String text, int minValue, int maxValue)
    {
        NumericMenuItem menuItem = null;

        try
        {
            menuItem = (NumericMenuItem)menu.constructMenuItem(MenuItem.MENUITEM_NUMERIC);
            menuItem.setText(text);
            menuItem.setMinValue(minValue);
            menuItem.setMaxValue(maxValue);
        }
        catch (LCDException e) //NOPMD
        {
            // Supress, would only get one if we asked for an invalid menu item.
        }

        return menuItem;
    }
}
