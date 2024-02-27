package kanban.service.tasklist;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class LinkedListTasks<E> implements TaskLinkedInterface<E> {

    private Node<E> firstNode;
    private Node<E> lastNode;
    private int size = 0;

    public LinkedListTasks() {
        lastNode = new Node<E>(null, firstNode, null);
        firstNode = new Node<E>(null, null, lastNode);
    }

    public Node<E> getLastNode() {
        final Node<E> l = lastNode;
        if (l == null)
            throw new NoSuchElementException();
        return l;
    }

    public void removeNode(Node<E> node) {
        final Node<E> prev = node.getPrevElement();
        final Node<E> next = node.getNextElement();

        if (prev == null) {
            firstNode = next;
        } else {
            prev.setNextElement(next);
            node.setPrevElement(null);
        }

        if (next == null) {
            lastNode = prev;
        } else {
            next.setPrevElement(prev);
            node.setNextElement(null);
        }

        node.setElement(null);
        size--;

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
