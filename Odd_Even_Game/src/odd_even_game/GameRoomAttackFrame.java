package odd_even_game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;

import odd_even_game.WaitRoomFrame.ClientReceiver;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class GameRoomAttackFrame extends JFrame{
	JPanel pane;
	static JTextArea display;
	static JTextField tfChat;
	JButton sendBtn;
	JPanel inputPane;

	static String nick = null;
	int win;
	int lose;

	private JFrame frame;
	private JPanel contentPane;
	private JTextField textField;

	Socket socket = null;
	static DataOutputStream out;
	static DataInputStream in;
	public boolean isClickReadyBtn = false;
	public boolean isStart = false;
	public boolean isEnd = false;
	public boolean isRightAnswer = false;
	public int checkReady = 0; // 입장 인원 중 몇 명이 준비 버튼을 눌렀는지 담는 변수
	public int userTurn = 0; // 접속하는 클라이언트에게 턴을 부여해주는 변수
	public int selectTurn = 1; // 누가 턴인지 정하는 변수
	public String clientID; // 클라이언트 아이디를 담는 변수

	/**
	 * Create the application.
	 */
	public GameRoomAttackFrame(Socket socket, final String nickName) {
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch(Exception e) {}
		
		Thread receiver = new Thread(new ClientReceiver(socket));
		receiver.start();
		
		setTitle("[" + nickName + "] 홀짝게임 스타트! - 공격");
		setSize(523, 460);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		
		//		JPanel JScrollpane = new JPanel();
		//		JScrollpane.setBorder(new LineBorder(new Color(0, 0, 0)));
		//		JScrollpane.setBackground(Color.WHITE);
		//		JScrollpane.setBounds(10, 12, 340, 217);
		//		frame.getContentPane().add(JScrollpane);

		pane = new JPanel();
		display = new JTextArea(11, 30);
		display.setEditable(false);
		JScrollPane js3 = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js3.setBounds(10, 12, 340, 217);
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

		inputPane.setBounds(10, 228, 270, 31);
		sendBtn.setBounds(285, 230, 65, 25);
		inputPane.add(tfChat);

		add(js3);
		add(inputPane);
		add(sendBtn);


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


		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(360, 12, 147, 247);
		add(panel_1);

		JLabel lblNewLabel = new JLabel("\uD68C\uC6D0 \uC815\uBCF4 \uBCF4\uAE30");
		panel_1.add(lblNewLabel);

		final JButton readyButton = new JButton("준비 완료!");
		readyButton.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 30));
		readyButton.setBackground(new Color(200, 200, 255));
		readyButton.setBounds(10, 270, 497, 155);

		add(readyButton);

		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				// 버튼 누르면 서버에게 준비 완료되었음을 전송
				try {
					out.writeUTF("ready");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

//		String response = "";
//		while(!response.equals("ready") && !response.equals("no")) {
//			try {
//				response = in.readUTF();
//			} catch (IOException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//			System.out.println(response);
//		}
//
//		if(response.equals("ready")) {
//			readyButton.setVisible(false);
//		}
//		else if(response.equals("no")) {
//			JOptionPane.showMessageDialog(null, "아직 준비 완료가 되지 않았나봅니다!", " 대 결 거 절", JOptionPane.PLAIN_MESSAGE);
//		}

		
		//		// 둘 다 준비 완료가 되면
		//		if (user1.ready == 1 && user2.ready == 2) {
		//		 // 버튼 사라짐
		//			readyButton.setVisible(false);
		//		}

		final String oppMessage = "홀";

		// 공격이 홀을 눌렀을 때
		JButton btnNewButton = new JButton("\uD640");
		btnNewButton.setBackground(new Color(224, 255, 255));
		btnNewButton.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 60));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 서버에 "홀"을 전송

				// 상대방이 고르기를 기다림

				if (oppMessage == "짝") {
					// 상대방이 짝을 골랐으면
					JOptionPane.showMessageDialog(null, "공격 성공!");
					win++;
				}
				else {
					JOptionPane.showMessageDialog(null, "공격 실패!");
					lose++;
				}

			}
		});
		btnNewButton.setBounds(10, 271, 241, 152);
		add(btnNewButton);


		// 공격이 짝을 눌렀을 때
		JButton btnNewButton_1 = new JButton("\uC9DD");
		btnNewButton_1.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 60));
		btnNewButton_1.setBackground(new Color(224, 255, 255));
		btnNewButton_1.setBounds(266, 271, 241, 152);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (oppMessage == "홀") {
					// 서버에 "홀"을 전송

					// 상대방이 고르기를 기다림

					JOptionPane.showMessageDialog(null, "공격 성공!");
				}
				else {
					JOptionPane.showMessageDialog(null, "공격 실패!");
				}
			}
		});
		add(btnNewButton_1);

		textField = new JTextField();
		textField.setBounds(10, 228, 290, 31);
		add(textField);
		textField.setColumns(10);

//		JButton btnNewButton_2 = new JButton("Send");
//		btnNewButton_2.setBackground(new Color(230, 230, 250));
//		btnNewButton_2.setFont(new Font("�ؽ� ǲ����� L", Font.PLAIN, 9));
//		btnNewButton_2.setBounds(298, 228, 52, 31);
//		add(btnNewButton_2);
	}

	private void sendReady() {
		try {
			out.writeUTF("ready");
		} catch (Exception e) {
		}

	}

	static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;

		ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch(IOException e) {}
		}

		public void run() {
			while (in != null) {
				try {
					String query = in.readUTF();

					if (query.equals("game chating in")) {
						display.append(in.readUTF());
						tfChat.selectAll();
					}
					else if (query.equals("all ready")) {
						int result = JOptionPane.showConfirmDialog(null, "상대방 준비 완료! 게임을 시작하시겠습니까?", "준비", JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.CLOSED_OPTION) {
							// 사용자가 "예", "아니오"의 선택 없이 다이얼로그 창을 닫은 경우
							out.writeUTF("no");
						}
						else if (result == JOptionPane.YES_OPTION) {
							// 예
							out.writeUTF("ready");
						}
						else {
							// 아니오
							out.writeUTF("no");
						}
					}
				} catch (IOException e) {}
			}
		}
	}


}
