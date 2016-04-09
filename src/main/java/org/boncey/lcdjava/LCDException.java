package org.boncey.lcdjava;

/**
 * Exception that wraps any Exceptions thrown when interacting with LCDProc.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves.
 * @version $Id: LCDException.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class LCDException extends RuntimeException
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: LCDException.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";


    /**
     * Constructor.
     * @param message The error message.
     * @param t The wrapped exception.
     */
    public LCDException(String message, Throwable t)
    {
        super(message, t);
    }


    /**
     * Constructor.
     * @param t The wrapped exception.
     */
    public LCDException(Throwable t)
    {
        super(t);
    }


    /**
     * Constructor.
     * @param message The error message.
     */
    public LCDException(String message)
    {
        super(message);
    }
}



