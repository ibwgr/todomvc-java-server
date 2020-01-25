package ch.ibw.appl.todo.server.shared.service;

import ch.ibw.appl.todo.server.item.model.ModelId;

import java.util.List;

public interface Repository<T> {
  List<T> all();
  ModelId add(T obj);
  T get(Long id);
}
