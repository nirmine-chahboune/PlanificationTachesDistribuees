import java.io.Serializable;

public class MatrixMultiplicationTask implements Task, Serializable {
    private int[][] matrix1;
    private int[][] matrix2;
    private TaskQueue taskQueue;
    private int id;

    public MatrixMultiplicationTask(int id ,int[][] matrix1, int[][] matrix2) {
        this.id=id;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int execute() {
        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;
        int columns2 = matrix2[0].length;
        int[][] result = new int[rows1][columns2];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns2; j++) {
                for (int k = 0; k < columns1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        TaskResult taskResult = new TaskResult(id, result);
        taskQueue.addResult(taskResult);
        return id;
    }

    @Override
    public void setTaskQueue(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public TaskResult getResult() throws InterruptedException {
        return taskQueue.getResult(id);
    }

    @Override
    public void run() {
        execute();
    }
}