package kanban.test;

import kanban.service.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


class HttpTaskServerTest {

    private static final File file = new File("storage");
    private static final File fileTask = new File("storage", "TASKstorage.csv");
    private static final File fileEpic = new File("storage", "EPICstorage.csv");
    private static final File fileSubtask = new File("storage", "SUBTASKstorage.csv");
    private static final File fileHistory = new File("storage", "HISTORYstorage.csv");
    private HttpTaskServer httpTaskServer;
    private static final String taskString = "{\"status\":\"NEW\",\"name\":\"15.07.2024\",\"id\":0,\"description\":\"1hours duration\",\"duration\":60,\"startTime\":\"2024-07-11 10:00\"}";
    private static final String subtaskString = "{\"epicID\":0,\"status\":\"NEW\",\"name\":\"15.08.2024\",\"id\":0,\"description\":\"2hours duration\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
    private static final String epicString = "{\"idSubTask\":[],\"endTime\":\"0001-01-01 01:01\",\"status\":\"NEW\",\"name\":\"Epic\",\"id\":0,\"description\":\"Epic epic\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";

    @BeforeEach
    public void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        fileEpic.deleteOnExit();
        fileTask.deleteOnExit();
        fileSubtask.deleteOnExit();
        fileHistory.deleteOnExit();
        file.deleteOnExit();
        httpTaskServer.stop();
    }

    @Test
    public void shouldCreateReadUpdateDeleteTaskServer() throws IOException, InterruptedException {
        //Создаем задачу на сервере и проверяем статус ответа.
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/api/v1/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskString)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Запрашиваем с сервера созданую задачу, смотрим статус ответа и сравнием задачи.
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(taskString, response.body().substring(1, response.body().length() - 1));
        //Обновляем уже созданую задачу и проверяем статус ответа.
        String newTaskString = "{\"status\":\"NEW\",\"name\":\"NEW TASK\",\"id\":0,\"description\":\"1hours duration\",\"duration\":60,\"startTime\":\"2024-10-11 10:00\"}";
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/task/1"))
                .POST(HttpRequest.BodyPublishers.ofString(newTaskString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Удалаем задачу и проверяем статус ответа.
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/task/1"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldCreateReadUpdateDeleteSubtaskServer() throws IOException, InterruptedException {
        //Создаем задачу на сервере и проверяем статус ответа.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/epic/0"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlEpic = URI.create("http://localhost:8080/api/v1/epic/");
        String epic = "{\"idSubTask\":[1],\"endTime\":\"0001-01-01 01:01\",\"status\":\"NEW\",\"name\":\"Epic\",\"id\":0,\"description\":\"Epic epic\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
        request = HttpRequest.newBuilder()
                .uri(urlEpic)
                .POST(HttpRequest.BodyPublishers.ofString(epic)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        URI url = URI.create("http://localhost:8080/api/v1/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskString)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Запрашиваем с сервера созданую задачу, смотрим статус ответа и сравнием задачи.
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(subtaskString, response.body().substring(1, response.body().length() - 1));
        //Обновляем уже созданую задачу и проверяем статус ответа.
        String newTaskString = "{\"epicID\":3,\"status\":\"NEW\",\"name\":\"NEW SUBTASK\",\"id\":0,\"description\":\"NEW SUBTASK\",\"duration\":120,\"startTime\":\"2024-09-15 12:00\"}";
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/subtask/1"))
                .POST(HttpRequest.BodyPublishers.ofString(newTaskString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Удалаем задачу и проверяем статус ответа.
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/subtask/1"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldCreateReadUpdateDeleteEpicServer() throws IOException, InterruptedException {
        //Создаем задачу на сервере и проверяем статус ответа.
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/api/v1/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicString)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Запрашиваем с сервера созданую задачу, смотрим статус ответа и сравнием задачи.
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(epicString, response.body().substring(1, response.body().length() - 1));
        //Обновляем уже созданую задачу и проверяем статус ответа.
        String newTaskString = "{\"idSubTask\":[],\"endTime\":\"0001-01-01 01:01\",\"status\":\"NEW\",\"name\":\"NEW EPIC\",\"id\":0,\"description\":\"NEW EPIC\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/epic/0"))
                .POST(HttpRequest.BodyPublishers.ofString(newTaskString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(201, response.statusCode());
        //Удалаем задачу и проверяем статус ответа.
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/epic/1"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }


}