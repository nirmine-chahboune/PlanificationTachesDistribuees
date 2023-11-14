
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Window3img extends JFrame {
    private JButton jButton1;
    private JLabel jLabel1;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel7;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private TaskScheduler taskScheduler;




    public Window3img(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
      initComponents();


    }
    private void initComponents() {
        jButton3 = new JButton();
        jButton1 = new JButton();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jButton2 = new JButton();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Convolution Task");
        setBackground(new Color(0, 204, 255));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        setFocusable(false);
        setForeground(new Color(0, 204, 204));

        jButton1.setBackground(new Color(0, 204, 204));
        jButton1.setFont(new Font("Microsoft YaHei Light", 1, 12)); // NOI18N
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("Convolve");
        jButton1.setActionCommand("Convolve");
        jButton2.setBackground(new Color(0, 204, 204));
        jButton2.setFont(new Font("Microsoft YaHei Light", 1, 12)); // NOI18N
        jButton2.setForeground(new Color(255, 255, 255));
        jButton2.setText("Import");
        jButton2.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fileChooser.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        jTextField1.setText(selectedFile.getAbsolutePath());

                    }
            }
        });
        jButton3.setFont(new Font("Microsoft YaHei Light", 1, 12)); // NOI18N
        jButton3.setForeground(new Color(0, 204, 204));
        jButton3.setText("Save");
        jButton3.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showSaveDialog(null);

                // If the user selects a file, save the image to the selected file path
                if (result == JFileChooser.APPROVE_OPTION) {
                    File outputFile = fileChooser.getSelectedFile();

                    try {
                        // get the processed image from jLabel2's icon
                        Image processedImage = ((ImageIcon) jLabel2.getIcon()).getImage();

                        // create a BufferedImage to write to the selected file
                        BufferedImage outputImage = new BufferedImage(processedImage.getWidth(null), processedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = outputImage.createGraphics();
                        g.drawImage(processedImage, 0, 0, null);
                        g.dispose();

                        // write the output image to the selected file
                        ImageIO.write(outputImage, "png", new File(outputFile + ".png"));
                    } catch (IOException ex) {
                        System.err.println("Error saving image: " + ex.getMessage());
                    }
                }
            }
        });
        jLabel3.setText("Image Path :");
         jLabel3.setFont(new Font("Segoe UI", 3, 12));
        jLabel5.setText("Processed Image");
        jLabel5.setFont(new Font("Segoe UI", 3, 12));
        jLabel6.setText("Original Image");
        jLabel6.setFont(new Font("Segoe UI", 3, 12));
        jLabel7.setText("Kernel:");
        jLabel7.setFont(new Font("Segoe UI", 3, 12));
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileName = jTextField1.getText();
                BufferedImage originalImage = null;
                try {
                    originalImage = ImageIO.read(new File(fileName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image newImg1 = originalImage.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
                jLabel1.setIcon(new ImageIcon(newImg1));
                String kernelValues = jTextField2.getText();
                double[][] kernel = parsekernel(kernelValues);

                try {
                    ImageTask task=  new ImageTask(new File(fileName), kernel,2);
                    int taskId = taskScheduler.submitTask(task);
                    TaskResult result = taskScheduler.getResult(taskId);
                    System.out.println(taskId);
                    while (true) {
                        try {
                            BufferedImage processedImage = ImageIO.read(new File("C:\\Users\\HP\\Documents\\MATLAB\\ner\\mol\\processed_" + taskId + ".jpg"));
                            Image newImg = processedImage.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
                            jLabel2.setIcon(new ImageIcon(newImg));


                            pack();
                            System.out.println(result);
                            break;
                        } catch (IOException e1) {
                            Thread.sleep(500);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }

            }
        });


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(136, 136, 136)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                                                .addGap(53, 53, 53)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jLabel3)
                                                                .addGap(27, 27, 27))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField1, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                                        .addComponent(jTextField2))
                                                .addGap(64, 64, 64)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(112, 112, 112)
                                                                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(31, 31, 31)
                                                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 370, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(50, 50, 50)
                                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(28, 28, 28)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel3)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(41, 41, 41)
                                                                .addComponent(jButton2)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(jButton3))
                                                .addGap(52, 52, 52)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel6)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }

    public static double[][] parsekernel(String matrixStr) {
        String[] rows = matrixStr.split(";");
        double[][] matrix = new double[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            String[] elements = rows[i].split(" ");
            matrix[i] = new double[elements.length];

            for (int j = 0; j < elements.length; j++) {
                try {
                    matrix[i][j] = Double.parseDouble(elements[j]);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid number format in input string: " + elements[j]);
                    return null;
                }
            }
        }

        return matrix;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {

                    }
                });
            }
        });
    }
}