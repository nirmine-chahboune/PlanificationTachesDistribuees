import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Slave5 {
    public static void main(String[] args) throws IOException, Exception {
        ServerSocket ss = new ServerSocket(2440);
        System.out.println("Waiting for the master");
        while (true) {
            Socket s = ss.accept();
            System.out.println("Master connected");
            InputStream inputStream = s.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] sizeBuffer = new byte[4];
            inputStream.read(sizeBuffer);
            int dataSize = ByteBuffer.wrap(sizeBuffer).getInt();
            byte[] buffer = new byte[1024];
            int totalBytesRead = 0;
            System.out.println("Receiving image.");
            while (totalBytesRead < dataSize) {
                int bytesRead = inputStream.read(buffer, 0, Math.min(buffer.length, dataSize - totalBytesRead));
                if (bytesRead == -1) {
                    break;
                }
                baos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
            System.out.println("Image received.");
            byte[] imageData = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            System.out.println("Transforming byteArray to BufferedImage");
            BufferedImage image = ImageIO.read(bais);

            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(inputStream));
            double[][] kernel = (double[][]) ois.readObject();
            System.out.println("receiving kernel : done.");

            BufferedImage filteredImage = applyKernel(image, kernel);

            ByteArrayOutputStream filteredBaos = new ByteArrayOutputStream();
            ImageIO.write(filteredImage, "jpg", filteredBaos);
            byte[] filteredImageData = filteredBaos.toByteArray();
            OutputStream os = s.getOutputStream();
            os.write(filteredImageData);
            os.flush();
            System.out.println("sending filtered image : done.");

            s.close();

        }


    }

    private static BufferedImage applyKernel(BufferedImage image, double[][] kernel) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb & 0xFF);
                double newRed = 0;
                double newGreen = 0;
                double newBlue = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        int x = i + k - 1;
                        int y = j + l - 1;
                        if (x < 0 || x >= width || y < 0 || y >= height) {
                            continue;
                        }
                        int rgb2 = image.getRGB(x, y);
                        int red2 = (rgb2 >> 16) & 0xFF;
                        int green2 = (rgb2 >> 8) & 0xFF;
                        int blue2 = (rgb2 & 0xFF);
                        newRed += red2 * kernel[k][l];
                        newGreen += green2 * kernel[k][l];
                        newBlue += blue2 * kernel[k][l];
                    }
                }
                newRed = Math.min(Math.max(newRed, 0), 255);
                newGreen = Math.min(Math.max(newGreen, 0), 255);
                newBlue = Math.min(Math.max(newBlue, 0), 255);
                int newRGB = (int) newRed << 16 | (int) newGreen << 8 | (int) newBlue;
                result.setRGB(i, j, newRGB);
            }
        }
        return result;
    }
}