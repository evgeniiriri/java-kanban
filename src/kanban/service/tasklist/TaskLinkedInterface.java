package kanban.service.tasklist;

import java.util.ArrayList;

public interface TaskLinkedInterface<E> {

    void linkLast(E element);

    ArrayList<E> getTasks();
}
