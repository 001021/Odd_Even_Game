package odd_even_game;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class WaitingRoomClient {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		String nickName;
		
		System.out.println("Write your nickName : ");
		nickName = keyboard.nextLine();
		
		try {
			String serverIp = "127.0.0.1";
			int nPort = 21118;
			Socket clientSocket = new Socket(serverIp, nPort);
			System.out.println("Connected to Server");
			Thread sender = new Thread(new ClientSender(clientSocket, nickName));
			Thread receiver = new Thread(new ClientReceiver(clientSocket));
			
			sender.start();
			receiver.start();
		} catch(ConnectException ce) {
			ce.printStackTrace();
		} catch(Exception e) {}
	}
	
	static class ClientSender extends Thread{
		Socket socket;
		DataOutputStream out;
		String name;
		
		ClientSender(Socket socket, String name){
			this.socket = socket;
			try {
				this.out = new DataOutputStream(socket.getOutputStream());
				this.name = name;
			} catch(Exception e) {}
		}
		
		public void run() {
			Scanner keyboard = new Scanner(System.in);
			try {
				if(out != null) {
					out.writeUTF(name); // 처음 닉네임 send
				}
				
				while(out != null) {
					out.writeUTF("[" + name + "] " + keyboard.nextLine());
				}
			} catch(IOException e) {}
			
			keyboard.close();
		}
	}
	
	static class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		
		ClientReceiver(Socket socket){
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch(IOException e) {}
			
		}
		
		public void run() {
			while (in != null) {
				try {
					System.out.println(in.readUTF());
				} catch(IOException e) {}
			}
		}
	}
}
