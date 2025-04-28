package com.dam;

import com.dam.enums.TaskStatus;
import com.dam.http.HttpTaskServer;
import com.dam.taskManagers.Managers;
import com.dam.taskManagers.TaskManager;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ServerStarter {

    private static HttpTaskServer httpTaskManager;

    public static void main(String[] args) {

        try {
            httpTaskManager = Managers.getHttpTaskServer(8000); // на 8080 у меня jankins
        } catch (IOException e) {
            System.out.println(e.getCause() + ":" + e.getMessage());
        }

        addTasks();
        httpTaskManager.start();
//        httpTaskManager.stop(300);
    }

    public static void addTasks() {
        TaskManager taskManager = httpTaskManager.getTaskManager();

        Task task = new Task(TaskStatus.NEW, "TaskName", "Task description", Instant.parse("2025-01-01T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addTask(task);
        Task task2 = new Task(TaskStatus.NEW, "TaskName2", "Task description", Instant.parse("2025-01-01T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addTask(task2);
        Task task3 = new Task(TaskStatus.NEW, "TaskName3", "Task description", Instant.parse("2025-01-01T12:00:00.000000000Z"), Duration.ofHours(1));
        taskManager.addTask(task3);

        Epic myEpic = new Epic(TaskStatus.NEW, "My Epic1", "Task description", Instant.parse("2025-01-02T12:00:00.000000000Z"), Duration.ofHours(2));
        taskManager.addEpic(myEpic);

        Subtask subTask1 = new Subtask(TaskStatus.NEW, "My subTask1", "description", myEpic.getTaskId(), Instant.parse("2025-01-03T12:00:00.000000000Z"), Duration.ofHours(3));
        taskManager.addSubtask(subTask1);
        Subtask subTask2 = new Subtask(TaskStatus.NEW, "My subTask2", "description", myEpic.getTaskId(), Instant.parse("2025-01-04T12:00:00.000000000Z"), Duration.ofHours(4));
        taskManager.addSubtask(subTask2);
        Subtask subTask3 = new Subtask(TaskStatus.NEW, "My subTask3", "description", myEpic.getTaskId(), Instant.parse("2025-02-05T12:00:00.000000000Z"), Duration.ofHours(5));
        taskManager.addSubtask(subTask3);

        Epic myEpic2 = new Epic(TaskStatus.NEW, "My Epic2", "Task description2", Instant.parse("2025-01-06T12:00:00.000000000Z"), Duration.ofHours(6));
        taskManager.addEpic(myEpic2);

        Subtask subTask12 = new Subtask(TaskStatus.NEW, "My subTask1", "Task description", myEpic2.getTaskId(), Instant.parse("2025-01-07T12:00:00.000000000Z"), Duration.ofHours(7));
        taskManager.addSubtask(subTask12);
        Subtask subTask22 = new Subtask(TaskStatus.NEW, "My subTask2", "description", myEpic2.getTaskId(), Instant.parse("2025-01-08T12:00:00.000000000Z"), Duration.ofHours(8));
        taskManager.addSubtask(subTask22);
        Subtask subTask32 = new Subtask(TaskStatus.NEW, "My subTask3", "description", myEpic2.getTaskId(), Instant.parse("2025-01-09T12:00:00.000000000Z"), Duration.ofHours(9));
        taskManager.addSubtask(subTask32);

    }

    // tests done
    // method                             statusOfTest
    //localhost:8000/tasks                GET       +
    //localhost:8000/tasks/470497308      GET       +
    //localhost:8000/tasks/               Post      +
    //localhost:8000/tasks/470497308      DELETE    +

    //localhost:8000/subtasks             GET       +
    //localhost:8000/subtasks/901277371   GET       +
    //localhost:8000/subtasks/901277371   DELETE    +

    //localhost:8000/epics                GET       +
    //localhost:8000/epics/-173942281     GET       +
    //localhost:8000/epics/-173942281     DELETE    +

    //localhost:8000/prioritized          GET       +

    //localhost:8000/history              GET       +

}
