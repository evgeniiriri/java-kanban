package kanban.model;

import java.util.Objects;

public class Task implements TaskInterface, Cloneable {
    protected boolean view;
    protected Status status;
    protected String name;
    protected int id;
    protected String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.view = false;
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && status == task.status && name.equals(task.name) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, name, id, description);
    }

    @Override
    public boolean isView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isEqualsStatus(Status status) {
        return this.status.equals(status);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
