import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskSchedulerServer extends UnicastRemoteObject implements TaskScheduler{
    private static final int NUM_WORKER_THREADS = 8;
    private ExecutorService threadPool ;
    private TaskQueue taskQueue ;

    public TaskSchedulerServer() throws RemoteException {
        super();
        threadPool = Executors . newFixedThreadPool ( NUM_WORKER_THREADS );
        taskQueue = new TaskQueue ();
    }

    public void start() throws InterruptedException {
        // Start worker threads to process tasks from the queue
        for (int i = 0; i < NUM_WORKER_THREADS ; i ++) {
            threadPool . submit ( new WorkerThread ((BlockingQueue<Task>) taskQueue));
        }

        // Wait for tasks to be submitted and processed
        while ( true ) {
            Task task = (Task) ((BlockingQueue<?>) taskQueue).take ();
            int id = taskQueue.add(task);
            task.setId(id);
        }
    }


    @Override
    public int submitTask(Task task) throws RemoteException, InterruptedException {
        task.setTaskQueue(taskQueue);
        int taskId = taskQueue.add(task);
        return taskId;
    }


    public TaskResult getResult(int taskId) throws RemoteException {
        TaskResult result = taskQueue.getResult(taskId);
        return result;
    }

    public static void main(String[] args) {
        try {
            TaskSchedulerServer server = new TaskSchedulerServer();
            LocateRegistry.createRegistry(1091);

            Naming.rebind("rmi://localhost:1091/TaskSchedulerServer",server);
            System.out.println("Server started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int submitTask(MatrixMultiplicationTask matrixMultiplicationTask) throws RemoteException, InterruptedException {
        matrixMultiplicationTask.setTaskQueue(taskQueue);
        int taskId = taskQueue.add(matrixMultiplicationTask);
        return taskId;
    }
}
