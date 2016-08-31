package org.boncey.lcdjava;




/**
 * SliderMenuItem.
 * <p>Is visible as a text. When selected, a screen comes up that shows a
 * slider. You can set the slider using the cursor keys. When Enter is 
 * pressed, the menu returns. 
 * @author StefanKrupop
 */
public class SliderMenuItem extends NumericMenuItem 
{
	private String _mintext;
	private String _maxtext;
	private int _stepsize;
	
    /**
     * Constructor.
     * @param id the of the action.
     * @param menu the Submenu that knows about this MenuItem.
     */
    protected SliderMenuItem(String id, Submenu menu)
    {
        super(id, menu);
        _mintext = "";
        _maxtext = "";
        _stepsize = 1;
    }
    
	/** 
     * Get the menu item type.
     * @return the menu item type.
     */
    public String getType()
    {
        return MenuItem.MENUITEM_SLIDER;
    }
    
    /**
     * Sets label for minimum value of the slider
     * @param value text to be shown on left side of slider 
     */
    public void setMinText(String value) {
    	_mintext = value;
    	update();
    }

    /**
     * Sets label for maximum value of the slider
     * @param value text to be shown on right side of slider 
     */
    public void setMaxText(String value) {
    	_maxtext = value;
    	update();
    }

    /**
     * Sets step size of the slider
     * @param size step size 
     */
    public void setStepSize(int size) {
    	_stepsize = size;
   		update();
    }

    @Override
    public String getData() {
    	StringBuilder strb = new StringBuilder();
    	strb.append(super.getData());
    	strb.append(" -stepsize ");
    	strb.append(_stepsize);
    	if (!_mintext.isEmpty()) {
        	strb.append(" -mintext \"");
        	strb.append(_mintext);
        	strb.append('"');
    	}
    	if (!_maxtext.isEmpty()) {
        	strb.append(" -maxtext \"");
        	strb.append(_maxtext);
        	strb.append('"');
    	}
    	return strb.toString();
    }

    /** 
     * Construct a new SliderMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @return a new SliderMenuItem.
     */
    public static SliderMenuItem construct(Submenu menu, String text)
    {
    	return construct(menu, text, 0, 100);
	}
    
    /** 
     * Construct a new SliderMenuItem.
     * @param menu the Submenu that owns the menu item.
     * @param text the menu text.
     * @param minValue the minimum value of the slider
     * @param maxValue the maximum value of the slider
     * @return a new SliderMenuItem.
     */
    public static SliderMenuItem construct(Submenu menu, String text, int minValue, int maxValue)
    {
        SliderMenuItem menuItem = null;

        try
        {
            menuItem = (SliderMenuItem)menu.constructMenuItem(MenuItem.MENUITEM_SLIDER);
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
