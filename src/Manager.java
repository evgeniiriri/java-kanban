import java.util.*;

public class Manager {
    private HashMap<Integer, Task> taskHashMap;
    private HashMap<Integer, EpicTask> epicTaskHashMap;
    private HashMap<Integer, SubTask> subTaskHashMap;
    private int id;

    Manager() {
        this.taskHashMap = new HashMap<>();
        this.epicTaskHashMap = new HashMap<>();
        this.subTaskHashMap = new HashMap<>();
        this.id = 0;
    }

    public ArrayList<Task> getAllTask() {
        return Collection<Task>;
    }

    public ArrayList<EpicTask> getAllEpicTask() {
        return (ArrayList<EpicTask>) epicTaskHashMap.values();
    }

    public ArrayList<SubTask> getAllSubTask() {
        return (ArrayList<SubTask>) subTaskHashMap.values();
    }

    public void removeAllTask() {
        taskHashMap.clear();
        epicTaskHashMap.clear();
        subTaskHashMap.clear();
        id = 0;
    }

    public Task getTask(int id) {
        return taskHashMap.get(id);
    }

    public EpicTask getEpicTask(int id) {
        return epicTaskHashMap.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTaskHashMap.get(id);
    }

    public void createTask(Task task) {
        this.taskHashMap.put(id, task);
        this.id++;
    }

    public void createEpicTask(EpicTask epicTask) {
        epicTaskHashMap.put(id, epicTask);
        id++;
    }

    public void createSubTask(SubTask subTask) {
        subTaskHashMap.put(id, subTask);
        id++;
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpicTask(EpicTask epicTask) {
        epicTask.status = checkStatusEpic(epicTask);
        epicTaskHashMap.put(epicTask.getId(), epicTask);
    }

    private String checkStatusEpic (EpicTask epicTask) {
        if (epicTask.getSubTasks().isEmpty()) {
            return "NEW";
        }

        ArrayList<String> statusList = new ArrayList<>();
        for (SubTask subTask : epicTask.getSubTasks()) {
            statusList.add(subTask.status);
        }

        Set<String> s = new HashSet<>(statusList);
        if (s.equals("NEW")) {
            return "NEW";
        } else if (s.equals("DONE")) {
            return "DONE";
        }
        return "IN_PROGRESS";
    }

    public void updateSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
    }

    public void delTaskWithID(int id) {
        taskHashMap.remove(id);
    }

    public void delEpicTaskWithID(int id) {
        epicTaskHashMap.remove(id);
    }

    public void delSubTaskWithID(int id) {
        subTaskHashMap.remove(id);
    }

    public ArrayList<SubTask> getAllSubTaskFromEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }


}
