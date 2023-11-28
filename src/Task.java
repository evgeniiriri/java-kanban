import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected String status;

    Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }


    public int getId() {
        return id;
    }
}
