package kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kanban.service.HttpHandles.TaskHttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final int PORT = 8080;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void start() {
        httpServer.createContext("/api/v1/task/", new TaskHttpHandler());
        httpServer.start();
    }


}
