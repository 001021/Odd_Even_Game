package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
	Database db = new Database();
	RoomManager roomManger;
   
   static HashMap<String, Object> loginList;
   static HashMap<String, Object> waitingList;   // ���ǿ� waiting List
   static ArrayList<GameUser> GamersList;      // user outputStream ��Ƴ��� hashMap
   
   
   Server() {
      loginList = new HashMap<String, Object>();
      waitingList = new HashMap<String, Object>();
      Collections.synchronizedMap(waitingList);
      
      GamersList = new ArrayList<GameUser>();
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
            out.writeUTF("chating in");
            out.writeUTF(msg);
         } catch(IOException e) {}
      }
   }
   
   
   boolean sendRequestGame(String myNickName, String oppNickName) {
	      try {
	    	  DataOutputStream sendout = (DataOutputStream)waitingList.get(oppNickName);
	    	  
	    	  if(sendout == null)
	    		  return false;
	    	  
	    	  sendout.writeUTF("battleRequest from others");
	    	  sendout.writeUTF(myNickName);
	    	  
	    	  System.out.println("battleRequest from others");
	    	  return true;
	      } catch(IOException e) {}
	      
	      return false;
   }
   
   void sendToGameRoom(String msg, GameRoom room) {
      try {
         DataOutputStream sendout1 = (DataOutputStream)room.user1.out;
         DataOutputStream sendout2 = (DataOutputStream)room.user2.out;
         
         sendout1.writeUTF(msg);
         sendout2.writeUTF(msg);
         
      } catch(IOException e) {}
   }
   
   void sendTo1(String msg, GameRoom room) {
	      try {
	         DataOutputStream sendout1 = (DataOutputStream)room.user1.out;
	         sendout1.writeUTF(msg);
	      } catch(IOException e) {}
	   }
   
   void sendTo2(String msg, GameRoom room) {
	      try {
	         DataOutputStream sendout2 = (DataOutputStream)room.user2.out;
	         sendout2.writeUTF(msg);
	      } catch(IOException e) {}
	   }
   
   void sendResult(GameRoom room) {
	   if(room.ans.equals("odd"))
		   room.odd_even = 1;
	   else if (room.ans.equals("even"))
		   room.odd_even = 0;
	
	try {
		DataOutputStream sendout1 = (DataOutputStream)room.user1.out;
		DataOutputStream sendout2 = (DataOutputStream)room.user2.out;
        
		if(room.number % 2 == room.odd_even) {
			System.out.println("correct");
			sendout2.writeUTF("you win");
			sendout1.writeUTF("you lose");
		 }
		 else {
			 System.out.println("incorrect");
			 sendout1.writeUTF("you win");
			 sendout2.writeUTF("you lose");
		 }
	}
	catch(Exception e) {}
   }
   

   public static void main(String[] args) {
      new Server().start();
   }
   
   class ServerReceiver extends Thread{   // Server Receiver �������� ä�� �޴� ��
      Socket socket;
      DataInputStream in;
      DataOutputStream out;
      private String myNickName;
      Socket oppSocket;
      
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
            
            loginList.put(name, out);
            System.out.println("The current number of server users : " + loginList.size());
            
            
            while(in != null) {
            	String query = in.readUTF();
            	
            	if (loginList.get(name) != null) {
            		if(query.equals("login")) {
            			String id = in.readUTF();
                    	String password = in.readUTF();
                    	
                    	if(db.IsValid(id, password)) {
                    		out.writeUTF("Success");
                    		out.writeUTF(db.getNickName(id));
                    		loginList.remove(name);
                    		waitingList.put(db.getNickName(id), out);
                    		
                    		name = db.getNickName(id);
                    	}
                    	else
                    		out.writeUTF("Fail");
            		}
            		else {
            			String id = in.readUTF();
                    	String password = in.readUTF();
                    	name = in.readUTF();
                    	String nickName = in.readUTF();
                    	String email = in.readUTF();
                    	String sns = in.readUTF();
                    	
                    	LocalDateTime today = LocalDateTime.now(); // ���� ��¥
                		String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // ���� ��¥ �˻� ���ǿ� �°� ����
                    	
                    	if(db.addUser(id, password, name, nickName, email, sns, socket.getInetAddress(), todayString)){
                    		System.out.println(id + " : join complete!");
                    		out.writeUTF("Success");
                    	}
                    	else {
                    		System.out.println(id + " : join failed!");
                    		out.writeUTF("Fail");
                    	}
            		}
                }
            	
            	else if (waitingList.get(name) != null) {
            		
            		if(query.equals("memberInfo")) {
            			String nickName = in.readUTF();
            			String info = db.getUserInfo(nickName);
            			
            			out.writeUTF("memberInfo res");
            			out.writeUTF(info);
            		}
            		else if (query.equals("battleRequest")) {
            			System.out.println("battleRequest");
            			String oppNickName = in.readUTF();
            			if(!sendRequestGame(name, oppNickName))
            				out.writeUTF("Error : write correct nickname");
            			out.writeUTF(oppNickName);
            		}
            		
            		else if(query.equals("yes")) {
            			String oppNickName = in.readUTF();
            			
            			DataOutputStream resout = (DataOutputStream)waitingList.get(oppNickName);
            			resout.writeUTF("game start");
            			out.writeUTF("game start");
            			
            			GameUser user1 = new GameUser(name, out);
            			GameUser user2 = new GameUser(oppNickName, (DataOutputStream)waitingList.get(oppNickName));
            			GamersList.add(user1);
            			GamersList.add(user2);
            			roomManger.CreateRoom(user1, user2);
            			
            			
            			System.out.println(RoomManager.roomList.get(0).user1.nickName);
        				System.out.println(RoomManager.roomList.get(0).user2.nickName);
            			
            			oppSocket = user2.socket;
            		}
            		
            		else if(query.equals("no")) {
            			String oppNickName = in.readUTF();
            			DataOutputStream resout = (DataOutputStream)waitingList.get(oppNickName);
            			resout.writeUTF("rejected");
            			resout.writeUTF(oppNickName);
            		}
            		
            		else if (query.equals("chat")) {
            			sendToAll(in.readUTF());
            		}
            	}
            	
            	else {
            		System.out.println(name + query);
            		
            		int gameRoomNum = 0;
            		
            		for(int i=0; i < RoomManager.roomList.size(); i++) {
            			if (RoomManager.roomList.get(i).user1.nickName.equals(name) ||
            					RoomManager.roomList.get(i).user2.nickName.equals(name)) {
            				gameRoomNum = i;
            				break;
            			}
            		}
            		
            		if(query.equals("11 chat")) {
            			String msg = in.readUTF();
            			sendToGameRoom("game chating in", RoomManager.roomList.get(gameRoomNum));
            			sendToGameRoom(msg, RoomManager.roomList.get(gameRoomNum));
            		}
            		
            		else if(query.equals("ready")) {
            			RoomManager.roomList.get(gameRoomNum).ready ++;
            			if(RoomManager.roomList.get(gameRoomNum).checkAllReady()) {
            				sendTo1("defence", RoomManager.roomList.get(gameRoomNum));
            			}
            		}
            		
            		else if (query.equals("how many")) {
            			RoomManager.roomList.get(gameRoomNum).number = Integer.parseInt(in.readUTF());
        				sendTo2("attack", RoomManager.roomList.get(gameRoomNum));
            		}
            		
            		else if (query.equals("ans is")) {
            			RoomManager.roomList.get(gameRoomNum).ans = in.readUTF();
            			
            			System.out.println(RoomManager.roomList.get(gameRoomNum).number + RoomManager.roomList.get(gameRoomNum).ans);
            			sendResult(RoomManager.roomList.get(gameRoomNum));
            		}
            		
            	}
            }// while(in != null)
            
         } catch(IOException e) {
        	 System.out.println(e);
         } finally {
            sendToAll("#" + name + " left");
            loginList.remove(name);
            System.out.println("[" + socket.getInetAddress() + " : " +
                  socket.getPort() + "] connection closed");
            System.out.println("The current number of server users : " + loginList.size());
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
         
         waitingList.remove(user1.nickName);
         waitingList.remove(user2.nickName);
         
         System.out.println("Room Created!");
         
         return room;
      }
      
      public static void RemoveRoom(GameRoom room) {
         waitingList.put(room.user1.nickName, room.user1.socket);
         waitingList.put(room.user2.nickName, room.user2.socket);
         roomList.remove(room);
         
         System.out.println("Room Deleted!");
         System.out.println("The current number of server users : " + waitingList.size());
      }
      
      public int RoomCount(){
         return roomList.size();
      }
   }
}