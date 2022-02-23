package ch.ibw.appl.todo.server.functional;

import ch.ibw.appl.todo.server.functional.shared.FunctionalTest;
import org.junit.Test;

import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class HelloTest extends FunctionalTest {

  @Test
  public void hello() {
    HttpResponse<String> response = executeGet("/hello");

    assertEquals(200, response.statusCode());
    assertEquals("Hello World", response.body());
  }
}
