#!/bin/bash

PELLET_VERSION=2.0.1

mvn install:install-file -Dfile=pellet-core.jar -DgroupId=pellet \
    -DartifactId=pellet-core -Dversion=$PELLET_VERSION -Dpackaging=jar

mvn install:install-file -Dfile=pellet-jena.jar -DgroupId=pellet \
    -DartifactId=pellet-jena -Dversion=$PELLET_VERSION -Dpackaging=jar

mvn install:install-file -Dfile=pellet-rules.jar -DgroupId=pellet \
    -DartifactId=pellet-rules -Dversion=$PELLET_VERSION -Dpackaging=jar

mvn install:install-file -Dfile=pellet-datatypes.jar -DgroupId=pellet \
    -DartifactId=pellet-datatypes -Dversion=$PELLET_VERSION -Dpackaging=jar

mvn install:install-file -Dfile=pellet-el.jar -DgroupId=pellet \
    -DartifactId=pellet-el -Dversion=$PELLET_VERSION -Dpackaging=jar

mvn install:install-file -Dfile=aterm-java-1.6.jar -DgroupId=aterm-java \
    -DartifactId=aterm-java -Dversion=1.6 -Dpackaging=jar

mvn install:install-file -Dfile=pivot-jfree-1.5.jar -DgroupId=org.apache.pivot \
    -DartifactId=pivot-charts-jfree -Dversion=1.5 -Dpackaging=jar

