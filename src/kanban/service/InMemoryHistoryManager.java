package kanban.service;

import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final List<T> storage = new ArrayList<>();


//    @Override
//    public void add(T task) {
//        if (storage.size() == 10) {
//            storage.remove(0);
//        }
//        storage.add(task);
//    }

    public void add(T task) {
        if (storage.size() == 10) {
            storage.remove(0);
        }
        
        storage.add((T) task);
    }


    @Override
    public List<T> getHistory() {
        return List.copyOf(storage);
    }
}
