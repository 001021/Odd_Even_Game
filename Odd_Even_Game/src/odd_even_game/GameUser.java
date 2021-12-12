package odd_even_game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameUser {
	GameRoom room;
	Socket socket;
	String nickName;
	int id;
	
	DataInputStream in;
	DataOutputStream out;
	
	public GameUser() {
	
	}
	
	public GameUser(String nickName) {
		this.nickName = nickName;
	}
	
	public GameUser(String nickName, DataOutputStream dataOutputStream) {
		this.nickName = nickName;
		
		try {
			this.out = dataOutputStream;
		} catch(Exception e) {}
	}
	
	public GameUser(int id, String nickName) {
		this.id = id;
		this.nickName = nickName;
	}


}
