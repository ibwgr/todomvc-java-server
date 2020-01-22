package ch.ibw.appl.todo.server.hello;

import ch.ibw.appl.todo.server.Main;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;

public class HelloController {

  public HelloController(Boolean isTest) {

  }

  public void createRoutes(Service server){

    server.get("/hello", new Route() {
      @Override
      public Object handle(Request request, Response response) {
        LoggerFactory.getLogger(Main.class).info("Hello Route Called!");
        return "Hello World";
      }
    });
  }
}
