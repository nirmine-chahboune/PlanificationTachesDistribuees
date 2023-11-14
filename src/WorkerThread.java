import java.io.IOException;
import java.util.concurrent.BlockingQueue;

 public class WorkerThread implements Runnable {
 private BlockingQueue <Task > taskQueue ;

         public WorkerThread(BlockingQueue <Task > taskQueue ) {
         this . taskQueue = taskQueue ;
         }

         @Override
      public void run () {
         while ( true ) {
             try {
                 Task task = taskQueue . take ();
                 task . execute ();
                 } catch ( InterruptedException e) {
                 break ;
                 } catch (IOException e) {
                 throw new RuntimeException(e);
             }
         }
         }
}