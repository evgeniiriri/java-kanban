package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task implements Cloneable {
    private ArrayList<Integer> idSubTask = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Epic clone() throws CloneNotSupportedException {
        Epic cloneEpic = (Epic) super.clone();
        cloneEpic.idSubTask = (ArrayList<Integer>) idSubTask.clone();
        //Добавил клонирование списка подзадач, но я не понял, как клонировать status.
        //Но я думаю это и не нужно, так как статус либо высчитывается программой или
        //устанавливается при создании Task.
        //Теперь список подзадач защищен от вмешательства 8)
        return cloneEpic;
    }

    @Override
    public String toString() {
        return String.format("EPIC,%s,%s,%s,%s,%s", id, name, status, description, idSubTask);
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
