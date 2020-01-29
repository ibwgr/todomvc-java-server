package ch.ibw.appl.todo.server.functional;

import ch.ibw.appl.todo.server.shared.service.JSONSerializer;
import ch.ibw.appl.todo.server.functional.shared.FunctionalTest;
import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ItemTest extends FunctionalTest {

  @Test
  public void notAcceptable() throws HttpClientException {
    GetMethod method = httpClient.get("/todo/items", false);
    HttpResponse response = httpClient.execute(method);

    assertEquals(HttpStatus.NOT_ACCEPTABLE_406, response.code());
  }

  @Test
  public void get_todos() {
    HttpResponse response = executeGet("/todo/items");
//    HttpResponse response = executeGet("/todo/items", "text/csv");

    assertEquals(HttpStatus.OK_200, response.code());
    assertEquals("application/json", response.headers().get("Content-Type").get(0));

    String body = new String(response.body());

    List<TodoItem> items = new JSONSerializer().deserialize(body, new TypeReference<List<TodoItem>>() {});
    assertEquals("Item 23", items.get(0).description);
  }

  @Test
  public void get_byId() {
    HttpResponse response = executeGet("/todo/items/23");

    assertEquals(HttpStatus.OK_200, response.code());
    assertEquals("application/json", response.headers().get("Content-Type").get(0));

    String body = new String(response.body());

    TodoItem item = new JSONSerializer().deserialize(body, new TypeReference<TodoItem>() {});
    assertEquals("Item 23", item.description);
  }

  @Test
  public void get_byId_nodFound() {
    HttpResponse response = executeGet("/todo/items/42");

    assertEquals(HttpStatus.NOT_FOUND_404, response.code());
    assertEquals("application/json", response.headers().get("Content-Type").get(0));

    String body = new String(response.body());
    assertEquals("", body);
  }

  @Test
  public void create_todo() {
    Object item = TodoItem.create("Neues Item");
    HttpResponse response = executePost("/todo/items", item);

    assertEquals(HttpStatus.OK_200, response.code());
    assertEquals("application/json", response.headers().get("Content-Type").get(0));

    String body = new String(response.body());
    ModelId id = new JSONSerializer().deserialize(body, new TypeReference<ModelId>() {});
    assertNotNull(id);
  }

  @Test
  public void create_todo_validationFailed() {
    Object item = TodoItem.create("");
    HttpResponse response = executePost("/todo/items", item);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.code());
    assertEquals("application/json", response.headers().get("Content-Type").get(0));

    String body = new String(response.body());
    assertTrue(body.contains("message"));
  }
}
