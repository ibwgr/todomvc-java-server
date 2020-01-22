package ch.ibw.appl.todo.server.item.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TodoItem {
  public String description;

  @Id
  @GeneratedValue
  public Long id;

  public static TodoItem create(String description) {
    TodoItem todoItem = new TodoItem();
    todoItem.description = description;
    return todoItem;
  }

  @Override
  public String toString() {
    return "TodoItem{" +
            "description='" + description + '\'' +
            ", id=" + id +
            '}';
  }
}
