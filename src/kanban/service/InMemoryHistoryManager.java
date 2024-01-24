package kanban.service;

import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final List<T> storage = new ArrayList<>();

    public void add(T task) throws CloneNotSupportedException {
        if (storage.size() == 10) {
            storage.remove(0);
        }
        Task clon = task.clone();
        storage.add((T) clon);
    }


    @Override
    public List<T> getHistory() {
        return List.copyOf(storage);
    }
}
