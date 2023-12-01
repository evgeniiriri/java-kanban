import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.Manager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        Manager manager = new Manager();

        Task testTask1 = new Task("Кот", "Покормить, напоить.");
        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
        Epic testEpic1 = new Epic("Зарядка" , "...");
        Epic testEpic2 = new Epic("Покупки", "Список продуктов.");
        SubTask testSubTask1 = new SubTask("Анжумания", "От пола.");
        SubTask testSubTask2 = new SubTask("Прец качат", "100 раз");
        SubTask testSubTask3 = new SubTask("Бегит", "5 км");
        SubTask testSubTask4 = new SubTask("Молоко", "3.2%");

        manager.createTask(testTask1);
        manager.createTask(testTask2);
        manager.createEpic(testEpic1);
        manager.createEpic(testEpic2);
        manager.createSubTask(testSubTask1, testEpic1);
        manager.createSubTask(testSubTask2, testEpic1);
        manager.createSubTask(testSubTask3, testEpic1);
        manager.createSubTask(testSubTask4, testEpic2);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        testSubTask2.setStatus(Status.DONE);
        testTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(testSubTask2);
        manager.updateTask(testTask1);

        SubTask testSubTask5  = new SubTask("Тест обновления епика.", "Описание.");
        manager.createSubTask(testSubTask5, testEpic2);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        manager.delTask(testTask1.getId());
        manager.delEpic(testEpic1.getId());

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());


    }

}
