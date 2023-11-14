import java.io.Serializable;

public class TaskResult implements Serializable {
 private int taskId ;
    private boolean completed = false;
 private Object result ;
         public TaskResult(int taskId , Object result) {
         this . taskId = taskId ;
         this . result = result ;
        }

         public int getTaskId () {
         return taskId ;
         }

         public Object getResult () {
             return result ;
         }
    public synchronized void setCompleted(boolean completed) {
        this.completed = completed;
        notifyAll();
    }
 }