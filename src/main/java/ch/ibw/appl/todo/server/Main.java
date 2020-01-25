package ch.ibw.appl.todo.server;

import ch.ibw.appl.todo.server.shared.infra.HttpServer;

import java.util.Arrays;
import java.util.Optional;

/**
 * 1. setup spark maven based hello world project
 *  - maven & pom basics
 *    http://sparkjava.com/tutorials/maven-setup
 *    https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
 *  - http server abstraction
 *    api server framework commons
 *  - testing
 *    junit functional testing
 *    curl (install for windows curl and jq-win)
 *  - logging
 *    http://sparkjava.com/documentation#examples-and-faq
 *
 * 2. route to retrieve JSON payload of java class
 *  - json serialization repetition
 *    1. POJO TodoItem, 2. override ToString, 3. serialize as json, 4. content-type & response transformer
 *  - server, controller, model separation
 *
 * 2. in memory database. create CRUD routes
 *  - 1. get all, 2. create (generate id, create object, store object, return id)
 *  - path params
 *    1. get by id, 2. 404
 *  - validation
 *    1. description is required
 *
 * 4. jdbc database
 *  - JPA, ORM, Hibernate
 *    persistence.xml, entity / id, sql abstraction
 *    https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html
 *  - prepared statement, SQL Injection
 *  - abstraction via interfaces
 *
 * 5. optional: deployment
 *  - fat jar packaging
 *    mvn clean package && java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar
 *  - CLI port argument parsing
 *
 * Outlook:
 *  - CDI, Contextual Dependency Injection
 *    google guice
 *  - Configuration Management
 *    test vs dev vs production
 *    database migrations
 *  - Advanced ORM
 *    relations
 * */

public class Main {

  public static void main(String[] args) {
    String httpPort =  getCLIArgument(args, "server.port", "4567");
    Boolean isTest = Boolean.parseBoolean(getCLIArgument(args, "test", "false"));

    new HttpServer(httpPort, isTest).start();
  }

  static String getCLIArgument(String[] args, String name, String defaultValue){
    Optional<String> argument = Arrays.stream(Optional.ofNullable(args).orElse(new String[]{})).filter(a -> a.startsWith("--"+name+"=")).findFirst();
    if(argument.isPresent()){
      return argument.get().split("=")[1];
    }else{
      return defaultValue;
    }
  }
}