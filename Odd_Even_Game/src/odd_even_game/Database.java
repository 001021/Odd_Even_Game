package odd_even_game;

import java.sql.*;

public class Database {
	public static void main(String[] args) {
		new Database();
	}
		/* �����ͺ��̽����� ���ῡ ����� ������ */
		Connection con = null;
		Statement stmt = null;
		String url = "jdbc:mysql://localhost/user?serverTimezone=Asia/Seoul";
		String user = "ogame";
		String passwd = "oddevengame12345";
		
		Database() {	//Database ��ü ���� �� �����ͺ��̽� ������ �����Ѵ�.
			try {	//�����ͺ��̽� ������ try-catch������ ���ܸ� ����ش�.
				//�����ͺ��̽��� �����Ѵ�.
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection(url, user, passwd);
				stmt = con.createStatement();
				System.out.println("[Server] MySQL ���� ���� ����");	//�����ͺ��̽� ���ῡ �����ϸ� ������ �ַܼ� �˸���.
			} catch(Exception e) {	//�����ͺ��̽� ���ῡ ���ܰ� �߻����� �� ���и� �ַܼ� �˸���.
				System.out.println("[Server] MySQL ���� ���� ����> " + e.toString());
			}
		}
		
		//�α��� ���θ� Ȯ���ϴ� �޼ҵ�. ������ �г����� String �������� ��ȯ�Ѵ�.
		boolean loginCheck(String _i, String _p) {
			String nickname = "null";	//��ȯ�� �г��� ������ "null"�� �ʱ�ȭ.
			
			//�Ű������� ���� id�� password���� id�� pw���� �ʱ�ȭ�Ѵ�.
			String id = _i;
			String pw = _p;
			
			try {
				//id�� ��ġ�ϴ� ��й�ȣ�� �г����� �ִ��� ��ȸ�Ѵ�.
				String checkingStr = "SELECT password, nickname FROM user_info WHERE id='" + id + "'";
				ResultSet result = stmt.executeQuery(checkingStr);
				
				int count = 0;
				while(result.next()) {
					//��ȸ�� ��й�ȣ�� pw ���� ��.
					if(pw.equals(result.getString("password"))) {	//true�� ��� nickname�� ��ȸ�� �г��ӿ� ��ȯ�ϰ� �α��� ������ �ַܼ� �˸���.
						nickname = result.getString("nickname");
						System.out.println("[Server] �α��� ����");
					}
					
					else {	//false�� ��� nickname�� "null"�� �ʱ�ȭ�ϰ� �α��� ���и� �ַܼ� �˸���.
						nickname = "null";
						System.out.println("[Server] �α��� ����");
					}
					count++;
				}
			} catch(Exception e) {	//��ȸ�� �������� �� nickname�� "null"�� �ʱ�ȭ. ���и� �ַܼ� �˸���.
				System.out.println("[Server] �α��� ���� > " + e.toString());
				return false;
			}
			
			return true;
		}
		
		//ȸ�������� �����ϴ� �޼ҵ�. ȸ�����Կ� �����ϸ� true, �����ϸ� false�� ��ȯ�Ѵ�.
		boolean joinCheck(String _n, String _nn, String _i, String _p) {
			boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false
			
			//�Ű������� ���� �� ���ڿ����� �� ������ �ʱ�ȭ�Ѵ�.
			String na = _n;
			String nn = _nn;
			String id = _i;
			String pw = _p;
			
			try {
				//user_info ���̺� �� ���ڿ����� ������� ������Ʈ�ϴ� ����. ��, �д� �ʱⰪ�� ���� 0���� �Ѵ�.
				String insertStr = "INSERT INTO user_info VALUES('" + na + "', '" + nn + "', '" + id + "', '" + pw + "', 0, 0)";
				stmt.executeUpdate(insertStr);
				
				flag = true;	//������Ʈ���� ���������� ����Ǹ� flag�� true�� �ʱ�ȭ�ϰ� ������ �ַܼ� �˸���.
				System.out.println("[Server] ȸ������ ����");
			} catch(Exception e) {	//ȸ������ ������ �������� ���ϸ� flag�� false�� �ʱ�ȭ�ϰ� ���и� �ַܼ� �˸���.
				flag = false;
				System.out.println("[Server] ȸ������ ���� > " + e.toString());
			}
			
			return flag;	//flag ��ȯ
		}
		
