package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    protected int id = 1;
    protected final InMemoryHistoryManager<Task> inMemoryHistoryManager = new InMemoryHistoryManager<>();

    protected void setIdForManager(int id) {
        this.id = id;
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public List<Task> getAllTask() {
        for (Task task : taskHashMap.values()) {
            inMemoryHistoryManager.add(task);
        }
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        for (Epic epic : epicHashMap.values()) {
            inMemoryHistoryManager.add(epic);
        }
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public List<Subtask> getAllSubTask() {
        for (Subtask subtasks : subTaskHashMap.values()) {
            inMemoryHistoryManager.add(subtasks);
        }
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void deleteAllSubTask() {
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            for (int i = 0; i < epic.getSubTasks().size(); i++) {
                epic.deleteSubtaskID(i);
            }
            setStatus(epic.getId());
        }
    }

    @Override
    public Task getTask(int id) {
        if (!taskHashMap.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(taskHashMap.get(id));
        return taskHashMap.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (!epicHashMap.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(epicHashMap.get(id));
        return epicHashMap.get(id);
    }

    @Override
    public Subtask getSubTask(int id) {
        if (!subTaskHashMap.containsKey(id)) {
            return null;
        }
        inMemoryHistoryManager.add(subTaskHashMap.get(id));
        return subTaskHashMap.get(id);
    }

    @Override
    public void createTask(Task task) {
        if (taskHashMap.containsValue(task)) {
            return;
        }
        task.setStatus(Status.NEW);
        task.setId(this.id);
        taskHashMap.put(this.id, task);
        this.id++;
    }

    @Override
    public void createEpic(Epic epic) {
        if (epicHashMap.containsValue(epic)) {
            return;
        }
        epic.setStatus(Status.NEW);
        epic.setId(this.id);
        epicHashMap.put(this.id, epic);
        this.id++;
    }

    @Override
    public void createSubTask(Subtask subTask, Epic epic) {
        if (subTaskHashMap.containsValue(subTask) || epicHashMap.containsKey(id)) {
            return;
        }
        subTask.setMyEpic(epic.getId());
        subTask.setStatus(Status.NEW);
        subTask.setId(this.id);
        epic.setSubTasks(subTask.getId());
        subTaskHashMap.put(this.id, subTask);
        this.id++;
    }

    @Override
    public void updateTask(int id, Task task) {
        if (task == null) {
            return;
        }
        task.setId(id);
        taskHashMap.put(id, task);
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        if (epic == null) {
            return;
        }
        epicHashMap.put(id, epic);
        epic.setId(id);
        setStatus(epic.getId());
    }

    @Override
    public void updateSubTask(int id, Subtask subTask) {
        if (subTask == null) {
            return;
        }
        subTaskHashMap.put(id, subTask);
        subTask.setId(id);
        int epicId = epicHashMap.get(subTask.getMyEpicId()).getId();
        setStatus(epicId);
    }

    @Override
    public void deleteTask(int id) {
        if (!taskHashMap.containsKey(id)) {
            return;
        }
        inMemoryHistoryManager.remove(id);
        taskHashMap.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (!epicHashMap.containsKey(id)) {
            return;
        }
        for (int subID : epicHashMap.get(id).getSubTasks()) {
            inMemoryHistoryManager.remove(subID);
            subTaskHashMap.remove(subID);
        }
        inMemoryHistoryManager.remove(id);
        epicHashMap.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        if (!subTaskHashMap.containsKey(id)) {
            return;
        }
        subTaskHashMap.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
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

    public Duration getDurationForEpic(Epic epic) {
        int duration = 0;
        for (int id : epic.getSubTasks()) {
            duration += subTaskHashMap.get(id).getDuration().toMinutes();
        }
        return Duration.ofMinutes(duration);
    }

    public LocalDateTime getEndTimeForEpic(Epic epic) {
        Optional<Subtask> subtask = getSubtasks(epic).stream()
                .max(Comparator.comparing(Subtask::getStartTime));
        if (subtask.isPresent()) {
            return subtask.get().getStartTime();
        } else {
            throw new DateTimeException("Не удалось посчитать startTime для " + epic.getClass());
        }
    }

    public LocalDateTime getStartTimeForEpic(Epic epic) {
        Optional<Subtask> subtask = getSubtasks(epic).stream()
                .min(Comparator.comparing(Subtask::getStartTime));
        if (subtask.isPresent()) {
            return subtask.get().getStartTime();
        } else {
            throw new DateTimeException("Не удалось посчитать startTime для " + epic.getClass());
        }
    }

    private List<Subtask> getSubtasks(Epic epic) {
        List<Subtask> subtasks = new ArrayList<>();
        for (int id : epic.getSubTasks()) {
            subtasks.add(subTaskHashMap.get(id));
        }
        return subtasks;
    }

    @Override
    public void setStatus(int epicID) {
        Epic epic = epicHashMap.get(epicID);

        if (epic.getSubTasks().isEmpty() || subTaskHashMap.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        //Получаем статус первой подзадачи.
        Status sampleSubStatus = subTaskHashMap.get(epic.getSubTasks().get(0)).getStatus();
        for (int i = 0; i < epic.getSubTasks().size(); i++) {
            //Получаем статус текущей подзадачи.
            Status comareStatus = subTaskHashMap.get(epic.getSubTasks().get(i)).getStatus();

            if (!sampleSubStatus.equals(comareStatus)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(sampleSubStatus);
    }
}
