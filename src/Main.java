import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.*;
import kanban.service.taskexception.TaskManagerBaseException;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("15.07.2024", "1hours duration");
        task1.setStartTime(LocalDateTime.of(2024, 7, 11, 10, 00));
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("15.07.2024", "2hours duration");
        task2.setStartTime(LocalDateTime.of(2024, 7, 11, 11, 30));
        task2.setDuration(Duration.ofMinutes(30));

        Task task3 = new Task("15.07.2024", "3hours duration");
        task3.setStartTime(LocalDateTime.of(2024, 7, 11, 11, 35));
        task3.setDuration(Duration.ofMinutes(10));

        Subtask subtask1 = new Subtask("15.08.2024", "1hours duration");
        subtask1.setStartTime(LocalDateTime.of(2024, 6, 15, 11, 00));
        subtask1.setDuration(Duration.ofMinutes(60));

        Subtask subtask2 = new Subtask("15.08.2024", "2hours duration");
        subtask2.setStartTime(LocalDateTime.of(2024, 8, 15, 12, 00));
        subtask2.setDuration(Duration.ofMinutes(120));

        Epic epic = new Epic("Epic", "Epic epic");

        File file = new File("storage.csv");

        InMemoryTaskManager fileBackedTaskManager = new InMemoryTaskManager();

        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);
        fileBackedTaskManager.createTask(task3);
        try {
            System.out.println(fileBackedTaskManager.getPrioritizedTasks());
        } catch (TaskManagerBaseException e) {
            System.out.println("sss");
        }
    }

}
