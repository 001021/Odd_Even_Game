package odd_even_game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRoom {
	GameUser user1;
	GameUser user2;
	
	int ready = 0;
	int number = 0;
	String ans;
	int odd_even;
	
	public GameRoom() {}
	
	public GameRoom(GameUser user1, GameUser user2) {
		this.user1 = user1;
		this.user2 = user2;
		
//		try {
//			user1.out.writeUTF("Welcome");
//			user2.out.writeUTF("Welcome");
//		} catch (IOException e) {}
	}
	
	public void ExitGame(GameUser user) {	//Exit game
		Server.RoomManager.RemoveRoom(this);
	}
	
	public boolean checkAllReady() {
		if(ready == 2)
			return true;
		return false;
	}
	
	public void whoWin() {
		if(ans.equals("odd"))
			odd_even = 1;
		else if (ans.equals("even"))
			odd_even = 0;
		
		try {
			if(number % 2 == odd_even) {
				user2.out.writeUTF("you win");
				user1.out.writeUTF("you lose");
			 }
			 else {
				 user1.out.writeUTF("you win");
				 user2.out.writeUTF("you lose");
			 }
		}
		catch(Exception e) {}
	}
}