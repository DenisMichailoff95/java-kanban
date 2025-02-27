package test;

import com.dam.taskManager.Task;
import com.dam.taskManager.InMemoryTaskManager;
import com.dam.taskManager.Managers;
import com.dam.taskManager.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetEpicName() {
        Task task = new Task(TaskStatus.NEW, "My com.dam.taskManager.Task", "com.dam.taskManager.Task deskription");
        taskManager.addTask(task);
        assertEquals("My com.dam.taskManager.Task", task.getTaskName());
    }

}
