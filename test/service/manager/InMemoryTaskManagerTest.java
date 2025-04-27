package manager;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.InMemoryTaskManager;
import com.dam.taskManagers.Managers;

import com.dam.tasks.Task;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryTaskManagerTest {

    private static final InMemoryTaskManager taskManager = Managers.getDefault();
    
    @Test
    void getTaskByID() {
        Task task1 = new Task(TaskStatus.NEW, "My subTask1", "com.dam.tasks.Task description", Instant.parse("2025-01-05T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addTask(task1);
        int taskId1 = task1.getTaskId();
        System.out.println(taskId1);
        String taskId = Integer.toString(taskId1);
        String taskParams = "Task{status=NEW, taskName='My subTask1', taskDescription='com.dam.tasks.Task description', taskId=" + taskId + ", startTime=2025-01-05 12:00:00, endTime=2025-01-05 13:00:00, duration=01:00:00}";
        assertEquals(taskParams, taskManager.getTaskByID(taskId1).toString());
    }
}