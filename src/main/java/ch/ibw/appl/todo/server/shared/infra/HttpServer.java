package ch.ibw.appl.todo.server.shared.infra;

import ch.ibw.appl.todo.server.hello.HelloController;
import ch.ibw.appl.todo.server.item.infra.TodoItemController;
import ch.ibw.appl.todo.server.item.service.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.LoggerFactory;
import spark.Service;

public class HttpServer {

  private final String httpPort;
  private final Boolean isTest;
  private Service server;

  public HttpServer(String httpPort, Boolean isTest) {
    this.httpPort = httpPort;
    this.isTest = isTest;
  }

  public void start() {
    server = Service.ignite();
    server.port(Integer.parseInt(httpPort));

    new TodoItemController(isTest).createRoutes(server);
    new HelloController(isTest).createRoutes(server);

    server.before(((request, response) -> {
      // exclude /hello (and CORS OPTIONS request) from requiring to accept application/json
      final boolean isCorsPreflight = request.requestMethod().equalsIgnoreCase("options");
      final boolean isHello = request.pathInfo().equalsIgnoreCase("/hello");
      if(!isHello && !isCorsPreflight){
        final boolean clientWantsJson = request.headers("Accept").contains("application/json");
        if(!clientWantsJson){
          server.halt(HttpStatus.NOT_ACCEPTABLE_406);
        }
      }
    }));

    server.before((request,response) -> response.header("Access-Control-Allow-Origin", "*"));

    server.options("/*", (request,response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }
      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if(accessControlRequestMethod != null){
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }
      return "OK";
    });

    server.afterAfter(((request, response) -> response.type("application/json")));

    server.exception(RuntimeException.class, (exception, request, response) -> {
      if(exception instanceof ValidationError){
        String message = ((ValidationError) exception).message;
        JsonNode node = JsonNodeFactory.instance.objectNode().set("message", JsonNodeFactory.instance.textNode(message));
        response.body(node.toString());
        response.status(HttpStatus.BAD_REQUEST_400);
      } else {
        LoggerFactory.getLogger(HttpServer.class).error(exception.toString());
        response.body("");
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
      }
    });

    server.notFound((request, response) -> "");

    server.awaitInitialization();
  }

  public void stop() {
    server.stop();
    server.awaitStop();
  }
}
