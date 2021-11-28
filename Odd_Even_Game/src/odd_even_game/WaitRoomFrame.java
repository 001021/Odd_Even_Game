package odd_even_game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.table.*;

public class WaitRoomFrame extends JFrame{
   JTable table2; // 접속자 정보가 들어갈 table2
   DefaultTableModel model2;
   JPanel pane;
   JPanel inputPane;
   JTextArea display;
   JTextField tf;
   private JTextField tfChat;
   JComboBox box;
   JButton b3, b5, b6;
   JScrollBar bar;
   
   String nick = null;
   
	Socket socket=null;
    DataOutputStream out;
    DataInputStream in;



   public WaitRoomFrame(Socket socket, final String nickName) {
      setTitle("Waiting Room");
      
      this.socket = socket;
      try {
          out = new DataOutputStream(socket.getOutputStream());
          in = new DataInputStream(socket.getInputStream());
      } catch(Exception e) {}

      // 접속자 정보 출력
      String col2[] = {"ID", "Nickname", "Record"}; // 아이디, 대화명, 게임 전적
      String row2[][] = new String[0][3];
      model2 = new DefaultTableModel(row2, col2);

      table2 = new JTable(model2);
      JScrollPane js2 = new JScrollPane(table2);
      table2.setBackground(Color.WHITE);
      table2.getTableHeader().setReorderingAllowed(false); // 테이블2 고정

      pane = new JPanel();
      display = new JTextArea(11, 30);
      display.setEditable(false);
      JScrollPane js3 = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

      pane.add(js3);

      inputPane = new JPanel();


      tfChat = new JTextField(25);
      JButton sendBtn = new JButton("send");
      
      sendBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 try {
				out.writeUTF("chat");
				out.writeUTF("[" + nickName + "] " + tfChat.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	 
            display.append(  "[" + nick + "님]: " + tfChat.getText() + "\n");
            tfChat.selectAll();
         }
      });
      
      
      inputPane.add(tfChat);
      
      Action ok = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent arg0) {

            display.append( "[" + nick + "님]: " + tfChat.getText() + "\n");
            tfChat.selectAll();

         }
      };

      KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
      tfChat.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
      tfChat.getActionMap().put("ENTER", ok);
      JPanel p2 = new JPanel();
      b3 = new JButton("1:1 신청");
      b5 = new JButton("View info");
      b6 = new JButton("Exit");

      b3.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e1) {
        	  
        	  try {
				out.writeUTF("battleRequest");
				
				JOptionPane aa = new JOptionPane();
				String oppID = aa.showInputDialog("대결을 신청할 상대의 NickName을 입력하세요.");
				out.writeUTF(oppID);
				
				
        	  } catch (IOException e) {}
             
             JOptionPane aa = new JOptionPane();
             String myID = null;
             // 누구한테 신청할 것인지?
             String oppID = aa.showInputDialog("대결을 신청할 상대의 NickName을 입력하세요.");
             
             
             
             
             
             
             
             
             
             
             // 상대방 아이디가 접속중 아이디에 있으면 대결 메시지 전송
             if (oppID.equals("001021")) {
                // oppID에게 메시지 전송
                // 나에게 오는 메세지
                JOptionPane.showMessageDialog(null, "대결 수락을 기다리는 중...", " 대 결 대 기", JOptionPane.PLAIN_MESSAGE);
                // 상대방에게 가는 메시지
                // confirm dialog의 리턴값 : YES == 0 NO == 1 X == -1 (팝업 종료)
                int YorN = JOptionPane.showConfirmDialog(null, myID + "님으로부터 대결 신청!\n수락하시겠습니까?", " 대 결 신 청", JOptionPane.YES_NO_OPTION);
                
                if (YorN == 0) {
                   JOptionPane.showMessageDialog(null, "대결 성사! 게임방으로 이동합니다!", " 대 결 성 사", JOptionPane.PLAIN_MESSAGE);
                }
                
                else {
                   // 나에게 오는 메세지
                   JOptionPane.showMessageDialog(null, nick + "님께서 대결을 거절하셨습니다...", " 대 결 거 절", JOptionPane.PLAIN_MESSAGE);
                   // 상대방에게 가는 메세지
                   JOptionPane.showMessageDialog(null, myID + "님과의 대결을 거절하셨습니다...", " 대 결 거 절", JOptionPane.PLAIN_MESSAGE);
                }
             }
             else { // 없으면 잘못됐다고 띄우기
                JOptionPane.showMessageDialog(null, "접속 중인 ID가 아닙니다!", " 에러!", JOptionPane.PLAIN_MESSAGE);
             }
          }
       });
      
      
      
      b5.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e2) {
        	  try {
              	out.writeUTF("memberInfo");
              	
              	 // 누구한테 신청할 것인지?
                String oppID = JOptionPane.showInputDialog("정보를 보고싶은 상대의 NickName을 입력하세요.");
              	
              	if(oppID != null)
              		out.writeUTF(oppID);
              	int win = Integer.parseInt(in.readUTF());
    			int lose = Integer.parseInt(in.readUTF());
    			
    			new MemberInfoFrame(oppID, win, lose);
    			
  			} catch (IOException e) {}
          }
       });
      
      
      
      

      // Exit Button action
      b6.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e2) {
            System.exit(0);
         }
      });


      p2.setLayout(new GridLayout(1, 3, 3, 3));
      p2.add(b3);
      p2.add(b5); p2.add(b6);

      // 배치
      setLayout(null);
      js2.setBounds(10, 15, 300, 550);
      js3.setBounds(330, 15, 350, 400);
      inputPane.setBounds(330, 415, 270, 30);
      sendBtn.setBounds(600, 420, 80, 20);


      p2.setBounds(330, 450, 350, 100);
      add(js2);
      add(js3);
      add(inputPane);
      add(sendBtn);
      add(p2);

      setSize(700, 600); // 창 사이즈
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

}