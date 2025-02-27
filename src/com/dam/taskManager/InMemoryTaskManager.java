package com.dam.taskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {


    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addTask(TaskStatus taskStatus, String taskName, String taskDescription) {
        Task task = new Task(taskStatus, taskName, taskDescription);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addEpic(TaskStatus taskStatus, String taskName, String taskDescription) {
        Epic epic = new Epic(taskStatus, taskName, taskDescription);
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void addEpic(Task task) {
        Epic epic = new Epic(task);
        epics.put(task.getTaskId(), epic);
    }

    @Override
    public void addSubtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId) {
        Subtask subtask = new Subtask(taskStatus, taskName, taskDescription, epicId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        adjustingEpicStatus(epicId);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        adjustingEpicStatus(subtask.getEpicId());
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Subtask subtask = epic.getSubtask(subtaskId);
                if (subtask != null) {
                    historyManager.add(subtask);
                }
                return subtask;
            }
        }
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskID = task.getTaskId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    @Override
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

    @Override
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

    @Override
    public void deleteTaskByID(int id) {
        Task task = tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) {
        Epic epic = epics.remove(id);
    }

    @Override
    public void deleteSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();
                epicSubtasksMap.remove(subtaskId);
            }
        }
        adjustingEpicStatus(subtaskId);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {

        for (Epic epic : epics.values()) {
            epic.clearSubtask();
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
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

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
