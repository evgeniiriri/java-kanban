package kanban.service;

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    public List<Subtask> getSubTasksWithEpic(Epic epic);

    public void createSubTask(Subtask subTask);

    TreeSet<Task> getPrioritizedTasks();
    List<Task> getHistory();

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubTask();

    List<Subtask> getAllSubTask(int id);

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubTask();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubTask(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(Subtask subTask, Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    void setStatus(int epicID);
}
