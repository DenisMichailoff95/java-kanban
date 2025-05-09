package com.dam.http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.dam.exceptions.OverlapException;
import com.dam.tasks.Subtask;
import com.dam.taskManagers.TaskManager;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private void handleGet(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            response = gson.toJson(taskManager.getSubtasks());
            sendText(httpExchange, response, 200);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                Subtask subtask = taskManager.getSubtaskByID(id);
                if (subtask != null) {
                    response = gson.toJson(subtask);
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
            Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
            if (taskManager.getSubtaskByID(subtask.getTaskId()) != null) {
                Subtask oldSubtask = taskManager.getSubtaskByID(subtask.getTaskId());
                taskManager.updateSubtask(oldSubtask, subtask);
                sendText(httpExchange, "success", 200);
            } else {
                taskManager.addSubtask(subtask);
                sendText(httpExchange, Integer.toString(subtask.getTaskId()), 201);
            }
        } catch (OverlapException oe) {
            sendHasInteractions(httpExchange);
        } catch (JsonSyntaxException e) {
            sendNotFound(httpExchange);
        }
    }

    private void handleDelete(HttpExchange httpExchange, String[] path) throws IOException {
        if (path.length == 2) {
            sendNotFound(httpExchange);
        } else {
            try {
                int id = Integer.parseInt(path[2]);
                taskManager.deleteSubtaskByID(id);
                sendText(httpExchange, "success", 200);
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                sendNotFound(httpExchange);
            }
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