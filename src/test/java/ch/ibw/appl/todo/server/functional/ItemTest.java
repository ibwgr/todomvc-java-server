package ch.ibw.appl.todo.server.functional;

import ch.ibw.appl.todo.server.functional.shared.FunctionalTest;
import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import ch.ibw.appl.todo.server.shared.service.JSONSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ItemTest extends FunctionalTest {

  @Test
  public void notAcceptable() {
    HttpRequest.Builder method = httpClient.get("/todo/items");
    HttpResponse<String> response = httpClient.execute(method);

    assertEquals(HttpStatus.NOT_ACCEPTABLE_406, response.statusCode());
  }

  @Test
  public void get_todos() {
    HttpResponse<String> response = executeGet("/todo/items");
//    HttpResponse<String response = executeGet("/todo/items", "text/csv");

    assertEquals(HttpStatus.OK_200, response.statusCode());
    assertEquals("application/json", response.headers().firstValue("Content-Type").get());

    String body = response.body();

    List<TodoItem> items = new JSONSerializer().deserialize(body, new TypeReference<List<TodoItem>>() {
    });
    assertEquals("Item 23", items.get(0).description);
  }

  @Test
  public void get_byId() {
    HttpResponse<String> response = executeGet("/todo/items/23");

    assertEquals(HttpStatus.OK_200, response.statusCode());
    assertEquals("application/json", response.headers().firstValue("Content-Type").get());

    String body = response.body();

    TodoItem item = new JSONSerializer().deserialize(body, new TypeReference<TodoItem>() {
    });
    assertEquals("Item 23", item.description);
  }

  @Test
  public void get_byId_notFound() {
    HttpResponse<String> response = executeGet("/todo/items/42");

    assertEquals(HttpStatus.NOT_FOUND_404, response.statusCode());
    assertEquals("application/json", response.headers().firstValue("Content-Type").get());

    String body = response.body();
    assertEquals("", body);
  }

  @Test
  public void create_todo() {
    Object item = TodoItem.create("Neues Item");
    HttpResponse<String> response = executePost("/todo/items", item);

    assertEquals(HttpStatus.CREATED_201, response.statusCode());
    assertEquals("application/json", response.headers().firstValue("Content-Type").get());

    String body = response.body();
    ModelId id = new JSONSerializer().deserialize(body, new TypeReference<ModelId>() {
    });
    assertNotNull(id);
  }

  @Test
  public void create_todo_validationFailed() {
    Object item = TodoItem.create("");
    HttpResponse<String> response = executePost("/todo/items", item);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.statusCode());
    assertEquals("application/json", response.headers().firstValue("Content-Type").get());

    String body = response.body();
    assertTrue(body.contains("message"));
  }
}
