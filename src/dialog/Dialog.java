package dialog;

public class Dialog 
{
	long ID;	
	String title;
	int isUnread;
	Message lastMessage;
		
	public long ID() {return this.ID;}
	public String title() {return this.title;}
	public int isUnread() {return this.isUnread;}
	public Message lastMessage() {return this.lastMessage;}
}
