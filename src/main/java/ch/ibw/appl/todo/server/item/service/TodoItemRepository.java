package ch.ibw.appl.todo.server.item.service;

import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import ch.ibw.appl.todo.server.service.Repository;

import java.util.List;

public interface TodoItemRepository<T extends TodoItem> extends Repository<T> {
  List<T> all();
  ModelId add(T obj);
  T get(Long id);
  T findByDescription(String description);
}
