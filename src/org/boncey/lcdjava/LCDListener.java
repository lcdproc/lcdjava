package org.boncey.lcdjava;

/**
 * Interface for listening to the state of a Screen.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: LCDListener.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public interface LCDListener
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: LCDListener.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /** 
     * The listen/ignore state of a Screen has been notified to us by LCDd.
     * @param screenId the id of the Screen.
     * @param listening <code>true</code> if the server is listening to us,
     * <code>false</code> if not.
     */
    public void setListenStatus(int screenId, boolean listening);
}

