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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class GameRoomAttackFrame {
	JPanel pane;
	JTextArea display;
	JTextField tfChat;
	JButton sendBtn;
	JPanel inputPane;
	
	String nick = null;
	int win;
	int lose;
	
	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameRoomAttackFrame window = new GameRoomAttackFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	/**
	 * Create the application.
	 */
	public GameRoomAttackFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("홀짝게임 스타트! - 공격");
		frame.setSize(523, 460);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
				display.append(  "[" + nick + "님]: " + tfChat.getText() + "\n");
				tfChat.selectAll();
			}
		});
		
		inputPane.setBounds(10, 228, 270, 31);
		sendBtn.setBounds(285, 230, 65, 25);
		inputPane.add(tfChat);
		
		frame.getContentPane().add(js3);
		frame.getContentPane().add(inputPane);
		frame.getContentPane().add(sendBtn);
		
		
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

		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(360, 12, 147, 247);
		frame.getContentPane().add(panel_1);
		
		JLabel lblNewLabel = new JLabel("\uD68C\uC6D0 \uC815\uBCF4 \uBCF4\uAE30");
		panel_1.add(lblNewLabel);
		
		final JButton readyButton = new JButton("준비 완료!");
		readyButton.setFont(new Font("넥슨 풋볼고딕 B", Font.PLAIN, 30));
		readyButton.setBackground(new Color(200, 200, 255));
		readyButton.setBounds(10, 270, 497, 155);
		
		frame.getContentPane().add(readyButton);
		
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				// 버튼 누르면 서버에게 준비 완료되었음을 전송
				readyButton.setVisible(false);
			}
		});
		
		// 둘 다 준비 완료가 되면
//		if (user1.ready == 1 && user2.ready == 2) {
		// 버튼 사라짐
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
		frame.getContentPane().add(btnNewButton);

		
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
		frame.getContentPane().add(btnNewButton_1);
		
//		textField = new JTextField();
//		textField.setBounds(10, 228, 290, 31);
//		frame.getContentPane().add(textField);
//		textField.setColumns(10);
//		
//		JButton btnNewButton_2 = new JButton("Send");
//		btnNewButton_2.setBackground(new Color(230, 230, 250));
//		btnNewButton_2.setFont(new Font("�ؽ� ǲ����� L", Font.PLAIN, 9));
//		btnNewButton_2.setBounds(298, 228, 52, 31);
//		frame.getContentPane().add(btnNewButton_2);
	}
	
	
}
