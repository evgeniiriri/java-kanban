package kanban.service;

import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final List<T> storage = new ArrayList<>();

    public void add(T task) {
        if (storage.size() == 10) {
            storage.remove(0);
        }
        //Тут мы должны ловить ошибку, которую выкидывают Task'и при неудачном клонировании.
        Task clon;
        try {
            clon = task.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(); //И складывать программу, если что-то не то.
                                        //Наверно это не лучший способ 8)
                                        //Пока с исключениями знаком отдаленно.
        }
        storage.add((T) clon);
    }

    @Override
    public List<T> getHistory() {
        return List.copyOf(storage);
    }

    public void remove(int id) {
        //in works.
    }
}
