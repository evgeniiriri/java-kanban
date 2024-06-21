import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        testInMemoryTaskManager();


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
    }

        public static void testInMemoryTaskManager() {
            LocalDateTime forSubtask = LocalDateTime.of(2024, 06, 11, 14, 30);

            LocalDateTime forNewSub = LocalDateTime.of(2024, 06, 11, 9, 00);
            Duration durationTask = Duration.ofHours(10);
            Task testTask1 = new Task("Кот", "Покормить, напоить.");
            testTask1.setDuration(durationTask);
            testTask1.setStartTime(forNewSub);

            Duration durationSubtask = Duration.ofHours(1);
            LocalDateTime forTask = LocalDateTime.of(2024, 06, 11, 10, 00);
            Task testTask2 = new Task("Попугай", "Покормить, напоить.");
            testTask2.setDuration(durationSubtask);
            testTask2.setStartTime(forTask);

            LocalDateTime forEpic = LocalDateTime.of(2024, 06, 11, 11, 30);
            Duration durationNeSub = Duration.ofHours(10);
            Task testTask3 = new Task("sssssssssss", "Покормить, напоить.");
            testTask3.setStartTime(forEpic);
            testTask3.setDuration(durationNeSub);

            Epic testEpic1 = new Epic("Зарядка", "...");
            Epic testEpic2 = new Epic("Покупки", "Список продуктов.");

            Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
            testSubtask1.setDuration(durationSubtask);
            testSubtask1.setStartTime(forSubtask);
            Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
            testSubtask2.setDuration(durationSubtask);
            testSubtask2.setStartTime(forTask);
            Subtask testSubtask3 = new Subtask("Получилось", "5 км");
            testSubtask3.setDuration(durationSubtask);
            testSubtask3.setStartTime(forSubtask);


//            TaskManager manager = Manager.getDefault();
            InMemoryTaskManager manager = new InMemoryTaskManager();
            manager.createTask(testTask1);
            manager.createTask(testTask2);
            manager.getPrioritizedTasks();
//            manager.createEpic(testEpic1);
//            manager.createSubTask(testSubtask1, testEpic1);
//            manager.createSubTask(testSubtask2, testEpic1);

            for (Task task : manager.getAllTask()) {
                System.out.println(task.getStartTime());
                System.out.println(task.getEndTime());
                System.out.println("---");
            }
            System.out.println(testTask3.getStartTime());
            System.out.println(testTask3.getDuration().toHours());
            System.out.println(testTask3.getEndTime());

            manager.add(testTask3);

        }


    }
