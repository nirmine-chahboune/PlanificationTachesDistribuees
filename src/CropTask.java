import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class CropTask implements Task {
    private String imagePath;
    private double[][] kernel;
    private TaskQueue taskQueue;

    private int nombre;
    private int id;

    public CropTask(String imagePath, double[][] kernel, int nombre, int id) throws RemoteException {
        this.imagePath = imagePath;
        this.kernel = kernel;
        this.id = id;
        this.nombre = nombre;
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
    public int execute() throws IOException {

        File file = new File(this.imagePath);
        BufferedImage image = ImageIO.read(file);
        List<BufferedImage> imageSplits = splitImage(image, this.nombre);

        List<ThreadImageSender> threadImageSenders = new ArrayList<>();
        for (int i = 0; i < this.nombre; i++) {
            threadImageSenders.add(new ThreadImageSender(imageSplits.get(i), 2040 + (i * 100), this.kernel));
            threadImageSenders.get(i).start();
            System.out.println("thread sender created");
        }


        try {
            for (ThreadImageSender threadImageSender : threadImageSenders) {
                threadImageSender.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<BufferedImage> toReassemble = new ArrayList<>();
        System.out.println("all thread senders finished their job succesfully.");
        for (ThreadImageSender threadImageSender : threadImageSenders) {
            toReassemble.add(threadImageSender.getProcessedImage());
            JFrame frame = new JFrame();
            frame.setTitle("Image received");
            JLabel label = new JLabel(new ImageIcon(threadImageSender.getProcessedImage()));
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
        }

        BufferedImage[] reassembledImages = toReassemble.toArray(new BufferedImage[toReassemble.size()]);
        BufferedImage reassembledImage = reassembleImage(reassembledImages);


        TaskResult taskResult = new TaskResult(id, reassembledImage);
        taskQueue.addResult(taskResult);
        ImageIO.write(reassembledImage, "jpg",new File("ressembled" + id + ".jpg"));

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

        int result = 0;
        try {
            result = execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskQueue.setResult(id, result);
    }

    public static BufferedImage reassembleImage(BufferedImage[] splitImages) {
        // Get the dimensions of the split images
        int splitWidth = splitImages[0].getWidth();
        int splitHeight = splitImages[0].getHeight();

        // Calculate the dimensions of the reassembled image
        int reassembledWidth = splitWidth ;
        int reassembledHeight = splitHeight* splitImages.length;

        // Create a new image for the reassembled image
        BufferedImage reassembledImage = new BufferedImage(reassembledWidth, reassembledHeight, BufferedImage.TYPE_INT_RGB);

        // Draw the split images onto the reassembled image
        Graphics graphics = reassembledImage.getGraphics();
        int y = 0;
        for (BufferedImage splitImage : splitImages) {
            graphics.drawImage(splitImage, 0, y, null);
            y += splitHeight;
        }
        graphics.dispose();

        return reassembledImage;
    }

    public static List<BufferedImage> splitImage(BufferedImage image, int splits) {
        List<BufferedImage> parts = new ArrayList<>();
        int height = image.getHeight() / splits;
        for (int i = 0; i < splits; i++) {
            int y = i * height;
            BufferedImage part = image.getSubimage(0, y, image.getWidth(), height);
            parts.add(part);
        }
        return parts;
    }
}

