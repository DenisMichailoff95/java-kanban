package com.dam.taskManagers;

import com.dam.enums.TaskStatus;
import com.dam.enums.TaskType;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.time.Instant;
import java.util.List;

import java.io.*;
import java.util.Map;

import com.dam.exceptions.ManagerSaveException;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;
    private static final Path filePath = Path.of("resources/base.csv");

    public FileBackedTaskManager() {
        super();
        this.file = new File(filePath.toString());
    }

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager(InMemoryTaskManager inMemoryTaskManager, File file) {
        super(inMemoryTaskManager);
        this.file = file;
    }

    public void addTaskFromFile(Task task) {
        super.addTask(task);
    }

    public void addEpicFromFile(Epic epic) {
        super.addEpic(epic);
    }

    public void addSubtaskFromFile(Subtask subtask) {
        super.addSubtask(subtask);
    }

    @Override
    public void addTask(TaskStatus taskStatus, String taskName, String taskDescription, Instant startTime, long duration) {
        super.addTask(taskStatus, taskName, taskDescription, startTime, duration);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(TaskStatus taskStatus, String taskName, String taskDescription) {
        super.addEpic(taskStatus, taskName, taskDescription);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    public void addEpic(Task task) {
        super.addEpic(task);
        save();
    }

    @Override
    public void addSubtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId, Instant startTime, long duration) {
        super.addSubtask(taskStatus, taskName, taskDescription, epicId, startTime, duration);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int epicID) {
        super.deleteEpicByID(epicID);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
        super.deleteSubtaskByID(id);
        save();
    }

    @Override
    public Task updateTask(Task task) {
        var updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic oldEpic, Epic newEpic) {
        var updatedEpic = super.updateEpic(oldEpic, newEpic);
        save();
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        var updatedSubtask = super.updateSubtask(oldSubtask, newSubtask);
        save();
        return updatedSubtask;
    }

    @Override
    public Task getTaskByID(int id) {
        var savedTask = super.getTaskByID(id);
        save();
        return savedTask;
    }

    @Override
    public Epic getEpicByID(int id) {
        var savedEpic = super.getEpicByID(id);
        save();
        return savedEpic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        var savedSubtask = super.getSubtaskByID(id);
        save();
        return savedSubtask;
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {

            StringBuilder sb = new StringBuilder();
            String header = "id,type,name,status,description,startTime,duration,endTime,epic" + "\n";

            sb.append(header);

            for (Task task : getTasks()) {
                sb.append(toString(task)).append("\n");
            }

            for (Epic epic : getEpics()) {
                sb.append(toString(epic)).append("\n");

                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();

                for (Subtask subtask : epicSubtasksMap.values()) {
                    sb.append(toString(subtask)).append("\n");
                }
            }
            fileWriter.write(sb.toString());

        } catch (IOException e) {

            throw new ManagerSaveException("Ошибка записи в файл");

        }

    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%s,%s,%d", task.getTaskId(), TaskType.SUBTASK, task.getTaskName(), task.getTaskStatus(), task.getTaskDescription(), task.getStartTime(), task.getDuration(), subtask.getEpicId());
        } else if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s,%s,%s,", task.getTaskId(), TaskType.EPIC, task.getTaskName(), task.getTaskStatus(), task.getTaskDescription(), task.getStartTime(), task.getDuration());
        } else {
            return String.format("%d,%s,%s,%s,%s,%s,%s,", task.getTaskId(), TaskType.TASK, task.getTaskName(), task.getTaskStatus(), task.getTaskDescription(), task.getStartTime(), task.getDuration());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addEpicFromFile((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtaskFromFile((Subtask) task);
                } else {
                    manager.addTaskFromFile(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки менеджера из файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        String status = fields[3];
        String description = fields[4];
        Instant startTime = Instant.parse(fields[5]);
        int duration = Integer.parseInt(fields[6]);
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        int epicId = type.equals("SUBTASK") ? Integer.parseInt(fields[7]) : -1;

        Task task;
        switch (type) {
            case "TASK":
                task = new Task(taskStatus, name, description, id, startTime, duration);
                break;
            case "EPIC":
                task = new Epic(taskStatus, name, description, id, startTime, duration);
                break;
            case "SUBTASK":
                task = new Subtask(taskStatus, name, description, epicId, startTime, duration);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        return task;
    }

}