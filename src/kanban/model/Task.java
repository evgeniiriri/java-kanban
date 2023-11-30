package kanban.model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    private Status status;

    public Task() {
        this.id = 0;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }
}
