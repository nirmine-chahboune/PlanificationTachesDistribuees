import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

public class ImageTask implements Task , Serializable {
    private File imagePath;
    private double[][] kernel;
    private TaskQueue taskQueue;

    private  int id;
    public ImageTask(File imagePath, double[][] kernel,int id) throws RemoteException {
        this.imagePath = imagePath;
        this.kernel = kernel;
        this.id=id;    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int execute() throws IOException {
        int[][] pixel ;
        pixel = imageToMatrix(imagePath);

        int[][] output = convolution2D(pixel, this.kernel);
        int height = output.length;
        int width = output[0].length;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int value = output[x][y];
                int rgb = (value << 16) | (value << 8) | value;
                outputImage.setRGB(y, x, rgb);
            }
        }

        TaskResult taskResult = new TaskResult(id, outputImage);
        taskQueue.addResult(taskResult);
        ImageIO.write(outputImage, "png",new File("processed_" + id + ".jpg"));


        return id;
    }




    public static int[][] imageToMatrix(File imageFile) {
        int[][] imageMatrix = null;
        try {
            BufferedImage image = ImageIO.read(imageFile);
            int width = image.getWidth();
            int height = image.getHeight();
            imageMatrix = new int[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    imageMatrix[row][col] = image.getRGB(col, row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageMatrix;
    }

    public static int[][] convolution2D(int[][] image,double[][] kernel) {
        int[][] result = new int[image.length][image[0].length];

        int kernelSize = kernel.length;
        int kernelRadius = kernelSize / 2;

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                int sum = 0;

                for (int m = -kernelRadius; m <= kernelRadius; m++) {
                    for (int n = -kernelRadius; n <= kernelRadius; n++) {
                        int ii = i + m;
                        int jj = j + n;

                        if (ii >= 0 && ii < image.length && jj >= 0 && jj < image[0].length) {
                            sum += image[ii][jj] * kernel[m + kernelRadius][n + kernelRadius];
                        }
                    }
                }

                result[i][j] = sum;
            }
        }

        return result;
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

        int result = 0;
        try {
            result = execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskQueue.setResult(id, result);

    }
}
