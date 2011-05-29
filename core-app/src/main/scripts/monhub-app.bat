
set HOST=%1
java -Djava.rmi.server.hostname=%HOST% -jar lib\\mon-hub-app-0.1-SNAPSHOT.jar %*
