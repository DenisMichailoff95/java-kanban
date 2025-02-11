import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task{
    private final Map<Integer, Subtask> epicSubtasksMap;

    Epic(Task task) {
        super(task);
        epicSubtasksMap = new HashMap<>();
    }

    Epic(TaskStatus taskStatus, String taskName, String taskDescription) {
        super(taskStatus, taskName, taskDescription);
        epicSubtasksMap = new HashMap<>();
    }

    public boolean hasSubtask(int subtaskId){
        if (epicSubtasksMap.containsKey(subtaskId)) {
            return true;
        } else {
            return false;
        }
    }
    public void addSubtask(Subtask subtask){
        epicSubtasksMap.put(subtask.getTaskId(),subtask);
    }

    public Subtask getSubtask(int subtaskId){
        return epicSubtasksMap.get(subtaskId);
    }

    public Map<Integer, Subtask> getSubtaskMap(){
        return epicSubtasksMap;
    }

    public void clearSubtask(){
        epicSubtasksMap.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                ": name=" + super.getTaskName() +
                ", description=" + super.getTaskDescription() +
                ", status=" + super.getTaskStatus() +
                ", epicId=" + super.getTaskId() +
                ", epicSubtasksMap=" + epicSubtasksMap.toString() +
                '}';
    }
}
