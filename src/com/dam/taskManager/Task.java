package com.dam.taskManager;

import java.util.Objects;

public class Task {

    private TaskStatus taskStatus;
    private String taskName;
    private String taskDescription;
    private final int taskId;

    public Task(Task task) {
        this.taskStatus = task.taskStatus;
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskId = this.hashCode();
    }

    public Task(TaskStatus taskStatus, String taskName, String taskDescription) {
        this.taskStatus = taskStatus;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = this.hashCode();
    }

    @Override
    public String toString() {
        return "com.dam.taskManager.Task{" +
                taskName +
                ": description=" + taskDescription +
                ", status=" + taskStatus +
                ", taskId=" + taskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, getClass());
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

}
