package kanban.service.httphandles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.service.FileBackedTaskManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PrioritizedHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final Logger log = Logger.getLogger(PrioritizedHttpHandler.class.getName());

    public PrioritizedHttpHandler(FileBackedTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                if (Pattern.matches("^/api/v1/prioritized/$", path)) {
                    sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotCorrectMethod(exchange);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "" + System.lineSeparator() + e.getMessage());
            sendServerError(exchange);
        }
    }
}
