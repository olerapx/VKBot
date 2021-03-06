package api.dialog;

import api.object.VKObject;

/**
 * Base class for all chats.
 */
public class Dialog extends VKObject
{
	private static final long serialVersionUID = 4901355272877799949L;
	
	long ID;	
	String title;
	int unreadMessagesNumber;
	Message lastMessage;
		
	public long ID() {return this.ID;}
	public String title() {return this.title;}
	public int unreadMessagesNumber() {return this.unreadMessagesNumber;}
	public Message lastMessage() {return this.lastMessage;}
	public boolean isRead() {return this.lastMessage.isRead;}
}
