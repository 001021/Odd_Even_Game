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
	
	public GameUser(String nickName, Socket socket) {
		this.nickName = nickName;
		this.socket = socket;
		
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
		} catch(IOException e) {}
	}
	
	public GameUser(int id, String nickName) {
		this.id = id;
		this.nickName = nickName;
	}


}