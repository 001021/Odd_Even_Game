package odd_even_game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	private JTextField tfEmail;
	private JTextField tfPhone;
	
	DataOutputStream out;
    DataInputStream in;
	
	public JoinFrame() {
		setTitle("Join");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(430, 490);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblJoin = new JLabel("Join");
		Font f1 = new Font("µ¸¿ò", Font.BOLD, 20);
		lblJoin.setFont(f1);
		lblJoin.setBounds(159, 41, 101, 20);
		contentPane.add(lblJoin);
		
		JLabel lblID = new JLabel("ID");
		lblID.setBounds(69, 113, 69, 20);
		contentPane.add(lblID);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setBounds(69, 163, 69, 20);
		contentPane.add(lblPassword);
		
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setBounds(69, 210, 69, 20);
		contentPane.add(lblNickname);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(69, 257, 69, 20);
		contentPane.add(lblEmail);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(69, 304, 69, 20);
		contentPane.add(lblPhone);
		
		tfID = new JTextField();
		tfID.setColumns(10);
		tfID.setBounds(159, 106, 186, 35);
		contentPane.add(tfID);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(159, 156, 186, 35);
		contentPane.add(tfPassword);
		
		tfNickname = new JTextField();
		tfNickname.setColumns(10);
		tfNickname.setBounds(159, 206, 186, 35);
		contentPane.add(tfNickname);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(159, 256, 186, 35);
		contentPane.add(tfEmail);
		
		tfPhone = new JTextField();
		tfPhone.setColumns(10);
		tfPhone.setBounds(159, 306, 186, 35);
		contentPane.add(tfPhone);		
		
		joinCompleteBtn = new JButton("Join Complete");
		joinCompleteBtn.setBounds(206, 356, 139, 29);
		contentPane.add(joinCompleteBtn);
		
		setVisible(true);
		
		// join button action
		joinCompleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = tfID.getText();
	            String password = tfPassword.getText();
            	String name = tfPassword.getText();
            	String nickName = tfPassword.getText();
            	String email = tfPassword.getText();
            	String sns = tfPassword.getText(); // ¹Ù²ã¾ß µÊ!
				
				
	            try {
					out.writeUTF("join");
					if(id != null)
	            		out.writeUTF(id);
	            	if(password != null)
	            		out.writeUTF(password);
				} catch (IOException e1) {}
				
				
				
				
				
				
				JOptionPane.showMessageDialog(null, "Welcome!");
				dispose();
			}
		});
	}
}
