import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private int id;

    public Manager() {
        this.id = 0;
    }

    public ArrayList<SubTask> getSubTaskFromEpic(int id) {
        EpicTask epicTask = (EpicTask) tasks.get(id);
        return epicTask.getSubTasks();
    }

    public void updateTask(int id, Task task) {
        this.tasks.put(id, task);
    }

    public void createTask (Task task) {
        id++;
        this.tasks.put(id, task);
    }

    public void delTaskWithID (int id) {
        this.tasks.remove(id);
    }

    public Task getTaskWithID (int id) {
        return this.tasks.get(id);
    }

    public void deletedAllTasks() {
        this.tasks.clear();
        this.id = 0;
    }

    public ArrayList<Task> getAllTask() {
        return (ArrayList<Task>) tasks.values();
    }

}
