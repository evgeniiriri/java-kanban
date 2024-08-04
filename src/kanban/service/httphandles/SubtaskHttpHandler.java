package kanban.service.httphandles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.model.Subtask;
import kanban.service.FileBackedTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SubtaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final Logger log = Logger.getLogger(TaskHttpHandler.class.getName());

    public SubtaskHttpHandler(FileBackedTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/subtask/$", path)) {
                        sendText(exchange, gson.toJson(manager.getAllSubTask()));
                        break;
                    }
                    if (Pattern.matches("^/api/v1/subtask/\\d+$", path)) {
                        int id = getId(path.replaceFirst("^/api/v1/subtask/", ""));
                        if (id >= 0) {
                            Optional<Subtask> subtask = Optional.ofNullable(manager.getSubTask(id));
                            //Возможно нужно сделать отдельный метод ответа.
                            if (subtask.isPresent()) {
                                //Обработка случая с несущетвующим ID.
                                sendText(exchange, gson.toJson(subtask.get()));
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
                    if (Pattern.matches("^/api/v1/subtask/$", path)) {
                        Subtask subtask = getTaskFromJson(exchange);
                        if (!manager.isExistsSubtask(subtask)) {
                            manager.createSubTask(subtask);
                            sendCreate(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                        break;
                    }
                    if (Pattern.matches("^/api/v1/subtask/\\d+$", path)) {
                        Subtask subtask = getTaskFromJson(exchange);
                        if (manager.isExistsSubtask(subtask)) {
                            manager.updateSubTask(subtask);
                            sendCreate(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    int id = getId(path.replaceFirst("^/api/v1/subtask/", ""));
                    if (id >= 0) {
                        manager.deleteSubTask(id);
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

    private Subtask getTaskFromJson(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(taskJson, Subtask.class);
    }
}

