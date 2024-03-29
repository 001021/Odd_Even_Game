package odd_even_game;

import javax.swing.*;
import java.awt.*;

import java.awt.TextField;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import java.net.*;

//�α��� GUI
public class LoginFrame extends JFrame {
	Socket socket=null;
    DataOutputStream out;
    DataInputStream in;

   public LoginFrame(final Socket socket) {
      
      super("Login");
      setLayout(null);

      JPanel panel = new JPanel();
      JLabel explain1 = new JLabel("<html><body style='text-align:center;'><br/><br/> 선생님, 저랑 게임 하나 하시겠습니까?<br/><br/>---------------------------<br/><br/></body>");
      JLabel id = new JLabel("ID : ");
      JLabel pswrd = new JLabel("PassWord : ");
      final JTextField txtID = new JTextField(10);
      final TextField txtPass = new TextField(10);
      add(txtPass);
      txtPass.setEchoChar('*');//암호화
      JButton logBtn = new JButton("Log in");
      JButton joinBtn = new JButton("Join us!");
      Font font_explain = new Font("DX타자B", Font.PLAIN, 15);
      Font font = new Font("넥슨 풋볼고딕 L", Font.PLAIN, 15);
      
      explain1.setFont(font_explain);
      explain1.setBounds(40, 15, 270, 50);
      //contour.setFont(font);
      id.setFont(font);
      id.setBounds(20, 80, 100, 20);
      pswrd.setFont(font);
      pswrd.setBounds(20, 110, 100, 20);
      txtID.setFont(font);
      txtID.setBounds(120, 80, 150, 20);
      txtPass.setFont(font);
      txtPass.setBounds(120, 110, 150, 20);
      logBtn.setFont(font);
      logBtn.setBounds(20, 140, 100, 20);
      joinBtn.setFont(font);
      joinBtn.setBounds(120, 140, 150, 20);
      
      add(explain1);
      //panel.add(contour);
      
      add(id);
      add(txtID);
      add(pswrd);
      add(txtPass);
      add(logBtn);
      add(joinBtn);
      
      this.socket = socket;
      try {
          out = new DataOutputStream(socket.getOutputStream());
          in = new DataInputStream(socket.getInputStream());
      } catch(Exception e) {}
      

      // Login Button action
      logBtn.addActionListener(new ActionListener() {		// login button
         @Override
         public void actionPerformed(ActionEvent e2) {
            String id = txtID.getText();
            String password = txtPass.getText();
            
            try {
            	out.writeUTF("login");
            	if(id != null)
            		out.writeUTF(id);
            	if(password != null)
            		out.writeUTF(password);
            		
                if(in.readUTF().equals("Success")) {
                	String nickName = in.readUTF();
                	JOptionPane.showMessageDialog(null, "게임 한 판 할까요!");
                	// User가 로그인 했을 때 뜨는 창으로 연결
                	new WaitRoomFrame(socket, nickName);		// call waiting room
                	dispose();
                }
                else {
                	JOptionPane.showMessageDialog(null, "로그인 실패");		// invalid id or password
                }
			} catch (IOException e) {}
         }
      });
      
      
      
      // Join Button Action
      joinBtn.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            new JoinFrame(socket);
         }
      });
      
      add(panel);
      setVisible(true);
      setSize(300, 200);
      setLocationRelativeTo(null);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   }
   
   public static void main(String[] args) {
	   Random randomGen = new Random();
	   long registrationNumber = randomGen.nextLong();
	   DataOutputStream out = null;
	   Socket clientSocket = null;
	      
	   try {
		   String serverIp = "127.0.0.1";
		   int nPort = 21118;
		   clientSocket = new Socket(serverIp, nPort);
		   System.out.println("Connected to Server");
		   
		   out = new DataOutputStream(clientSocket.getOutputStream());
		   out.writeUTF(String.valueOf(registrationNumber));
		   
		   
		   
		   
//		   Thread sender = new Thread(new ClientSender(clientSocket, registrationNumber));
//		   Thread receiver = new Thread(new ClientReceiver(clientSocket));
//			
//		   sender.start();
//		   receiver.start();
		   
	   } catch(ConnectException ce) {
		   ce.printStackTrace();
	   } catch(Exception e) {}
	   
	   new LoginFrame(clientSocket);
	   try {
		   UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	   } catch (Exception e) {}
   }
   
   static class ClientSender extends Thread{
      Socket socket;
      DataOutputStream out;
      String registrationNumber;
      
      ClientSender(Socket socket, long registrationNumber){
         this.socket = socket;
         try {
            this.out = new DataOutputStream(socket.getOutputStream());
            this.registrationNumber = String.valueOf(registrationNumber);
         } catch(Exception e) {}
      }
      
      public void run() {
         Scanner keyboard = new Scanner(System.in);
         try {
            if(out != null) {
               out.writeUTF(registrationNumber); // ó�� �г��� send
            }
            
            while(out != null) {
               out.writeUTF("[" + registrationNumber + "] " + keyboard.nextLine());
            }
         } catch(IOException e) {}
         
         keyboard.close();
      }
   }
   
   static class ClientReceiver extends Thread{
      Socket socket;
      static DataInputStream in;
      
      ClientReceiver(Socket socket){
         this.socket = socket;
         try {
            in = new DataInputStream(socket.getInputStream());
         } catch(IOException e) {}
         
      }
      
      public void run() {
         while (in != null) {
            try {
               System.out.println(in.readUTF());
            } catch(IOException e) {}
         }
      }
      
   }
}