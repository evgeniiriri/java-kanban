package kanban.model;

public class SubTask extends Task {
    private int idMyEpic;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public int getMyEpic() {
        return this.idMyEpic;
    }

    public void setMyEpic(int idMyEpic) {
        this.idMyEpic = idMyEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idMyEpic=" + idMyEpic +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
