import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class M extends JFrame {

    protected static Object taskScheduler;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jLabel1;
    // End of variables declaration

    public M() {
        initComponents();
    }
    private void initComponents() {



        jButton3 = new JButton();
        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        setTitle(" Task Scheduler System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(255, 255, 255));

        jLabel1.setBackground(new Color(255, 255, 255));
        jLabel1.setFont(new Font("Segoe Print", 3, 16)); // NOI18N
        jLabel1.setForeground(new Color(0, 204, 204));
        jLabel1.setText("Disturbed Task Scheduling System");

        jButton1.setBackground(new Color(0, 204, 204));
        jButton1.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("Multiplication Task");
        jButton3.setBackground(new Color(0, 204, 204));
        jButton3.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton3.setForeground(new Color(255, 255, 255));
        jButton3.setText("Crop  Task");

        TaskScheduler taskScheduler;
        try {
            taskScheduler = (TaskScheduler) Naming.lookup("rmi://localhost:1091/TaskSchedulerServer");




            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {


                        new Window2(taskScheduler).setVisible(true);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            jButton3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new wind(taskScheduler).setVisible(true);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            jButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new Window3img(taskScheduler).setVisible(true);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        jButton2.setBackground(new Color(0, 204, 204));
        jButton2.setFont(new Font("Microsoft YaHei Light", 1, 14)); // NOI18N
        jButton2.setForeground(new Color(255, 255, 255));
        jButton2.setText("Convolution Task");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 176, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(195, 195, 195)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(148, Short.MAX_VALUE))
        );

        pack();

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new M().setVisible(true);
            }
        }); 
    
    
    }
    
    }