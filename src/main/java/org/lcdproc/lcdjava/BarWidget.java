package org.lcdproc.lcdjava;

/**
 * Abstract Widget for displaying Bars.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: BarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public abstract class BarWidget extends PositionalWidget
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: BarWidget.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * The length of the Widget.
     */
    private int _length;

    /**
     * Constructor.
     * @param id the of the Widget.
     * @param screen the Screen that knows about this Widget.
     */
    protected BarWidget(int id, Screen screen)
    {
        super(id, screen);
    }

    /**
     * Set the Widget length.
     * @param length the Widget length.
     */
    public void setLength(int length)
    {
        _length = length;
        update();
    }

    /**
     * Get the Widget length.
     * @return the Widget length.
     */
    public int getLength()
    {
        return _length;
    }

    /**
     * Return the data this Widget needs to update itself.
     * @return the data to update this Widget.
     */
    public String getData()
    {
        return super.getData() + _length;
    }

}

