package kanban.service;

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<Task> getAllTask() throws CloneNotSupportedException;

    ArrayList<Epic> getAllEpic() throws CloneNotSupportedException;

    ArrayList<Subtask> getAllSubTask() throws CloneNotSupportedException;

    ArrayList<Subtask> getAllSubTask(int id);

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubTask();

    Task getTask(int id) throws CloneNotSupportedException;

    Epic getEpic(int id) throws CloneNotSupportedException;

    Subtask getSubTask(int id) throws CloneNotSupportedException;

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(Subtask subTask, Epic epic);

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubTask(int id, Subtask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    void setStatus(int epicID);
}
