# Java
Build jars (fat and thin jar): `mvn -DskipTests=true clean dependency:copy-dependencies package`

## Thin jar, no Manifest
`java -classpath "target/server-1.0-SNAPSHOT.jar:target/dependency/*" ch.ibw.appl.todo.server.Main --test=true`

WINDOWS: Use ; instead of : in classpath.

## Fat jar, with Manifest
This does not work with hsqldb in memory database. Hence, you require a running mysql/mariadb database.

`java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar`

# Docker

Build image `docker build --tag todomvc-java-server .`

Run built image `docker run --rm -p 4567:4567 todomvc-java-server` 
