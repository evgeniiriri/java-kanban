import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.Manager;
import kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager manager = Manager.getDefault();

        Task testTask1 = new Task("Кот", "Покормить, напоить.");
        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
        Epic testEpic1 = new Epic("Зарядка" , "...");
        Epic testEpic2 = new Epic("Покупки", "Список продуктов.");
        Subtask testSubtask1 = new Subtask("Анжумания", "От пола.");
        Subtask testSubtask2 = new Subtask("Прец качат", "100 раз");
        Subtask testSubtask3 = new Subtask("Бегит", "5 км");
        Subtask testSubtask4 = new Subtask("Молоко", "3.2%");

        manager.createTask(testTask1);
        manager.createTask(testTask2);
        manager.createEpic(testEpic1);
        manager.createEpic(testEpic2);
        manager.createSubTask(testSubtask1, testEpic1);
        manager.createSubTask(testSubtask2, testEpic1);
        manager.createSubTask(testSubtask3, testEpic1);
        manager.createSubTask(testSubtask4, testEpic2);

        manager.getEpic(4);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getEpic(4);

        System.out.println(manager.getHistory());

        System.out.println("Тест создания задач.");

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("Тест изменения статуса и обновления задач");
        testSubtask1.setStatus(Status.DONE);
        manager.updateSubTask(testSubtask1);


        Subtask testSubtask5 = new Subtask("Тест обновления епика.", "Описание.");
        manager.createSubTask(testSubtask5, testEpic2);

         System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("Тест удаления.");

        manager.deleteTask(testTask1.getId());
        manager.deleteAllSubTask();

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
    }

}
