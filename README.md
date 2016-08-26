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

See [lcdjava-demos][] for two example projects using LCDjava. They demonstrate both how to use the LCDjava API and how to integrate the library in a larger Maven application.

[lcdjava-demos]: https://github.com/lcdproc/lcdjava-demos

Project information
-------------------

I used to use this with LCDproc on Linux a few years back but I have stopped
using it now. I don't even have an LCD display hooked up anymore so am unable to
test it.

However, when I did use it seemed to work fine - I ran it as part of my Wiphi
home music player project (http://boncey.org/tags/wiphi).

I'm happy to answer questions about the code via
http://github.com/boncey/lcdjava.

Project dependencies
--------------------

LCDjava uses the [Simple Logging Facade for Java (SLF4J)][slf4j] as its logger abstracter so as to not dictate a logging implementation on its users. SLF4J supports logging to its built-in Simple Logger (logs everything to the console), Log4j 1.2 and 2.x, Logback and java.util.logging.

[slf4j]: http://www.slf4j.org/
