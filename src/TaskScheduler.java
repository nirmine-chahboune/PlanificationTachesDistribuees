import java.rmi.Remote;
import java.rmi.RemoteException;


public interface TaskScheduler extends Remote {
    int submitTask(MatrixMultiplicationTask matrixMultiplicationTask) throws RemoteException, InterruptedException;

    int submitTask (Task task ) throws RemoteException, InterruptedException;

TaskResult getResult (int taskId ) throws RemoteException ;


}