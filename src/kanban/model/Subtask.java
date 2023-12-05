package kanban.model;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public int getMyEpic() {
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
}
