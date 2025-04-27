package com.dam.tasks;

import com.dam.enums.TaskStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected TaskStatus status;
    protected String taskName;
    protected String taskDescription;
    protected final int taskId;
    protected Instant startTime;
    protected Duration duration; // можно использовать класс Duration, пока точность не нужна можно без него

    public Task(Task task) {
        this.status = task.status;
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskId = this.hashCode();
        this.startTime = task.startTime;
        this.duration = task.duration;
    }

    public Task(TaskStatus taskStatus, String taskName, String taskDescription, Instant startTime, Duration duration) {
        this.status = taskStatus;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = this.hashCode();
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(TaskStatus taskStatus, String taskName, String taskDescription, int taskId, Instant startTime, Duration duration) {
        this.status = taskStatus;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        final byte SECONDS_IN_ONE_MINUTE = 60;
        return startTime.plusSeconds(duration.toSeconds());
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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

    @Override
    public String toString() {

        long durationHH = duration.toHours();
        long durationMM = duration.toMinutesPart();
        long durationSS = duration.toSecondsPart();

        return "Task{" +
                "status=" + status +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", startTime=" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(startTime.atOffset(ZoneOffset.UTC)) +
                ", endTime=" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(getEndTime().atOffset(ZoneOffset.UTC)) +
                ", duration=" + String.format("%02d:%02d:%02d", durationHH, durationMM, durationSS) +
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


}
