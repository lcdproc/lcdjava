Java client for LCDproc (http://lcdproc.org/).

This project was originally created by Darren Greaves in 2004-2008. It has been
updated in 2016 and builds with JDK 1.8 and Maven 3.3.

To build the project:

    $ mvn compile

To generate documentation:

    $ mvn javadoc:javadoc

You can also run `mvn site` to generate a full Maven website, including the Javadocs.

To package the project (excluding dependencies):

	$ mvn package

To run the demos from the resulting jar, or directly from the compiled class files:

	$ java -cp target/classes:$HOME/.m2/repository/log4j/log4j/1.2.6/log4j-1.2.6.jar org.boncey.lcdjava.demo.Demo <host> <port>
	$ java -cp target/lcdjava-1.0-SNAPSHOT.jar:$HOME/.m2/repository/log4j/log4j/1.2.6/log4j-1.2.6.jar: org.boncey.lcdjava.demo.Demo <host> <port>

Replace *Demo* with *BigClock* for the other demo.

I used to use this with LCDproc on Linux a few years back but I have stopped
using it now. I don't even have an LCD display hooked up anymore so am unable to
test it.

However, when I did use it seemed to work fine - I ran it as part of my Wiphi
home music player project (http://boncey.org/tags/wiphi).

I'm happy to answer questions about the code via
http://github.com/boncey/lcdjava.
