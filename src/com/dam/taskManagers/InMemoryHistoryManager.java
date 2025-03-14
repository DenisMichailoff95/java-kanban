package com.dam.taskManagers;

import com.dam.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10; // размер задан заданием
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == MAX_HISTORY_SIZE) {
            historyList.removeFirst(); // делаем подобие стека
        }
        historyList.add(task);
    }

    @Override
    public void clearHistory() {
        historyList.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void removeFromHistory(Task task) {
        historyList.remove(task);
    }

}
