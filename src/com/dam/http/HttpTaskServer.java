package com.dam.http;

import com.sun.net.httpserver.HttpServer;
import com.dam.http.handlers.*;
import com.dam.taskManagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager, int port) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));

    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void main() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен");
    }

    public void stop(int delay) {
        httpServer.stop(delay);
        System.out.println("HTTP-сервер остановлен");
    }
}