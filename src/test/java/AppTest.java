import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.sql2o.*; //for DB support
import org.junit.*; // for @Before and @After
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();


  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Todo List!");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Categories"));
    fill("#name").with("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Tasks"));
    fill("#description").with("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void categoryShowPageDisplaysName() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    String url = String.format("http://localhost:4567/category/%d", testCategory.getId());
    goTo(url);
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskIsAddedToCategory() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/category/%d", testCategory.getId());
    goTo(url);
    fillSelect("#task_id").withText("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void categoryIsAddedToTask() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/task/%d", testTask.getId());
    goTo(url);
    fillSelect("#category_id").withText("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskShowPageDisplaysDescription() {
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/task/%d", testTask.getId());
    goTo(url);
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void updateCategory() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    String url = String.format("http://localhost:4567/category/%d/edit", testCategory.getId());
    goTo(url);
    fill("#category_name").with("Errands");
    submit(".btn", withText("Update Category Name"));
    assertThat(pageSource()).contains("Errands");
  }

  @Test
  public void updateTask() {
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/task/%d/edit", testTask.getId());
    goTo(url);
    fill("#task_description").with("Cut the grass");
    submit(".btn", withText("Update Task Description"));
    assertThat(pageSource()).contains("Cut the grass");
  }

  @Test
  public void deleteCategory() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    String url = String.format("http://localhost:4567/category/%d/edit", testCategory.getId());
    goTo(url);
    submit(".btn", withText("Delete Category"));
    assertThat(pageSource()).doesNotContain("Household chores");
  }

  @Test
  public void deleteTask() {
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/task/%d/edit", testTask.getId());
    goTo(url);
    submit(".btn", withText("Delete Task"));
    assertThat(pageSource()).doesNotContain("Mow the lawn");
  }
}
