package kanban.test;

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.FileBackedTaskManager;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private static final File file = new File("storage.csv");
    private static final File fileTask = new File("storage", "TASKstorage.csv");
    private static final File fileEpic = new File("storage", "EPICstorage.csv");
    private static final File fileSubtask = new File("storage", "SUBTASKstorage.csv");
    private static final File fileHistory = new File("storage", "HISTORYstorage.csv");
    private static FileBackedTaskManager fbtm;

    @BeforeEach
    public void beforeEach() {
        fbtm = FileBackedTaskManager.loadFromFile(file);
    }

    @AfterEach
    public void afterEach() {
        fileTask.deleteOnExit();
        fileEpic.deleteOnExit();
        fileSubtask.deleteOnExit();
        fileHistory.deleteOnExit();
    }

    @Test
    public void shouldCreateDirAndFilesForStorage() {
        Assertions.assertTrue(Files.isDirectory(Paths.get("storage")));
        Assertions.assertTrue(Files.exists(Paths.get("storage", "TASKstorage.csv")));
        Assertions.assertTrue(Files.exists(Paths.get("storage", "EPICstorage.csv")));
        Assertions.assertTrue(Files.exists(Paths.get("storage", "SUBTASKstorage.csv")));
        Assertions.assertTrue(Files.exists(Paths.get("storage", "HISTORYstorage.csv")));
    }

    @Test
    public void shouldWriteAndReadeTasksAndHistoryFromStorage() {
        Task testTask = new Task("Task test", "Task test test");
        Epic testEpic = new Epic("Epic test", "Epic test test");
        Subtask testSubtask = new Subtask("Subtask test", "Subtask test test");
        fbtm.createTask(testTask);
        fbtm.createEpic(testEpic);
        fbtm.createSubTask(testSubtask, testEpic);
        fbtm.getTask(0);
        fbtm.getEpic(1);
        try (
                BufferedReader bft = new BufferedReader(new FileReader(fileTask, StandardCharsets.UTF_8));
                BufferedReader bfe = new BufferedReader(new FileReader(fileEpic, StandardCharsets.UTF_8));
                BufferedReader bfs = new BufferedReader(new FileReader(fileSubtask, StandardCharsets.UTF_8));
                BufferedReader bfh = new BufferedReader(new FileReader(fileHistory, StandardCharsets.UTF_8));
        ) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = bft.readLine()) != null) {
                lines.add(line);
            }
            Assertions.assertEquals("TASK,0,Task test,NEW,Task test test", lines.get(1));
            lines.clear();
            while ((line = bfe.readLine()) != null) {
                lines.add(line);
            }
            Assertions.assertEquals("EPIC,1,Epic test,NEW,Epic test test,[2]", lines.get(1));
            lines.clear();
            while ((line = bfs.readLine()) != null) {
                lines.add(line);
            }
            Assertions.assertEquals("SUBTASK,2,Subtask test,NEW,Subtask test test,1", lines.get(1));
            lines.clear();
            while ((line = bfh.readLine()) != null) {
                lines.add(line);
            }
            assertEquals("0,1,", lines.get(0));

            fbtm = null;
            FileBackedTaskManager fbtmNew = FileBackedTaskManager.loadFromFile(file);

            Assertions.assertEquals(testTask.getName(), fbtmNew.getTask(0).getName());
            Assertions.assertEquals(testEpic.getName(), fbtmNew.getEpic(1).getName());
            Assertions.assertEquals(testSubtask.getName(), fbtmNew.getSubTask(2).getName());
            Assertions.assertEquals(fbtmNew.getHistory().get(0).getName(),"Task test");
            Assertions.assertEquals(fbtmNew.getHistory().get(1).getName(),"Epic test");

        }catch (IOException e) {
            System.out.println("Test ERROR.");
        }
    }


}