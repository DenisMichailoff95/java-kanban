package com.dam;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.*;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

public class Main {

    private static final InMemoryTaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {

        addTasksAndView();
        printHistory();

    }

    public static void addTasksAndView() {
        Task task = new Task(TaskStatus.NEW, "My com.dam.tasks.Task", "com.dam.tasks.Task deskription");
        taskManager.addTask(task);
        Epic myEpic = new Epic(TaskStatus.NEW, "My com.dam.tasks.Epic", "com.dam.tasks.Task deskription");

        taskManager.addEpic(myEpic);
        System.out.println(myEpic);

        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.tasks.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask1);
        Subtask subTask2 = new Subtask(TaskStatus.NEW, "My subTask2", "com.dam.tasks.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask2);
        Subtask subTask3 = new Subtask(TaskStatus.NEW, "My subTask3", "com.dam.tasks.Task deskription", myEpic.getTaskId());
        taskManager.addSubtask(subTask3);

        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));

        Epic myEpic2 = new Epic(TaskStatus.NEW, "My Epic2", "com.dam.tasks.Task deskription2");
        taskManager.addEpic(myEpic2);
        System.out.println(myEpic2);

        Subtask subTask12 = new Subtask(TaskStatus.NEW, "My subTask1", "com.dam.tasks.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask12);
        Subtask subTask22 = new Subtask(TaskStatus.NEW, "My subTask2", "com.dam.tasks.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask22);
        Subtask subTask32 = new Subtask(TaskStatus.NEW, "My subTask3", "com.dam.tasks.Task deskription", myEpic2.getTaskId());
        taskManager.addSubtask(subTask32);

        System.out.println(myEpic);
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));

        taskManager.updateEpic(myEpic, myEpic2);
        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());

    }

    public static void printHistory() {
        System.out.println("History");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}