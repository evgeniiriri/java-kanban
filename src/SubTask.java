public class SubTask extends Task {
    private EpicTask myEpic;

    SubTask(String name, String description) {
        super(name, description);
    }

    public void setEpic(EpicTask epicTask) {
        this.myEpic = epicTask;
    }
}
