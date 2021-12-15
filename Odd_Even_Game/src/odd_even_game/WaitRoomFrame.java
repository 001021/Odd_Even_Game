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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.*;


public class WaitRoomFrame extends JFrame{
	JTable table2; // 접속자 정보가 들어갈 table2
	DefaultTableModel model2;
	JPanel pane;
	JPanel inputPane;
	static JTextArea display;
	JTextField tf;
	private static JTextField tfChat;
	JComboBox box;
	JButton b3, b5, b6;
	JScrollBar bar;
	JList userList;

	String nickName = null;

	Socket socket=null;
	static DataOutputStream out;
	DataInputStream in;

	Server server = new Server();

	public WaitRoomFrame(final Socket socket, final String nickName) {
		setTitle("Waiting Room");
		
		this.socket = socket;
		this.nickName = nickName;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch(Exception e) {}

		Thread receiver = new Thread(new ClientReceiver(socket, nickName));
		receiver.start();

		// 접속자 정보 출력
//		private DefaultListModel<Set<String>> model;
//		private JList<Set<String>> = server.waitingList;
//		Collection<Object> keySet = server.waitingList.values();
//		userList = new JList(keySet);
//		Object[][] tableData = { {"a", "a", "0"},
//				{"b", "b", "0"},
//				{"c", "c", "1"}
//		};
		
//		int indext = 0;
//		for (String key : server.waitingList.keySet()) {
//			
//		}
		String col2[] = {"ID", "Nickname", "Record"}; // 아이디, 대화명, 게임 전적
		Object[][] tableData = new Object[server.waitingList.keySet().size()][3];
		
		Object ID[] = {};
		Object Nickname[] = {};
		Object Record[] = {};
		
		Set<String> keyList = server.waitingList.keySet();
		Iterator<String> itr = keyList.iterator();
		int index = 0;
		while (itr.hasNext()) {
			String name = itr.next();
			ID[index] = server.waitingList.get(ID);
			Nickname[index] = server.waitingList.get(nickName);
			Record[index] = server.waitingList.get(Record);
			index++;
		}
		
		for (int i = 0 ; i < index; i++) {
			tableData[i][0] = ID[i];
			tableData[i][1] = Nickname[i];
			tableData[i][2] = Record[i];
		}
		

		
		model2 = new DefaultTableModel(tableData, col2);

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
					out.writeUTF("[" + nickName + "] " + tfChat.getText() + "\n");
				} catch (IOException e1) {}
			}
		});


		inputPane.add(tfChat);

		Action ok = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					out.writeUTF("chat");
					out.writeUTF("[" + nickName + "] " + tfChat.getText() + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
				String oppID;
				try {
					out.writeUTF("battleRequest");

					oppID = JOptionPane.showInputDialog("대결을 신청할 상대의 NickName을 입력하세요.");
					out.writeUTF(oppID);

					JOptionPane.showMessageDialog(null, oppID + "님에게 대결을 신청했습니다!", " 대 결 신 청", JOptionPane.PLAIN_MESSAGE);

				} catch (IOException e) {}
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

					//              	JOptionPane.showMessageDialog(null, win_lose, "a", JOptionPane.PLAIN_MESSAGE);
					//    			
					//    			
					////    			new MemberInfoFrame(oppID, win, lose);

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


	static class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		String nickName;

		ClientReceiver(Socket socket, String nickName){
			this.nickName = nickName;
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch(IOException e) {}

		}

		public void run() {
			while (in != null) {
				try {
					String query = in.readUTF();

					if(query.equals("chating in")) {
						display.append(in.readUTF());
						tfChat.selectAll();
					}
					else if (query.equals("battleRequest from others")) { // *************************************************************************
						String oppNickName = in.readUTF();

						// 상대방에게 가는 메시지
						// confirm dialog의 리턴값 : YES == 0 NO == 1 X == -1 (팝업 종료)
						int YorN = JOptionPane.showConfirmDialog(null, oppNickName + "님으로부터 대결 신청!\n수락하시겠습니까?", " 대 결 신 청", JOptionPane.YES_NO_OPTION);

						if (YorN == 0) {
							out.writeUTF("yes");
							out.writeUTF(oppNickName);
						}

						else {
							// 상대방에게 가는 메세지
							JOptionPane.showMessageDialog(null, oppNickName + "님과의 대결을 거절하셨습니다...", " 대 결 거 절", JOptionPane.PLAIN_MESSAGE);
							// 나에게 오는 메세지
							out.writeUTF("no");
							out.writeUTF(oppNickName);
						}
					}

					if(query.equals("game start")) {
						JOptionPane.showMessageDialog(null, "대결 성사! 게임방으로 이동합니다!", "[" + nickName + "] 대 결 성 사", JOptionPane.PLAIN_MESSAGE);
						new GameRoomAttackFrame(socket, nickName);
					}
					else if(query.equals("rejected")) {
						String oppNickName = in.readUTF();
						JOptionPane.showMessageDialog(null, oppNickName + "님께서 대결을 거절하셨습니다...", " 대 결 거 절", JOptionPane.PLAIN_MESSAGE);
					}

					else if(query.equals("Error : write correct nickname")) {
						String oppNickName = in.readUTF();
						JOptionPane.showMessageDialog(null, oppNickName + "님을 찾을 수 없습니다...", " 검 색 실 패", JOptionPane.PLAIN_MESSAGE);
					}

					else if(query.equals("memberInfo res")) {
						String info = in.readUTF();
						JOptionPane.showMessageDialog(null, info, "정 보 보 기", JOptionPane.PLAIN_MESSAGE);
					}

				} catch(IOException e) {}
			}
		}
	}

}