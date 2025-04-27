package task;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.*;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetSubtaskName() {
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.tasks.Epic", "com.dam.tasks.Task deskription", Instant.parse("2025-01-05T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addEpic(myEpic);
        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.tasks.Task deskription", myEpic.getTaskId(), Instant.parse("2025-01-05T12:00:00.000000000Z"),Duration.ofHours(1));
        taskManager.addSubtask(subTask1);
        assertEquals("My subTask1", subTask1.getTaskName());
    }

}