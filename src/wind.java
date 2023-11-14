
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

import java.rmi.Naming ;
import java.rmi.NotBoundException;

public class wind  extends JFrame {

    private TaskScheduler taskScheduler;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    // End of variables declaration


    public wind(TaskScheduler taskScheduler) {

        this.taskScheduler=taskScheduler;
        initComponents();}
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jLabel4 = new JLabel();
        jButton3 = new JButton();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        setTitle("Crop Task");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new Color(204, 255, 255));

        jLabel1.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Image Path : ");

        jLabel2.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Kernel :");

        jLabel3.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Number of crops( between 1 and 5):");


        jButton1.setBackground(new Color(0, 204, 204));
        jButton1.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("Import");
        jLabel6.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Processed");

        jLabel7.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Original");
        jButton2.setBackground(new Color(0, 204, 204));
        jButton2.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton2.setForeground(new Color(255, 255, 255));
        jButton2.setText("Apply");
        jButton1.addActionListener( new ActionListener() {
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
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileName = jTextField1.getText();
                String kernelValues = jTextField2.getText();
                int splits = Integer.parseInt(jTextField3.getText());
                BufferedImage originalImage = null;
                try {
                    originalImage = ImageIO.read(new File(fileName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image newImg1 = originalImage.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
                jLabel4.setIcon(new ImageIcon(newImg1));



                String[] values = kernelValues.trim().split(" ");
                int size = (int) Math.sqrt(values.length);
                double[][] kernel = new double[size][size];

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        kernel[i][j] = Double.parseDouble(values[i * size + j]);
                    }
                }

                try {
                    //TaskScheduler taskScheduler = (TaskScheduler) Naming.lookup("rmi://localhost:1091/TaskSchedulerServer");
                    int taskId = taskScheduler.submitTask(new CropTask(fileName, kernel, splits, 2));
                    TaskResult result = taskScheduler.getResult(taskId);
                    System.out.println(taskId);
                    while (true) {
                        try {
                            BufferedImage processedImage = ImageIO.read(new File("C:\\Users\\HP\\Documents\\MATLAB\\ner\\mol\\ressembled" + taskId + ".jpg"));
                            Image newImg = processedImage.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
                            jLabel5.setIcon(new ImageIcon(newImg));


                            pack();
                            System.out.println(result);
                            break;
                        } catch (IOException e1) {
                            Thread.sleep(500);
                        }
                    }

                    if (result != null) {
                        System.out.println("Task result : " + result.getResult());
                    } else {
                        System.out.println("Task result : null");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }

            }
        });
        jButton3.setBackground(new Color(0, 204, 204));
        jButton3.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton3.setForeground(new Color(255, 255, 255));
        jButton3.setText("Save");

        jButton3.addActionListener(new ActionListener() {
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
                    Image processedImage = ((ImageIcon) jLabel5.getIcon()).getImage();

                    // create a BufferedImage to write to the selected file
                    BufferedImage outputImage = new BufferedImage(processedImage.getWidth(null), processedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = outputImage.createGraphics();
                    g.drawImage(processedImage, 0, 0, null);
                    g.dispose();

                    // write the output image to the selected file
                    ImageIO.write(outputImage, "png", new File(outputFile + ".jpg"));
                } catch (IOException ex) {
                    System.err.println("Error saving image: " + ex.getMessage());
                }
            }
        }
    });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(179, 179, 179)
                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addGap(0, 7, Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(26, 26, 26)
                                                                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(jLabel3)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))))
                                                .addGap(63, 63, 63)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE)))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(48, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jButton2)
                                                                .addGap(55, 55, 55)
                                                                .addComponent(jButton3)))
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                        .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();



    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
