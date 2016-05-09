import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

import java.util.ArrayList;

public class App {

  public static void main (String[] args){
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save(); // ***ADDED FOR DB VERSION***
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/category/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String category_name = request.queryParams("category_name");
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      category.update(category_name);
      model.put("category", category);
      model.put("allTasks", Task.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/category/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("category", category);
      model.put("allTasks", Task.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/category/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("category", category);
      model.put("template", "templates/update-category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/tasks", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("tasks", Task.all());
      model.put("template", "templates/tasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      newTask.save();
      model.put("tasks", Task.all());
      model.put("template", "templates/tasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/task/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params(":id")));
      model.put("allCategories", Category.all());
      model.put("task", task);
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/task/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params(":id")));
      model.put("task", task);
      model.put("template", "templates/update-task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_task", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      category.addTask(task);
      response.redirect("/category/" + categoryId);
      return null;
    });

    post("/add_category", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      task.addCategory(category);
      response.redirect("/task/" + taskId);
      return null;
    });
  }
}
