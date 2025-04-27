package task;

import com.dam.tasks.Task;
import com.dam.taskManagers.InMemoryTaskManager;
import com.dam.taskManagers.Managers;
import com.dam.enums.TaskStatus;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetEpicName() {
        Task task = new Task(TaskStatus.NEW, "My com.dam.tasks.Task", "com.dam.tasks.Task deskription", Instant.parse("2025-01-05T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addTask(task);
        assertEquals("My com.dam.tasks.Task", task.getTaskName());
    }

}
