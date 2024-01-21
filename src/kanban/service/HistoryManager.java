package kanban.service;

import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.model.Task;

import javax.security.auth.Subject;
import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    ArrayList<Task> getHistory();

}
