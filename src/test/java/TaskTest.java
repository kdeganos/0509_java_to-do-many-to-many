import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.sql2o.*;

public class TaskTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();
 //  @Before
 //  public void setUp() {
 //    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
 //  }
 //
 //  @After
 // public void tearDown() {
 //   try(Connection con = DB.sql2o.open()) {
 //     String deleteTasksQuery = "DELETE FROM tasks *;";
 //     String deleteCategoriesQuery = "DELETE FROM categories *;";
 //     con.createQuery(deleteTasksQuery).executeUpdate();
 //     con.createQuery(deleteCategoriesQuery).executeUpdate();
 //   }
 // }

 @Test
 public void Task_instantiatesCorrectly_true(){
   Task myTask = new Task("Mow the lawn");
   assertEquals(true, myTask instanceof Task);
 }

 @Test
  public void getDescription_taskWithDescription_String(){
    Task myTask = new Task("Mow the lawn");
    assertEquals("Mow the lawn", myTask.getDescription());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Task.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAreTheSame() {
    Task firstTask = new Task("Mow the lawn");
    Task secondTask = new Task("Mow the lawn");
    assertTrue(firstTask.equals(secondTask));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    assertTrue(Task.all().get(0).equals(myTask));
  }

  @Test
  public void save_assignsIdToObject() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(myTask.getId(), savedTask.getId());
  }

  @Test
  public void find_findsTasksInDatabase_True() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertTrue(myTask.equals(savedTask));
  }

  @Test
  public void update_updatesTaskDescription_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myTask.update("Take a nap");
    assertEquals("Take a nap", Task.find(myTask.getId()).getDescription());
  }

  @Test
  public void delete_deletesTask_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    int myTaskId = myTask.getId();
    myTask.delete();
    assertEquals(null, Task.find(myTaskId));
  }
}
