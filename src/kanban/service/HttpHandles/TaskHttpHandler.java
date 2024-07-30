package kanban.service.HttpHandles;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

class TaskList extends TypeToken<List<Task>>{}

public class TaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final Logger log = Logger.getLogger(TaskHttpHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        try {

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/api/v1/task/$", path)) {
                        sendText(exchange, gson.toJson(manager.getAllTask()));
                    }
                    if (Pattern.matches("^/api/v1/task/\\d+$", path)) {
                        int id = getId(path.replaceFirst("^/api/v1/task/", ""));
                        if (id >= 0) {
                            //Обработать случай, когда прилетает ID которого нет в программе.
                            sendText(exchange, gson.toJson(manager.getTask(id)));
                        } else {
                            sendNotFound(exchange);
                            return;
                        }
                    }
                }
                case "POST": {
                }
                case "DELETE": {
                }
                default: {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "НУЖНО НОРМАЛЬНО ЗАПОЛНИТЬ.");
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
}

