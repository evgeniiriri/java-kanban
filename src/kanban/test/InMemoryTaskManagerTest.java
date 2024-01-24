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

public class InMemoryTaskManagerTest {
    private static TaskManager inMemoryTaskManager;
    private static Epic epic;
    private static Subtask subtask;
    private static Task task;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("Epic test", "Epic test test");
        subtask = new Subtask("Subtask test", "Subtask test test");
        task = new Task("Task test", "Task test test");
    }

    @Test
    public void equalsTasksIdAndObjectTaskTest() {
        inMemoryTaskManager.createTask(task);
        int idTask = 1;

        Assertions.assertEquals(idTask, inMemoryTaskManager.getTask(idTask).getId());
        Assertions.assertEquals(task, inMemoryTaskManager.getTask(idTask));
    }

    @Test
    public void equalsHeirsTasksWithIdAndObjectTest() {
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
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        int epicId = inMemoryTaskManager.getEpic(epic.getId()).getId();
        int subtaskEpicId = inMemoryTaskManager.getSubTask(subtask.getId()).getMyEpicId();
        Assertions.assertEquals(epicId, subtaskEpicId);
    }

    @Test
    public void InitManagerTest() {
        TaskManager manager = Manager.getDefault();

        Assertions.assertEquals(0, manager.getAllTask().size());
        Assertions.assertEquals(0, manager.getAllEpic().size());
        Assertions.assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void CreateOperationTest() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        Assertions.assertFalse(inMemoryTaskManager.getAllTask().isEmpty());
        Assertions.assertFalse(inMemoryTaskManager.getAllEpic().isEmpty());
        Assertions.assertFalse(inMemoryTaskManager.getAllSubTask().isEmpty());
    }

    @Test
    public void UpdateOperationTest() {
        Task newTask = new Task("New task", "New description");
        Epic newEpic = new Epic("New task", "New description");
        Subtask newSubtask = new Subtask("New task", "New description");

        newTask.setId(task.getId());
        newEpic.setId(epic.getId());
        newSubtask.setId(subtask.getId());
        newSubtask.setMyEpic(newEpic.getId());
        newEpic.setSubTasks(newSubtask.getId());
        newSubtask.setStatus(Status.NEW);

        inMemoryTaskManager.updateTask(task.getId(), newTask);
        inMemoryTaskManager.updateEpic(epic.getId(), newEpic);
        inMemoryTaskManager.updateSubTask(subtask.getId(), newSubtask);

        Assertions.assertEquals("New task",
                inMemoryTaskManager.getTask(newTask.getId()).getName());
        Assertions.assertEquals("New task",
                inMemoryTaskManager.getEpic(newEpic.getId()).getName());
        Assertions.assertEquals("New task",
                inMemoryTaskManager.getSubTask(newSubtask.getId()).getName());
    }

    @Test
    public void DeleteOperationTest() {
        inMemoryTaskManager.deleteTask(task.getId());
        inMemoryTaskManager.deleteEpic(epic.getId());

        Assertions.assertTrue(inMemoryTaskManager.getAllTask().isEmpty());
        Assertions.assertTrue(inMemoryTaskManager.getAllEpic().isEmpty());
        Assertions.assertTrue(inMemoryTaskManager.getAllSubTask().isEmpty());
    }

    @Test
    public void shouldSaveHistoryTaskTest() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());

        Assertions.assertEquals(3, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldSaveTo10TaskTest() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);
        for (int i = 0; i < 12; i++) {
            inMemoryTaskManager.getTask(task.getId());
            inMemoryTaskManager.getEpic(epic.getId());
            inMemoryTaskManager.getSubTask(subtask.getId());
        }

        Assertions.assertEquals(10, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldShowRemoveTaskInHistoryTest() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subtask, epic);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getEpic(epic.getId());
        inMemoryTaskManager.getSubTask(subtask.getId());

        Assertions.assertEquals(inMemoryTaskManager.getTask(task.getId()), inMemoryTaskManager.getHistory().get(0));

        inMemoryTaskManager.deleteTask(task.getId());

        Assertions.assertNotEquals(inMemoryTaskManager.getTask(task.getId()), inMemoryTaskManager.getHistory().get(0));
        Assertions.assertEquals(task, inMemoryTaskManager.getHistory().get(0));
        Assertions.assertEquals(epic, inMemoryTaskManager.getHistory().get(1));
        Assertions.assertEquals(subtask, inMemoryTaskManager.getHistory().get(2));
    }

}