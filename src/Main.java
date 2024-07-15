import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.*;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("15.07.2024", "1hours duration");
        task1.setStartTime(LocalDateTime.of(2024, 7, 15,10, 00));
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("15.07.2024", "2hours duration");
        task2.setStartTime(LocalDateTime.of(2024, 7, 15,11, 00));
        task2.setDuration(Duration.ofMinutes(120));

        Task task3 = new Task("15.07.2024", "3hours duration");
        task3.setStartTime(LocalDateTime.of(2024, 7, 15,12, 00));
        task3.setDuration(Duration.ofMinutes(180));

        Subtask subtask1 = new Subtask("15.08.2024", "1hours duration");
        subtask1.setStartTime(LocalDateTime.of(2024, 6, 15,10, 00));
        subtask1.setDuration(Duration.ofMinutes(60));

        Subtask subtask2 = new Subtask("15.08.2024", "2hours duration");
        subtask2.setStartTime(LocalDateTime.of(2024, 8, 15,10, 00));
        subtask2.setDuration(Duration.ofMinutes(120));

        Epic epic = new Epic("Epic", "Epic epic");

        File file = new File("storage.csv");

        FileBackedTaskManager fileBackedTaskManager = Manager.getFileBackedManager(file);
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createTask(task3);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subtask1, epic);
        fileBackedTaskManager.createSubTask(subtask2, epic);

        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpic());
        System.out.println(fileBackedTaskManager.getEpic(epic.getId()).getEndTime());
        System.out.println(fileBackedTaskManager.getEpic(epic.getId()).getDuration().toMinutes());
    }

}
