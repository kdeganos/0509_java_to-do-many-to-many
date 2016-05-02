import java.util.ArrayList;

public class Category {
  private static ArrayList<Category> instances = new ArrayList<Category>();
  private String mCategory;
  private int mId;
  private ArrayList<Task> mTasks;

  public Category(String category) {
    instances.add(this);
    mCategory = category;
    mId = instances.size();
    mTasks = new ArrayList<Task>();
  }

  public String getName() {
    return mCategory;
  }

  public int getId() {
    return mId;
  }

  public ArrayList<Task> getTasks() {
    return mTasks;
  }

  public void addTask(Task newTask) {
    mTasks.add(newTask);
  }

  public static Category find(int id) {
    try {
      return instances.get(id - 1);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public static ArrayList<Category> all() {
    return instances;
  }

  public static void clear() {
    instances.clear();
  }
}
