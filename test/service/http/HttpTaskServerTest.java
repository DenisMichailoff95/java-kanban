package http;

import com.dam.http.HttpTaskServer;
import com.google.gson.Gson;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;
import com.dam.enums.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.dam.taskManagers.Managers;
import com.dam.taskManagers.InMemoryTaskManager;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskServerTest {

    private static HttpTaskServer taskServer;
    private final HttpClient client = HttpClient.newHttpClient();
    private static InMemoryTaskManager taskManager;
    private static final Gson gson = Managers.getGson();

    private static final String BASE_URL = "http://localhost:8000";
    private static final int BASE_PORT = 8000;
    private static final String TASK_URL = BASE_URL + "/tasks";
    private static final String EPIC_URL = BASE_URL + "/epics";
    private static final String SUBTASK_URL = BASE_URL + "/subtasks";

    @BeforeEach
    void beforeEach() {
        try {
            taskServer = Managers.getHttpTaskServer(BASE_PORT);
            taskManager = (InMemoryTaskManager) taskServer.getTaskManager();
            taskServer.main();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void afterEach() {
        taskServer.stop(2);
    }

    private HttpResponse<String> sendGET(String path, int id) throws IOException, InterruptedException {
        HttpRequest request;
        URI uri = URI.create(id == -1 ? path : path + "/" + id);

        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendPost(String path, Task task) throws IOException, InterruptedException {
        URI uri = URI.create(path);
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() == 201) {
            return res;
        }
        return null;
    }

    private Task sendTask1() throws IOException, InterruptedException {
        Task task1 = new Task(TaskStatus.NEW, "Task1", "Task description1", Instant.parse("2025-01-01T12:00:00.000000000Z"), Duration.ofHours(1));
        sendPost(TASK_URL, task1);
        return task1;
    }


    private Epic sendEpicWithSubtasks() throws IOException, InterruptedException {
        Epic myEpic = new Epic(TaskStatus.NEW, "My Epic1", "Task description", Instant.parse("2025-01-06T12:00:00.000000000Z"), Duration.ofHours(6));
        if (sendPost(EPIC_URL, myEpic) != null) {
            Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "Subtask1 description1", myEpic.getTaskId(), Instant.parse("2025-01-03T12:00:00.000000000Z"), Duration.ofHours(3));
            Subtask subTask2 = new Subtask(TaskStatus.NEW, "My subTask2", "Subtask2 description2", myEpic.getTaskId(), Instant.parse("2025-01-03T14:00:00.000000000Z"), Duration.ofHours(6));
            int epicId = myEpic.getTaskId();
            sendPost(SUBTASK_URL, subTask1);
            sendPost(SUBTASK_URL, subTask2);
        }
        return myEpic;
    }


    @Test
    void getTasksAfterPostTest() throws IOException, InterruptedException {
        Task taskBefore = sendTask1();
        HttpResponse<String> response = sendGET(TASK_URL, taskBefore.getTaskId());
        assertEquals(200, response.statusCode());
        Task testTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(testTask);
        assertEquals(taskBefore.getTaskName(), testTask.getTaskName());
    }


    @Test
    void shouldGetSubtaskAndEpicByIdAfterPostTest() throws IOException, InterruptedException {
        Epic epicBefore1 = sendEpicWithSubtasks();
        HttpResponse<String> response = sendGET(EPIC_URL, epicBefore1.getTaskId());
        assertEquals(200, response.statusCode());
        Epic testEpic = gson.fromJson(response.body(), Epic.class);
        assertNotNull(testEpic);
        assertEquals(epicBefore1.getTaskName(), testEpic.getTaskName());
        assertEquals(2, testEpic.getSubtaskCount());
    }

}