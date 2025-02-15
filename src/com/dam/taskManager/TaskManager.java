package com.dam.taskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    public void addTask(TaskStatus taskStatus, String taskName, String taskDescription) {
        Task task = new Task(taskStatus, taskName, taskDescription);
        tasks.put(task.getTaskId(), task);
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void addEpic(TaskStatus taskStatus, String taskName, String taskDescription) {
        Epic epic = new Epic(taskStatus, taskName, taskDescription);
        epics.put(epic.getTaskId(), epic);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void addEpic(Task task) {
        Epic epic = new Epic(task);
        epics.put(task.getTaskId(), epic);
    }

    public void addSubtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId) {
        Subtask subtask = new Subtask(taskStatus, taskName, taskDescription, epicId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        adjustingEpicStatus(epicId);
    }

    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        adjustingEpicStatus(subtask.getEpicId());
    }

    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        return task;
    }
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        return epic;
    }

    public Subtask getSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Subtask subtask = epic.getSubtask(subtaskId);
                return subtask;
            }
        }
        return null;
    }

    public Task updateTask(Task task) {
        Integer taskID = task.getTaskId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    public Epic updateEpic(Epic oldEpic, Epic newEpic) {
        Integer taskID = oldEpic.getTaskId();
        if (taskID == null || !epics.containsKey(taskID)) {
            return null;
        }
        if (epics.containsKey(taskID)) {
            epics.replace(taskID, newEpic);
            return newEpic;
        }
        return null;
    }

    public Subtask updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        Integer subtaskId = newSubtask.getTaskId();
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();
                epicSubtasksMap.replace(oldSubtask.getTaskId(), newSubtask);
                return newSubtask;
            }
        }
        return oldSubtask;
    }

    public void deleteTaskByID(int id) {
        Task task = tasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        Epic epic = epics.remove(id);
    }

    public void deleteSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();
                epicSubtasksMap.remove(subtaskId);
            }
        }
        adjustingEpicStatus(subtaskId);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubtasks() {

        for (Epic epic : epics.values()) {
            epic.clearSubtask();
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void adjustingEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean newStatus = false;
        boolean doneStatus = false;
        boolean inProgressStatus = false;

        Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();

        for (Subtask subtask : epicSubtasksMap.values()) {
            if (subtask.getTaskStatus() == TaskStatus.NEW) {
                newStatus = true;
            } else if (subtask.getTaskStatus() == TaskStatus.DONE) {
                doneStatus = true;
            } else if (subtask.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                inProgressStatus = true;
            }
        }

        if (newStatus == true && (doneStatus == false && inProgressStatus == false)) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (doneStatus == true && (newStatus == false && inProgressStatus == false)) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "com.dam.taskManager.TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                '}';
    }

}