		//���̵� �г����� �ߺ��Ǿ����� Ȯ�����ִ� �޼ҵ�. �ߺ� ���� �����ϸ� false, �������� ������ true�� ��ȯ�Ѵ�.
		boolean overCheck(String _a, String _v) {
			boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false
			
			//att�� �Ӽ�(���̵�, �г���)�� �����ϰ�, val�� Ȯ���� ���� �ʱ�ȭ.
			String att = _a;
			String val = _v;
			
			try {
				//user_info ���̺� �����ϴ� ���̵�(Ȥ�� �г���)�� ��� ã�´�.
				String selcectStr = "SELECT " + att + " FROM user_info";
				ResultSet result = stmt.executeQuery(selcectStr);
				
				int count = 0;
				while(result.next()) {
					//��ȸ�� ���̵�(Ȥ�� �г���)�� val�� ��.
					if(!val.equals(result.getString(att))) {	//val�� ���� ���� �����ϸ� flag�� true�� �����Ѵ�.
						flag = true;
					}
					
					else {	//val�� ���� ���� �������� ������ flag�� false�� �����Ѵ�.
						flag = false;
					}
					count++;
				}
				System.out.println("[Server] �ߺ� Ȯ�� ����");	//���������� ����Ǿ��� �� ������ �ַܼ� �˸���.
			} catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
				System.out.println("[Server] �ߺ� Ȯ�� ���� > " + e.toString());
			}
			
