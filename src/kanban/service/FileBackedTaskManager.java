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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final Logger log = Logger.getLogger(FileBackedTaskManager.class.getName());
    private final Path pathDir;
    private final Path pathTask;
    private final Path pathEpic;
    private final Path pathSubtask;
    private final Path pathHistory;

    public FileBackedTaskManager(File file) {
        /* Так как у меня в InMemoryTaskManager есть три HashMap для хранения задач,
         * Я решил сделать для каждого типа задачи свое файловое хранилище.*/
        log.log(Level.INFO, "Инициализация " + FileBackedTaskManager.class.getName());

        pathDir = Paths.get("storage");
        pathTask = Paths.get("storage", "TASK" + file);
        pathEpic = Paths.get("storage", "EPIC" + file);
        pathSubtask = Paths.get("storage", "SUBTASK" + file);
        pathHistory = Paths.get("storage", "HISTORY" + file);
        //Проверяем на наличие хранилища и создаем его.
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
            log.log(Level.SEVERE, "Ошибка создания хранилища." + System.lineSeparator() + e.getMessage());
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);
        //Создаем ридеры для каждого из файлов хранилища.
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
            //Храним максимальное id задачи, что бы менеджер после загрузки, мог корректно работать
            int idForManager = 0;
            //Загружаем в память задачи, пропуская шапку таблицы.
            String line;
            log.log(Level.INFO, "Загрузка задач.");
            while ((line = readerTask.readLine()) != null) {
                if (line.startsWith("type")) {
                    continue;
                }
                Task task = fbtm.fromString(line);
                fbtm.taskHashMap.put(task.getId(), task);
                if (idForManager < task.getId()) {
                    idForManager = task.getId();
                }
            }
            while ((line = readerEpic.readLine()) != null) {
                if (line.startsWith("type")) {
                    continue;
                }
                Epic epic = (Epic) fbtm.fromString(line);
                fbtm.epicHashMap.put(epic.getId(), epic);
                if (idForManager < epic.getId()) {
                    idForManager = epic.getId();
                }
            }
            while ((line = readerSubtask.readLine()) != null) {
                if (line.startsWith("type")) {
                    continue;
                }
                Subtask subtask = (Subtask) fbtm.fromString(line);
                fbtm.subTaskHashMap.put(subtask.getId(), subtask);
                if (idForManager < subtask.getId()) {
                    idForManager = subtask.getId();
                }
            }
            //Устанавливаем корректный id.
            fbtm.setIdForManager(idForManager);
            //Загружаем историю.
            log.log(Level.INFO, "Загрузка истории.");
            if (readerHistory.ready()) {
                String[] history = readerHistory.readLine().split(",");
                for (String idHistory : history) {
                    int id = Integer.parseInt(idHistory);
                    if (fbtm.taskHashMap.containsKey(id)) {
                        Task task = fbtm.taskHashMap.get(id);
                        fbtm.inMemoryHistoryManager.add(task);
                    } else if (fbtm.epicHashMap.containsKey(id)) {
                        Epic epic = fbtm.epicHashMap.get(id);
                        fbtm.inMemoryHistoryManager.add(epic);
                    } else if (fbtm.subTaskHashMap.containsKey(id)) {
                        Subtask subtask = fbtm.subTaskHashMap.get(id);
                        fbtm.inMemoryHistoryManager.add(subtask);
                    }
                }
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Ошибка загрузки задач из хранилища." + System.lineSeparator() + e.getMessage());
        }
        return fbtm;
    }

    public void save() throws ManagerSaveException {
        log.log(Level.INFO, "Сохранение задач.");
        //Создаем врайтеров.
        try (
                Writer writerTask = new FileWriter(String.valueOf(pathTask), StandardCharsets.UTF_8, false);
                Writer writerEpic = new FileWriter(String.valueOf(pathEpic), StandardCharsets.UTF_8, false);
                Writer writerSubtask = new FileWriter(String.valueOf(pathSubtask), StandardCharsets.UTF_8, false);
                Writer writeHistory = new FileWriter(String.valueOf(pathHistory), StandardCharsets.UTF_8, false);
        ) {
            //Пишем шапку таблиц.
            writerTask.write("type;id;name;status;description;start time;duration" + System.lineSeparator());
            writerEpic.write("type;id;name;status;description;start time;duration;my subtasks" + System.lineSeparator());
            writerSubtask.write("type;id;name;status;description;start time;duration;my epic" + System.lineSeparator());
            writeHistory.write(historyToString(super.inMemoryHistoryManager));

            //Заполняем хранилище.
            for (Task task : taskHashMap.values()) {
                writerTask.write(toString(task) + System.lineSeparator());
            }
            for (Epic epic : epicHashMap.values()) {
                writerEpic.write(toString(epic) + System.lineSeparator());
            }
            for (Subtask subtask : subTaskHashMap.values()) {
                writerSubtask.write(toString(subtask) + System.lineSeparator());
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Не удалось сохранить задачи.");
            throw new ManagerSaveException("Ошибка сохранения.");
        }
    }

    public static String historyToString(HistoryManager<Task> historyManager) {
        return historyManager.toString();
    }

    public String toString(Task task) {
        return String.valueOf(task);
    }

    public Task fromString(String value) {
        //Разделяем строку из файла и возвращаем нужный тип задачи, полностью готовый к работе.
        if (!value.isEmpty()) {
            String[] splitValue = value.split(";");
            String type = splitValue[0];
            String name = splitValue[2];
            String description = splitValue[4];
            if (type.equals("TASK")) {
                Task task = new Task(name, description);
                task.setId(Integer.parseInt(splitValue[1]));
                task.setStatus(getStatus(splitValue[3]));
                task.setStartTime(LocalDateTime.parse(splitValue[5], task.getTaskDateTimeFormatter()));
                task.setDuration(Duration.parse("PT" + splitValue[6] + "M"));
                return task;
            } else if (type.equals("EPIC")) {
                Epic epic = new Epic(name, description);
                epic.setId(Integer.parseInt(splitValue[1]));
                epic.setStatus(getStatus(splitValue[3]));
                epic.setStartTime(LocalDateTime.parse(splitValue[5], epic.getTaskDateTimeFormatter()));
                epic.setDuration(Duration.parse("PT" + splitValue[6] + "M"));
                //Загружаем все Subtask данного Epic.
                String[] idSubtasks = splitValue[7].substring(1, splitValue[7].length() - 1).split(",");
                for (String idSubtask : idSubtasks) {
                    try {
                        epic.setSubTasks(Integer.parseInt(idSubtask.trim()));
                    } catch (NumberFormatException e) {
                        log.log(Level.INFO, "У " + epic.getClass() +" нет подзадач." );
                        break;
                    }
                }
                return epic;
            } else if (type.equals("SUBTASK")) {
                //Получаем id epic этого subtask
                int epicId = Integer.parseInt(splitValue[7]);
                Subtask subtask = new Subtask(name, description);
                subtask.setId(Integer.parseInt(splitValue[1]));
                subtask.setStatus(getStatus(splitValue[3]));
                subtask.setStartTime(LocalDateTime.parse(splitValue[5], subtask.getTaskDateTimeFormatter()));
                subtask.setDuration(Duration.parse("PT" + splitValue[6] + "M"));
                subtask.setMyEpic(epicId);
                return subtask;
            }
        }
        return null;
    }

    private Status getStatus(String status) {
        //Метод для перевода статуса из строки в Status
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
        List<Task> res = super.getAllTask();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю task.");
        }
        return res;
    }

    @Override
    public List<Epic> getAllEpic() {
        List<Epic> res = super.getAllEpic();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю epic.");
        }
        return res;
    }

    @Override
    public List<Subtask> getAllSubTask() {
        List<Subtask> res = super.getAllSubTask();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю subtask.");
        }
        return res;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить удаление всех task.");
        }
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить удаление всех epic.");
        }
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING,"Не удалось сохранить удаление всех subtask.");
        }
    }

    @Override
    public Task getTask(int id) {
        Task result = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю task.");
        }
        return result;
    }

    @Override
    public Epic getEpic(int id) {
        Epic result = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю epic.");
        }
        return result;
    }

    @Override
    public Subtask getSubTask(int id) {
        Subtask result = super.getSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить историю subtask.");
        }
        return result;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить новую task.");
        }

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить новый epic.");
        }
    }

    @Override
    public void createSubTask(Subtask subTask, Epic epic) {
        super.createSubTask(subTask, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить новый subtask.");
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить обновление task.");
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить обновление epic.");
        }
    }

    @Override
    public void updateSubTask(int id, Subtask subTask) {
        super.updateSubTask(id, subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить обновление subtask.");
        }
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить удаление task.");
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить удаление epic.");
        }
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            log.log(Level.WARNING, "Не удалось сохранить удаление subtask.");
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
            log.log(Level.WARNING, "Не удалось сохранить изменения статуса у задач.");
        }
    }
}
