package kanban.service.tasklist;

public class Node<E> {
    private E element;
    private Node<E> prevElement;
    private Node<E> nextElement;

    Node(E element, Node<E> prevElement, Node<E> nextElement) {
        this.element = element;
        this.prevElement = prevElement;
        this.nextElement = nextElement;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public Node<E> getPrevElement() {
        return prevElement;
    }

    public void setPrevElement(Node<E> prevElement) {
        this.prevElement = prevElement;
    }

    public Node<E> getNextElement() {
        return nextElement;
    }

    public void setNextElement(Node<E> nextElement) {
        this.nextElement = nextElement;
    }
}
