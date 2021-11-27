package odd_even_game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.*;

public class WaitRoomFrame extends JFrame{
	JTable table1, table2; // �� ������ �� table1, ������ ������ �� table2
	DefaultTableModel model1, model2;
	JTextPane pane;
	JTextField tf;
	JComboBox box;
	JButton b1, b2, b3, b4, b5, b6;
	JScrollBar bar;
	
	
	
	public static void main(String[] args) {
		new WaitRoomFrame();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {}
	}
	
	
	
	
	
	public WaitRoomFrame() {
		setTitle("Waiting Room");
		// �� ���� ���
		String col1[] = {"Room Name", "State", "Headcount"};
		String row1[][] = new String[0][3];
		model1 = new DefaultTableModel(row1, col1);

		table1 = new JTable(model1);
		JScrollPane js1 = new JScrollPane(table1);

		// ������ ���� ���
		String col2[] = {"ID", "Nickname", "Record", "State"}; // ���̵�, ��ȭ��, ���� ����, ���� ����(������/�����)
		String row2[][] = new String[0][4];
		model2 = new DefaultTableModel(row2, col2);
		
		table1.getTableHeader().setReorderingAllowed(false); // ���̺� ����
		
		table2 = new JTable(model2);
		JScrollPane js2 = new JScrollPane(table2);
		table2.getTableHeader().setReorderingAllowed(false); // ���̺�2 ����
		
		pane = new JTextPane();
		pane.setEditable(false);
		JScrollPane js3 = new JScrollPane(pane);
		bar = js3.getVerticalScrollBar();
		JTextField tfChat = new JTextField();
		JButton sendBtn = new JButton("send");
		
		tf = new JTextField(15);
		box = new JComboBox();
		box.addItem("black");
		box.addItem("pink");
		box.addItem("yellow");
		box.addItem("cyan");
		box.addItem("megenta");
		box.addItem("green");
		box.addItem("blue"); // �޺� �ڽ� ����
		
		JPanel p1 = new JPanel();
		p1.add(tf); p1.add(box); // �˻� �г�
		
		JPanel p2 = new JPanel();
		b1 = new JButton("Create Room");
		b2 = new JButton("Enter Room");
		b3 = new JButton("1:1 ��û");
		b4 = new JButton("Send Message");
		b5 = new JButton("View information");
		b6 = new JButton("Exit");
		
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e1) {
				// �������� ��û�� ������?
				JOptionPane aa = new JOptionPane();
				String oppID = aa.showInputDialog("����� ��û�� ����� ID�� �Է��ϼ���.");
				// ���� ���̵� ������ ���̵� ������ ��� �޽��� ����
				if (oppID == "001021") {
					// �޽��� ����
				}
				else { // ������ �߸��ƴٰ� ����
					 JOptionPane.showMessageDialog(null, "���� ���� ID�� �ƴմϴ�!");
				}
			}
		});
		
		// Exit Button action
		b6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e2) {
				System.exit(0);
			}
		});

		
		p2.setLayout(new GridLayout(3, 2, 3, 3));
		p2.add(b1); p2.add(b2);
		p2.add(b3); p2.add(b4);
		p2.add(b5); p2.add(b6);
		
		// ��ġ
		setLayout(null);
		js1.setBounds(10, 15, 400, 250);
		js2.setBounds(10, 300, 400, 250);
		js3.setBounds(420, 15, 250, 220);
		tfChat.setBounds(420, 235, 170, 30);
		sendBtn.setBounds(590, 235, 80, 30);
		
		
		p1.setBounds(420, 300, 250, 50);
		p2.setBounds(420, 390, 250, 150);
		add(js1);
		add(js2);
		add(js3);
		add(tfChat);
		add(sendBtn);
		add(p1);
		add(p2);
		
		setSize(700, 600); // â ������
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
