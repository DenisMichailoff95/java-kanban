package test;

import com.dam.taskManager.*;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    @org.junit.jupiter.api.Test
    void testGetSubtaskName() {
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.taskManager.Epic", "com.dam.taskManager.Task deskription");
        taskManager.addEpic(myEpic);
        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.taskManager.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask1);
        assertEquals("My subTask1", subTask1.getTaskName());
    }


}