package kanban.service;

import com.sun.net.httpserver.HttpServer;
import kanban.service.httphandles.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpTaskServer {

    private final Logger log = Logger.getLogger(HttpTaskServer.class.getName());

    private final HttpServer httpServer;

    private final int PORT = 8080;
    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void start() {
        log.log(Level.INFO, "Сервер стартовал на порту " + PORT);

        FileBackedTaskManager fileBackedTaskManager = Manager.getFileBackedManager(new File("storage.csv"));

        httpServer.createContext("/api/v1/task/", new TaskHttpHandler(fileBackedTaskManager));
        httpServer.createContext("/api/v1/subtask/", new SubtaskHttpHandler(fileBackedTaskManager));
        httpServer.createContext("/api/v1/epic/", new EpicHttpHandler(fileBackedTaskManager));
        httpServer.createContext("/api/v1/history/", new HistoryHttpHandler(fileBackedTaskManager));
        httpServer.createContext("/api/v1/prioritized/", new PrioritizedHttpHandler(fileBackedTaskManager));
        httpServer.start();
    }

    public void stop() {
        log.log(Level.INFO, "Сервер отановлен");
        httpServer.stop(0);

    }

}
