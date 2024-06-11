package kanban.model;

import java.util.Objects;

public class Subtask extends Task implements Cloneable{
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
    public Subtask clone() throws CloneNotSupportedException {
        //Тут мы имеем только примитивные поля, а их, как я понял,
        //поверхностное клонирование замечательно клонирует.
        return (Subtask) super.clone();
    }

    @Override
    public String toString() {
        return String.format("SUBTASK,%s,%s,%s,%s,%s,%s,%s",
                id, name, status, description, getStrDateTime(), getStrDurationSecond(), epicID);
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
