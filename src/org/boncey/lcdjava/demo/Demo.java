package org.boncey.lcdjava.demo;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.boncey.lcdjava.LCD;
import org.boncey.lcdjava.LCDException;
import org.boncey.lcdjava.Screen;
import org.boncey.lcdjava.StringWidget;
import org.boncey.lcdjava.TitleWidget;

/**
 * Sample class to demonstrate how to display various widgets.
 * @author Darren Greaves
 * @version $Id: Demo.java,v 1.1 2008-07-06 15:38:34 boncey Exp $
 */
public class Demo
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: Demo.java,v 1.1 2008-07-06 15:38:34 boncey Exp $";

    /**
     * Logger for log4j.
     */
    private static Logger _log = null;

    /**
     * Public constructor.
     * @param host the LCDd host.
     * @param port the LCDd port.
     * @throws LCDException if there was a problem connecting to the server.
     */
    private Demo(String host, int port)
        throws LCDException
    {
        LCD lcd = new LCD(host, port);
        System.out.println("Connected to LCDd: " + lcd);

        // Construct an unactivated Screen
        Screen demoScreen = lcd.constructScreen("demo");

        // Set to foreground priority (still won't be seen until activated)
        demoScreen.setPriority(Screen.PRIORITY_FOREGROUND);

        // Add some Widgets to the screen - nothing will be seen until activated
        TitleWidget titleWidget =
            TitleWidget.construct(demoScreen, "Demo Title");
        StringWidget stringWidget =
            StringWidget.construct(demoScreen, 1, 2, "Demo Text");

        // Must call Screen.activate before activating any of its Widgets
        demoScreen.activate();

        // This will fail (check return value) if Screen not activated
        titleWidget.activate();
        stringWidget.activate();
    }

    public static void main(String args[])
    {
        if (args.length != 2)
        {
            System.err.println(
                "Usage: java org.boncey.lcdjava.demo.Demo <LCD host> <LCD port>");
            System.exit(-1);
        }

        configureLogging();

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try
        {
            new Demo(host, port);
        }
        catch (LCDException e)
        {
            e.printStackTrace();
        }
    }

    /** 
     * Configure log4j before we use the logger in this class.
     */
    private static void configureLogging()
    {
        PatternLayout layout = new PatternLayout(
                "%m%n");
        Appender app = new ConsoleAppender(layout);
        BasicConfigurator.configure(app);
        
        _log = Logger.getLogger(Demo.class);
    }
}

