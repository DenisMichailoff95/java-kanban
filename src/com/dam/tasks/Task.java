package com.dam.tasks;

import com.dam.enums.TaskStatus;

import java.util.Objects;

public class Task {

    private TaskStatus status;
    private String taskName;
    private String taskDescription;
    private final int taskId;

    public Task(Task task) {
        this.status = task.status;
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskId = this.hashCode();
    }

    public Task(TaskStatus taskStatus, String taskName, String taskDescription) {
        this.status = status;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = this.hashCode();
    }

    @Override
    public String toString() {
        return "Task{" +
                taskName +
                ": description=" + taskDescription +
                ", status=" + status +
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
        return status;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.status = status;
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
