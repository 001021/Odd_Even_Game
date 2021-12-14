package odd_even_game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class WaitingDialog extends JFrame{
	
	public WaitingDialog() {
		setTitle("대 결 대 기");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(456, 119);
		
		JLabel lblNewLabel = new JLabel("대결을 기다리는 중...");
		lblNewLabel.setFont(new Font("AppleSDGothicNeoM00", Font.ITALIC, 20));
		lblNewLabel.setBounds(0, 0, 456, 119);
		add(lblNewLabel);
	}
	
	public void main(String args[]) {
		new WaitingDialog();
	}
}
//
//	private JFrame frame;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					WaitingDialog window = new WaitingDialog();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public WaitingDialog() {
//		initialize();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frame = new JFrame();
//		frame.setTitle("대 결 대 기");
//		frame.setBounds(100, 100, 454, 117);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		JLabel lblNewLabel = new JLabel("대결을 기다리는 중...");
//		lblNewLabel.setFont(new Font("AppleSDGothicNeoM00", Font.ITALIC, 20));
//		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		frame.getContentPane().add(lblNewLabel, BorderLayout.CENTER);
//	}
//	
//	public void dispose() {
//		dispose();
//	}
//}
