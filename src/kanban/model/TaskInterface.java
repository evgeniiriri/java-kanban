package kanban.model;

public interface TaskInterface {
    boolean isView();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    void setId(int id);

    Status getStatus();

    void setStatus(Status status);

    boolean isEqualsStatus(Status status);

    int getId();
}
