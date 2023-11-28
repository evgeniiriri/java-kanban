public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        Manager manager = new Manager();
        Task testTaskOne = new Task("Тестовая первая",
                "Тестовое описание первой задачи");
        Task testTaskTwo = new Task("Тестовая вторая",
                "Тестовое описание второй задачи");
        EpicTask testEpicOne = new EpicTask("Тестовый эпик первый",
                "Тестовый эпик с одной подзадачей");
        EpicTask testEpicTwo = new EpicTask("Тестовый эпик второй",
                "Тестовый эпик с двумя подзадачами");
        EpicTask testEpicThree = new EpicTask("Тестовый эпик три",
                "Тестовый эпик с наполовину выполненными подзадачами в кол-ве 3шт");
        SubTask testSubTask1 = new SubTask("Подзадача1",
                "Описание 1");
        SubTask testSubTask2 = new SubTask("Подзадача2",
                "Описание 2");
        SubTask testSubTask3 = new SubTask("Подзадача3",
                "Описание 3");
        SubTask testSubTask4 = new SubTask("Подзадача4",
                "Описание 4");
        SubTask testSubTask5 = new SubTask("Подзадача5",
                "Описание 5");
        SubTask testSubTask6 = new SubTask("Подзадача6",
                "Описание 6");
        SubTask testSubTask7 = new SubTask("Подзадача7",
                "Описание 7");
        SubTask testSubTask8 = new SubTask("Подзадача8",
                "Описание 8");

        manager.createTask(testTaskOne);
        System.out.println(manager.getAllTask());
    }
}
