package kanban.service;

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> storage = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (storage.size() == 10 ) {
            storage.remove(0);
        }
        Task cloneTask = new Task(task.getName(), task.getDescription());
        cloneTask.setId(task.getId());
        cloneTask.setStatus(task.getStatus());
        storage.add(cloneTask);
    }

    @Override
    public void add(Epic epic) {
        if (storage.size() == 10 ) {
            storage.remove(0);
        }
        Epic cloneEpic = new Epic(epic.getName(), epic.getDescription());
        cloneEpic.setSubTasks(epic.getSubTasks());
        cloneEpic.setId(epic.getId());
        cloneEpic.setStatus(epic.getStatus());
        storage.add(cloneEpic);
    }

    @Override
    public void add(Subtask subtask) {
        if (storage.size() == 10 ) {
            storage.remove(0);
        }
        Subtask cloneSubtask = new Subtask(subtask.getName(), subtask.getDescription());
        cloneSubtask.setStatus(subtask.getStatus());
        cloneSubtask.setMyEpic(subtask.getMyEpicId());
        cloneSubtask.setId(subtask.getId());
        storage.add(cloneSubtask);
     }

    @Override
    public ArrayList<Task> getHistory() {
        return storage;
    }
}
