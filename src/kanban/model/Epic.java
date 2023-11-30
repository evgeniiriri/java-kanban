package kanban.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTask = new ArrayList<>();
    private Status status;

    public Epic() {
        super();
    }

    public ArrayList<Integer> getSubTasks() {
        return this.idSubTask;
    }

    public void setSubTasks(ArrayList<Integer> idSubTask) {
        this.idSubTask.addAll(idSubTask);
    }

    public void prrrr() {
        System.out.println(status);;
    }

    public void setSubTasks(int idSubTask) {
        this.idSubTask.add(idSubTask);
    }
}
