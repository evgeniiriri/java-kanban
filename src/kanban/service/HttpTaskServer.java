package kanban.service;

import com.sun.net.httpserver.HttpServer;
import kanban.service.httphandles.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpTaskServer {

    private final String urlTask = "/api/v1/task/";
    private final String urlSubtask = "/api/v1/subtask/";
    private final String urlEpic = "/api/v1/epic/";
    private final String urlHistory = "/api/v1/history/";
    private final String urlPrioritized = "/api/v1/prioritized/";

    private final Logger log = Logger.getLogger(HttpTaskServer.class.getName());

    private final HttpServer httpServer;

    private final int PORT = 8080;
    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void start() {
        log.log(Level.INFO, "Сервер стартовал на порту " + PORT);

        FileBackedTaskManager fileBackedTaskManager = Manager.getFileBackedManager(new File("storage.csv"));

        httpServer.createContext(urlTask, new TaskHttpHandler(fileBackedTaskManager));
        httpServer.createContext(urlSubtask, new SubtaskHttpHandler(fileBackedTaskManager));
        httpServer.createContext(urlEpic, new EpicHttpHandler(fileBackedTaskManager));
        httpServer.createContext(urlHistory, new HistoryHttpHandler(fileBackedTaskManager));
        httpServer.createContext(urlPrioritized, new PrioritizedHttpHandler(fileBackedTaskManager));
        httpServer.start();
    }

    public void stop() {
        log.log(Level.INFO, "Сервер отановлен");
        httpServer.stop(0);

    }

}
