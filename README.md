Java client for LCDproc (http://lcdproc.org/).

This project was originally created by Darren Greaves in 2004-2008. It has been
slightly updated in 2016 and builds with JDK 1.8 and Ant 1.9, but still bundles
ancient libraries and uses fairly old Java style.

To build the project, generate documentation and run the demos:

    $ ant build
    $ ant javadocs
    $ java -cp build/classes:build/lib/log4j-1.2.6.jar org.boncey.lcdjava.demo.Demo <host> <port>
    $ java -cp build/classes:build/lib/log4j-1.2.6.jar org.boncey.lcdjava.demo.BigClock <host> <port>

Example:

    $ ant build
    Buildfile: /home/anton/git/lcdjava/build.xml

    compile:
	[javac] Compiling 40 source files to /home/anton/git/lcdjava/build/classes

    build:

    BUILD SUCCESSFUL
    Total time: 1 second
    $ java -cp build/classes:build/lib/log4j-1.2.6.jar org.boncey.lcdjava.demo.BigClock 192.168.1.242 13666
    Connected to LCDd: Version = 0.5.5; protocol version = 0.3; width = 20; height = 4; cell width = 5; cell height = 8
    Press Ctrl+C to exit
    ^C0 [Thread-0] DEBUG org.boncey.lcdjava.demo.BigClock  - Interrupted
    2 [Thread-0] DEBUG org.boncey.lcdjava.LCD  - Shutdown requested
    2 [Thread-0] DEBUG org.boncey.lcdjava.LCD  - Waiting for LCDSocketPoller to terminate...
    2 [Thread-1] DEBUG org.boncey.lcdjava.LCDSocketPoller  - Terminating
    2 [Thread-0] DEBUG org.boncey.lcdjava.LCD  - Closing socket
    2 [Thread-0] DEBUG org.boncey.lcdjava.demo.BigClock  - Terminating

I used to use this with LCDproc on Linux a few years back but I have stopped
using it now. I don't even have an LCD display hooked up anymore so am unable to
test it.

However, when I did use it seemed to work fine - I ran it as part of my Wiphi
home music player project (http://boncey.org/tags/wiphi).

I'm happy to answer questions about the code via
http://github.com/boncey/lcdjava.
