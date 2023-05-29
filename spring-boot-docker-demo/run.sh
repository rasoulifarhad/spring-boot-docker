#!/bin/sh
# use exec java …​ to launch the java process (so that it can handle the KILL signals)
exec java ${JAVA_OPTS} -jar /app.jar
