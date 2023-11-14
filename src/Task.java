import java.io.IOException;
import java.io.Serializable;

public interface Task extends Runnable, Serializable {
 void setId(int id);
 int getId();
 int execute () throws IOException;
 void setTaskQueue(TaskQueue taskQueue);

    TaskResult getResult() throws InterruptedException;
}