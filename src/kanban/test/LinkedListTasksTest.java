package kanban.test;

import kanban.service.tasklist.LinkedListTasks;
import kanban.service.tasklist.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LinkedListTasksTest {

    private static LinkedListTasks<Integer> testLinked;

    @BeforeEach
    public void beforeEach() {
        testLinked = new LinkedListTasks<>();
        for (int i = 0; i < 15; i++) {
            testLinked.linkLast(i);
        }
    }

    @Test
    void shouldLastNodeReturnNull() {
        Assertions.assertNull(testLinked.getLastNode().getNextElement());
    }

    @Test
    void shouldNonEqualsLastNods() {
        Node<Integer> n = testLinked.getLastNode();
        testLinked.removeNode(n);

        Assertions.assertNotEquals(n, testLinked.getLastNode());
    }

    @Test
    void shouldGetLastElements() {
        int expected = 404;
        testLinked.linkLast(404);
        List<Integer> actually = testLinked.getTasks();

        Assertions.assertEquals(expected, actually.get(actually.size() - 1));
    }

    @Test
    void shouldEqualsValuesTasks() {
        List<Integer> actually = testLinked.getTasks();
        for (int i = 0; i < actually.size(); i++) {
            Assertions.assertEquals(i, actually.get(i));
        }
    }
}