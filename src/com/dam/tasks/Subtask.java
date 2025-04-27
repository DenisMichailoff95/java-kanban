package com.dam.tasks;

import com.dam.enums.TaskStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(Task task, int epicId) {
        super(task);
        this.epicId = epicId;

    }

    public Subtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId, Instant startTime, Duration duration) {
        super(taskStatus, taskName, taskDescription, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId, int id, Instant startTime, Duration duration) {
        super(taskStatus, taskName, taskDescription, id, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "status=" + status +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", endTime=" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(getEndTime().atOffset(ZoneOffset.UTC)) +
                ", duration=" + duration +
                '}';
    }
}
