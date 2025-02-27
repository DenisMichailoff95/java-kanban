package test;

import com.dam.taskManager.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetEpicName() {
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.taskManager.Epic", "com.dam.taskManager.Task deskription");
        taskManager.addEpic(myEpic);
        assertEquals("My com.dam.taskManager.Epic", myEpic.getTaskName());
    }

}