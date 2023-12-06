package kanban.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTasks() {
        return this.idSubTask;
    }

    public void setSubTasks(ArrayList<Integer> idSubTask) {
        this.idSubTask.addAll(idSubTask);
    }

    public void setSubTasks(int idSubTask) {
        this.idSubTask.add(idSubTask);
    }

    public void deleteSubtaskID(int id) {
        if (idSubTask.contains(id)){
            idSubTask.remove(id);
        }
    }

    @Override
    public String toString() {
        String subTaskString = (idSubTask == null) ? "null" : String.valueOf(idSubTask.size());
        return "Epic{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", subTaks.size()='" + subTaskString + '}';
    }
}
