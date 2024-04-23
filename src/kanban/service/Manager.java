package kanban.service;

import java.io.File;

public class Manager {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        return new FileBackedTaskManager(file);
    }
}
