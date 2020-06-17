package ch.ibw.appl.todo.server.item.infra;

import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import ch.ibw.appl.todo.server.item.service.TodoItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TodoItemInMemoryRepository implements TodoItemRepository<TodoItem> {
  private Map<ModelId, TodoItem> map = Collections.synchronizedMap(new LinkedHashMap<>());
  private long nextId = 0;

  TodoItemInMemoryRepository(boolean isTest) {
    if(isTest){
      nextId = 23;
      this.add(TodoItem.create("Item 23"));
    }
  }

  @Override
  public List<TodoItem> all() {
    return new ArrayList<>(map.values());
  }

  @Override
  public ModelId add(TodoItem item) {
    ModelId id = ModelId.create(nextId++);
    map.put(id, item);
    return id;
  }

  @Override
  public TodoItem get(Long id) {
    return map.entrySet().stream()
            .filter(e -> e.getKey().id.equals(id))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(null);
  }

  @Override
  public TodoItem findByDescription(String description) {
    // TODO
    return null;
  }

  @Override
  public void remove(Long id) {
    map.remove(ModelId.create(id));
  }
}
