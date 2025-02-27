package com.dam.taskManager;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(Task task, int epicId) {
        super(task);
        this.epicId = epicId;
    }

    public Subtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId) {
        super(taskStatus, taskName, taskDescription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "com.dam.taskManager.Subtask{" +
                "epicId=" + epicId +
                ": subtaskName=" + super.getTaskName() +
                ": description=" + super.getTaskDescription() +
                ", status=" + super.getTaskStatus() +
                ", taskId=" + super.getTaskId() +
                '}';
    }
}
