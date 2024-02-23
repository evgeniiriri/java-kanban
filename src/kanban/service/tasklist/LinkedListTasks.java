package kanban.service.tasklist;

import java.util.ArrayList;

public class LinkedListTasks<E> implements TaskLinkedInterface<E> {

    private Node<E> firstNode;
    private Node<E> lastNode;
    private int size;

    public LinkedListTasks() {
        lastNode = new Node<E>(null, firstNode, null);
        firstNode = new Node<E>(null, null, lastNode);
    }

    @Override
    public void linkLast(E element) {
        Node<E> prev = lastNode;
        prev.setElement(element);
        lastNode = new Node<E>(null, prev, null);
        prev.setNextElement(lastNode);
        size++;
    }

    @Override
    public ArrayList<E> getTasks() {
        ArrayList<E> result = new ArrayList<>();
        Node<E> x = firstNode.getNextElement();
        for (int i = 0; i < size; i++) {
            result.add(x.getElement());
            x = x.getNextElement();
        }
        return result;
    }
}
