package kanban.service;

import kanban.model.Epic;
import kanban.model.Status;
import kanban.model.Subtask;
import kanban.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final Path pathDir;
    private final Path pathTask;
    private final Path pathEpic;
    private final Path pathSubtask;
    private final Path pathHistory;

    public FileBackedTaskManager(File file) {
        /*С тремя hashMap для хранения задач оказалось не удобно работать сейчас.
         * Я решил сделать для каждого типа задачи свое хранилище, что бы
         * не нагромождать код ветвлениями if-else.*/
        pathDir = Paths.get("storage");
        pathTask = Paths.get("storage", "TASK" + file);
        pathEpic = Paths.get("storage", "EPIC" + file);
        pathSubtask = Paths.get("storage", "SUBTASK" + file);
        pathHistory = Paths.get("storage", "HISTORY" + file);

        try {
            if (!Files.exists(pathDir)) {
                Path dir = Files.createDirectories(pathDir);
            }
            if (!Files.exists(pathTask)) {
                Path task = Files.createFile(pathTask);
            }
            if (!Files.exists(pathEpic)) {
                Path epic = Files.createFile(pathEpic);
            }
            if (!Files.exists(pathSubtask)) {
                Path subtask = Files.createFile(pathSubtask);
            }
            if (!Files.exists(pathHistory)) {
                Path history = Files.createFile(pathHistory);
            }
        } catch (IOException e) {
            System.out.println("Create directory or file ERROR " + System.lineSeparator() + e.getStackTrace());
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);
        try (
                BufferedReader readerTask = new BufferedReader(
                        new FileReader(String.valueOf(fbtm.pathTask), StandardCharsets.UTF_8));
                BufferedReader readerEpic = new BufferedReader(
                        new FileReader(String.valueOf(fbtm.pathEpic), StandardCharsets.UTF_8));
                BufferedReader readerSubtask = new BufferedReader(
                        new FileReader(String.valueOf(fbtm.pathSubtask), StandardCharsets.UTF_8));
                BufferedReader readerHistory = new BufferedReader(
                        new FileReader(String.valueOf(fbtm.pathHistory), StandardCharsets.UTF_8));
        ) {

        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return fbtm;
    }

    public void save() throws ManagerSaveException {

        try (
                Writer writerTask = new FileWriter(String.valueOf(pathTask), StandardCharsets.UTF_8, false);
                Writer writerEpic = new FileWriter(String.valueOf(pathEpic), StandardCharsets.UTF_8, false);
                Writer writerSubtask = new FileWriter(String.valueOf(pathSubtask), StandardCharsets.UTF_8, false);
                Writer writeHistory = new FileWriter(String.valueOf(pathHistory), StandardCharsets.UTF_8, false);
        ) {
            writerTask.write("type,id,name,status,description" + System.lineSeparator());
            writerEpic.write("type,id,name,status,description,subtask" + System.lineSeparator());
            writerSubtask.write("type,id,name,status,description,epic" + System.lineSeparator());
            writeHistory.write(historyToString(super.inMemoryHistoryManager));

            for (Task task : getAllTask()) {
                writerTask.write(toString(task) + System.lineSeparator());
            }
            for (Epic epic : getAllEpic()) {
                writerEpic.write(toString(epic) + System.lineSeparator());
            }
            for (Subtask subtask : getAllSubTask()) {
                writerSubtask.write(toString(subtask) + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Write ERROR");
        }
    }

    public static String historyToString(HistoryManager<Task> historyManager) {
        return historyManager.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        for (String id : value.split(",")) {
            try {
                result.add(Integer.parseInt(id));
            } catch (ClassCastException e) {
                System.out.println("Error = " + e.getMessage());
            }
        }
        return result;
    }

    public String toString(Task task) {
        return String.valueOf(task);
    }

    public Task fromString(String value) {
        String[] splitValue = value.split(",");
        int id = Integer.parseInt(splitValue[1]);
        String type = splitValue[0];
        String name = splitValue[2];
        String description = splitValue[4];
        Status status = getStatus(splitValue[3]);
        if (type.equals("TASK")) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        } else if (type.equals("EPIC")) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(splitValue[5]);
            Subtask subtask = new Subtask(name, description);
            subtask.setId(id);
            subtask.setStatus(status);
            subtask.setMyEpic(epicId);
            return subtask;
        }
        return null;
    }

    private Status getStatus(String status) {
        if (status.equals("NEW")) {
            return Status.NEW;
        } else if (status.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else if (status.equals("DONE")) {
            return Status.DONE;
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public List<Task> getAllTask() {
        return super.getAllTask();
    }

    @Override
    public List<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public List<Subtask> getAllSubTask() {
        return super.getAllSubTask();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public Subtask getSubTask(int id) {
        return super.getSubTask(id);
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("create tasks ERROR" + System.lineSeparator() + e.getMessage());
        }

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("create epic ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void createSubTask(Subtask subTask, Epic epic) {
        super.createSubTask(subTask, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("create subtask ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("update task ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("update epic ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void updateSubTask(int id, Subtask subTask) {
        super.updateSubTask(id, subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("update subtask ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("delete task ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("delete epic ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("delete subtask ERROR" + System.lineSeparator() + e.getMessage());
        }
    }

    @Override
    public ArrayList<Subtask> getAllSubTask(int id) {
        return super.getAllSubTask(id);
    }

    @Override
    public void setStatus(int epicID) {
        super.setStatus(epicID);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("set status ERROR" + System.lineSeparator() + e.getMessage());
        }
    }
}
