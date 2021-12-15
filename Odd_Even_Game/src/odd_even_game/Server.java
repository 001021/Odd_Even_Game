package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
	Database db = new Database();
	RoomManager roomManger;
   
   static HashMap<String, Object> loginList;		// login list
   static HashMap<String, Object> waitingList;   // waiting List
   static ArrayList<GameUser> GamersList;      // user outputStream who is gaming hashMap
   
   
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
         welcomeSocket = new ServerSocket(nPort);		// welcome socket
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
      
   void sendToAll(String msg) {		// send all users in waiting room
      Iterator<String> it = waitingList.keySet().iterator();
      
      while(it.hasNext()) {
         try {
            DataOutputStream out = (DataOutputStream)waitingList.get(it.next());
            out.writeUTF("chating in");
            out.writeUTF(msg);
         } catch(IOException e) {}
      }
   }
   
   
   boolean sendRequestGame(String myNickName, String oppNickName) {		// send battle request to one user
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
   
   void sendToGameRoom(String msg, GameRoom room) {		// send 1 : 1 message in game room in ready state
      try {
         DataOutputStream sendout1 = (DataOutputStream)room.user1.out;
         DataOutputStream sendout2 = (DataOutputStream)room.user2.out;
         
         sendout1.writeUTF(msg);
         sendout2.writeUTF(msg);
         
      } catch(IOException e) {}
   }
   
   void sendTo1(String msg, GameRoom room) {		// send to user1 in game room
	      try {
	         DataOutputStream sendout1 = (DataOutputStream)room.user1.out;
	         sendout1.writeUTF(msg);
	      } catch(IOException e) {}
	   }
   
   void sendTo2(String msg, GameRoom room) {		// send to user2 in game room
	      try {
	         DataOutputStream sendout2 = (DataOutputStream)room.user2.out;
	         sendout2.writeUTF(msg);
	      } catch(IOException e) {}
	   }
   
   boolean checkResult(GameRoom room) {			// check odd_even game's result
	   if(room.ans.equals("odd"))
		   room.odd_even = 1;
	   else if (room.ans.equals("even"))
		   room.odd_even = 0;
		   
	   if(room.number % 2 == room.odd_even)
		   return true;
	   else
		   return false;
   }
   
   void update_waitingList() {		// update waiting list in waiting room when someone come or leave
	   Iterator<String> it = waitingList.keySet().iterator();
	   String info = "";
	   while(it.hasNext()) {
	      String nickName = it.next();
	      info = db.getUserInfo(nickName);
	   }
   }
   

   public static void main(String[] args) {s
      new Server().start();
   }
   
   class ServerReceiver extends Thread{   // Server Receiver
      Socket socket;
      DataInputStream in;
      DataOutputStream out;
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
            	
            	LocalDateTime today = LocalDateTime.now(); // today in localdatetime type
        		String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // today time in string type
            	
            	if (loginList.get(name) != null) {
            		if(query.equals("login")) {		// login
            			String id = in.readUTF();
                    	String password = in.readUTF();
                    	
                    	if(db.IsValid(id, password)) {
                    		out.writeUTF("Success");
                    		out.writeUTF(db.getNickName(id));
                    		loginList.remove(name);
                    		waitingList.put(db.getNickName(id), out);
                    		
                    		name = db.getNickName(id);
                    		db.updateConnection(name, todayString, socket.getInetAddress());
                    		
                    	}
                    	else
                    		out.writeUTF("Fail");
            		}
            		else {		// join
            			String id = in.readUTF();
                    	String password = in.readUTF();
                    	name = in.readUTF();
                    	String nickName = in.readUTF();
                    	String email = in.readUTF();
                    	String sns = in.readUTF();
                    	
                    	if(db.addUser(id, password, name, nickName, email, sns, socket.getInetAddress(), todayString)){		// add user in user database
                    		System.out.println(id + " : join complete!");
                    		out.writeUTF("Success");
                    	}
                    	else {
                    		System.out.println(id + " : join failed!");		// join failed because of redundant id
                    		out.writeUTF("Fail");
                    	}
            		}
                }
            	
            	else if (waitingList.get(name) != null) {
            		
            		if(query.equals("memberInfo")) {		// check users information
            			String nickName = in.readUTF();
            			String info = db.getUserInfo(nickName);
            			
            			out.writeUTF("memberInfo res");
            			out.writeUTF(info);
            		}
            		else if (query.equals("battleRequest")) {		// receive battle request
            			System.out.println("battleRequest");
            			String oppNickName = in.readUTF();
            			if(!sendRequestGame(name, oppNickName))
            				out.writeUTF("Error : write correct nickname");
            			out.writeUTF(oppNickName);
            		}
            		
            		else if(query.equals("yes")) {		// receive yes answer for battle request
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
            		
            		else if(query.equals("no")) {		// receive no answer for battle request
            			String oppNickName = in.readUTF();
            			DataOutputStream resout = (DataOutputStream)waitingList.get(oppNickName);
            			resout.writeUTF("rejected");
            			resout.writeUTF(oppNickName);
            		}
            		
            		else if (query.equals("chat")) {		// want to chat in waiting room
            			sendToAll(in.readUTF());
            		}
            	}
            	
            	else {
            		int gameRoomNum = 0;
            		
            		for(int i=0; i < RoomManager.roomList.size(); i++) {		// find game room that the user is in
            			if (RoomManager.roomList.get(i).user1.nickName.equals(name) ||
            					RoomManager.roomList.get(i).user2.nickName.equals(name)) {
            				gameRoomNum = i;
            				break;
            			}
            		}
            		
            		if(query.equals("11 chat")) {		// want to 1:1 chat in game room in ready state
            			String msg = in.readUTF();
            			sendToGameRoom("game chating in", RoomManager.roomList.get(gameRoomNum));
            			sendToGameRoom(msg, RoomManager.roomList.get(gameRoomNum));
            		}
            		
            		else if(query.equals("ready")) {		// ready for this game
            			RoomManager.roomList.get(gameRoomNum).ready ++;
            			if(RoomManager.roomList.get(gameRoomNum).checkAllReady()) {
            				sendTo1("defence", RoomManager.roomList.get(gameRoomNum));
            			}
            		}
            		
            		else if (query.equals("how many")) {		// set marbles for defence
            			RoomManager.roomList.get(gameRoomNum).number = Integer.parseInt(in.readUTF());
        				sendTo2("attack", RoomManager.roomList.get(gameRoomNum));
            		}
            		
            		else if (query.equals("ans is")) {		// guess odd or even for attack
            			RoomManager.roomList.get(gameRoomNum).ans = in.readUTF();
            			
            			if(checkResult(RoomManager.roomList.get(gameRoomNum))){			// check result and alert who is winner
            				sendTo2("you win", RoomManager.roomList.get(gameRoomNum));
            				sendTo1("you lose", RoomManager.roomList.get(gameRoomNum));
            				
            				db.updateLose(RoomManager.roomList.get(gameRoomNum).user1.nickName);
            				db.updateWin(RoomManager.roomList.get(gameRoomNum).user2.nickName);
            			}
            			else {
            				sendTo1("you win", RoomManager.roomList.get(gameRoomNum));
            				sendTo2("you lose", RoomManager.roomList.get(gameRoomNum));
            				
            				db.updateLose(RoomManager.roomList.get(gameRoomNum).user2.nickName);
            				db.updateWin(RoomManager.roomList.get(gameRoomNum).user1.nickName);
            			}
            		}
            		
            		else if (query.equals("go to waiting room")) {		// go back to waiting room
            			RoomManager.RemoveRoom(RoomManager.roomList.get(gameRoomNum));
            		}
            		
            	}
            }// while(in != null)
            
         } catch(IOException e) {
        	 System.out.println(e);
         } finally {
            loginList.remove(name);
            System.out.println("[" + socket.getInetAddress() + " : " +
                  socket.getPort() + "] connection closed");
            System.out.println("The current number of server users : " + waitingList.size());
            
         }
      }
   }
   
   public static class RoomManager {		// manage for multiroom structure
      static List<GameRoom> roomList;
      
      RoomManager(){
         roomList = new ArrayList<GameRoom>();
      }
      
      GameRoom CreateRoom(GameUser user1, GameUser user2) {		// create game room
         GameRoom room = new GameRoom(user1, user2);
         roomList.add(room);
         
         waitingList.remove(user1.nickName);
         waitingList.remove(user2.nickName);
         
         System.out.println("Room Created!");
         
         return room;
      }
      
      public static void RemoveRoom(GameRoom room) {		// remove game room
         waitingList.put(room.user1.nickName, room.user1.socket);
         waitingList.put(room.user2.nickName, room.user2.socket);
         roomList.remove(room);
         
         System.out.println("Room Deleted!");
         System.out.println("The current number of server users : " + waitingList.size());
      }
      
      public int RoomCount(){		// return number of game room that is running
         return roomList.size();
      }
   }
}