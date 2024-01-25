import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;
import kanban.service.Manager;
import kanban.service.TaskManager;

public class Main {

    public static void main(String[] args){

        System.out.println("Поехали!");
        TaskManager manager = Manager.getDefault();
        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager){
        Task testTask1 = new Task("Кот", "Покормить, напоить.");
        Task testTask2 = new Task("Попугай", "Покормить, напоить.");
        Epic testEpic1 = new Epic("Зарядка", "...");
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
        manager.createSubTask(testSubtask2, testEpic2);
        manager.createSubTask(testSubtask3, testEpic2);
        manager.createSubTask(testSubtask4, testEpic2);

        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubTask(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}
