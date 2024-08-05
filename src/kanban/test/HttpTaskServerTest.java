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
import java.nio.charset.StandardCharsets;


class HttpTaskServerTest {

    private static final File file = new File("storage");
    private static final File fileTask = new File("storage", "TASKstorage.csv");
    private static final File fileEpic = new File("storage", "EPICstorage.csv");
    private static final File fileSubtask = new File("storage", "SUBTASKstorage.csv");
    private static final File fileHistory = new File("storage", "HISTORYstorage.csv");
    private HttpTaskServer httpTaskServer;
    private static final String taskString = "{\"status\":\"NEW\",\"name\":\"15.07.2024\",\"id\":0,\"description\":\"1hours duration\",\"duration\":60,\"startTime\":\"2024-07-11 10:00\"}";
    private static final String taskStringExpected = "{\"status\":\"NEW\",\"name\":\"15.07.2024\",\"id\":1,\"description\":\"1hours duration\",\"duration\":60,\"startTime\":\"2024-07-11 10:00\"}";
    private static final String subtaskString = "{\"epicID\":0,\"status\":\"NEW\",\"name\":\"15.08.2024\",\"id\":0,\"description\":\"2hours duration\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
    private static final String subtaskStringExpected = "{\"epicID\":0,\"status\":\"NEW\",\"name\":\"15.08.2024\",\"id\":1,\"description\":\"2hours duration\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
    private static final String epicString = "{\"idSubTask\":[],\"endTime\":\"0001-01-01 01:01\",\"status\":\"NEW\",\"name\":\"Epic\",\"id\":0,\"description\":\"Epic epic\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";
    private static final String epicStringExpected = "{\"idSubTask\":[1],\"endTime\":\"2024-08-15 14:00\",\"status\":\"NEW\",\"name\":\"Epic\",\"id\":0,\"description\":\"Epic epic\",\"duration\":120,\"startTime\":\"2024-08-15 12:00\"}";

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

    private HttpResponse<String> methodGET(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> methodPOST(String url, String text) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(text, StandardCharsets.UTF_8))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> methodDELETE(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    @Test
    public void shouldGetAndCreateTaskFromServer() throws IOException, InterruptedException {
        HttpResponse<String> responsePOST = methodPOST("http://localhost:8080/api/v1/task/", taskString);
        Assertions.assertEquals(201, responsePOST.statusCode());

        HttpResponse<String> responseGET = methodGET("http://localhost:8080/api/v1/task/");
        Assertions.assertEquals(200, responseGET.statusCode());
        Assertions.assertEquals(taskStringExpected, responseGET.body().substring(1, responseGET.body().length() - 1));
    }

    @Test
    public void shouldDeleteTaskFromServer() throws IOException, InterruptedException {
        HttpResponse<String> responseDELETE = methodDELETE("http://localhost:8080/api/v1/task/1");
        Assertions.assertEquals(200, responseDELETE.statusCode());
    }

    @Test
    public void shouldGetAndCreateEpicAndSubtasksFromServer() throws IOException, InterruptedException {
        HttpResponse<String> responsePOSTEpic = methodPOST("http://localhost:8080/api/v1/epic/", epicString);
        Assertions.assertEquals(201, responsePOSTEpic.statusCode());

        HttpResponse<String> responsePOSTSubtask = methodPOST("http://localhost:8080/api/v1/subtask/", subtaskString);
        Assertions.assertEquals(201, responsePOSTSubtask.statusCode());

        HttpResponse<String> responseGETEpic = methodGET("http://localhost:8080/api/v1/epic/0");
        Assertions.assertEquals(200, responseGETEpic.statusCode());
        Assertions.assertEquals(epicStringExpected, responseGETEpic.body());

        HttpResponse<String> responseGETSubtask = methodGET("http://localhost:8080/api/v1/subtask/1");
        Assertions.assertEquals(200, responseGETSubtask.statusCode());
        Assertions.assertEquals(subtaskStringExpected, responseGETSubtask.body());

    }

    @Test
    public void shouldDeleteEpicAndSubtaskFromServer() throws IOException, InterruptedException {
        HttpResponse<String> responseDELETEEpic = methodDELETE("http://localhost:8080/api/v1/epic/0");
        Assertions.assertEquals(200, responseDELETEEpic.statusCode());

        HttpResponse<String> responseGETSubtask = methodGET("http://localhost:8080/api/v1/subtask/");
        Assertions.assertEquals("[]", responseGETSubtask.body());
    }

    @Test
    public void shouldGetHistoryAndPrioritizedTask() throws IOException, InterruptedException {
        HttpResponse<String> responseGETHistory = methodGET("http://localhost:8080/api/v1/history/");
        HttpResponse<String> responseGETPrioritized = methodGET("http://localhost:8080/api/v1/history/");
        //История и приоритетные задачи уже покерыты тестами, здесь я решил просто проверить, что сервер возвращает не пустоту.
        Assertions.assertNotNull(responseGETHistory.body());
        Assertions.assertNotNull(responseGETPrioritized.body());
    }


}