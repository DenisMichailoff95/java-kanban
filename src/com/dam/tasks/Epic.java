package com.dam.tasks;

import com.dam.enums.TaskStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final Map<Integer, Subtask> epicSubtasksMap;

    protected Instant endTime;

    public Epic(Task task) {
        super(task);
        epicSubtasksMap = new HashMap<>();
    }

    public Epic(TaskStatus taskStatus, String taskName, String taskDescription, Instant startTime, Duration duration) {
        super(taskStatus, taskName, taskDescription, startTime, duration);
        epicSubtasksMap = new HashMap<>();
    }

    public Epic(TaskStatus taskStatus, String taskName, String taskDescription, int id, Instant startTime, Duration duration) {
        super(taskStatus, taskName, taskDescription, id, startTime, duration);
        epicSubtasksMap = new HashMap<>();
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    // обновление состояния эпика

    public boolean hasSubtask(int subtaskId) {
        return epicSubtasksMap.containsKey(subtaskId);
    }

    public void addSubtask(Subtask subtask) {
        epicSubtasksMap.put(subtask.getTaskId(), subtask);
    }

    public Subtask getSubtask(int subtaskId) {
        return epicSubtasksMap.get(subtaskId);
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return epicSubtasksMap;
    }

    public void clearSubtask() {
        epicSubtasksMap.clear();
    }

    @Override
    public String toString() {

        long durationHH = duration.toHours();
        long durationMM = duration.toMinutesPart();
        long durationSS = duration.toSecondsPart();

        return "Epic{" +
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
}
