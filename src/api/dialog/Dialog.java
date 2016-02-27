package api.dialog;

import api.object.VKObject;

/**
 * Base class for all chats.
 */
public class Dialog extends VKObject
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
