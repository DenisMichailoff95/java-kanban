package manager;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.InMemoryTaskManager;
import com.dam.taskManagers.Managers;

import com.dam.tasks.Task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryTaskManagerTest {

    private static final InMemoryTaskManager taskManager = Managers.getDefault();
    
    @Test
    void getTaskByID() {
        Task task1 = new Task(TaskStatus.NEW, "My subTask1", "com.dam.tasks.Task description");
        taskManager.addTask(task1);
        int taskId1 = task1.getTaskId();
        String taskParams = "Task{My subTask1: description=com.dam.tasks.Task description, status=NEW, taskId=";
        String taskId = Integer.toString(taskId1);
        taskParams = taskParams + taskId + '}';
        assertEquals(taskParams, taskManager.getTaskByID(taskId1).toString());
    }
}