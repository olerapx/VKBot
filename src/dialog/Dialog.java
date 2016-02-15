package dialog;

/**
 * 
 * Base class for all chats.
 *
 */
public class Dialog 
{
	long ID;	
	String title;
	int unreadMessagesNumber;
	Message lastMessage;
		
	public long ID() {return this.ID;}
	public String title() {return this.title;}
	public int unreadMessagesNumber() {return this.unreadMessagesNumber;}
	public Message lastMessage() {return this.lastMessage;}
}
