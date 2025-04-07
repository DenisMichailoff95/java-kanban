package com.dam.taskManagers;

import com.dam.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void remove(Task task);

    void clearHistory();

    List<Task> getHistory();

    void printRequestHistory();

}
