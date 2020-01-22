package ch.ibw.appl.todo.server.item.service;

import ch.ibw.appl.todo.server.item.infra.TodoItemHibernateRepository;
import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;

import java.util.List;

public class TodoItemService {
  private final TodoItemRepository<TodoItem> repo;

  public TodoItemService() {
    repo = new TodoItemHibernateRepository();
  }

  public TodoItemService(TodoItemRepository<TodoItem> repo) {
    this.repo = repo;
  }

  public List<TodoItem> all() {
    return repo.all();
  }

  public ModelId create(TodoItem item) {
    if(item.description == null || item.description.isEmpty()){
      throw new ValidationError("description can not be empty");
    }

    return repo.add(item);
  }

  public TodoItem getById(Long id) {
    return repo.get(id);
  }
}
