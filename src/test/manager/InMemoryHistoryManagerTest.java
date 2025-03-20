package test.manager;

import java.util.List;

import com.dam.enums.TaskStatus;
import com.dam.tasks.Task;
import com.dam.taskManagers.Managers;
import com.dam.taskManagers.HistoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    @Test
    void newHistoryManagerTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "historyManager is NULL");
    }

    @Test
    void checkRepeatedRequestHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description");
        final int sizeForChecking = 1;
        final int sizeForRepeats = 10;
        for (int i = 0; i <= sizeForRepeats; i++) {
            historyManager.add(task);
        }
        List<Task> exampleOfRequestHistoryList = historyManager.getHistory();

        assertEquals(sizeForRepeats, exampleOfRequestHistoryList.size(), "The repeat limit does not work");
    }

    @Test
    void addInHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TaskStatus.NEW, "My Task1", "com.dam.tasks.Task description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is not NULL");
        assertEquals(1, history.size(), "History size is NULL");
    }
}
