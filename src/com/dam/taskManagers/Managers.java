package com.dam.taskManagers;

import com.dam.http.HttpTaskServer;
import com.dam.http.adapters.DurationAdapter;
import com.dam.http.adapters.InstantAdapter;
import com.dam.http.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedManager() {
        return new FileBackedTaskManager();
    }

    public static HttpTaskServer getHttpTaskServer(int port) throws IOException {
        return new HttpTaskServer(new InMemoryTaskManager(), port);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

}
