package com.dam.taskManagers;

import com.dam.enums.TaskStatus;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

import java.time.Instant;
import java.util.List;

public interface TaskManager {
    void addTask(TaskStatus taskStatus, String taskName, String taskDescription, Instant startTime, long duration);

    void addTask(Task task);

    void addEpic(TaskStatus taskStatus, String taskName, String taskDescription);

    void addEpic(Epic epic);

    void addEpic(Task task);

    void addSubtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId, Instant startTime, long duration);

    void addSubtask(Subtask subtask);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int subtaskId);

    Task updateTask(Task task);

    Epic updateEpic(Epic oldEpic, Epic newEpic);

    Subtask updateSubtask(Subtask oldSubtask, Subtask newSubtask);

    void deleteTaskByID(int id);

    void deleteEpicByID(int id);

    void deleteSubtaskByID(int subtaskId);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Task> getHistory();
}
