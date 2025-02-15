import com.dam.taskManager.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task = new Task(TaskStatus.NEW, "My com.dam.taskManager.Task", "com.dam.taskManager.Task deskription");
        taskManager.addTask(task);
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.taskManager.Epic", "com.dam.taskManager.Task deskription");

        taskManager.addEpic(myEpic);
        //System.out.println(myEpic);

        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.taskManager.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask1);
        Subtask subTask2 = new Subtask(TaskStatus.NEW, "My subTask2", "com.dam.taskManager.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask2);
        Subtask subTask3 = new Subtask(TaskStatus.NEW, "My subTask3", "com.dam.taskManager.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask3);

        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));

        Epic myEpic2 = new Epic(TaskStatus.NEW, "My Epic2", "com.dam.taskManager.Task deskription2");
        taskManager.addEpic(myEpic2);
        //System.out.println(myEpic2);

        Subtask subTask12 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.taskManager.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask12);
        Subtask subTask22 = new Subtask(TaskStatus.NEW, "My subTask2", "com.dam.taskManager.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask22);
        Subtask subTask32 = new Subtask(TaskStatus.NEW, "My subTask3", "com.dam.taskManager.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask32);

        //System.out.println(myEpic);
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));


        taskManager.updateEpic(myEpic, myEpic2);
        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());

    }
}