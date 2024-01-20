package kanban.model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public int getMyEpicId() {
        return this.epicID;
    }

    public void setMyEpic(int idMyEpic) {
        this.epicID = idMyEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idMyEpic=" + epicID +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }
}
