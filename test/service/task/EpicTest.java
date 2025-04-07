package task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.*;
import com.dam.tasks.Epic;


class EpicTest {

    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @Test
    void testGetEpicName() {
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.tasks.Epic", "com.dam.tasks.Task deskription");
        taskManager.addEpic(myEpic);
        assertEquals("My com.dam.tasks.Epic", myEpic.getTaskName());
    }

}