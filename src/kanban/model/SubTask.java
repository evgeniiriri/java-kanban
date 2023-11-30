package kanban.model;

public class SubTask extends Task {
    private int idMyEpic;
    private Status status;

    public SubTask() {
        super();
    }

    public int getMyEpic() {
        return this.idMyEpic;
    }

    public void setMyEpic(int idMyEpic) {
        this.idMyEpic = idMyEpic;
    }
}
