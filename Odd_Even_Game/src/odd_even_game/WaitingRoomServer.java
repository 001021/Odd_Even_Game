package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;

public class WaitingRoomServer {
	RoomManager roomManger;
	
	static HashMap<String, Object> userSocket;	// user outputStream 모아놓은 hashMap
	static ArrayList<GameUser> waitingList;		// 대기실에 waiting List

	
	WaitingRoomServer() {
		userSocket = new HashMap<String, Object>();
		Collections.synchronizedMap(userSocket);
		
		waitingList = new ArrayList<GameUser>();
		roomManger = new RoomManager();
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
		Iterator<String> it = userSocket.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)userSocket.get(it.next());
				out.writeUTF(msg);
			} catch(IOException e) {}
		}
	}

	public static void main(String[] args) {
		new WaitingRoomServer().start();
	}
	
	class ServerReceiver extends Thread{	// Server Receiver 유저들의 채팅 받는 곳
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
				
				userSocket.put(name, out);
				waitingList.add(new GameUser(name, socket));	// GameUser arrayList에 추가
				if (waitingList.size() % 2 == 0) {
					roomManger.CreateRoom(waitingList.get(0), waitingList.get(1));
				}
				
				System.out.println("The current number of server users : " + userSocket.size());
				
				while(in != null) {
					sendToAll(in.readUTF());
				}
			} catch(IOException e) {
				
			} finally {
				sendToAll("#" + name + " left");
				userSocket.remove(name);
				System.out.println("[" + socket.getInetAddress() + " : " +
						socket.getPort() + "] connection closed");
				System.out.println("The current number of server users : " + userSocket.size());
			}
		}
	}
	
	public static class RoomManager {
		static List<GameRoom> roomList;
		
		RoomManager(){
			roomList = new ArrayList<GameRoom>();
		}
		
		GameRoom CreateRoom(GameUser user1, GameUser user2) {
			GameRoom room = new GameRoom(user1, user2);
			roomList.add(room);
			System.out.println("Room Created!");
			
			waitingList.remove(user1);
			waitingList.remove(user2);
			
			return room;
		}
		
		public static void RemoveRoom(GameRoom room) {
			roomList.remove(room);
			System.out.println("Room Deleted!");
		}
		
		public int RoomCount(){
			return roomList.size();
		}
	}
}
