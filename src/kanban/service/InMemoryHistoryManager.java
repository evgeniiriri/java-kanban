package kanban.service;

import kanban.model.Task;
import kanban.service.tasklist.LinkedListTasks;
import kanban.service.tasklist.Node;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final LinkedListTasks<T> linkedListTasks = new LinkedListTasks<>();
    private final HashMap<Integer, Node<T>> linkedTasksMap = new HashMap<>();

    @Override
    public String toString() {
        return super.toString();
    }

    public void add(T task) {
        //Тут мы должны ловить ошибку, которую выкидывают Task'и при неудачном клонировании.
        Task clon;
        try {
            clon = task.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(); //И складывать программу, если что-то не то.
            //Наверно это не лучший способ 8)
            //Пока с исключениями знаком отдаленно.
        }
        int id = clon.getId();
        //Проверяем наличие в истории нужной записи.
        if (linkedTasksMap.containsKey(id)) {
            //Удаляем и перезаписываем ноду в конец LinkedListTasks. Так же меняем объект в HashMap.
            remove(id);
        }
        linkedListTasks.linkLast((T) clon);
        linkedTasksMap.put(clon.getId(), linkedListTasks.getLastNode());
    }

    @Override
    public List<T> getHistory() {
        return List.copyOf(linkedListTasks.getTasks());
    }

    @Override
    public void remove(int id) {
        //Получаем нужную ноду по индексу из HashMap и удаляем ноду.
        Node<T> n = linkedTasksMap.get(id);
        linkedListTasks.removeNode(n);
    }
}
