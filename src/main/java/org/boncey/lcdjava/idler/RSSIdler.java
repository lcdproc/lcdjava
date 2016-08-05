package org.boncey.lcdjava.idler;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.fetcher.impl.SyndFeedInfo;
import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.boncey.lcdjava.LCDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Display the titles of an RSS feed.
 * <p>Copyright (c) 2004-2005 Darren Greaves.
 * @author Darren Greaves
 * @version $Id: RSSIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $
 */
public class RSSIdler extends AbstractIdler
{
    /**
     * Version details.
     */
    public static final String CVSID =
        "$Id: RSSIdler.java,v 1.2 2005-03-03 14:13:16 boncey Exp $";

    /**
     * Logger for log4j.
     */
    private static Logger _log = LoggerFactory.getLogger(RSSIdler.class);

    /**
     * Pad the display to enhance readability.
     */
    private static final String PADDING = "  ***  ";

    /**
     * The User Agent for fetching feeds.
     */
    private static final String USER_AGENT =
        "FeedFetcher (Rome Client)/0.1";

    /**
     * The text to display.
     */
    private String _text = "";

    /**
     * The feed URL.
     */
    private URL _feed;

    /**
     * Does this Idler have anything to display?
     */
    private boolean _valid;

    /**
     * How often to update this feed in minutes.
     */
    private int _update;

    /**
     * Limit the number of entries to this number.
     * Will only show the first 'n' entries if set, otherwise shows all.
     */
    private int _limit = -1;

    /**
     * How many minutes old this feed has to be before we stop displaying it?
     * <p>The feed will continue to be checked in the background so will
     * re-appear when it has been updated.
     */
    private int _expires = -1;

    /**
     * Whether or not to display the feed's description field.
     * <p>False unless specified as <code>includeDescription="true"</code> in
     * the config.
     */
    private boolean _includeDescription;

    /**
     * Any entry matching this pattern will be ignored.
     * <p>Example: <code>ignoreEntryPattern=".*advert.*"</code> to skip ads.
     */
    private Pattern _ignoreEntryPattern;

    /**
     * Public constructor, read the text from the config.
     * @param config the XML element for this Idler.
     * @param name the name of the Idler.
     * @throws LCDException if there was a problem.
     */
    public RSSIdler(Element config, String name)
        throws LCDException
    {
        super(config, name);

        try
        {
            String feed = config.getAttribute("feed");
            String update = config.getAttribute("update");
            String limit = config.getAttribute("limit");
            String expires = config.getAttribute("expires");
            String ignoreEntryPattern =
                config.getAttribute("ignoreEntryPattern");
            String includeDescription =
                config.getAttribute("includeDescription");

            _includeDescription =
                Boolean.valueOf(includeDescription).booleanValue();
            _update = Integer.parseInt(update) *
                      IdlerLoader.ONE_SECOND *
                      IdlerLoader.ONE_MINUTE;
            if (expires.length() > 0)
            {
                _expires = Integer.parseInt(expires);
            }
            if (limit.length() > 0)
            {
                _limit = Integer.parseInt(limit);
            }

            _feed = new URL(feed);
            _ignoreEntryPattern = Pattern.compile(ignoreEntryPattern);

            RSSUpdater updater = new RSSUpdater();
            Thread thread = new Thread(updater);
            thread.start();

        }
        catch (MalformedURLException e)
        {
            throw new LCDException(e);
        }
    }

    /**
     * Re-read the feed from the given URL.
     * @return <code>true</code> if success, <code>false</code> otherwise.
     */
    private boolean updateFeed()
    {
        _log.info("Fetching " + _feed);

        StringBuffer text = new StringBuffer();
        boolean success = false;
        boolean expired = false;
        try
        {
            FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
            FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
            feedFetcher.setUserAgent(USER_AGENT);
            SyndFeed feed = feedFetcher.retrieveFeed(_feed);
            WireFeed wire = feed.createWireFeed();
            SyndFeedInfo info = feedInfoCache.getFeedInfo(_feed);
            long lastModified =
                Long.parseLong(String.valueOf(info.getLastModified()));
            Date pubDate = new Date(lastModified);

            int max;
            if (_limit > 0)
            {
                max = _limit;
            }
            else
            {
                max = feed.getEntries().size();
            }
            int i = 0;

            if (_includeDescription)
            {
                text.append(PADDING + feed.getDescription());
            }

            for (Iterator it = feed.getEntries().iterator();
                 it.hasNext() && i < max;)
            {
                SyndEntry entry = (SyndEntry)it.next();
                String title = entry.getTitle();
                Matcher m = _ignoreEntryPattern.matcher(title);
                if (!m.matches())
                {
                    text.append(PADDING + title);
                    i++;
                }
            }

            synchronized (_text)
            {
                _text = text.toString();
            }

            if (_expires != -1)
            {
                Calendar feedCal = Calendar.getInstance();
                feedCal.setTime(pubDate);

                feedCal.add(Calendar.MINUTE, _expires);

                if (feedCal.getTime().before(new Date()))
                {
                    expired = true;
                }
            }

            if (_text.length() > 0 && !expired)
            {
                success = true;
            }
        }
        catch (FetcherException e)
        {
            // HTTP error, don't abort - temporary error.
            _log.warn("Failed fetching feed " + _feed + " with status code " +
                    e.getResponseCode());
        }
        catch (FeedException e)
        {
            // Feed error (likely invalid XML), don't abort - temporary error.
            _log.warn("Failed parsing feed " + _feed + " with message " + e);
        }
        catch (IOException e)
        {
            // Network error, don't abort - temporary error.
            _log.warn("Failed fetching feed " + _feed, e);
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
        return _text;
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
        return "Feed = " + _feed + "; Valid = " + _valid;
    }

    /**
     * Thread for handling periodic updates.
     * @author Darren Greaves
     */
    private class RSSUpdater implements Runnable
    {
        /**
         * Public constructor.
         */
        public RSSUpdater()
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
                        _valid = updateFeed();
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
