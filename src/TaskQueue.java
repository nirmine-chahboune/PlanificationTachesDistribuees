import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class TaskQueue implements Serializable {
    private ConcurrentHashMap<Integer, TaskResult> results;
    private int taskIdCounter = 0;
    public void setResult(int taskId, int result) {
        results.put(taskId, new TaskResult(taskId, result));
    }

    public TaskQueue() {
        results = new ConcurrentHashMap<>();
    }



    public TaskResult getResult(int taskId) {
        return results.get(taskId);
    }

    public int add(Task task) {
        task.setId(++taskIdCounter);
        new Thread(task).start();
        return taskIdCounter;
    }


    public void addResult(TaskResult result) {
        result.setCompleted(true);
        results.put(result.getTaskId(), result);
    }
}
