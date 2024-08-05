package kanban.service.httphandles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import kanban.service.FileBackedTaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseHttpHandler {

    protected FileBackedTaskManager manager;
    protected Gson gson = getGson();
    private final Logger log = Logger.getLogger(BaseHttpHandler.class.getName());
    public BaseHttpHandler(FileBackedTaskManager manager) {
        this.manager = manager;
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendOk(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.close();
    }

    protected void sendCreate(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(201, 0);
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        log.log(Level.SEVERE, "Задача не была найдена. 404.");
        httpExchange.sendResponseHeaders(404, 0);
        httpExchange.close();
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        log.log(Level.SEVERE, "Задачи пересекаются. 406.");
        httpExchange.sendResponseHeaders(406, 0);
        httpExchange.close();
    }

    protected void sendServerError(HttpExchange httpExchange) throws IOException {
        log.log(Level.SEVERE, "Серверная ошибка. 500");
        httpExchange.sendResponseHeaders(500, 0);
        httpExchange.close();
    }

    protected void sendNotCorrectMethod(HttpExchange httpExchange) throws IOException {
        log.log(Level.WARNING, httpExchange.getRequestMethod() + " не облуживается. 406");
        httpExchange.sendResponseHeaders(406, 0);
        httpExchange.close();
    }

    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}

class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime startTime) throws IOException {

        jsonWriter.value(startTime.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), dtf);
    }
}
class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}