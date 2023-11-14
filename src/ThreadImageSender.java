import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

public class ThreadImageSender extends Thread {

    private BufferedImage image;
    private int port;
    private double[][] kernel;
    private BufferedImage filteredImage;

    public ThreadImageSender(BufferedImage image, int port, double[][] kernel) {
        this.image = image;
        this.port = port;
        this.kernel = kernel;
    }

    public void run() {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", port);
            System.out.println("socket created : " + port);

            // Convert the image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageData = baos.toByteArray();
            System.out.println("sending image.");

            // Send the size of the image data first
            OutputStream os = socket.getOutputStream();
            byte[] sizeBuffer = ByteBuffer.allocate(4).putInt(imageData.length).array();
            os.write(sizeBuffer);
            os.flush();

            // Send the image data over the socket
            os.write(imageData);

            System.out.println("sending image : done.");

            // Send the kernel over the socket
            OutputStream kernelOs = new BufferedOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(kernelOs);
            oos.writeObject(kernel);
            oos.flush();

            System.out.println("sending kernel : done.");

            // Receive the processed image data over the socket
            System.out.println("receiving new image.");
            InputStream is = socket.getInputStream();
            BufferedImage processedImage = ImageIO.read(is);
            this.filteredImage = processedImage;
            System.out.println("image received.");

            // Clean up
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getProcessedImage() {
        return filteredImage;
    }
}