package ch.ibw.appl.todo.server.item.infra;

import ch.ibw.appl.todo.server.item.model.ModelId;
import ch.ibw.appl.todo.server.item.model.TodoItem;
import ch.ibw.appl.todo.server.item.service.TodoItemRepository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TodoItemSQL2ORepository implements TodoItemRepository<TodoItem> {

  private final Sql2o sql2o;

  TodoItemSQL2ORepository(boolean isTest) {
    if(isTest){
      sql2o = new Sql2o("jdbc:hsqldb:mem:todo", "sa", "");
      try(Connection conn = sql2o.open()){
        executeFile(conn, "META-INF/createtables.sql");
        executeFile(conn, "META-INF/testdata.sql");
      }
    }else{
      String defaultURI = "jdbc:mysql://localhost:3306/todo";
      String jdbcURI = System.getenv("JDBC_URI");
      if (jdbcURI == null || jdbcURI.isEmpty()) {
        jdbcURI = defaultURI;
      }
      System.out.println("JDBC URI:" + jdbcURI);
      sql2o = new Sql2o(jdbcURI, "todo-web", "1234");
    }
  }

  private void executeFile(Connection conn, String s) {
    String path = getClass().getClassLoader().getResource(s).getPath();
    String content;
    try {
      content = new String(Files.readAllBytes(Paths.get(path)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    for(String line : content.split(";")){
      if(!line.trim().isEmpty()){
        conn.createQuery(line).executeUpdate();
      }
    }
  }

  @Override
  public List<TodoItem> all() {
    try(Connection conn = sql2o.open()){
      return conn.createQuery("select * from TodoItem").executeAndFetch(TodoItem.class);
    }
  }

  @Override
  public ModelId add(TodoItem item) {
    try(Connection conn = sql2o.open()){
      Query preparedStatement = conn.createQuery("insert into TodoItem (description) values (:description)").bind(item);
      int newId = preparedStatement.executeUpdate().getResult();
      return ModelId.create(Long.valueOf(newId));
    }
  }

  @Override
  public TodoItem get(Long id) {
    try (Connection conn = sql2o.open()) {
      return conn.createQuery("select * from TodoItem where id=:id").addParameter("id", id).executeAndFetchFirst(TodoItem.class);
    }
  }

  @Override
  public TodoItem findByDescription(String description) {
    return null;
  }
}
