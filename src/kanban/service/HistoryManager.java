package kanban.service;

import java.util.List;

public interface HistoryManager<T> {

    void add(T task) throws CloneNotSupportedException;

    List<T> getHistory();
}
