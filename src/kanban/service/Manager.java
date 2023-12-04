package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Manager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private int id = 1;



    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public void delAllTasks() {
        taskHashMap.clear();
    }

    public void delAllEpic() {
        epicHashMap.clear();
    }

    public void delAllSubTask() {
        subTaskHashMap.clear();
    }

    public Task getTask(int id) {
        if (!taskHashMap.containsKey(id)) {
            return null;
        }
        return taskHashMap.get(id);
    }

    public Epic getEpic(int id) {
        if (!epicHashMap.containsKey(id)) {
            return null;
        }
        return epicHashMap.get(id);
    }

    public SubTask getSubTask(int id) {
        if(!subTaskHashMap.containsKey(id)) {
            return null;
        }
        return subTaskHashMap.get(id);
    }

    public void createTask(Task task) {
        if (taskHashMap.containsValue(task)) {
            return;
        }
        task.setStatus(Status.NEW);
        task.setId(this.id);
        taskHashMap.put(this.id, task);
        this.id++;
    }

    public void createEpic(Epic epic) {
        if (epicHashMap.containsValue(epic)) {
            return;
        }
        epic.setStatus(Status.NEW);
        epic.setId(this.id);
        epicHashMap.put(this.id, epic);
        this.id++;
    }

    public void createSubTask(SubTask subTask, Epic epic) {
        if (subTaskHashMap.containsValue(subTask)) {
            return;
        }
        subTask.setMyEpic(epic.getId());
        subTask.setStatus(Status.NEW);
        subTask.setId(this.id);
        epic.setSubTasks(subTask.getId());
        subTaskHashMap.put(this.id, subTask);
        this.id++;
    }

    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        epicHashMap.put(epic.getId(), epic);
        setStatus(epic.getId());
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        subTaskHashMap.put(subTask.getId(), subTask);
        setStatus(epicHashMap.get(subTask.getMyEpic()).getId());
    }

    public void delTask(int id) {
        if (!taskHashMap.containsKey(id)) {
            return;
        }
        taskHashMap.remove(id);
    }

    public void delEpic(int id) {
        if (!epicHashMap.containsKey(id)) {
            return;
        }
        for (int subID : epicHashMap.get(id).getSubTasks()) {
            delSubTask(subID);
        }
        epicHashMap.remove(id);
    }

    public void delSubTask(int id) {
        if (!subTaskHashMap.containsKey(id)) {
            return;
        }
        subTaskHashMap.remove(id);
    }

    public ArrayList<SubTask> getAllSubTask(int id) {
        if (!epicHashMap.containsKey(id)) {
            return null;
        }
        ArrayList<SubTask> result = new ArrayList<>();
        for (int idSubTask : epicHashMap.get(id).getSubTasks()) {
            result.add(subTaskHashMap.get(idSubTask));
        }
        return result;

    }
    private void setStatus(int epicID) {
        Set<Status> statusSubTask = new HashSet<>();
        Epic currentEpic = epicHashMap.get(epicID);
        for (int subID : currentEpic.getSubTasks()) {
            statusSubTask.add(subTaskHashMap.get(subID).getStatus());
        }
        if (statusSubTask.size() >= 2) {
            currentEpic.setStatus(Status.IN_PROGRESS);
        } else {
            if (!statusSubTask.add(Status.NEW)) {
                currentEpic.setStatus(Status.NEW);
            } else {
                currentEpic.setStatus(Status.DONE);
            }
        }
    }
}
