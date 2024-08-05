package kanban.service.httphandles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.model.Task;
import kanban.service.FileBackedTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final Logger log = Logger.getLogger(TaskHttpHandler.class.getName());

    public TaskHttpHandler(FileBackedTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/task/$", path)) {
                        sendText(exchange, gson.toJson(manager.getAllTask()));
                        break;
                    }
                    if (Pattern.matches("^/api/v1/task/\\d+$", path)) {
                        int id = getId(path.replaceFirst("^/api/v1/task/", ""));
                        if (id >= 0) {
                            Optional<Task> task = Optional.ofNullable(manager.getTask(id));
                            if (task.isPresent()) {
                                //Обработка случая с несущетвующим ID.
                                sendText(exchange, gson.toJson(task.get()));
                            } else {
                                sendNotFound(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                        break;
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/api/v1/task/$", path)) {
                        Task task = getTaskFromJson(exchange);
                        if (!manager.isExistsTask(task)) {
                            manager.createTask(task);
                            sendCreate(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                        break;
                    }
                    if (Pattern.matches("^/api/v1/task/\\d+$", path)) {
                        Task task = getTaskFromJson(exchange);
                        if (manager.isExistsTask(task)) {
                            manager.updateTask(task);
                            sendCreate(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    int id = getId(path.replaceFirst("^/api/v1/task/", ""));
                    if (id >= 0) {
                        manager.deleteTask(id);
                        sendOk(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                default: {
                    sendHasInteractions(exchange);
                    break;
                }
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "" + System.lineSeparator() + e.getMessage());
            sendServerError(exchange);
        }
    }

    private int getId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "Передан не корректный id - " + id);
        }
        return -1;
    }

    private Task getTaskFromJson(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(taskJson, Task.class);
    }
}

