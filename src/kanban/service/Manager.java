package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Manager {
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    private int id = 1;



    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllEpic() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    public void deleteAllSubTask() {
        for (Epic epic : epicHashMap.values()) {
            for (int idSubtask : epic.getSubTasks()) {
                delSubTask(idSubtask);
                epic.deleteSubtaskID(id);
            }
            setStatus(epic.getId());
        }
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

    public Subtask getSubTask(int id) {
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

    public void createSubTask(Subtask subTask, Epic epic) {
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

    public void updateSubTask(Subtask subTask) {
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

    public ArrayList<Subtask> getAllSubTask(int id) {
        if (!epicHashMap.containsKey(id)) {
            return null;
        }
        ArrayList<Subtask> result = new ArrayList<>();
        for (int idSubTask : epicHashMap.get(id).getSubTasks()) {
            result.add(subTaskHashMap.get(idSubTask));
        }
        return result;

    }
    private void setStatus(int epicID) {
        Epic epic = epicHashMap.get(epicID);
        //Проверим на пустой список подзадач.
        if (epic.getSubTasks().isEmpty() || subTaskHashMap.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        //Получаем статус первой подзадачи.
        Status sampleSubStatus = subTaskHashMap.get(epic.getSubTasks().get(0)).getStatus();
        for (int i = 0; i < epic.getSubTasks().size(); i++) {
            //Получаем статус текущей подзадачи.
            System.out.println(i);
            Status comareStatus =  subTaskHashMap.get(epic.getSubTasks().get(i)).getStatus();
            //Если статусы подзадач не равны, то статус эпика в процессе, завершаем метод.
            if (!sampleSubStatus.equals(comareStatus)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        // Если статусы равны, то статус эпика равен статусу первой подзадачи.
        epic.setStatus(sampleSubStatus);
    }
}
