package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.tasklist.Node;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static final Logger log = Logger.getLogger(InMemoryTaskManager.class.getName());

    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subTaskHashMap = new HashMap<>();
    protected int id = 1;
    protected final InMemoryHistoryManager<Task> inMemoryHistoryManager = new InMemoryHistoryManager<>();
    protected TreeSet<Task> sortedPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager() {
        log.log(Level.INFO, "Инициализация " + InMemoryTaskManager.class.getName());
    }

    protected void setIdForManager(int id) {
        this.id = id;
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

    public LocalDateTime getEndTimeForEpic(Epic epic) {
        if (epic.getDuration().isZero() || epic.getDuration().isNegative()) {
            log.log(Level.SEVERE, "Нет возможности высчитать конец задачи, так как продолжительность не корректная.");
            throw new DateTimeException("Нет возможности высчитать конец задачи, так как продолжительность не корректная.");
        }
        if (epic.getStartTime() == null) {
            log.log(Level.SEVERE, "Нет возможности высчитать конец задачи, так как начальное время пустое.");
            throw new DateTimeException("Нет возможности высчитать конец задачи, так как начальное время пустое.");
        }
        LocalDateTime endTime = epic.getStartTime();
        return endTime.plus(epic.getDuration());
    }

    public LocalDateTime getStartTimeForEpic(Epic epic) {
        Optional<Subtask> subtask = getSubtasks(epic).stream()
                .min(Comparator.comparing(Subtask::getStartTime));
        if (subtask.isPresent()) {
            return subtask.get().getStartTime();
        } else {
            log.log(Level.SEVERE, "Не удалось посчитать startTime для " + epic.getClass());
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

    public Duration getDurationForEpic(Epic epic) {
        int duration = 0;
        for (int id : epic.getSubTasks()) {
            duration += (int) subTaskHashMap.get(id).getDuration().toMinutes();
        }
        return Duration.ofMinutes(duration);
    }

    public TreeSet<Task> getPrioritizedTasks() {
//        if (sortedPrioritizedTasks == null || sortedPrioritizedTasks.isEmpty()) {
//            sortedPrioritizedTasks = taskHashMap.values().stream()
//                    .filter(task -> task.getStartTime() != null && task.getDuration() != null)
//                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
//            //Заполняем Task.
//            sortedPrioritizedTasks.addAll(
//                    subTaskHashMap.values().stream()
//                            .filter(subtask -> subtask.getStartTime() != null && subtask.getDuration() != null)
//                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Subtask::getStartTime))))
//            );
//            //Добавляем Subtasks.
//        }

        return sortedPrioritizedTasks;
    }

    public void add(Task task) {
        boolean havFreeTimeForTask = sortedPrioritizedTasks.stream().noneMatch(task1 -> validationTime(task, task1));

        if (havFreeTimeForTask) {
            sortedPrioritizedTasks.add(task);
        } else {
            throw new DateTimeException("В это время выполняется другая задача.");
        }

    }

    public boolean validationTime(Task taskAdded, Task taskSecond) {
        System.out.println(sortedPrioritizedTasks);
        LocalDateTime t1 = taskAdded.getEndTime();
        LocalDateTime t2 = taskSecond.getStartTime();
        System.out.println(t1 + " " + taskAdded);
        System.out.println(t2 + " " + taskSecond);
        System.out.println(" ");
        if (sortedPrioritizedTasks.isEmpty()) {
            return false;
        } else if (taskAdded.getEndTime().isBefore(taskSecond.getStartTime())
                || taskAdded.getEndTime().equals(taskSecond.getStartTime())) {
            return true; //Пересечение начала задачи.
        } else if (taskSecond.getStartTime().isAfter(taskAdded.getEndTime())
                || taskSecond.getStartTime().equals(taskAdded.getEndTime())) {
            return true; //Пересечение конца задачи.
        } else {
            return false;
        }
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
        } catch (DateTimeException e) {
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
            } catch (DateTimeException e) {
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
        } catch (DateTimeException e) {
            log.log(Level.SEVERE, e.getMessage() + "\n" + subTask.getName());
        }
        this.id++;

        try {
            add(subTask);
        } catch (DateTimeException e) {
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
        Subtask subtask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTaskHashMap.get(id).getMyEpicId());

        subTaskHashMap.put(id, subTask);
        setStatus(epic.getId());

        if (!subTask.getStartTime().equals(subtask.getStartTime())) {
            epic.setStartTime(getStartTimeForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
        }
        if (!subTask.getDuration().equals(subtask.getDuration())) {
            epic.setDuration(getDurationForEpic(epic));
            epic.setEndTime(getEndTimeForEpic(epic));
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
            subTaskHashMap.remove(subID);
        }
        if (inMemoryHistoryManager.getHistory().size() > 1) {
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
        if (epic.getStartTime().equals(subtask.getStartTime())) {
            subTaskHashMap.remove(id);
            epic.deleteSubtask(subtask.getId());
            try {
                epic.setStartTime(getStartTimeForEpic(epic));
                epic.setDuration(getDurationForEpic(epic));
                epic.setEndTime(getEndTimeForEpic(epic));
            } catch (DateTimeException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        } else {
            epic.setDuration(epic.getDuration().minus(subtask.getDuration()));
            epic.setEndTime(epic.getEndTime().minusMinutes(subtask.getDuration().toMinutes()));
            subTaskHashMap.remove(id);
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
