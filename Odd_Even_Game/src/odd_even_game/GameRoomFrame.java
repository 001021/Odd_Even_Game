package odd_even_game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GameRoomFrame {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameRoomFrame window = new GameRoomFrame();
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
	public GameRoomFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\uD640\uC9DD\uAC8C\uC784 \uC2A4\uD0C0\uD2B8!");
		frame.setBounds(100, 100, 523, 460);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel JScrollpane = new JPanel();
		JScrollpane.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollpane.setBackground(Color.WHITE);
		JScrollpane.setBounds(10, 12, 340, 217);
		frame.getContentPane().add(JScrollpane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(360, 12, 147, 247);
		frame.getContentPane().add(panel_1);
		
		JLabel lblNewLabel = new JLabel("\uD68C\uC6D0 \uC815\uBCF4 \uBCF4\uAE30");
		panel_1.add(lblNewLabel);
		
		final String oppMessage = "È¦";
		
		// È¦
		JButton btnNewButton = new JButton("\uD640");
		btnNewButton.setBackground(new Color(224, 255, 255));
		btnNewButton.setFont(new Font("³Ø½¼ Ç²º¼°íµñ B", Font.PLAIN, 60));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (oppMessage == "È¦") {
				// »ó´ë¹æÀÌ º¸³½ ´äÀÌ È¦ÀÌ¸é ½Â¸® Ãâ·Â
					JOptionPane.showMessageDialog(null, "WIN!");
				}
				else {
					JOptionPane.showMessageDialog(null, "LOSE!");
				}
					
			}
		});
		btnNewButton.setBounds(10, 271, 241, 152);
		frame.getContentPane().add(btnNewButton);
		
		// Â¦
		JButton btnNewButton_1 = new JButton("\uC9DD");
		btnNewButton_1.setFont(new Font("³Ø½¼ Ç²º¼°íµñ B", Font.PLAIN, 60));
		btnNewButton_1.setBackground(new Color(224, 255, 255));
		btnNewButton_1.setBounds(266, 271, 241, 152);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (oppMessage == "Â¦") {
					// »ó´ë¹æÀÌ º¸³½ ´äÀÌ Â¦ÀÌ¸é ½Â¸® Ãâ·Â
					JOptionPane.showMessageDialog(null, "WIN!");
				}
				else {
					JOptionPane.showMessageDialog(null, "LOSE!");
				}
			}
		});
		frame.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(10, 228, 290, 31);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("Send");
		btnNewButton_2.setBackground(new Color(230, 230, 250));
		btnNewButton_2.setFont(new Font("³Ø½¼ Ç²º¼°íµñ L", Font.PLAIN, 9));
		btnNewButton_2.setBounds(298, 228, 52, 31);
		frame.getContentPane().add(btnNewButton_2);
	}
}
