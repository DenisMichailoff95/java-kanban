package com.dam;

import com.dam.enums.TaskStatus;
import com.dam.taskManagers.*;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

import java.io.File;

public class Main {

    private static File file = new File("resources/base.csv");
    private static final InMemoryTaskManager taskManagerIM = Managers.getDefault();
    private static FileBackedTaskManager taskManager = null;
//    private static FileBackedTaskManager taskManager = Managers.getDefaultFileBackedManager();


    public static void main(String[] args) {

        taskManager = FileBackedTaskManager.loadFromFile(file);
        addTasksAndView();
        printAll();
        printHistory();
        updateEpic();
    }

    public static void printAll() {
        for (Task t : taskManager.getTasks()) {
            System.out.println(t.toString());
        }

        for (Epic e : taskManager.getEpics()) {
            System.out.println(e.toString());
        }
    }

    public static void addTasksAndView() {
        Task task = new Task(TaskStatus.NEW, "TaskName", "Task description");
        System.out.println(task.toString());
        taskManager.addTask(task);
        System.out.println(taskManager.getTaskByID(task.getTaskId()).toString());

        Epic myEpic = new Epic(TaskStatus.NEW, "My Epic1", "Task description");

        taskManager.addEpic(myEpic);
        System.out.println(myEpic);

        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "description", myEpic.getTaskId());
        taskManager.addSubtask(subTask1);
        Subtask subTask2 = new Subtask(TaskStatus.NEW, "My subTask2", "description", myEpic.getTaskId());
        taskManager.addSubtask(subTask2);
        Subtask subTask3 = new Subtask(TaskStatus.NEW, "My subTask3", "description", myEpic.getTaskId());
        taskManager.addSubtask(subTask3);

        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));

        Epic myEpic2 = new Epic(TaskStatus.NEW, "My Epic2", "Task description2");
        taskManager.addEpic(myEpic2);
        System.out.println(myEpic2);

        Subtask subTask12 = new Subtask(TaskStatus.NEW, "My subTask1", "Task description", myEpic2.getTaskId());
        taskManager.addSubtask(subTask12);
        Subtask subTask22 = new Subtask(TaskStatus.NEW, "My subTask2", "description", myEpic2.getTaskId());
        taskManager.addSubtask(subTask22);
        Subtask subTask32 = new Subtask(TaskStatus.NEW, "My subTask3", "description", myEpic2.getTaskId());
        taskManager.addSubtask(subTask32);

        System.out.println(myEpic);
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));

        taskManager.updateEpic(myEpic, myEpic2);
        System.out.println(taskManager.getEpicByID(myEpic.getTaskId()));
        System.out.println(taskManager.getEpicByID(myEpic2.getTaskId()));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());

    }

    public static void updateEpic() {

    }

    public static void printHistory() {

        System.out.println("printRequestHistory");
        taskManager.printRequestHistory();
        System.out.println("taskManager.getHistory()");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}