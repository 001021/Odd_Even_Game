package odd_even_game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class JoinFrame extends JFrame {
   private JPanel contentPane;
   private JLabel lblJoin;
   JButton joinCompleteBtn;
   private JTextField tfID;
   private JTextField tfPassword;
   private JTextField tfNickname;
   private JTextField tfName;
   private JTextField tfEmail;
   private JTextField tfSNS;
   
   Socket socket;
   DataOutputStream out;
   DataInputStream in;
   
   public JoinFrame(final Socket socket) {
      setTitle("Join");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(430, 490);
      setLocationRelativeTo(null);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      lblJoin = new JLabel("Join");
      Font f1 = new Font("넥슨 풋볼고딕 B", Font.BOLD, 20);
      lblJoin.setFont(f1);
      lblJoin.setBounds(215, 41, 101, 20);
      contentPane.add(lblJoin);
      
      JLabel lblID = new JLabel("ID");
      lblID.setBounds(69, 110, 69, 20);
      contentPane.add(lblID);
      
      JLabel lblPassword = new JLabel("password");
      lblPassword.setBounds(69, 150, 69, 20);
      contentPane.add(lblPassword);
      
      JLabel lblName = new JLabel("Name");
      lblName.setBounds(69, 190, 69, 20);
      contentPane.add(lblName);
      
      JLabel lblNickname = new JLabel("Nickname");
      lblNickname.setBounds(69, 230, 69, 20);
      contentPane.add(lblNickname);
      
      JLabel lblEmail = new JLabel("Email");
      lblEmail.setBounds(69, 270, 69, 20);
      contentPane.add(lblEmail);

      JLabel lblSNS = new JLabel("SNS");
      lblSNS.setBounds(69, 310, 69, 20);
      contentPane.add(lblSNS);
      
      tfID = new JTextField();
      tfID.setColumns(10);
      tfID.setBounds(159, 106, 186, 30);
      contentPane.add(tfID);
      
      tfPassword = new JTextField();
      tfPassword.setColumns(10);
      tfPassword.setBounds(159, 146, 186, 30);
      contentPane.add(tfPassword);
      
      tfName = new JTextField();
      tfName.setColumns(10);
      tfName.setBounds(159, 186, 186, 30);
      contentPane.add(tfName);
      
      tfNickname = new JTextField();
      tfNickname.setColumns(10);
      tfNickname.setBounds(159, 226, 186, 30);
      contentPane.add(tfNickname);
      
      tfEmail = new JTextField();
      tfEmail.setColumns(10);
      tfEmail.setBounds(159, 266, 186, 30);
      contentPane.add(tfEmail);
      
      tfSNS = new JTextField();
      tfSNS.setColumns(10);
      tfSNS.setBounds(159, 306, 186, 30);
      contentPane.add(tfSNS);      
      
      joinCompleteBtn = new JButton("Join Complete");		// join button
      joinCompleteBtn.setBounds(206, 356, 139, 29);
      contentPane.add(joinCompleteBtn);
      
      setVisible(true);
      
      this.socket = socket;
      try {
          out = new DataOutputStream(socket.getOutputStream());
          in = new DataInputStream(socket.getInputStream());
      } catch(Exception e) {}
      
      // join button action
      joinCompleteBtn.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            String id = tfID.getText();
            String password = tfPassword.getText();
            String name = tfName.getText();
            String nickName = tfNickname.getText();
            String email = tfEmail.getText();
            String sns = tfSNS.getText();
            
            
            try {
               out.writeUTF("join");
               out.writeUTF(id);
               out.writeUTF(password);
               out.writeUTF(name);
               out.writeUTF(nickName);
               out.writeUTF(email);
               out.writeUTF(sns);
               
               if(in.readUTF().equals("Success")) {		// join success
            	   JOptionPane.showMessageDialog(null, "Welcome!");
               }
               else {		// redundant id or network error
            	   JOptionPane.showMessageDialog(null, "join failed");
               }
            } catch (IOException e1) {}
            
            dispose();
         }
      });
   }
}