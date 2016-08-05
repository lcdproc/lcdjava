package org.boncey.lcdjava.idler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.boncey.lcdjava.LCDException;
import org.boncey.lcdjava.ScrollerWidget;
import org.boncey.lcdjava.util.WidgetTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Load implementations of Idler from an XML config.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: IdlerLoader.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class IdlerLoader
{
    private static Logger _log = LoggerFactory.getLogger(IdlerLoader.class);

    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: IdlerLoader.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * XML config value.
     */
    private static final String IDLER_ELEMENT = "idler";

    /**
     * XML config value.
     */
    private static final String CLASSNAME = "className";

    /**
     * XML config value.
     */
    private static final String NAME = "name";

    /**
     * The amount of milliseconds in a second.
     */
    public static final int ONE_SECOND = 1000;

    /**
     * The amount of seconds in a minute.
     */
    public static final int ONE_MINUTE = 60;

    /**
     * The timer for controlling display of each Idler.
     */
    private WidgetTimer _timer;

    /**
     * The WidgetUpdater that mananges displaying of the Idlers.
     */
    private IdlerWidgetUpdater _updater;

    /**
     * Default constructor, initialise.
     * @param doc the XML Document.
     * @param scroller the Widget to display each Idler upon.
     * @param name the Widget to display the Idler name upon.
     * @throws LCDException if there was a problem.
     */
    public IdlerLoader(Document doc,
                       ScrollerWidget scroller,
                       ScrollerWidget name)
        throws LCDException
    {
        Element config = (Element)doc.getFirstChild();
        if (config == null)
        {
            throw new LCDException(
                    "Unable to find a child element in Document");
        }

        load(config, scroller, name);
    }

    /**
     * Default constructor, initialise.
     * @param config the XML config.
     * @param scroller the Widget to display each Idler upon.
     * @param name the Widget to display the Idler name upon.
     * @throws LCDException if there was a problem.
     */
    public IdlerLoader(Element config,
                       ScrollerWidget scroller,
                       ScrollerWidget name)
        throws LCDException
    {
        load(config, scroller, name);
    }

    /**
     * Load the Idlers from the config file.
     * @param config the XML config.
     * @param scroller the Widget to display each Idler upon.
     * @param name the Widget to display the Idler name upon.
     * @throws LCDException if there was a problem.
     */
    private void load(Element config,
                      ScrollerWidget scroller,
                      ScrollerWidget name)
        throws LCDException
    {
        List idlers = loadConfig(config);

        if (idlers.size() > 0)
        {
            _updater = new IdlerWidgetUpdater(scroller, name, idlers);

            _timer = new WidgetTimer(_updater, ONE_MINUTE);
            Thread thread = new Thread(_timer);
            thread.start();
        }
    }

    /**
     * IdlerLoader is being destroyed, kill off any threads.
     */
    public void destroy()
    {
        if (_timer != null)
        {
            _timer.destroy();
        }
    }

    /**
     * Display the next idler in the sequence.
     */
    public void next()
    {
        _updater.updateWidget(_timer);
    }

    /**
     * Load the configs and store the classes in the internal Map.
     * @param config the Element that holds the config.
     * @return a List of Idlers.
     * @throws LCDException a wrapper around any Exception thrown.
     */
    private List loadConfig(Element config)
        throws LCDException
    {
        List<Idler> idlers = new ArrayList<>();
        NodeList nodes = config.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element moduleConfig = (Element)node;
                String tagName = moduleConfig.getTagName();
                if (IDLER_ELEMENT.equals(tagName))
                {
                    String className = moduleConfig.getAttribute(CLASSNAME);
                    if (className == null || className.length() == 0)
                    {
                        throw new LCDException("Expected '" + CLASSNAME +
                                "' attribute");
                    }
                    String name = moduleConfig.getAttribute(NAME);
                    if (name == null || name.length() == 0)
                    {
                        throw new LCDException("Expected '" + NAME +
                                "' attribute");
                    }

                    try
                    {
                        Idler idler = loadIdler(moduleConfig, name, className);
                        _log.info("Loaded idler from class " + className);
                        idlers.add(idler);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new LCDException("'" + className +
                                "' is not a valid class", e);
                    }
                    catch (InstantiationException e)
                    {
                        throw new LCDException
                            ("Cannot construct class '" + className + "'", e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new LCDException
                            ("Cannot construct class '" + className + "'", e);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new LCDException
                            ("Cannot construct class '" + className + "'", e);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new LCDException
                            ("Cannot construct class '" + className + "'",
                             e.getCause());
                    }
                    catch (NoSuchMethodException e)
                    {
                        throw new LCDException
                            ("Cannot construct class '" + className + "'", e);
                    }
                    catch (ClassCastException e)
                    {
                        throw new LCDException
                            ("Class '" + className + "' is not an Idler", e);
                    }
                }
            }
        }

        return idlers;
    }

    /**
     * Instantiate a new Idler.
     * @param moduleConfig the Element that holds the config.
     * @param name the name of the Idler.
     * @param className the name of the class to instantiate.
     * @return a new Idler.
     * @throws ClassNotFoundException in the event of a problem.
     * @throws InstantiationException in the event of a problem.
     * @throws IllegalAccessException in the event of a problem.
     * @throws IllegalArgumentException in the event of a problem.
     * @throws InvocationTargetException in the event of a problem.
     * @throws NoSuchMethodException in the event of a problem.
     * @throws ClassCastException in the event of a problem.
     */
    private Idler loadIdler(Element moduleConfig, String name, String className)
        throws ClassNotFoundException,
               InstantiationException,
               IllegalAccessException,
               IllegalArgumentException,
               InvocationTargetException,
               NoSuchMethodException,
               ClassCastException
    {

        Class[] types = new Class[] {Element.class, String.class};

        Object[] args = new Object[] {moduleConfig, name};

        Class c = Class.forName(className);
        @SuppressWarnings("unchecked")
        Constructor constructor = c.getConstructor(types);
        return (Idler)constructor.newInstance(args);
    }
}
