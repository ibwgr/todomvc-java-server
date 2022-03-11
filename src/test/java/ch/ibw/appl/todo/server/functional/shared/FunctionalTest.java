package ch.ibw.appl.todo.server.functional.shared;

import ch.ibw.appl.todo.server.shared.infra.HttpServer;
import ch.ibw.appl.todo.server.shared.service.JSONSerializer;
import org.junit.Rule;
import spark.servlet.SparkApplication;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FunctionalTest {

  public static class TodoApplication implements SparkApplication {
    private final HttpServer httpServer;

    public TodoApplication(Integer port) {
      httpServer = new HttpServer(String.valueOf(port), true);
    }

    @Override
    public void init() {
      httpServer.start();
    }

    @Override
    public void destroy() {
      httpServer.stop();
    }
  }

  @Rule
  public SparkServer<TodoApplication> httpClient = new SparkServer<>(TodoApplication.class, 4567);

  public HttpResponse<String> executeGet(String path, String acceptType) {
    HttpRequest.Builder method = httpClient.get(path);
    method.header("Accept", acceptType);
    return httpClient.execute(method);
  }

  public HttpResponse<String> executeGet(String path) {
    return executeGet(path, "application/json");
  }

  public HttpResponse<String> executePost(String path, Object body) {
    return executePost(path, body, "application/json");
  }

  public HttpResponse<String> executePost(String path, Object body, String acceptType) {
    HttpRequest.Builder method = httpClient.post(path, new JSONSerializer().serialize(body));
    method.header("Accept", acceptType);
    return httpClient.execute(method);
  }
}
