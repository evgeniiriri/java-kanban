import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.Manager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        Manager manager = new Manager();

        Task testTask1 = new Task();
        Task testTask2 = new Task();
        Epic testEpic1 = new Epic();
        SubTask testSubTask1 = new SubTask();
        SubTask testSubTask2 = new SubTask();
        Epic testEpic2 = new Epic();
        SubTask testSubTask3 = new SubTask();

        manager.createTask(testTask1);
        manager.createTask(testTask2);

        manager.createEpic(testEpic1);
        manager.createEpic(testEpic2);

        manager.createSubTask(testSubTask1, testEpic1);
        manager.createSubTask(testSubTask2, testEpic2);
        manager.createSubTask(testSubTask3, testEpic2);



    }

}
