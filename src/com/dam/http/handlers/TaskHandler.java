package com.dam.http.handlers;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.dam.exceptions.OverlapException;
import com.dam.tasks.Task;
import com.dam.taskManagers.TaskManager;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    //
    private void handleGet(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            try {
                response = gson.toJson(taskManager.getTasks());
            } catch (JsonIOException jio) {
                System.out.println(jio.getMessage() + " : " + jio.getCause());
            }

            sendText(httpExchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                Task task = taskManager.getTaskByID(id);
                if (task != null) {
                    response = gson.toJson(task);
                    sendText(httpExchange, response, 200);
                } else {
                    sendNotFound(httpExchange);
                }
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                sendNotFound(httpExchange);
            }
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String bodyRequest = readText(httpExchange);
        if (bodyRequest.isEmpty()) {
            sendNotFound(httpExchange);
            return;
        }
        try {
            Task task = gson.fromJson(bodyRequest, Task.class);
            if (taskManager.getTaskByID(task.getTaskId()) != null) {
                taskManager.updateTask(task);
                sendText(httpExchange, "success", 201);
            } else {
                taskManager.addTask(task);
                sendText(httpExchange, Integer.toString(task.getTaskId()), 201);
            }
        } catch (OverlapException oe) {
            sendHasInteractions(httpExchange);
        } catch (JsonSyntaxException e) {
            sendNotFound(httpExchange);
        }
    }

    private void handleDelete(HttpExchange httpExchange, String[] path) throws IOException {
        try {
            int id = Integer.parseInt(path[2]);
            taskManager.deleteTaskByID(id);
            sendText(httpExchange, "success", 200);
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            sendNotFound(httpExchange);
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().getPath().split("/");


        switch (method) {
            case "GET" -> handleGet(httpExchange, path);
            case "POST" -> handlePost(httpExchange);
            case "DELETE" -> handleDelete(httpExchange, path);
            default -> sendNotFound(httpExchange);
        }
    }
}