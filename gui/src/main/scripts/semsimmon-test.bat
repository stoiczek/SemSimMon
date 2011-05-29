set PORT=%1
java -Dcom.sun.management.jmxremote.port=%PORT% -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -cp resources:. -jar lib/gui-0.1-SNAPSHOT.jar -test
