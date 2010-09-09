package org.boncey.lcdjava.util;

import org.boncey.lcdjava.Widget;

/**
 * Interface for holding a Widget that will be updated by the WidgetTimer class.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: WidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public interface WidgetUpdater
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: WidgetUpdater.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /** 
     * Update the Widget.
     * @param timer the WidgetTimer.
     */
    public void updateWidget(WidgetTimer timer);


    /** 
     * Retrurn the wrapped Widget.
     * @return the Widget.
     */
    public Widget getWidget();
}
