import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

    HttpTaskServer httpTaskServer = new HttpTaskServer();
    httpTaskServer.start();



//
//        Task task1 = new Task("15.07.2024", "1hours duration");
//        task1.setStartTime(LocalDateTime.of(2024, 7, 11, 10, 00));
//        task1.setDuration(Duration.ofMinutes(60));
//
//        Task task2 = new Task("15.07.2024", "2hours duration");
//        task2.setStartTime(LocalDateTime.of(2024, 7, 11, 11, 30));
//        task2.setDuration(Duration.ofMinutes(30));
//
//        Task task3 = new Task("15.07.2024", "3hours duration");
//        task3.setStartTime(LocalDateTime.of(2024, 7, 11, 11, 35));
//        task3.setDuration(Duration.ofMinutes(10));
//
//        Subtask subtask1 = new Subtask("15.08.2024", "1hours duration");
//        subtask1.setStartTime(LocalDateTime.of(2024, 6, 15, 11, 00));
//        subtask1.setDuration(Duration.ofMinutes(60));
//
//        Subtask subtask2 = new Subtask("15.08.2024", "2hours duration");
//        subtask2.setStartTime(LocalDateTime.of(2024, 8, 15, 12, 00));
//        subtask2.setDuration(Duration.ofMinutes(120));
//
//        Epic epic = new Epic("Epic", "Epic epic");
//
//        File file = new File("storage.csv");
//
//        TaskManager fileBackedTaskManager = Manager.getFileBackedManager(file);
//
//        fileBackedTaskManager.createTask(task1);
//        fileBackedTaskManager.createTask(task2);
//        fileBackedTaskManager.createTask(task3);
//        fileBackedTaskManager.createEpic(epic);
//        fileBackedTaskManager.createSubTask(subtask1, epic);
//        fileBackedTaskManager.createSubTask(subtask2, epic);
//
//        printMenu(fileBackedTaskManager);
//        System.out.println("Удаление задачи");
//        fileBackedTaskManager.deleteSubTask(subtask1.getId());
//
//        printMenu(fileBackedTaskManager);
    }

//    public static void printMenu(TaskManager fileBackedTaskManager) {

//        System.out.println("Задачи " + System.lineSeparator());
//        for (Task task : fileBackedTaskManager.getAllTask()) {
//            System.out.println(task + System.lineSeparator());
//        }
//        System.out.println("Эпики и подзадачи "  + System.lineSeparator());
//        for (Epic epic : fileBackedTaskManager.getAllEpic()) {
//            System.out.println(epic  + System.lineSeparator());
//            for (Subtask subtask : fileBackedTaskManager.getSubTasksWithEpic(epic)) {
//                System.out.println(subtask  + System.lineSeparator());
//            }
//        }
//        System.out.println("Приоритет задач "  + System.lineSeparator());
//        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
//        System.out.println("История" + System.lineSeparator() + fileBackedTaskManager.getHistory());

//    }

}
