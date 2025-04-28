package com.dam.taskManagers;

import com.dam.enums.TaskStatus;
import com.dam.exceptions.OverlapException;
import com.dam.tasks.Epic;
import com.dam.tasks.Subtask;
import com.dam.tasks.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager, Comparator<Task> {


    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private Set<Task> prioritizedTasks = new TreeSet<>(this);

    InMemoryTaskManager() {

    }

    InMemoryTaskManager(InMemoryTaskManager inMemoryTaskManager) {
    }

    // получение приоритетного списка + его конвертация из TreeSet в ArrayList
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // добавление & проверка
    private void addToPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
        checkIntersections();
    }

    // проверка нет ли пересечения
    private void checkIntersections() {
        var prioritizedTasks = getPrioritizedTasks();
        for (int i = 1; i < prioritizedTasks.size(); i++) {
            var prioritizedTask = prioritizedTasks.get(i);
            if (prioritizedTask.getStartTime().isBefore(prioritizedTasks.get(i - 1).getEndTime()))
                throw new OverlapException("Найдено пересечение между " + prioritizedTasks.get(i) + " и " + prioritizedTasks.get(i - 1));
        }
    }

    public void printPrioritizedTasks() {
        System.out.println("ПРИОРИТЕЗАЦИЯ ЗАДАЧ: ");
        prioritizedTasks.forEach(System.out::println);
    }

    @Override // сравнение тасков по getStartTime()
    public int compare(Task o1, Task o2) {
        Optional<Instant> time1 = Optional.ofNullable(o1.getStartTime());
        Optional<Instant> time2 = Optional.ofNullable(o2.getStartTime());

        if (time1.isPresent() && time2.isPresent()) {
            return time1.get().compareTo(time2.get());
        } else if (time1.isPresent()) {
            return -1; // null считается "больше"
        } else if (time2.isPresent()) {
            return 1;  // null считается "больше"
        } else {
            return 0;  // оба null
        }
    }


    @Override
    public void addTask(TaskStatus taskStatus, String taskName, String taskDescription, Instant startTime, Duration duration) {
        Task task = new Task(taskStatus, taskName, taskDescription, startTime, duration);
        addToPrioritizedTasks(task);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addTask(Task task) {
        addToPrioritizedTasks(task);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addEpic(TaskStatus taskStatus, String taskName, String taskDescription) {  // пустой эпик не должен длится и иметь дату старта
        Epic epic = new Epic(taskStatus, taskName, taskDescription, null, null); // эпики не смотрим на приоритизацию, они через сабтаски видны
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void addEpic(Task task) {
        Epic epic = new Epic(task);
        epics.put(task.getTaskId(), epic);
    }

    @Override
    public void addSubtask(TaskStatus taskStatus, String taskName, String taskDescription, int epicId, Instant startTime, Duration duration) {
        Subtask subtask = new Subtask(taskStatus, taskName, taskDescription, epicId, startTime, duration);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
        addToPrioritizedTasks(subtask);
        adjustingEpicStatus(epicId);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
        addToPrioritizedTasks(subtask);
        adjustingEpicStatus(subtask.getEpicId());
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Subtask subtask = epic.getSubtask(subtaskId);
                if (subtask != null) {
                    historyManager.add(subtask);
                }
                return subtask;
            }
        }
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskID = task.getTaskId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic oldEpic, Epic newEpic) {
        Integer taskID = oldEpic.getTaskId();
        if (taskID == null || !epics.containsKey(taskID)) {
            return null;
        }
        if (epics.containsKey(taskID)) {
            epics.replace(taskID, newEpic);
            return newEpic;
        }
        return null;
    }

    @Override
    public Subtask updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        Integer subtaskId = newSubtask.getTaskId();
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();
                epicSubtasksMap.replace(oldSubtask.getTaskId(), newSubtask);
                return newSubtask;
            }
        }
        return oldSubtask;
    }

    @Override
    public void deleteTaskByID(int id) {
        historyManager.remove(tasks.get(id));
        Task task = tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) {
        historyManager.remove(epics.get(id));
        Epic epic = epics.remove(id);
    }

    @Override
    public void deleteSubtaskByID(int subtaskId) {
        for (Epic epic : epics.values()) {
            if (epic.hasSubtask(subtaskId)) {
                Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();
                historyManager.remove(epicSubtasksMap.get(subtaskId));
                epicSubtasksMap.remove(subtaskId);
            }
        }
        adjustingEpicStatus(subtaskId);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {

        for (Epic epic : epics.values()) {
            epic.clearSubtask();
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasks() {

        List<Subtask> st = new ArrayList();

        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtaskMap().values()) {
                    st.add(subtask);
            }
        }
        return st;
    }

    public void adjustingEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean newStatus = false;
        boolean doneStatus = false;
        boolean inProgressStatus = false;

        Map<Integer, Subtask> epicSubtasksMap = epic.getSubtaskMap();

        Instant startTime = null;
        Instant endTime = null;
        Duration epicDuration = null;

        for (Subtask subtask : epicSubtasksMap.values()) {

            if (startTime != null && endTime != null) {
                if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
                if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
                epicDuration.plus(subtask.getDuration());
            } else {
                startTime = subtask.getStartTime();
                endTime = subtask.getEndTime();
                epicDuration = subtask.getDuration();
            }


            if (subtask.getTaskStatus() == TaskStatus.NEW) {
                newStatus = true;
            } else if (subtask.getTaskStatus() == TaskStatus.DONE) {
                doneStatus = true;
            } else if (subtask.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                inProgressStatus = true;
            }
        }

        if (newStatus == true && (doneStatus == false && inProgressStatus == false)) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (doneStatus == true && (newStatus == false && inProgressStatus == false)) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime); // сохраняем, чтобы пересчитывать только при изменении
        epic.setDuration(epicDuration);

    }

    @Override
    public String toString() {
        return "TaskManager{" + "tasks=" + tasks + ", epics=" + epics + '}';
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void printRequestHistory() {
        historyManager.printRequestHistory();
    }

}
