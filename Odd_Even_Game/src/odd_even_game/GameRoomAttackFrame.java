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
		
		setTitle("[" + nickName + "] 홀짝게임 스타트!");
		setSize(523, 460);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);

		pane = new JPanel();
		display = new JTextArea(11, 30);
		display.setEditable(false);
		JScrollPane js3 = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js3.setBounds(10, 12, 340, 217);
		pane.add(js3);

		inputPane = new JPanel();


		tfChat = new JTextField(25);
		final JButton sendBtn = new JButton("send");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeUTF("11 chat");
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
					out.writeUTF("11 chat");
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


		final JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(360, 12, 147, 123);
		add(panel_1);
		
		final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(360, 137, 147, 123);
		add(panel_2);

		final JLabel lblNewLabel = new JLabel("상대 정보");
		panel_1.add(lblNewLabel);
		final JLabel lblNewLabel2 = new JLabel("내 정보");
		panel_2.add(lblNewLabel2);

		final JButton readyButton = new JButton("준비 완료!");
		readyButton.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 30));
		readyButton.setBackground(new Color(200, 200, 255));
		readyButton.setBounds(10, 270, 250, 155);
		
		final JButton WaitingRoomButton = new JButton("대기실로!");
		WaitingRoomButton.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 30));
		WaitingRoomButton.setBackground(new Color(200, 255, 200));
		WaitingRoomButton.setBounds(265, 270, 247, 155);

		add(readyButton);
		add(WaitingRoomButton);

		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				// 버튼 누르면 서버에게 준비 완료되었음을 전송
				try {
					out.writeUTF("ready");
					readyButton.setVisible(false);
					WaitingRoomButton.setVisible(false);
					panel_2.setVisible(false);
					lblNewLabel2.setVisible(false);
					panel_1.setVisible(false);
					lblNewLabel.setVisible(false);
					tfChat.setVisible(false);
					display.setVisible(false);
					pane.setVisible(false);
					inputPane.setVisible(false);
					sendBtn.setVisible(false);
					contentPane.setVisible(false);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		WaitingRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				// 버튼 누르면 대기실로
				dispose();
			}
		});
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
					
					else if (query.equals("defence")) {
						String num = JOptionPane.showInputDialog("구슬 몇 개 내시겠습니까 ?");
						out.writeUTF("how many");
						out.writeUTF(num);
					}
					else if (query.equals("attack")) {
						int YorN = JOptionPane.showConfirmDialog(null, "홀 ? 짝 ?", " 공 격 ", JOptionPane.YES_NO_OPTION);
                        
						out.writeUTF("ans is");
                        if (YorN == 0)
                           out.writeUTF("odd");
                        else
                        	out.writeUTF("even");
					}
					
					else if (query.equals("you win")) {
						String result = "승리";
						JOptionPane.showMessageDialog(null, result, " 승 리 ", JOptionPane.PLAIN_MESSAGE);
					}
					else if (query.equals("you lose")) {
						String result = "패배";
						JOptionPane.showMessageDialog(null, result, " 패 배 ", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (IOException e) {}
			}
		}
	}


}
