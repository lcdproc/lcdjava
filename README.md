LCDjava
=======

Java client for LCDproc (http://lcdproc.org/).

This project was originally created by Darren Greaves in 2004-2008. It has been
updated in 2016 and builds with JDK 1.8 and Maven 3.3.

Building
--------

To build the project:

    $ mvn compile

To generate documentation:

    $ mvn javadoc:javadoc

You can also run `mvn site` to generate a full Maven website, including the Javadocs.

To package the project (excluding dependencies):

	$ mvn package

Example usage
-------------

See [basic_log4j][] and [bigclock_slf4j_simple][] for two example projects using LCDjava.

[basic_log4j]: https://github.com/antoneliasson/lcdjava-demo-basic-log4j2
[bigclock_slf4j_simple]: https://github.com/antoneliasson/lcdjava-demo-bigclock-slf4j-simple

Project information
-------------------

I used to use this with LCDproc on Linux a few years back but I have stopped
using it now. I don't even have an LCD display hooked up anymore so am unable to
test it.

However, when I did use it seemed to work fine - I ran it as part of my Wiphi
home music player project (http://boncey.org/tags/wiphi).

I'm happy to answer questions about the code via
http://github.com/boncey/lcdjava.
