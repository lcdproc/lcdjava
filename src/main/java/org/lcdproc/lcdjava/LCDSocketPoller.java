package org.lcdproc.lcdjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread that listens for data on the LCD socket.
 */
class LCDSocketPoller extends Thread {
    private final Logger _log = LoggerFactory.getLogger(LCDSocketPoller.class);

    /**
     * The Pattern that matches ignore/listen events.
     */
    private static final Pattern IGNORE_STATUS = Pattern.compile(
            "(ignore|listen)\\s+(\\d+).*");

    /**
     * The Pattern that matches menu events.
     */
    private static final Pattern MENU_STATUS = Pattern.compile(
            "menuevent\\s+(\\w+)\\s+(\\w+)\\s*(.*)");

    /**
     * The Reader to read data from.
     */
    private final BufferedReader _in;

    /**
     * The last line of data received.
     * <p>Will be null if no data was received.
     */
    private String _lastLine;

    /**
     * The listener to notify of listen/ignore events.
     */
    private final LCDListener _listener;

    private Semaphore hello;

    /**
     * Public constructor.
     *
     * @param in       the BufferedReader that will receive data from the server.
     * @param listener the LCDListener that gets notified of screens being
     *                 listened to or ignored.
     */
    LCDSocketPoller(BufferedReader in, LCDListener listener, Semaphore hello) throws IOException {
        _in = in;
        _listener = listener;
        this.hello = hello;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = _in.readLine()) != null) {
                if (line.startsWith(LCD.RESPONSE_ERROR)) {
                    _log.warn("Got a response of " + line +
                            " from server");
                }
                Matcher listenIgnore = IGNORE_STATUS.matcher(line);
                Matcher menuEvent = MENU_STATUS.matcher(line);
                if (listenIgnore.matches()) {
                    boolean listen = (LCD.RESPONSE_LISTEN.equals(listenIgnore.group(1)));
                    int screenId = Integer.parseInt(listenIgnore.group(2));
                    _listener.setListenStatus(screenId, listen);
                } else if (menuEvent.matches()) {
                    _listener.menuAction(menuEvent.group(2), menuEvent.group(1), menuEvent.group(3));
                }
                synchronized (this) {
                    _lastLine = line;
                }
                hello.release();
            }
        } catch (IOException e) {
            throw new LCDException(e);
        }
        _log.debug("Terminating");
    }

    /**
     * Get the last line received <i>non-blocking</i>.
     * <p>Calling this clears the last line received.
     *
     * @return the last line received.
     */
    synchronized String getLastLine() {
        String ret = _lastLine;
        _lastLine = null;
        return ret;
    }
}
