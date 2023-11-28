import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> mySubTasks;

    EpicTask(String name, String description) {
        super(name, description);
    }

    public ArrayList<SubTask> getSubTasks() {
        return mySubTasks;
    }

    public void setSubTask(ArrayList<SubTask> subTasks) {
        this.mySubTasks = new ArrayList<>();
        this.mySubTasks.addAll(subTasks);
    }

}
