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

        File file = new File("storage.csv");
        FileBackedTaskManager fbtm = FileBackedTaskManager.loadFromFile(file);
//        FileBackedTaskManager fbtm = Manager.getFileBackedManager(file);
//        LocalDateTime forTask = LocalDateTime.of(2024, 06, 11, 12, 30);
//        LocalDateTime forNewSub = LocalDateTime.of(2024, 06, 11, 9, 30);
//        LocalDateTime forEpic = LocalDateTime.of(2024, 06, 11, 13, 30);
//        LocalDateTime forSubtask = LocalDateTime.of(2024, 06, 11, 14, 30);
//        Duration durationTask = Duration.ofDays(1);
//        Duration durationNeSub = Duration.ofDays(12);
//        Duration durationSubtask = Duration.ofDays(2);
//
//        Task testTask1 = new Task("Кот", "Покормить, напоить.");
//        testTask1.setDuration(durationTask);
//        testTask1.setStartTime(forTask);
//        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
//        testTask2.setDuration(durationTask);
//        testTask2.setStartTime(forTask);
//
//        Epic testEpic1 = new Epic("Зарядка", "...");
//        Epic testEpic2 = new Epic("Покупки", "Список продуктов.");
//
//        Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
//        testSubtask1.setDuration(durationSubtask);
//        testSubtask1.setStartTime(forSubtask);
//        Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
//        testSubtask2.setDuration(durationSubtask);
//        testSubtask2.setStartTime(forTask);
//        Subtask testSubtask3 = new Subtask("Получилось", "5 км");
//        testSubtask3.setDuration(durationSubtask);
//        testSubtask3.setStartTime(forSubtask);
//
//        fbtm.createTask(testTask1);
//        fbtm.createTask(testTask2);
//        fbtm.createEpic(testEpic1);
//        fbtm.createEpic(testEpic2);
//        fbtm.createSubTask(testSubtask1, testEpic1);
//        fbtm.createSubTask(testSubtask2, testEpic1);
//        fbtm.createSubTask(testSubtask3, testEpic1);
        System.out.println(fbtm.getAllTask());
//    }
//
//    public static void testInMemoryTaskManager() {
//        LocalDateTime forTask = LocalDateTime.of(2024, 06, 11, 12, 30);
//        LocalDateTime forNewSub = LocalDateTime.of(2024, 06, 11, 9, 30);
//        LocalDateTime forEpic = LocalDateTime.of(2024, 06, 11, 13, 30);
//        LocalDateTime forSubtask = LocalDateTime.of(2024, 06, 11, 14, 30);
//        Duration durationTask = Duration.ofDays(1);
//        Duration durationNeSub = Duration.ofDays(12);
//        Duration durationSubtask = Duration.ofDays(2);
//
//        Task testTask1 = new Task("Кот", "Покормить, напоить.");
//        testTask1.setDuration(durationTask);
//        testTask1.setStartTime(forTask);
//        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
//        testTask2.setDuration(durationTask);
//        testTask2.setStartTime(forTask);
//
//        Epic testEpic1 = new Epic("Зарядка", "...");
//        Epic testEpic2 = new Epic("Покупки", "Список продуктов.");
//
//        Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
//        testSubtask1.setDuration(durationSubtask);
//        testSubtask1.setStartTime(forSubtask);
//        Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
//        testSubtask2.setDuration(durationSubtask);
//        testSubtask2.setStartTime(forTask);
//        Subtask testSubtask3 = new Subtask("Получилось", "5 км");
//        testSubtask3.setDuration(durationSubtask);
//        testSubtask3.setStartTime(forSubtask);
//
//
//        TaskManager manager = Manager.getDefault();
//
//        manager.createEpic(testEpic1);
//        manager.createSubTask(testSubtask1, testEpic1);
//        manager.createSubTask(testSubtask2, testEpic1);
//        System.out.println(manager.getEpic(testEpic1.getId()).getStartTime());
//        System.out.println(manager.getEpic(testEpic1.getId()).getDuration().toMinutes());
//        System.out.println(manager.getEpic(testEpic1.getId()).getEndTime());
//
//        manager.getEpic(testEpic1.getId());
//        manager.getEpic(testSubtask1.getId());
//        manager.getEpic(testEpic1.getId());
//
//        Subtask testSubtask4 = new Subtask("Молоко", "3.2%");
//        testSubtask4.setDuration(durationNeSub);
//        testSubtask4.setStartTime(forNewSub);
//        testSubtask4.setId(testSubtask1.getId());
//        testSubtask4.setStatus(testSubtask1.getStatus());
//        testSubtask4.setMyEpic(testSubtask1.getMyEpicId());
//
//        manager.updateSubTask(testSubtask1.getId(), testSubtask4);
//
//        System.out.println(manager.getEpic(testEpic1.getId()).getStartTime());
//        System.out.println(manager.getEpic(testEpic1.getId()).getDuration().toMinutes());
//        System.out.println(manager.getEpic(testEpic1.getId()).getEndTime());
//
//        System.out.println(manager.getHistory());

    }


}
