import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window2 extends JFrame {
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private TaskScheduler taskScheduler;


    public Window2(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        initComponents();

    }
    private void initComponents() {

        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jButton1 = new JButton();
        jTextField3 = new JTextField();
        jLabel3 = new JLabel();
        setTitle("Product Task");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(153, 255, 255));



        jLabel1.setFont(new Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setText("Matrix 1:");

        jLabel2.setFont(new Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setText("Matrix 2:");

        jButton1.setBackground(new Color(0, 204, 204));
        jButton1.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("Product");

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matrix1 = jTextField1.getText();
                String matrix2 =jTextField2.getText();
                String[] rows = matrix2.split(";");
                int[][] matrix2Data = new int[rows.length][];

                for (int i = 0; i < rows.length; i++) {
                    String[] elements = rows[i].split(",");
                    matrix2Data[i] = new int[elements.length];

                    for (int j = 0; j < elements.length; j++) {
                        try {
                            matrix2Data[i][j] = Integer.parseInt(elements[j]);
                        } catch (NumberFormatException e1) {
                            System.out.println("Error: Invalid number format in input string: " + elements[j]);

                        }
                    }
                }


                String[] rows1 = matrix1.split(";");
                int[][] matrix1Data = new int[rows1.length][];

                for (int i = 0; i < rows1.length; i++) {
                    String[] elements2= rows1[i].split(",");
                    matrix1Data[i] = new int[elements2.length];

                    for (int j = 0; j < elements2.length; j++) {
                        try {
                            matrix1Data[i][j] = Integer.parseInt(elements2[j]);
                        } catch (NumberFormatException ej) {
                            System.out.println("Error: Invalid number format in input string: " + elements2[j]);

                        }
                    }
                }







                try {
                    //TaskScheduler taskScheduler = (TaskScheduler) Naming.lookup("rmi://localhost:1091/TaskSchedulerServer");

                    int taskId3=taskScheduler.submitTask(new MatrixMultiplicationTask(2,matrix1Data ,matrix2Data));
                    TaskResult result3 = taskScheduler.getResult(taskId3);

                    int[][] mat = (int[][])result3.getResult();





                    StringBuilder r  = new StringBuilder();

                    for (int i = 0; i < mat.length; i++) {
                        for (int j = 0; j < mat[i].length; j++) {
                            r.append(mat[i][j]);

                            if (j < mat[i].length - 1) {
                                r.append(",");
                            }
                        }

                        if (i < mat.length - 1) {
                            r.append(";");
                        }
                    }



                    jTextField3.setText(r.toString());

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }

            }
        });

        jLabel3.setFont(new Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setText("Your Result");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(117, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField1, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                                        .addComponent(jTextField2)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                .addGap(59, 59, 59)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
                                .addGap(87, 87, 87)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addContainerGap(86, Short.MAX_VALUE))
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
