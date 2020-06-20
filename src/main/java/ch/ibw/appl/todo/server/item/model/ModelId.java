package ch.ibw.appl.todo.server.item.model;

public class ModelId {
  public Long id;

  public static ModelId create(Long id) {
    ModelId model = new ModelId();
    model.id = id;
    return model;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ModelId modelId = (ModelId) o;

    return id != null ? id.equals(modelId.id) : modelId.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
