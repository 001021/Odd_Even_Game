package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;

public class WaitingRoomServer {
	RoomManager roomManger;
	
	static HashMap<String, Object> waitingList;	// ���ǿ� waiting List
	static ArrayList<GameUser> userSocket;		// user outputStream ��Ƴ��� hashMap

	
	WaitingRoomServer() {
		waitingList = new HashMap<String, Object>();
		Collections.synchronizedMap(waitingList);
		
		userSocket = new ArrayList<GameUser>();
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
		Iterator<String> it = waitingList.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)waitingList.get(it.next());
				out.writeUTF(msg);
			} catch(IOException e) {}
		}
	}
	
	void sendToGameRoom(String msg, GameRoom room) {
		try {
			DataOutputStream out = (DataOutputStream)room.userList.get(0).out;
			out.writeUTF(msg);
			
			out = (DataOutputStream)room.userList.get(1).out;
			out.writeUTF(msg);
		} catch(IOException e) {}
	}

	public static void main(String[] args) {
		new WaitingRoomServer().start();
	}
	
	class ServerReceiver extends Thread{	// Server Receiver �������� ä�� �޴� ��
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
				
				waitingList.put(name, out);
				userSocket.add(new GameUser(name, socket));	// GameUser arrayList�� �߰�
				if (userSocket.size() % 2 == 0) {
					roomManger.CreateRoom(userSocket.get(0), userSocket.get(1));
				}
				
				System.out.println("The current number of server users : " + waitingList.size());
				
				while(in != null) {
					if (waitingList.get(name) != null)
						sendToAll(in.readUTF());
					else
						for(int i=0; i < RoomManager.roomList.size(); i++) {
							if (RoomManager.roomList.get(i).userList.get(0).nickName.equals(name) || RoomManager.roomList.get(i).userList.get(1).nickName.equals(name)) {
								sendToGameRoom(in.readUTF(), RoomManager.roomList.get(i));
								break;
							}
						}
				}
			} catch(IOException e) {
				
			} finally {
				sendToAll("#" + name + " left");
				waitingList.remove(name);
				System.ouprintln("[" + socket.getInetAddress() + " : " +
						socket.t.getPort() + "] connection closed");
				System.out.println("The current number of server users : " + waitingList.size());
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
			
			waitingList.remove(user1.nickName);
			waitingList.remove(user2.nickName);
			
			return room;
		}
		
		public static void RemoveRoom(GameRoom room) {
			waitingList.put(room.userList.get(0).nickName, room.userList.get(0).socket);
			waitingList.put(room.userList.get(1).nickName, room.userList.get(1).socket);
			roomList.remove(room);
			
			System.out.println("Room Deleted!");
			System.out.println("The current number of server users : " + waitingList.size());
		}
		
		public int RoomCount(){
			return roomList.size();
		}
	}
}
