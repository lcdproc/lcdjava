package org.lcdproc.lcdjava;




/**
 * AlphaMenuItem.
 * <p>Is visible as a text. When selected, a screen comes up that shows the 
 * current string value, that you can edit with the cursor keys and Enter. 
 * The string is ended by selecting a 'null' input character. After that the 
 * menu returns. 
 * @author StefanKrupop
 */
public class AlphaMenuItem extends AbstractMenuItem 
{
	private String _value;
	private int _minlength;
	private int _maxlength;
	private String _passwordchar;
	private boolean _allowCaps;
	private boolean _allowNonCaps;
	private boolean _allowNumbers;
	private String _allowedExtra;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected AlphaMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _value = "";
        _minlength = 0;
        _maxlength = 10;
        _passwordchar = "";
        _allowCaps = true;
        _allowNonCaps = true;
        _allowNumbers = true;
        _allowedExtra = "";
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MENUITEM_ALPHA;
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
     * Gets the currently set value
     * @return value current value
     */
    public String getValue() {
    	return _value;
    }

    /**
     * Sets the minimum length of the string that can be entered
     * @param value min length 
     */
    public void setMinLength(int value) {
    	_minlength = value;    			
    	update();
    }

    /**
     * Sets the maximum length of the string that can be entered
     * @param value max length 
     */
    public void setMaxLength(int value) {
    	_maxlength = value;    			
    	update();
    }

    /**
     * Sets the character that should be displayed instead of typed characters
     * @param value password character 
     */
    public void setPasswordChar(String value) {
    	_passwordchar = value; 			
    	update();
    }

    /**
     * Sets if upper case characters can be entered
     * @param value new state
     */
    public void setAllowCaps(boolean value) {
    	_allowCaps = value; 			
    	update();
    }

    /**
     * Sets if lower case characters can be entered
     * @param value new state
     */
    public void setAllowNonCaps(boolean value) {
    	_allowNonCaps = value; 			
    	update();
    }
    
    /**
     * Sets if numbers can be entered
     * @param value new state
     */
    public void setAllowNumbers(boolean value) {
    	_allowNumbers = value; 			
    	update();
    }

    /**
     * Sets additional characters that can be entered
     * @param chars allowed characters
     */
    public void setAllowExtra(String chars) {
    	_allowedExtra = chars;
    	update();
    }
    
    @Override
    public String getData() {
    	StringBuilder strb = new StringBuilder();
    	strb.append(super.getData());
    	strb.append(" -value \"");
    	strb.append(_value);
    	strb.append("\" -minlength ");
    	strb.append(_minlength);
    	strb.append(" -maxlength ");
    	strb.append(_maxlength);
    	if (!_passwordchar.isEmpty()) {
    		strb.append(" -password_char \"");
    		strb.append(_passwordchar);
    		strb.append('"');
    	}
    	strb.append(" -allow_caps ");
    	strb.append(_allowCaps ? "true" : "false");
    	strb.append(" -allow_noncaps ");
    	strb.append(_allowNonCaps ? "true" : "false");
    	strb.append(" -allow_numbers ");
    	strb.append(_allowNumbers ? "true" : "false");
    	if (!_allowedExtra.isEmpty()) {
    		strb.append(" -allowed_extra \"");
    		strb.append(_allowedExtra);
    		strb.append('"');
    	}
    	
    	return strb.toString();
    }

    /** 
     * Construct a new AlphaMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param current the current string
     * @return a new AlphaMenuItem.
     */
    public static AlphaMenuItem construct(Submenu menu, String text, String current)
    {
        AlphaMenuItem menuItem = null;

        try
        {
            menuItem = (AlphaMenuItem)menu.constructMenuItem(MENUITEM_ALPHA);
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
