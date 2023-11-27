public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        Manager m = new Manager();
        EpicTask et = new EpicTask();
        m.createTask(et);
    }
}
