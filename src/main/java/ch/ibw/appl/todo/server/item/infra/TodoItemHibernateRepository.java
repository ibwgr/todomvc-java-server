package ch.ibw.appl.todo.server.item.infra;

import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import ch.ibw.appl.todo.server.item.service.TodoItemRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class TodoItemHibernateRepository implements TodoItemRepository<TodoItem> {
  private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
  private EntityManager em = emf.createEntityManager();

  @Override
  public List<TodoItem> all() {
    return (List<TodoItem>) em.createQuery("SELECT t FROM TodoItem t").getResultList();
  }

  @Override
  public ModelId add(TodoItem item) {
    em.getTransaction().begin();
    em.persist(item);
    em.getTransaction().commit();
    return ModelId.create((Long)emf.getPersistenceUnitUtil().getIdentifier(item));
  }

  @Override
  public TodoItem get(Long id) {
    return em.find(TodoItem.class, id);
  }

  @Override
  public TodoItem findByDescription(String description) {
    // TODO
    return null;
  }
}
