package org.boncey.lcdjava.util;

/**
 * Hook called when a ScreenThread times out.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: TimeoutHook.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public interface TimeoutHook
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: TimeoutHook.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /** 
     * This will be called when the thread times out.
     * @param screenThread the ScreenThread that called this timeout.
     */
    public void timeout(ScreenThread screenThread);
}

