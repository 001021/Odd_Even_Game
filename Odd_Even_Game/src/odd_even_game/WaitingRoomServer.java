package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;

public class WaitingRoomServer {
	HashMap<String, Object> users;
	
	WaitingRoomServer() {
		users = new HashMap<String, Object>();
		Collections.synchronizedMap(users);
	}
	
	public void start() {
		ServerSocket welcomeSocket = null;
		Socket connectionSocket = null;
		int nPort = 21118;
		
		try {
			welcomeSocket = new ServerSocket(nPort);
			System.out.println("Server start");
			
			while(true) {
				connectionSocket = welcomeSocket.accept();
				System.out.println("[" + connectionSocket.getInetAddress() + " : " +
						connectionSocket.getPort() + "] Connected");
				ServerReceiver thread = new ServerReceiver(connectionSocket);
				thread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	void sendToAll(String msg) {
		Iterator<String> it = users.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)users.get(it.next());
				out.writeUTF(msg);
			} catch(IOException e) {}
		}
	}

	public static void main(String[] args) {
		new WaitingRoomServer().start();
	}
	
	class ServerReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		
		ServerReceiver(Socket socket){
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch(IOException e) {}
		}
		
		public void run() {
			String name = "";
			
			try {
				name = in.readUTF();
				sendToAll("#" + name + " came in");
				
				users.put(name, out);
				System.out.println("The current number of server users : " + users.size());
				
				while(in != null) {
					sendToAll(in.readUTF());
				}
			} catch(IOException e) {
				
			} finally {
				sendToAll("#" + name + " left");
				users.remove(name);
				System.out.println("[" + socket.getInetAddress() + " : " +
						socket.getPort() + "] connection closed");
				System.out.println("The current number of server users : " + users.size());
			}
		}
	}
}
