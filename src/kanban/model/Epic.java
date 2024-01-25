package kanban.model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task implements Cloneable {
    private final ArrayList<Integer> idSubTask = new ArrayList<>();

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
        if (idSubTask.contains(id)) {
            idSubTask.remove(id);
        }
    }

    @Override
    public Epic clone() throws CloneNotSupportedException {
        return (Epic) super.clone();
    }

    @Override
    public String toString() {
        String subtaskSize = String.valueOf(idSubTask.size());
        return "Epic{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", size='" + subtaskSize + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return idSubTask.equals(epic.idSubTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTask);
    }
}
