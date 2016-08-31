package org.boncey.lcdjava;

/**
 * Icon Widget.
 * <p>Displays a Icon on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: IconWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class IconWidget extends PositionalWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: IconWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";


    /**
     * A predefined icon.
     */
    public static final String ICON_BLOCK_FILLED = "BLOCK_FILLED";

    /**
     * A predefined icon.
     */
    public static final String ICON_HEART_OPEN = "HEART_OPEN";

    /**
     * A predefined icon.
     */
    public static final String ICON_HEART_FILLED = "HEART_FILLED";

    /**
     * A predefined icon.
     */
    public static final String ICON_ARROW_UP = "ARROW_UP";

    /**
     * A predefined icon.
     */
    public static final String ICON_ARROW_DOWN = "ARROW_DOWN";

    /**
     * A predefined icon.
     */
    public static final String ICON_ARROW_LEFT = "ARROW_LEFT";

    /**
     * A predefined icon.
     */
    public static final String ICON_ARROW_RIGHT = "ARROW_RIGHT";

    /**
     * A predefined icon.
     */
    public static final String ICON_CHECKBOX_OFF = "CHECKBOX_OFF";

    /**
     * A predefined icon.
     */
    public static final String ICON_CHECKBOX_ON = "CHECKBOX_ON";

    /**
     * A predefined icon.
     */
    public static final String ICON_CHECKBOX_GRAY = "CHECKBOX_GRAY";

    /**
     * A predefined icon.
     */
    public static final String ICON_SELECTOR_AT_LEFT = "SELECTOR_AT_LEFT";

    /**
     * A predefined icon.
     */
    public static final String ICON_SELECTOR_AT_RIGHT = "SELECTOR_AT_RIGHT";

    /**
     * A predefined icon.
     */
    public static final String ICON_ELLIPSIS = "ELLIPSIS";

    /**
     * A predefined icon.
     */
    public static final String ICON_PAUSE = "PAUSE";

    /**
     * A predefined icon.
     */
    public static final String ICON_PLAY = "PLAY";

    /**
     * A predefined icon.
     */
    public static final String ICON_PLAYR = "PLAYR";

    /**
     * A predefined icon.
     */
    public static final String ICON_FF = "FF";

    /**
     * A predefined icon.
     */
    public static final String ICON_FR = "FR";

    /**
     * A predefined icon.
     */
    public static final String ICON_NEXT = "NEXT";

    /**
     * A predefined icon.
     */
    public static final String ICON_PREV = "PREV";

    /**
     * A predefined icon.
     */
    public static final String ICON_REC = "REC";

    /**
     * The Widget iconName.
     */
    private String _iconName;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected IconWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return Widget.WIDGET_ICON;
    }

    /**
     * Set the Widget iconName.
     * @param iconName the Widget iconName.
     */
    public void setText(String iconName)
    {
        _iconName = iconName;
        update();
    }

    /**
     * Get the Widget iconName.
     * @return the Widget iconName.
     */
    public String getText()
    {
        return _iconName;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return super.getData() + "\"" + stripQuotes(_iconName) + "\"";
    }

    /**
     * Construct a new IconWidget.
     * @param screen the Screen that owns the Widget.
     * @param x the x position.
     * @param y the y position.
     * @param iconName the widget iconName.
     * @return a new IconWidget.
     */
    public static IconWidget construct(Screen screen,
                                         int x,
                                         int y,
                                         String iconName)
    {
        IconWidget widget = null;

        try
        {
            widget = (IconWidget)screen.constructWidget(Widget.WIDGET_ICON);
            widget.setX(x);
            widget.setY(y);
            widget.setText(iconName);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }
}

