package api.dialog;

import api.attachment.Attachment;
import api.attachment.MediaAttachment;
import api.object.VKObject;

/**
 * Message in a chat.
 */
public class Message extends VKObject
{
	int messageID;
	boolean isOut;
	boolean isRead;
	boolean hasEmoji;
	MessageData data;
	
	Integer [] forwardMessagesIDs;

	public int messageID() {return this.messageID;}
	public boolean isOut() {return this.isOut;}
	public boolean isRead() {return this.isRead;}
	public boolean hasEmoji() {return this.hasEmoji;}
	
	public Integer [] forwardMessagesIDs() {return this.forwardMessagesIDs;}
	
	public MessageData data() {return this.data;}
		
	private void construct (String message, MediaAttachment[]attachments, MessageData[] forwardMessages, Integer[] forwardMessagesIDs, String title, long date, boolean hasEmoji)
	{
		this.data = new MessageData();
							
		this.data.userID = 0;
		
		this.data.text=message;
		
		if (attachments!=null)
			this.data.attachments = attachments;
		else this.data.attachments = new Attachment[0];
		
		if (forwardMessages!=null)
			this.data.forwardMessages = forwardMessages;
		else this.data.forwardMessages = new MessageData[0];
		
		if (title!=null)
			this.data.title=title;
		
		this.data.date=date;
		
		this.messageID=0;
		
		if (forwardMessagesIDs!=null)
			this.forwardMessagesIDs = forwardMessagesIDs;	
		else this.forwardMessagesIDs = new Integer[0];
		
		this.isOut = true;
		this.isRead = true;
				
		this.hasEmoji=hasEmoji;
	}
	
	public Message(String message, MediaAttachment[]attachments, Integer[] forwardMessagesIDs, String title)
	{
		this.construct(message, attachments, null, forwardMessagesIDs, title, 0, true);
	}
		
	public Message (String message, MediaAttachment[]attachments, Integer[] forwardMessagesIDs)
	{
		this.construct( message, attachments, null, forwardMessagesIDs, null, 0, true);
	}
		
	public Message (String message)
	{
		construct (message, null, null, null, null, 0, true);
	}
	
	public Message (MessageData data, Integer[] forwardMessagesIDs)
	{
		construct (data.text, data.mediaAttachments(), data.forwardMessages, forwardMessagesIDs, data.title, 0, true);
	}
	
	public Message (MessageData data)
	{
		construct (data.text, data.mediaAttachments(), data.forwardMessages, null, data.title, 0, true);	
	}
}