			return flag;	//flag ��ȯ
		}
		
		//�����ͺ��̽��� ����� �ڽ��� ������ ��ȸ�ϴ� �޼ҵ�. ��ȸ�� �������� String ���·� ��ȯ�Ѵ�.
		String viewInfo(String _nn) {
			String msg = "null";	//��ȯ�� ���ڿ� ������ "null"�� �ʱ�ȭ.
			
			//�Ű������� ���� �г����� nick�� �ʱ�ȭ�Ѵ�.
			String nick = _nn;
			
			try {
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� �̸��� ��ȸ�Ѵ�.
				String viewStr = "SELECT name FROM user_info WHERE nickname='" + nick + "'";
				ResultSet result = stmt.executeQuery(viewStr);
				
				int count = 0;
				while(result.next()) {
					//msg�� "�̸�//�г���" ���·� �ʱ�ȭ�Ѵ�.
					msg = result.getString("name") + "//" + nick ;
					count++;
				}
				System.out.println("[Server] ȸ������ ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
			} catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
				System.out.println("[Server] ȸ������ ��ȸ ���� > " + e.toString());
			}
			
			return msg;	//msg ��ȯ
		}
		boolean changeInfo(String _nn, String _a, String _v) {
			boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.
			
			//�Ű������� ���� �������� �ʱ�ȭ�Ѵ�. att�� �Ӽ�(�̸�, ��й�ȣ) ���п��̰� val�� �ٲ� ��.
			String nick = _nn;
			String att = _a;
			String val = _v;
			
			try {
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� att(�̸�, ��й�ȣ)�� val�� �����Ѵ�.
				String changeStr = "UPDATE user_info SET " + att + "='" + val + "' WHERE nickname='" + nick +"'";
				stmt.executeUpdate(changeStr);
				
				flag = true;	//���������� ����Ǹ� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
				System.out.println("[Server] ȸ������ ���� ����");
			} catch(Exception e) {	//���������� �������� ���ϸ� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
				flag = false;
				System.out.println("[Server] ȸ������ ���� ���� > " + e.toString());
			}
			
			return flag;	//flag ��ȯ
		}
		
		//��ü ȸ���� ������ ��ȸ�ϴ� �޼ҵ�. ��� ȸ���� ������ String ���·� ��ȯ�Ѵ�.
		String viewRank() {
			String msg = "";	//������ ���� ���ڿ�. �ʱⰪ�� ""�� �Ѵ�.
			
			try {
				//user_info ���̺��� �г���, ��, �и� ��� ��ȸ�Ѵ�.
				String viewStr = "SELECT nickname, win, lose FROM user_info";
				ResultSet result = stmt.executeQuery(viewStr);
				
				int count = 0;
				while(result.next()) {
					//������ msg�� "�г��� : n�� n��@" ������ ���ڿ��� ����ؼ� �߰��Ѵ�.
					msg = msg + result.getString("nickname") + " : " + result.getInt("win") + "�� " + result.getInt("lose") + "��@";
					count++;
				}
				System.out.println("[Server] ���� ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
			} catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
				System.out.println("[Server] ���� ��ȸ ���� > " + e.toString());
			}
			
			return msg;	//msg ��ȯ
		}
		
		//�� ���� ȸ���� ������ ��ȸ�ϴ� �޼ҵ�. �ش� ȸ���� ������ String ���·� ��ȯ�Ѵ�.
		String searchRank(String _nn) {
			String msg = "null";	//������ ���� ���ڿ�. �ʱⰪ�� "null"�� �Ѵ�.
			
			//�Ű������� ���� �г����� �ʱ�ȭ�Ѵ�.
			String nick = _nn;
			
			try {
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� ��, �и� ��ȸ�Ѵ�.
				String searchStr = "SELECT win, lose FROM user_info WHERE nickname='" + nick + "'";
				ResultSet result = stmt.executeQuery(searchStr);
				
				int count = 0;
				while(result.next()) {
					//msg�� "�г��� : n�� n��" ������ ���ڿ��� �ʱ�ȭ�Ѵ�.
					msg = nick + " : " + result.getInt("win") + "�� " + result.getInt("lose") + "��";
					count++;
				}
				System.out.println("[Server] ���� ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
			} catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
				System.out.println("[Server] ���� ��ȸ ���� > " + e.toString());
			}
			
			return msg;	//msg ��ȯ
		}
		
		//���� �¸� �� ������ ������Ʈ�ϴ� �޼ҵ�. ��ȸ �� ������Ʈ�� �����ϸ� true, �����ϸ� false�� ��ȯ�Ѵ�.
		boolean winRecord(String _nn) {
			boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.
			
			//�Ű������� ���� �г��Ӱ� ��ȸ�� �¸� Ƚ���� ������ ����. num�� �ʱⰪ�� 0.
			String nick = _nn;
			int num = 0;
			
			try {
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� ��ȸ�Ѵ�.
				String searchStr = "SELECT win FROM user_info WHERE nickname='" + nick + "'";
				ResultSet result = stmt.executeQuery(searchStr);
				
				int count = 0;
				while(result.next()) {
					//num�� ��ȸ�� �¸� Ƚ���� �ʱ�ȭ.
					num = result.getInt("win");
					count++;
				}
				num++;	//�¸� Ƚ���� �ø�
				
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� num���� ������Ʈ�Ѵ�.
				String changeStr = "UPDATE user_info SET win=" + num + " WHERE nickname='" + nick +"'";
				stmt.executeUpdate(changeStr);
				flag = true;	//��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
				System.out.println("[Server] ���� ������Ʈ ����");
			} catch(Exception e) {	//��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
				flag = false;
				System.out.println("[Server] ���� ������Ʈ ���� > " + e.toString());
			}
			
			return flag;	//flag ��ȯ
		}
		
		//���� �й� �� ������ ������Ʈ�ϴ� �޼ҵ�. ��ȸ �� ������Ʈ�� �����ϸ� true, �����ϸ� false�� ��ȯ�Ѵ�.
		boolean loseRecord(String _nn) {
			boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.
			
			//�Ű������� ���� �г��Ӱ� ��ȸ�� �й� Ƚ���� ������ ����. num�� �ʱⰪ�� 0.
			String nick =  _nn;
			int num = 0;
			
			try {
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� �й� Ƚ���� ��ȸ�Ѵ�.
				String searchStr = "SELECT lose FROM user_info WHERE nickname='" + nick + "'";
				ResultSet result = stmt.executeQuery(searchStr);
				
				int count = 0;
				while(result.next()) {
					//num�� ��ȸ�� �й� Ƚ���� �ʱ�ȭ.
					num = result.getInt("lose");
					count++;
				}
				num++;	//�й� Ƚ���� �ø�
				
				//user_info ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� num���� ������Ʈ�Ѵ�.
				String changeStr = "UPDATE user_info SET lose=" + num + " WHERE nickname='" + nick +"'";
				stmt.executeUpdate(changeStr);
				flag = true;	//��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
				System.out.println("[Server] ���� ������Ʈ ����");
			} catch(Exception e) {	//��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
				flag = false;
				System.out.println("[Server] Error: > " + e.toString());
			}
			
			return flag;	//flag ��ȯ
		}
}
