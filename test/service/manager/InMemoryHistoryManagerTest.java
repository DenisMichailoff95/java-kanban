package manager;

import java.time.Instant;
import java.util.List;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.InMemoryHistoryManager;
import com.dam.taskManagers.InMemoryTaskManager;
import com.dam.tasks.Task;
import com.dam.taskManagers.Managers;
import com.dam.taskManagers.HistoryManager;
import com.dam.taskManagers.TaskManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {

    @Test
    void newHistoryManagerTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "historyManager is NULL");
    }

    @Test
    void checkRepeatedRequestHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description", Instant.parse("2025-01-05T12:00:00.000000000Z"),300);
        final int sizeForChecking = 1;
        final int sizeForRepeats = 10;
        for (int i = 0; i <= sizeForRepeats; i++) {
            historyManager.add(task);
        }
        List<Task> RequestHistoryList = historyManager.getHistory();
        assertEquals(sizeForChecking, RequestHistoryList.size(), "The repeat limit does not work");
    }

    @Test
    void addInHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description", Instant.parse("2025-01-05T12:00:00.000000000Z"),300);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is not NULL");
        assertEquals(1, history.size(), "History size is NULL");
    }

    @Test
    void disappearanceFromHistoryAfterRemoveFromTaskManagerTest() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description", Instant.parse("2025-01-05T12:00:00.000000000Z"),300);
        int taskID = task.getTaskId();
        taskManager.addTask(task);
        taskManager.getTaskByID(taskID);
        List<Task> historyBefore = taskManager.getHistory();
        taskManager.deleteTaskByID(taskID);
        List<Task> historyAfter = taskManager.getHistory();
        String taskId = Integer.toString(taskID);
        System.out.println(historyBefore.toString());
        String taskParams = "[Task{status=NEW, taskName='My Task1', taskDescription='com.dam.tasks.Task description', taskId=" + taskId + ", startTime=2025-01-05 12:00:00, endTime=2025-01-05 17:00:00, duration=05:00}]";
        assertTrue(historyBefore.toString().equals(taskParams), "History size is empty");
        assertTrue(historyAfter.toString().equals("[]"), "History size is not empty");
    }

    @Test
    void disappearanceFromHistoryAfterRemoveFromHistoryManagerTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description", Instant.parse("2025-01-05T12:00:00.000000000Z"),300);
        int taskID = task.getTaskId();
        historyManager.add(task);
        List<Task> historyBefore = historyManager.getHistory();
        historyManager.remove(taskID);
        List<Task> historyAfter = historyManager.getHistory();
        String taskId = Integer.toString(taskID);
        String taskParams = "[Task{status=NEW, taskName='My Task1', taskDescription='com.dam.tasks.Task description', taskId="+taskId+", startTime=2025-01-05 12:00:00, endTime=2025-01-05 17:00:00, duration=05:00}]";
        assertTrue(historyBefore.toString().equals(taskParams), "History size is empty");
        assertTrue(historyAfter.toString().equals("[]"), "History size is not empty");
    }


}