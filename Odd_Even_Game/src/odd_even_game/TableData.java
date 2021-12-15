package odd_even_game;

public class TableData {
	private String ID;
	private String nickName;
	private int record;
	
	public TableData(String ID, String nickName, int record) {
		super();
		this.ID = ID;
		this.nickName = nickName;
		this.record = record;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public int getRecord() {
		return record;
	}
	
	public String toString() {
		return ID + "/" + nickName + "/" + record;
	}
}
