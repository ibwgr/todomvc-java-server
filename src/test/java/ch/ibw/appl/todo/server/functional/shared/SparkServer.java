package ch.ibw.appl.todo.server.functional.shared;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import spark.Service;
import spark.servlet.SparkApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SparkServer<T extends SparkApplication> extends ExternalResource {
  private final Class<T> sparkApplicationClass;
  private T sparkApplication;
  private final String protocolHostPort;
  private final HttpClient httpClient;
  private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

  SparkServer(Class<T> sparkApplicationClass) {
    this(sparkApplicationClass, Service.SPARK_DEFAULT_PORT);
  }

  SparkServer(Class<T> sparkApplicationClass, int port) {
    this.sparkApplicationClass = sparkApplicationClass;
    this.protocolHostPort = "http://localhost:" + port;
    this.httpClient = HttpClient.newHttpClient();
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return super.apply(base, description);
  }

  @Override
  protected void before() throws Throwable {
    this.sparkApplication = this.sparkApplicationClass.getDeclaredConstructor().newInstance();
    this.sparkApplication.init();
  }

  @Override
  protected void after() {
    this.sparkApplication.destroy();
  }

  public HttpRequest.Builder get(String path) {
    return requestBuilder.GET().uri(createURI(path));
  }

  public HttpRequest.Builder post(String path, String body) {
    return requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).uri(createURI(path));
  }

  public HttpRequest.Builder put(String path, String body) {
    return requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body)).uri(createURI(path));
  }

  public HttpRequest.Builder delete(String path) {
    return requestBuilder.DELETE().uri(createURI(path));
  }

  public HttpResponse<String> execute(HttpRequest.Builder request) {
    try {
      return this.httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private URI createURI(String path) {
    try {
      return new URI(this.protocolHostPort + path);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
