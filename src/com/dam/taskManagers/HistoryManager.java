package com.dam.taskManagers;

import com.dam.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void clearHistory();

    List<Task> getHistory();

    void removeFromHistory(Task task);
}
