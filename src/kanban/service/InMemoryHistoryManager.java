package kanban.service;

import kanban.model.Task;
import kanban.service.tasklist.LinkedListTasks;
import kanban.service.tasklist.Node;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {
    private static final Logger log = Logger.getLogger(InMemoryHistoryManager.class.getName());
    private final LinkedListTasks<T> linkedListTasks = new LinkedListTasks<>();
    private final HashMap<Integer, Node<T>> linkedTasksMap = new HashMap<>();
    private List<T> history;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Task task : linkedListTasks.getTasks()) {
            result.append(String.valueOf(task.getId())).append(",");
        }
        return result.toString();
    }

    public void add(T task) {
        //Тут мы должны ловить ошибку, которую выкидывают Task'и при неудачном клонировании.
        try {
            Task clon = task.clone();
            int id = clon.getId();
            //Проверяем наличие в истории нужной записи.
            if (linkedTasksMap.containsKey(id)) {
                //Удаляем и перезаписываем ноду в конец LinkedListTasks. Так же меняем объект в HashMap.
                remove(id);
            }
            linkedListTasks.linkLast((T) clon);
            linkedTasksMap.put(clon.getId(), linkedListTasks.getLastNode());
            history = List.copyOf(linkedListTasks.getTasks());
        } catch (CloneNotSupportedException e) {
            log.log(Level.SEVERE, "Не получилось сделать копию задачи - " + task + System.lineSeparator() + e.getMessage());
            //Будем логировать ошибку.
        }
    }

    @Override
    public List<T> getHistory() {
        return history;
    }

    @Override
    public void remove(int id) {
        //Получаем нужную ноду по индексу из HashMap и удаляем ноду.
        if (linkedTasksMap.containsKey(id)) {
            Node<T> n = linkedTasksMap.get(id);
            linkedListTasks.removeNode(n);
        }
        history = List.copyOf(linkedListTasks.getTasks());
    }
}
