import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.DateTimeManager;
import kanban.service.FileBackedTaskManager;
import kanban.service.Manager;
import kanban.service.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        LocalDateTime forTask = LocalDateTime.of(2024, 06, 11, 12, 30);
        LocalDateTime forEpic = LocalDateTime.of(2024, 06, 11, 13, 30);
        LocalDateTime forSubtask = LocalDateTime.of(2024, 06, 11, 14, 30);
        Duration durationTask = Duration.ofDays(1);
        Duration durationSubtask = Duration.ofDays(2);

        Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
        testSubtask1.setStartTime(forSubtask);
        testSubtask1.setDuration(durationSubtask);
        Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
        testSubtask2.setStartTime(forEpic);
        testSubtask2.setDuration(durationSubtask);
        Subtask testSubtask3 = new Subtask("Получилось", "5 км");
        testSubtask3.setStartTime(forTask);
        testSubtask3.setDuration(durationSubtask);
        Subtask testSubtask4 = new Subtask("Молоко", "3.2%");
        testSubtask4.setStartTime(forSubtask);
        testSubtask4.setDuration(durationSubtask);

        DateTimeManager d = new DateTimeManager();
        Subtask s = d.getMinDateTime(List.of(testSubtask1, testSubtask2, testSubtask3, testSubtask4));
        System.out.println(s.getName());

//        File file = new File("storage.csv");
//        //Создаем менеджера.
//        TaskManager manager = Manager.getFileBackedManager(file);
//        //Работаем с задачами.
//        printAllTasks(manager);
//        //Сохраняем результаты работы.
//        manager = FileBackedTaskManager.loadFromFile(file);
//        //Печатаем в консоль сохраненные задачи.
//        System.out.println(manager.getAllTask());
//        System.out.println(manager.getAllEpic());
//        System.out.println(manager.getAllSubTask());
//        System.out.println(manager.getHistory());
    }

    private static void printAllTasks(TaskManager manager) {
        LocalDateTime forTask = LocalDateTime.of(2024, 06, 11, 12, 30);
        LocalDateTime forEpic = LocalDateTime.of(2024, 06, 11, 13, 30);
        LocalDateTime forSubtask = LocalDateTime.of(2024, 06, 11, 14, 30);
        Duration durationTask = Duration.ofDays(1);
        Duration durationSubtask = Duration.ofDays(2);

        Task testTask1 = new Task("Кот", "Покормить, напоить.");
        testTask1.setStartTime(forTask);
        testTask1.setDuration(durationTask);
        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
        testTask2.setStartTime(forTask);
        testTask2.setDuration(durationTask);
        Epic testEpic1 = new Epic("Зарядка", "...");
        Epic testEpic2 = new Epic("Покупки", "Список продуктов.");

        Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
        testSubtask1.setStartTime(forSubtask);
        testSubtask1.setDuration(durationSubtask);
        Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
        testSubtask2.setStartTime(forEpic);
        testSubtask2.setDuration(durationSubtask);
        Subtask testSubtask3 = new Subtask("Получилось", "5 км");
        testSubtask3.setStartTime(forTask);
        testSubtask3.setDuration(durationSubtask);
        Subtask testSubtask4 = new Subtask("Молоко", "3.2%");
        testSubtask4.setStartTime(forSubtask);
        testSubtask4.setDuration(durationSubtask);

        manager.createTask(testTask1);
        manager.createTask(testTask2);
        manager.createEpic(testEpic1);
        manager.createEpic(testEpic2);
        manager.createSubTask(testSubtask1, testEpic2);
        manager.createSubTask(testSubtask2, testEpic1);
        manager.createSubTask(testSubtask3, testEpic1);
        manager.createSubTask(testSubtask4, testEpic1);

        DateTimeManager d = new DateTimeManager();
        Subtask s = d.getMinDateTime(List.of(testSubtask1, testSubtask2, testSubtask3, testSubtask4));
        System.out.println(s.getName());

//        System.out.println("Задачи:");
//        for (Task task : manager.getAllTask()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Task epic : manager.getAllEpic()) {
//            System.out.println(epic);
//
//            for (Task task : manager.getAllSubTask(epic.getId())) {
//                System.out.println("--> " + task);
//            }
//        }
//        System.out.println("Подзадачи:");
//        for (Task subtask : manager.getAllSubTask()) {
//            System.out.println(subtask);
//        }
//
//        System.out.println("Делаем вызов задач - " +
//                testTask1.getName() + " и " + testTask2.getName() + " по 2 раза");
//
//        manager.getTask(testTask1.getId());
//        manager.getTask(testTask2.getId());
//        manager.getTask(testTask2.getId());
//        manager.getTask(testTask1.getId());
//
//        printHistory(manager);
//
//        manager.deleteTask(testTask1.getId());
//        System.out.println("Удалаяем задачу - " + testTask1.getName());
//
//        printHistory(manager);
//
//        manager.deleteEpic(testEpic1.getId());
//        System.out.println("Удаляем эпик - " + testEpic1.getName() + System.lineSeparator() +
//                "с подзадачами - " + testEpic1.getSubTasks());
//
//        printHistory(manager);
    }

    public static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }


}
