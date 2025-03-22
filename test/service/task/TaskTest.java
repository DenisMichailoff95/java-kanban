package task;

import com.dam.tasks.Task;
import com.dam.taskManagers.InMemoryTaskManager;
import com.dam.taskManagers.Managers;
import com.dam.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetEpicName() {
        Task task = new Task(TaskStatus.NEW, "My com.dam.tasks.Task", "com.dam.tasks.Task deskription");
        taskManager.addTask(task);
        assertEquals("My com.dam.tasks.Task", task.getTaskName());
    }

}
