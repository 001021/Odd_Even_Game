package odd_even_game;

import javax.swing.*;
import java.awt.*;

import java.awt.TextField;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//로그인 GUI
public class LoginFrame extends JFrame {
	Database db = new Database();
	
	public LoginFrame() {
		
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

		// Login Button action
		logBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e2) {
				Database.loginCheck(txtID.getText(), txtPass.getText());
//				try{
//					String s;
//					String[] array;
//					BufferedReader bos = new BufferedReader(new FileReader("members.txt"));
//					while((s=bos.readLine())!=null){
//						array=s.split("/");
//					if(txtID.getText().equals(array[0])&&txtPass.getText().equals(array[1]))
//					{
//						JOptionPane.showMessageDialog(null, "게임 한 판 할까요!");
//						// User가 로그인 했을 때 뜨는 창으로 연결
//						new WaitRoomFrame();
//					}
//					else 
//					{
//						JOptionPane.showMessageDialog(null, "로그인 실패");
//					}
//					}
//					bos.close();
//				}catch (IOException E10){
//					System.out.println("파일 읽어오기 실패");
//				}
			}
		});
		
		// Join Button Action
		joinBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new JoinFrame();
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
		new LoginFrame();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {}
	}

}
