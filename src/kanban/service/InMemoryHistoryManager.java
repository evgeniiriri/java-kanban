package kanban.service;

import kanban.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> storage = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (storage.size() == 10 ) {
            storage.remove(0);
        }
        storage.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return storage;
    }
}
