package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.taskexception.DateTimeTaskManagerException;
import kanban.service.taskexception.TaskManagerBaseException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryTaskManager implements TaskManager {
    private static final Logger log = Logger.getLogger(InMemoryTaskManager.class.getName());
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    protected final InMemoryHistoryManager<Task> inMemoryHistoryManager = new InMemoryHistoryManager<>();
    protected TreeSet<Task> sortedPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int id = 1;

    public InMemoryTaskManager() {
        log.log(Level.INFO, "Инициализация " + InMemoryTaskManager.class.getName());
    }

    protected void setIdForManager(int id) {
        this.id = id;
    }

    @Override
    public List<Subtask> getAllSubTask(int id) {
        if (!epicHashMap.containsKey(id)) {
            return null;
        }
        return epicHashMap.get(id).getSubTasks().stream().map(subTaskHashMap::get).toList();
    }

    private LocalDateTime getEndTimeForEpic(Epic epic) throws DateTimeTaskManagerException {
        if (epic.getDuration().isZero() || epic.getDuration().isNegative()) {
            log.log(Level.SEVERE, "Нет возможности высчитать конец задачи, так как продолжительность не корректная.");
            throw new DateTimeTaskManagerException("Нет возможности высчитать конец задачи, так как продолжительность не корректная.");
        }
        if (epic.getStartTime() == null) {
            log.log(Level.SEVERE, "Нет возможности высчитать конец задачи, так как начальное время пустое.");
            throw new DateTimeTaskManagerException("Нет возможности высчитать конец задачи, так как начальное время пустое.");
        }
        LocalDateTime endTime = epic.getStartTime();
        return endTime.plus(epic.getDuration());
    }
    public List<Subtask> getEpicSubtask(Epic epic) {
        //метод нужен для корректой работы истории.
        return epic.getSubTasks().stream().map(subTaskHashMap::get).toList();
    }

    private LocalDateTime getStartTimeForEpic(Epic epic) throws DateTimeTaskManagerException {
        Optional<Subtask> subtask = getEpicSubtask(epic).stream()
                .min(Comparator.comparing(Subtask::getStartTime));
        if (subtask.isPresent()) {
            return subtask.get().getStartTime();
        } else {
            log.log(Level.SEVERE, "Не удалось посчитать startTime для " + epic.getClass());
            throw new DateTimeTaskManagerException("Не удалось посчитать startTime для " + epic.getClass());
        }
    }


    private Duration getDurationForEpic(Epic epic) {
        int duration = 0;
        for (int id : epic.getSubTasks()) {
            duration += (int) subTaskHashMap.get(id).getDuration().toMinutes();
        }
        return Duration.ofMinutes(duration);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedPrioritizedTasks;
    }

    private void add(Task task) throws DateTimeTaskManagerException {
        boolean havFreeTimeForTask = sortedPrioritizedTasks.stream().noneMatch(task1 -> validationTime(task, task1));

        if (havFreeTimeForTask) {
            sortedPrioritizedTasks.add(task);
        } else {
            throw new DateTimeTaskManagerException("В это время выполняется другая задача.");
        }
    }

    private boolean validationTime(Task taskAdded, Task taskSecond) {
        return taskAdded.getStartTime().isBefore(taskSecond.getEndTime())
                && taskAdded.getEndTime().isAfter(taskSecond.getStartTime());
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
                epic.deleteSubtask(i);
            }
            epic.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1, 1));
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(LocalDateTime.now());
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
    public List<Subtask> getSubTasksWithEpic(Epic epic) {
        for (int idSubtask : epic.getSubTasks()) {
            inMemoryHistoryManager.add(subTaskHashMap.get(idSubtask));
        }
        return epic.getSubTasks().stream().map(subTaskHashMap::get).toList();
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

        try {
            add(task);
        } catch (TaskManagerBaseException e) {
            log.log(Level.INFO, e.getMessage() + System.lineSeparator() + task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epicHashMap.containsValue(epic)) {
            return;
        }
        epic.setStatus(Status.NEW);
        epic.setId(this.id);
        epicHashMap.put(this.id, epic);
        if (!epic.getSubTasks().isEmpty()) {
            try {
                epic.setStartTime(getStartTimeForEpic(epic));
                epic.setDuration(getDurationForEpic(epic));
                epic.setEndTime(getEndTimeForEpic(epic));
            } catch (TaskManagerBaseException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
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
        try {
            epic.setStartTime(getStartTimeForEpic(epic));
            epic.setDuration(getDurationForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
            this.id++;
            add(subTask);
        } catch (TaskManagerBaseException e) {
            log.log(Level.INFO, e.getMessage() + System.lineSeparator() + subTask);
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        if (task == null) {
            return;
        }
        task.setId(id);
        taskHashMap.put(id, task);
        try {
            add(task);
        } catch (TaskManagerBaseException e) {
            log.log(Level.INFO, e + System.lineSeparator() + task);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setSubTasks(epicHashMap.get(id).getSubTasks());
        epic.setId(id);
        setStatus(epic.getId());
        epicHashMap.put(id, epic);
        try {
            epic.setStartTime(getStartTimeForEpic(epic));
            epic.setDuration(getDurationForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
        } catch (TaskManagerBaseException e) {
            log.log(Level.INFO, e.getMessage() + System.lineSeparator() + epic);
        }
    }

    @Override
    public void updateSubTask(int id, Subtask subtask) {
        if (subtask == null) {
            return;
        }
        Subtask subtaskOld = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subtaskOld.getMyEpicId());

        subTaskHashMap.put(id, subtask);
        setStatus(epic.getId());
        try {
            //Пересчет времени для эпика, если подзадача имеет другое время.
            if (!subtask.getStartTime().equals(subtaskOld.getStartTime())) {
                epic.setStartTime(getStartTimeForEpic(epic));
                epic.setEndTime(getEndTimeForEpic(epic));
            }
            if (!subtask.getDuration().equals(subtaskOld.getDuration())) {
                epic.setDuration(getDurationForEpic(epic));
                epic.setEndTime(getEndTimeForEpic(epic));
            }
            add(subtask);
        } catch (TaskManagerBaseException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void deleteTask(int id) {
        if (!taskHashMap.containsKey(id)) {
            return;
        }
        if (inMemoryHistoryManager.getHistory().size() > 1) {
            inMemoryHistoryManager.remove(id);
        }
        if (sortedPrioritizedTasks.contains(taskHashMap.get(id))) {
            sortedPrioritizedTasks.remove(taskHashMap.get(id));
        }
        taskHashMap.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (!epicHashMap.containsKey(id)) {
            return;
        }
        for (int subID : epicHashMap.get(id).getSubTasks()) {
            if (inMemoryHistoryManager.getHistory().size() > 1) {
                inMemoryHistoryManager.remove(id);
            }
            if (sortedPrioritizedTasks.contains(subTaskHashMap.get(subID))) {
                sortedPrioritizedTasks.remove(subTaskHashMap.get(subID));
            }
            subTaskHashMap.remove(subID);
        }
        if (inMemoryHistoryManager.getHistory().size() >= 1) {
            inMemoryHistoryManager.remove(id);
        }
        epicHashMap.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        if (!subTaskHashMap.containsKey(id)) {
            return;
        }
        if (inMemoryHistoryManager.getHistory().size() > 1) {
            inMemoryHistoryManager.remove(id);
        }
        Epic epic = epicHashMap.get(subTaskHashMap.get(id).getMyEpicId());
        Subtask subtask = subTaskHashMap.get(id);
        if (sortedPrioritizedTasks.contains(subtask)) {
            sortedPrioritizedTasks.remove(subtask);
        }
        subTaskHashMap.remove(id);
        epic.deleteSubtask(subtask.getId());
        try {
            epic.setStartTime(getStartTimeForEpic(epic));
            epic.setDuration(getDurationForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
        } catch (TaskManagerBaseException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
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
