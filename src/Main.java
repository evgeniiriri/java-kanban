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
    }

}
