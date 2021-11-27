package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	Database db = new Database();
	RoomManager roomManger;
   
   static HashMap<String, Object> loginList;
   static HashMap<String, Object> waitingList;   // 대기실에 waiting List
   static ArrayList<GameUser> userSocket;      // user outputStream 모아놓은 hashMap

   
   Server() {
      loginList = new HashMap<String, Object>();
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
      new Server().start();
   }
   
   class ServerReceiver extends Thread{   // Server Receiver 유저들의 채팅 받는 곳
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
            System.out.println(name);
//            sendToAll("#" + name + " came in");
            
            loginList.put(name, out);
            System.out.println("The current number of server users : " + loginList.size());
            
            
//            userSocket.add(new GameUser(name, socket));   // GameUser arrayList에 추가
//            if (userSocket.size() % 2 == 0) {
//               roomManger.CreateRoom(userSocket.get(0), userSocket.get(1));
//            }
            
            
            
            while(in != null) {
            	if (loginList.get(name) != null) {
                	String id = in.readUTF();
                	String password = in.readUTF();
                	
                	if(db.loginCheck(id, password)) {
                		out.writeUTF("Success");
                	}
                	else
                		out.writeUTF("Fail");
                }
            	
            	else if (waitingList.get(name) != null)
                  sendToAll(in.readUTF());
            	else
                  for(int i=0; i < RoomManager.roomList.size(); i++) {
                     if (RoomManager.roomList.get(i).userList.get(0).nickName.equals(name) || RoomManager.roomList.get(i).userList.get(1).nickName.equals(name)) {
                        sendToGameRoom(in.readUTF(), RoomManager.roomList.get(i));
                        break;
                     }
                  }
            } // while(in != null)
            
         } catch(IOException e) {
            
         } finally {
            sendToAll("#" + name + " left");
            waitingList.remove(name);
            System.out.println("[" + socket.getInetAddress() + " : " +
                  socket.getPort() + "] connection closed");
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