package kanban.test;

import kanban.model.Epic;
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

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void equalsTasksIdAndObjectTaskTest() {
        Task task = new Task("test", "test test");
        inMemoryTaskManager.createTask(task);
        int idTask = 1;

        Assertions.assertEquals(idTask, inMemoryTaskManager.getTask(idTask).getId());
        Assertions.assertEquals(task, inMemoryTaskManager.getTask(idTask));
    }

    @Test
    public void equalsHeirsTasksWithIdAndObjectTest() {
        Epic epic = new Epic("Epic test", "Epic Test test");
        Subtask subtask = new Subtask("Subtask test", "Subtask test test");
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
        Epic epic = new Epic("Epic test", "Epic Test test");
        Subtask subtask = new Subtask("Subtask test", "Subtask test test");
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






}