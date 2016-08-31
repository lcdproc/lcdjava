package org.lcdproc.lcdjava.idler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.lcdproc.lcdjava.LCDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Display the results of executing a program.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: ExecIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class ExecIdler extends AbstractIdler
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: ExecIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    private static Logger _log = LoggerFactory.getLogger(ExecIdler.class);

    /**
     * The size of the buffer the program's output is read into.
     */
    private static final int BUFF_SIZE = 4000;

    /**
     * Pad the display to enhance readability.
     */
    private static final String PADDING = "  ***  ";

    /**
     * The text to display.
     */
    private final StringBuffer _text = new StringBuffer();

    /**
     * The name of the program to execute.
     */
    private String _program;

    /**
     * Does this Idler have anything to display?
     */
    private boolean _valid;

    /**
     * How often to update this feed in minutes.
     */
    private int _update;

    /**
     * The Runtime object.
     */
    private static Runtime _runtime = Runtime.getRuntime();

    /**
     * Public constructor, read the text from the config.
     * @param config the XML element for this Idler.
     * @param name the name of the Idler.
     * @throws LCDException if there was a problem.
     */
    public ExecIdler(Element config, String name)
        throws LCDException
    {
        super(config, name);

        _program = config.getAttribute("program");
        if (_program == null)
        {
            throw new LCDException("Expected 'program' parameter");
        }
        String update = config.getAttribute("update");
        _update = Integer.parseInt(update) *
                  IdlerLoader.ONE_SECOND *
                  IdlerLoader.ONE_MINUTE;

        ExecUpdater updater = new ExecUpdater();
        Thread thread = new Thread(updater);
        thread.start();
    }

    /**
     * Re-read the feed from the given URL.
     * @return <code>true</code> if success, <code>false</code> otherwise.
     */
    private boolean execute()
    {
        _log.info("Executing " + _program);
        boolean success = false;

        try
        {
            synchronized (_text)
            {
                Process proc = _runtime.exec(_program);
                BufferedReader stdout = new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
                String line = stdout.readLine();
                while (line != null)
                {
                    _text.append(PADDING + line);
                    line = stdout.readLine();
                }

                stdout.close();
            }

            if (_text.length() > 0)
            {
                success = true;
            }
        }
        catch (IOException e)
        {
            // temporary error.
            _log.warn("Failed executing " + _program, e);
        }

        return success;
    }

    /**
     * Is there any text to display?
     * @return <code>true</code> if there is anything to display,
     * <code>false</code> otherwise.
     */
    public boolean isValid()
    {
        return _valid;
    }

    /**
     * Get the text to display.
     * @return the text to display.
     */
    public synchronized String getDisplayText()
    {
        return _text.toString();
    }

    /**
     * Idler is being destroyed, do nothing.
     */
    public void destroy()
    {
    }

    /**
     * Return a String representing this object.
     * @return a String representing this object.
     */
    public String toString()
    {
        return "Program = " + _program + "; Valid = " + _valid;
    }

    /**
     * Thread for handling periodic updates.
     * @author Darren Greaves
     */
    private class ExecUpdater implements Runnable
    {
        /**
         * Public constructor.
         */
        public ExecUpdater()
        {
        }

        /**
         * Update the feed.
         */
        public void run()
        {
            while (true)
            {
                synchronized (this)
                {
                    try
                    {
                        _valid = execute();
                        Thread.sleep(_update);
                    }
                    catch (InterruptedException e)
                    {
                        // Do nothing
                    }
                }
            }
        }
    }
}
