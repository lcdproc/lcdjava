package org.lcdproc.lcdjava;

/**
 * Scroller Widget.
 * <p>Displays a scroller on the LCD display.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: ScrollerWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class ScrollerWidget extends AbstractWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: ScrollerWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The Horizontal direction.
     */
    public static final char DIRECTION_HORIZONTAL = 'h';

    /**
     * The Vertical direction.
     */
    public static final char DIRECTION_VERTICAL = 'v';

    /**
     * The 'Marquee' direction.
     * <p>Will loop around horizontally rather than bounce back and forth.
     */
    public static final char DIRECTION_MARQUEE = 'm';

    /**
     * The top position of the scroller.
     */
    private int _top = 1;

    /**
     * The bottom position of the scroller.
     */
    private int _bottom = 1;

    /**
     * The left position of the scroller.
     */
    private int _left = 1;

    /**
     * The right position of the scroller.
     */
    private int _right = 1;

    /**
     * The direction to scroll.
     */
    private char _direction;

    /**
     * The speed of the scroller.
     */
    private int _speed;

    /**
     * The Widget text.
     */
    private String _text;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected ScrollerWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Get the Widget type.
     * @return the Widget type.
     */
    public String getType()
    {
        return WIDGET_SCROLLER;
    }

    /**
     * Set the Widget text.
     * @param text the Widget text.
     */
    public void setText(String text)
    {
        _text = text;
        update();
    }

    /**
     * Get the Widget text.
     * @return the Widget text.
     */
    public String getText()
    {
        return _text;
    }

    /**
     * Set the top position.
     * @param top the top position.
     */
    public void setTop(int top)
    {
        _top = top;
        update();
    }

    /**
     * Get the top position.
     * @return the top position.
     */
    public int getTop()
    {
        return _top;
    }


    /**
     * Set the bottom position.
     * @param bottom the bottom position.
     */
    public void setBottom(int bottom)
    {
        _bottom = bottom;
        update();
    }

    /**
     * Get the bottom position.
     * @return the bottom position.
     */
    public int getBottom()
    {
        return _bottom;
    }


    /**
     * Set the left position.
     * @param left the left position.
     */
    public void setLeft(int left)
    {
        _left = left;
        update();
    }

    /**
     * Get the left position.
     * @return the left position.
     */
    public int getLeft()
    {
        return _left;
    }


    /**
     * Set the right position.
     * @param right the right position.
     */
    public void setRight(int right)
    {
        _right = right;
        update();
    }

    /**
     * Get the right position.
     * @return the right position.
     */
    public int getRight()
    {
        return _right;
    }


    /**
     * Set the direction.
     * @param direction the direction.
     */
    public void setDirection(char direction)
    {
        _direction = direction;
        update();
    }

    /**
     * Get the direction.
     * @return the direction.
     */
    public char getDirection()
    {
        return _direction;
    }


    /**
     * Set the speed.
     * @param speed the speed.
     */
    public void setSpeed(int speed)
    {
        _speed = speed;
        update();
    }

    /**
     * Get the speed.
     * @return the speed.
     */
    public int getSpeed()
    {
        return _speed;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return _left + " " + _top + " " + _right + " " + _bottom + " " +
               _direction + " " + _speed + " \"" + stripQuotes(_text) + "\"";
    }

    /**
     * Construct a new ScrollerWidget.
     * @param screen the Screen that owns the Widget.
     * @param left the left position.
     * @param top the top position.
     * @param right the right position.
     * @param bottom the bottom position.
     * @param direction the direction.
     * @param speed the speed.
     * @param text the widget text.
     * @return a new ScrollerWidget.
     */
    public static ScrollerWidget construct(Screen screen,
                                           int left,
                                           int top,
                                           int right,
                                           int bottom,
                                           char direction,
                                           int speed,
                                           String text)
    {
        ScrollerWidget widget = null;

        try
        {
            widget = (ScrollerWidget)screen.constructWidget(
                    WIDGET_SCROLLER);
            widget.setLeft(left);
            widget.setTop(top);
            widget.setRight(right);
            widget.setBottom(bottom);
            widget.setDirection(direction);
            widget.setSpeed(speed);
            widget.setText(text);
        }
        catch (LCDException e)
        {
            // Supress, would only get one if we asked for an invalid Widget.
        }

        return widget;
    }
}
