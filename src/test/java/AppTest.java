import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.sql2o.*; //for DB support
import org.junit.*; // for @Before and @After

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Before
  public  void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM tasks *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Todo List!");
    assertThat(pageSource()).contains("View Category List");
    assertThat(pageSource()).contains("Add a New Category");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Add a New Category"));
    fill("#name").with("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("Your category has been saved.");
  }

  @Test
  public void categoryIsDisplayedTest() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Household chores");
  }
  @Test
  public void categoryShowPageDisplaysName() {
    goTo("http://localhost:4567/categories/new");
    fill("#name").with("Household chores");
    submit(".btn");
    click("a", withText("View Categories"));
    click("a", withText("Household chores"));
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void categoryTasksFormIsDisplayed() {
    goTo("http://localhost:4567/categories/new");
    fill("#name").with("Shopping");
    submit(".btn");
    click("a", withText("View Categories"));
    click("a", withText("Shopping"));
    click("a", withText("Add a new task"));
    assertThat(pageSource()).contains("Add a task to Shopping");
  }

  @Test
  public void tasksIsAddedAndDisplayed() {
    goTo("http://localhost:4567/categories/new");
    fill("#name").with("Banking");
    submit(".btn");
    click("a", withText("View Categories"));
    click("a", withText("Banking"));
    click("a", withText("Add a new task"));
    fill("#description").with("Deposit paycheck");
    submit(".btn");
    click("a", withText("View Categories"));
    click("a", withText("Banking"));
    assertThat(pageSource()).contains("Deposit paycheck");
  }

  @Test
  public void allTasksDisplayDescriptionOnCategoryPage() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task firstTask = new Task("Mow the lawn", myCategory.getId());
    firstTask.save();
    Task secondTask = new Task("Do the dishes", myCategory.getId());
    secondTask.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Mow the lawn");
    assertThat(pageSource()).contains("Do the dishes");
  }

// @Test
// public void rootTest() {
//   goTo("http://localhost:4567/");
//   assertThat(pageSource()).contains("Task list!");
// }
//
// @Test
// public void taskIsCreatedTest() {
//   goTo("http://localhost:4567/");
//   click("a", withText("Add a new task"));
//   fill("#description").with("Mow the lawn");
//   submit(".btn");
//   assertThat(pageSource()).contains("Your task has been saved.");
// }
//
// @Test
// public void taskIsDisplayedTest() {
//   goTo("http://localhost:4567/tasks/new");
//   fill("#description").with("Mow the lawn");
//   submit(".btn");
//   click("a", withText("View tasks"));
//   assertThat(pageSource()).contains("Mow the lawn");
// }
//
// @Test
// public void multipleTasksAreDisplayedTest() {
//   goTo("http://localhost:4567/tasks/new");
//   fill("#description").with("Mow the lawn");
//   submit(".btn");
//   goTo("http://localhost:4567/tasks/new");
//   fill("#description").with("Buy groceries");
//   submit(".btn");
//   click("a", withText("View tasks"));
//   assertThat(pageSource()).contains("Mow the lawn");
//   assertThat(pageSource()).contains("Buy groceries");
// }
//
// @Test
// public void taskShowPageDisplaysDescription() {
//   goTo("http://localhost:4567/tasks/new");
//   fill("#description").with("Do the dishes");
//   submit(".btn");
//   click("a", withText("View tasks"));
//   click("a", withText("Do the dishes"));
//   assertThat(pageSource()).contains("Do the dishes");
// }
//
// @Test
// public void taskNotFoundMessageShown() {
//   goTo("http://localhost:4567/tasks/999");
//   assertThat(pageSource()).contains("Task not found");
// }
}
