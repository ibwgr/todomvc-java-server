package ch.ibw.appl.todo.server.functional;

import ch.ibw.appl.todo.server.functional.shared.FunctionalTest;
import com.despegar.http.client.HttpResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloTest extends FunctionalTest {

  @Test
  public void hello() {
    HttpResponse response = executeGet("/hello");

    assertEquals(200, response.code());
    assertEquals("Hello World", new String(response.body()));
  }
}
