package kanban.model;

public class Task {
    protected Status status;
    protected String name;
    protected int id;
    protected String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {this.id = id;}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isEqualsStatus(Status status) {
        return this.status.equals(status);
    }

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
