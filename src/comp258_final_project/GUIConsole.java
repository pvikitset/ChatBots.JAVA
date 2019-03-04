/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp258_final_project;

import java.awt.event.KeyListener;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author pviki
 */
public class GUIConsole extends javax.swing.JFrame implements Displayable, KeyListener {

    final public static int DEFAULT_PORT = 5555;
    ChatClient client;
    Timer timer;
    TextToSpeech speechConvertor;

    private Integer bot_id_selected;
    private String host;
    private int port;
    private String user;

    public GUIConsole() {
        initComponents();
        switchFragment(0);
        generateDropDown();
        setVisible(true);
    }

    public static void main(String args[]) {

        String host = "";
        int port = 0;  //The port number

        try {
            host = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";
        }
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception ex) {
            port = DEFAULT_PORT;
        }

        GUIConsole chat = new GUIConsole();
        chat.host = host;
        chat.port = port;
    }

    public boolean logIn() {

        String username = txt_name.getText();

        try {
            client = new ChatClient(this.host, this.port, username, this.bot_id_selected, this);
            speechConvertor = new TextToSpeech();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");

            System.exit(1);
        }
        return false;
    }

    public void setBot_id_selected(Integer bot_id_selected) {
        this.bot_id_selected = bot_id_selected;
    }

    public void send(String msg) {
        if (!isNullOrBlank(msg)) {
            // msg = (msg.length() > 70) ? insertBreakline(msg) : msg;
            //dialog.insert("You: " + msg + "\n", 0);
            dialog2.insert("You: " + msg + "\n", 0);
            client.handleMessageFromClientUI(msg);
            input.setText("");
        }
    }

    public void playAudio(String msg) {
        this.signal.setVisible(true);
        this.speechConvertor.playTextToSpeech(msg);
        this.signal.setVisible(false);
    }

    @Override
    public void display(String message) {
        dialog2.insert(message + "\n", 0);
        playAudio(message);
    }
    
    public void displayList(Object contents) {
        String[] names = (String[]) contents;

        for (int i = 0; i < names.length; i++) {
            //messageList.insert(names[i] + "\n", 0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            input.setEditable(false);
            String msg = input.getText();
            send(msg);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            input.setEditable(true);
        }
    }

    public static boolean isNullOrBlank(String param) {
        return param == null || param.trim().length() == 0;
    }

    private void showDialog() {
        botlibreLogo.add(input, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 559, 425, 20));
        dialog2.setVisible(true);
        input.setVisible(true);
        scroll1.setVisible(true);
        sendB.setVisible(true);
        input.addKeyListener(this);
        logoutB.show();
    }

    private void generateDropDown() {

        ChatBot b = new ChatBot();
        Map<Integer, String> bot_dict = b.getBot_instances_dict();

        for (String name : bot_dict.values()) {
            bot_instances_dd.addItem(name);
        }

        setBot_id_selected(bot_dict.entrySet().stream().findFirst().get().getKey());

        bot_instances_dd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) bot_instances_dd.getSelectedItem();

                for (Integer id : bot_dict.keySet()) {
                    if (bot_dict.get(id).equals(name)) {
                        setBot_id_selected(id);
                    }
                }

            }
        });
    }

    private void switchFragment(int code) {

        switch (code) {
            case 0:
                dialog2.setVisible(false);
                signal.setVisible(false);
                input.setVisible(false);
                logoutB.setVisible(false);
                scroll1.setVisible(false);
                sendB.setVisible(false);
                login_button.setVisible(true);
                txt_name.setVisible(true);
                jSeparator1.setVisible(true);
                jLabel1.setVisible(true);
                botlibrebanner.setVisible(true);
                bot_instances_dd.setVisible(true);
                txt_name.setText("");
                dialog2.setText("");
                loader.hide();
                break;
            case 1:
                login_button.setVisible(false);
                txt_name.setVisible(false);
                jSeparator1.setVisible(false);
                jLabel1.setVisible(false);
                botlibrebanner.setVisible(false);
                bot_instances_dd.setVisible(false);
                loader.setVisible(true);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        signal = new javax.swing.JLabel();
        botlibreLogo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        login_button = new javax.swing.JButton();
        loader = new javax.swing.JLabel();
        logoutB = new javax.swing.JButton();
        sendB = new javax.swing.JButton();
        botlibrebanner = new javax.swing.JLabel();
        bot_instances_dd = new javax.swing.JComboBox();
        scroll1 = new javax.swing.JScrollPane();
        dialog2 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        signal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/comp258_final_project/signa33l.gif"))); // NOI18N
        getContentPane().add(signal, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 450, 300, 140));

        botlibreLogo.setBackground(new java.awt.Color(32, 33, 35));
        botlibreLogo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(153, 153, 255));
        jLabel1.setText("Name");
        botlibreLogo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, 70, 30));

        txt_name.setBackground(new java.awt.Color(32, 33, 35));
        txt_name.setForeground(new java.awt.Color(255, 255, 255));
        txt_name.setBorder(null);
        txt_name.setCaretColor(new java.awt.Color(255, 255, 255));
        botlibreLogo.add(txt_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 270, 210, 20));
        botlibreLogo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 210, 10));

        login_button.setBackground(new java.awt.Color(0, 204, 51));
        login_button.setFont(new java.awt.Font("Perpetua Titling MT", 0, 11)); // NOI18N
        login_button.setText("Log in");
        login_button.setBorder(null);
        login_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_buttonActionPerformed(evt);
            }
        });
        botlibreLogo.add(login_button, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, 210, 50));

        loader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/comp258_final_project/lg.dual-ring-loader.gif"))); // NOI18N
        loader.setMaximumSize(new java.awt.Dimension(50, 50));
        loader.setMinimumSize(new java.awt.Dimension(50, 50));
        botlibreLogo.add(loader, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 240, 180));

        logoutB.setBackground(new java.awt.Color(153, 0, 0));
        logoutB.setFont(new java.awt.Font("Perpetua Titling MT", 0, 11)); // NOI18N
        logoutB.setForeground(new java.awt.Color(255, 255, 255));
        logoutB.setText("Log out");
        logoutB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBActionPerformed(evt);
            }
        });
        botlibreLogo.add(logoutB, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, -1));

        sendB.setFont(new java.awt.Font("Perpetua Titling MT", 0, 11)); // NOI18N
        sendB.setText("Send");
        sendB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBActionPerformed(evt);
            }
        });
        botlibreLogo.add(sendB, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 520, 100, 60));

        botlibrebanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/comp258_final_project/banner.png"))); // NOI18N
        botlibreLogo.add(botlibrebanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 480, -1, -1));

        bot_instances_dd.setBackground(new java.awt.Color(204, 204, 255));
        botlibreLogo.add(bot_instances_dd, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 150, 180, -1));

        scroll1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll1.setAutoscrolls(true);

        dialog2.setEditable(false);
        dialog2.setColumns(20);
        dialog2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 18)); // NOI18N
        dialog2.setLineWrap(true);
        dialog2.setRows(5);
        scroll1.setViewportView(dialog2);

        botlibreLogo.add(scroll1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 530, 460));

        getContentPane().add(botlibreLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 600, 626));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/comp258_final_project/pexels-photo-1243365.jpeg"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 626));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void login_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_buttonActionPerformed
        switchFragment(1);
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (logIn() == true) {
                    showDialog();
                }
                loader.setVisible(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }//GEN-LAST:event_login_buttonActionPerformed

    private void logoutBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBActionPerformed
        switchFragment(0);
        try {
            client.processClientCommand("#logoff");
        } catch (IOException ex) {
            Logger.getLogger(GUIConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_logoutBActionPerformed

    private void sendBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBActionPerformed
        input.setEditable(false);
        String msg = input.getText();
        send(msg);
        input.setEditable(true);
    }//GEN-LAST:event_sendBActionPerformed

    //JPanel dialogPanel = new JPanel();
    //JTextArea dialog = new JTextArea(20, 50);
    JTextArea input = new JTextArea(1, 50);
   // JScrollPane scroll = new JScrollPane(dialog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox bot_instances_dd;
    private javax.swing.JPanel botlibreLogo;
    private javax.swing.JLabel botlibrebanner;
    private javax.swing.JTextArea dialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel loader;
    private javax.swing.JButton login_button;
    private javax.swing.JButton logoutB;
    private javax.swing.JScrollPane scroll1;
    private javax.swing.JButton sendB;
    private javax.swing.JLabel signal;
    private javax.swing.JTextField txt_name;
    // End of variables declaration//GEN-END:variables
}
