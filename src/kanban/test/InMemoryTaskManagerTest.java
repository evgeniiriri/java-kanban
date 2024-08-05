package kanban.test;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.InMemoryTaskManager;
import kanban.service.Manager;
import kanban.service.TaskManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest {
    private static TaskManager inMemoryTaskManager;
    private static Epic epic;
    private static Subtask subtask;
    private static Task task;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Epic test", "Epic test test");
        epic.setStartTime(LocalDateTime.of(2024, 7, 11, 12, 30));
        epic.setDuration(Duration.ofMinutes(30));

        subtask = new Subtask("Subtask test", "Subtask test test");
        subtask.setStartTime(LocalDateTime.of(2024, 7, 11, 13, 30));
        subtask.setDuration(Duration.ofMinutes(30));
        subtask.setStatus(Status.NEW);

        task = new Task("Task test", "Task test test");
        task.setStartTime(LocalDateTime.of(2024, 7, 1, 11, 30));
        task.setDuration(Duration.ofMinutes(30));
        task.setStatus(Status.NEW);

    }

    @Test
    public void equalsTasksIdAndObjectTaskTest() {
        //Сохраняется ли та задача, с ожидаемым ли id.
        inMemoryTaskManager.createTask(task);
        int idTask = 1;

        Assertions.assertEquals(idTask, inMemoryTaskManager.getTask(idTask).getId());
        Assertions.assertEquals(task, inMemoryTaskManager.getTask(idTask));
    }

    @Test
    public void equalsHeirsTasksWithIdAndObjectTest() {
        //Сохраняются ли эпики и подзадачи, с ожидаемым ли id.
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        int idEpic = 1;
        int idSubtask = 2;

        Assertions.assertEquals(idEpic, inMemoryTaskManager.getEpic(idEpic).getId());
        Assertions.assertEquals(epic, inMemoryTaskManager.getEpic(idEpic));

        Assertions.assertEquals(idSubtask, inMemoryTaskManager.getSubTask(idSubtask).getId());
        Assertions.assertEquals(subtask, inMemoryTaskManager.getSubTask(idSubtask));
    }

    @Test
    public void attachmentSubtaskToEpicTest() {
        //Корректна ли привязка подзадачи к эпику.
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        int epicId = inMemoryTaskManager.getEpic(epic.getId()).getId();
        int subtaskEpicId = inMemoryTaskManager.getSubTask(subtask.getId()).getMyEpicId();
        Assertions.assertEquals(epicId, subtaskEpicId);
    }

    @Test
    public void InitManagerTest() {
        //Тест инициализации менеджера задач.
        TaskManager manager = Manager.getDefault();

        Assertions.assertEquals(0, manager.getAllTask().size());
        Assertions.assertEquals(0, manager.getAllEpic().size());
        Assertions.assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void CreateOperationTest() {
        //Тест создания задач, эпиков и подзадач.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        Assertions.assertFalse(inMemoryTaskManager.getAllTask().isEmpty());
        Assertions.assertFalse(inMemoryTaskManager.getAllEpic().isEmpty());
        Assertions.assertFalse(inMemoryTaskManager.getAllSubTask().isEmpty());
    }

    @Test
    public void UpdateOperationTest() {
        //Тест обновления задач, эпиков и подзадач.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        Task newTask = new Task("New task", "New description");
        newTask.setStartTime(LocalDateTime.of(2024, 7, 11, 11, 30));
        newTask.setDuration(Duration.ofMinutes(30));
        newTask.setId(task.getId());
        newTask.setStatus(Status.NEW);

        Epic newEpic = new Epic("New task", "New description");
        newEpic.setStartTime(LocalDateTime.of(2024, 7, 11, 12, 30));
        newEpic.setDuration(Duration.ofMinutes(30));
        newEpic.setId(epic.getId());
        newEpic.setSubTasks(epic.getSubTasks());

        Subtask newSubtask = new Subtask("New task", "New description");
        newSubtask.setStartTime(LocalDateTime.of(2024, 7, 11, 13, 30));
        newSubtask.setDuration(Duration.ofMinutes(30));
        newSubtask.setId(subtask.getId());
        newSubtask.setMyEpic(newEpic.getId());
        newSubtask.setStatus(Status.NEW);

        inMemoryTaskManager.updateTask(newTask);
        inMemoryTaskManager.updateEpic(newEpic);
        inMemoryTaskManager.updateSubTask(newSubtask);

        Assertions.assertEquals("New task",
                inMemoryTaskManager.getTask(newTask.getId()).getName());
        Assertions.assertEquals("New task",
                inMemoryTaskManager.getEpic(newEpic.getId()).getName());
        Assertions.assertEquals("New task",
                inMemoryTaskManager.getSubTask(newSubtask.getId()).getName());
    }

    @Test
    public void DeleteOperationTest() {
        //Тест удаления, задач, эпиков и подзадач, к этому эпику привязанных.
        inMemoryTaskManager.deleteTask(task.getId());
        inMemoryTaskManager.deleteEpic(epic.getId());

        Assertions.assertTrue(inMemoryTaskManager.getAllTask().isEmpty());
        Assertions.assertTrue(inMemoryTaskManager.getAllEpic().isEmpty());
        Assertions.assertTrue(inMemoryTaskManager.getAllSubTask().isEmpty());
    }

    @Test
    public void calculateStatusForEpic() {
        //Тест расчета статуса для эпиков.
        Subtask subtask1 = new Subtask("Subtask test", "Subtask test test");
        subtask1.setStartTime(LocalDateTime.of(2024, 7, 11, 13, 30));
        subtask1.setDuration(Duration.ofMinutes(30));
        subtask1.setStatus(Status.NEW);

        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);
        inMemoryTaskManager.createSubTask(subtask1, epic);

        Assertions.assertEquals(Status.NEW, inMemoryTaskManager.getEpic(epic.getId()).getStatus());

        Subtask subtask2 = new Subtask("Subtask test", "Subtask test test");
        subtask2.setStartTime(LocalDateTime.of(2024, 7, 11, 13, 30));
        subtask2.setDuration(Duration.ofMinutes(30));
        subtask2.setId(subtask1.getId());
        subtask2.setMyEpic(subtask1.getMyEpicId());
        subtask2.setStatus(Status.IN_PROGRESS);

        inMemoryTaskManager.updateSubTask(subtask2);

        Assertions.assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void shouldSaveHistoryTaskTest() {
        //Тест сохранения задач в историю.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        Assertions.assertNull(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());

        Assertions.assertEquals(3, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldIndependenceTasksAndHistory() {
        //Тест на корректное сохранение задач в истории с их обновление.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());

        String descriptionBefore = inMemoryTaskManager.getTask(task.getId()).getDescription();

        Assertions.assertEquals("Task test test", descriptionBefore);

        inMemoryTaskManager.getHistory().get(0).setDescription("History description");
        String descriptionHistory = inMemoryTaskManager.getHistory().get(0).getDescription();

        Assertions.assertEquals("History description", descriptionHistory);
        Assertions.assertNotEquals(inMemoryTaskManager.getTask(task.getId()).getDescription(),
                inMemoryTaskManager.getHistory().get(0).getDescription());
        Assertions.assertNotEquals(task, inMemoryTaskManager.getHistory().get(0));
    }

    @Test
    public void shouldRewriteTaskInHistory() {
        //Тест на отсутствие дубликатов в истории.
        inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());

        int sizeHistory = inMemoryTaskManager.getHistory().size();

        Assertions.assertEquals(1, sizeHistory);
    }

    @Test
    public void shouldSaveQueueHistory() {
        //Тест на сохранение очереди добавления записей в историю.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());

        Assertions.assertEquals(subtask, inMemoryTaskManager.getHistory().get(2));
        Assertions.assertEquals(epic, inMemoryTaskManager.getHistory().get(1));
        Assertions.assertEquals(task, inMemoryTaskManager.getHistory().get(0));
    }

    @Test
    public void shouldReturnEmptyHistoryAfterRemoveTask() {
        //Тест на удаление задач и удаление их из истории.
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());
        Assertions.assertEquals(3, inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.deleteTask(task.getId());
        inMemoryTaskManager.deleteEpic(epic.getId());
        Assertions.assertEquals(0, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldDontSaveCrossTimePrioritizedTask() {
        //Тест на недобавление задачи в список приоритетных задач с пересечением по времени.
        Task taskCross = new Task("Task crossing", "Task test test");
        taskCross.setStartTime(LocalDateTime.of(2024, 7, 1, 10, 00));
        taskCross.setDuration(Duration.ofMinutes(61));
        taskCross.setStatus(Status.NEW);
        Task taskUnCross = new Task("Task test", "Task test test");
        taskUnCross.setStartTime(LocalDateTime.of(2024, 7, 1, 11, 00));
        taskUnCross.setDuration(Duration.ofMinutes(30));
        taskUnCross.setStatus(Status.NEW);

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(taskCross);
        inMemoryTaskManager.createTask(taskUnCross);

        Assertions.assertEquals(2, inMemoryTaskManager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldReturnSortedTasks() {
        //Тест на сортиорованный список приоритетных задач.
        Task task1 = new Task("Task1", "Task test test");
        task1.setStartTime(LocalDateTime.of(2024, 7, 1, 10, 00));
        task1.setDuration(Duration.ofMinutes(30));
        task1.setStatus(Status.NEW);
        Task task2 = new Task("Task2", "Task test test");
        task2.setStartTime(LocalDateTime.of(2024, 7, 1, 11, 00));
        task2.setDuration(Duration.ofMinutes(30));
        task2.setStatus(Status.NEW);
        Task task3 = new Task("Task3", "Task test test");
        task3.setStartTime(LocalDateTime.of(2024, 7, 1, 12, 00));
        task3.setDuration(Duration.ofMinutes(30));
        task3.setStatus(Status.NEW);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);

        Assertions.assertEquals(task1, inMemoryTaskManager.getPrioritizedTasks().first());
        Assertions.assertEquals(task3, inMemoryTaskManager.getPrioritizedTasks().last());
    }

    @Test
    public void endTimeTest() {
        //Тест на корректный расчет конца задачи.
        Task task1 = new Task("Task1", "Task test test");
        task1.setStartTime(LocalDateTime.of(2024, 7, 1, 10, 00));
        task1.setDuration(Duration.ofMinutes(30));
        task1.setStatus(Status.NEW);

        inMemoryTaskManager.createTask(task1);

        LocalDateTime end = LocalDateTime.of(2024, 7, 1, 10, 30);

        Assertions.assertEquals(end, inMemoryTaskManager.getTask(1).getEndTime());

        Subtask newSubtask = new Subtask("New task", "New description");
        newSubtask.setStartTime(LocalDateTime.of(2024, 7, 11, 14, 00));
        newSubtask.setDuration(Duration.ofMinutes(30));

        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);
        inMemoryTaskManager.createSubTask(newSubtask, epic);

        Assertions.assertEquals(Duration.ofMinutes(60), inMemoryTaskManager.getEpic(epic.getId()).getDuration());
    }

}