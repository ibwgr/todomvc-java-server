package ch.ibw.appl.todo.server.item.infra;

import ch.ibw.appl.todo.server.item.service.TodoItemService;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import com.fasterxml.jackson.core.type.TypeReference;
import spark.ResponseTransformer;
import spark.Service;

public class TodoItemController {
  private TodoItemService todoItemService;

  public TodoItemController(Boolean isTest) {
    if (isTest) {
      TodoItemInMemoryRepository repo = new TodoItemInMemoryRepository(isTest);
      todoItemService = new TodoItemService(repo);
    } else {
      todoItemService = new TodoItemService();
    }
  }

  public void createRoutes(Service server) {
    ResponseTransformer jsonSerializer = model -> new JSONSerializer().serialize(model);

    server.get("/todo/items", "application/json",
            (request, response) -> {
              response.type("application/json");
              return todoItemService.all();
            },
            jsonSerializer);

//    server.get("/todo/items", "text/csv",
//            (request, response) ->  todoItemService.all(),
//            model -> null/*make csv*/);

    server.get("/todo/items/:id", (request, response) -> {
      long id = Long.parseLong(request.params("id"));
      return todoItemService.getById(id);
    }, jsonSerializer);

    server.post("/todo/items", (request, response) -> {
      TodoItem item = new JSONSerializer().deserialize(request.body(), new TypeReference<TodoItem>() {
      });
      return todoItemService.create(item);
    }, jsonSerializer);
  }
}
