package kanban.service.httphandles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.model.Epic;
import kanban.model.Subtask;
import kanban.service.FileBackedTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class EpicHttpHandler extends BaseHttpHandler implements HttpHandler {

    private final Logger log = Logger.getLogger(TaskHttpHandler.class.getName());

    public EpicHttpHandler(FileBackedTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/epic/$", path)) {
                        sendText(exchange, gson.toJson(manager.getAllEpic()));
                        break;
                    }
                    if (Pattern.matches("^/api/v1/epic/\\d+$", path)) {
                        int id = getId(path.replaceFirst("^/api/v1/epic/", ""));
                        if (id >= 0) {
                            Optional<Epic> epic = Optional.ofNullable(manager.getEpic(id));
                            if (epic.isPresent()) {
                                //Обработка случая с несущетвующим ID.
                                sendText(exchange, gson.toJson(epic.get()));
                                break;
                            } else {
                                sendNotFound(exchange);
                                break;
                            }
                        } else {
                            sendNotFound(exchange);
                            break;
                        }
                    }
                    if (Pattern.matches("^/api/v1/epic/\\d+/subtasks/$", path)) {
                        int id = getId(path.replaceFirst("^/api/v1/epic/", "").replaceFirst("/subtasks/", ""));
                        if (id >= 0) {
                            Optional<Epic> epic = Optional.ofNullable(manager.getEpic(id));
                            if (epic.isPresent()) {
                                //Обработка случая с несущетвующим ID.
                                List<Subtask> subtasks = manager.getAllSubTask(epic.get().getId());
                                sendText(exchange, gson.toJson(subtasks));
                            } else {
                                sendNotFound(exchange);
                            }
                            break;
                        } else {
                            sendNotFound(exchange);
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/api/v1/epic/$", path)) {
                        Epic epic = getTaskFromJson(exchange);
                        if (!manager.isExistsEpic(epic)) {
                            manager.createEpic(epic);
                            sendCreate(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                        break;
                    }
                    if (Pattern.matches("^/api/v1/epic/\\d+$", path)) {
                        Epic epic = getTaskFromJson(exchange);
                        if (manager.isExistsEpic(epic)) {
                            manager.updateEpic(epic);
                            sendCreate(exchange);
                            break;
                        } else {
                            sendHasInteractions(exchange);
                            break;
                        }
                    }
                }
                case "DELETE": {
                    int id = getId(path.replaceFirst("^/api/v1/epic/", ""));
                    if (id >= 0) {
                        manager.deleteEpic(id);
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

    private Epic getTaskFromJson(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(taskJson, Epic.class);
    }
}

