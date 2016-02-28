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
	boolean hasEmoji;
	MessageData data;
	
	Integer [] forwardMessagesIDs;

	public int messageID() {return this.messageID;}
	public boolean isOut() {return this.isOut;}
	public boolean hasEmoji() {return this.hasEmoji;}
	
	public Integer [] forwardMessagesIDs() {return this.forwardMessagesIDs;}
	
	public MessageData data() {return this.data;}
		
	private void construct (String message, MediaAttachment[]attachments, MessageData[] forwardMessages, Integer[] forwardMessagesIDs, boolean isOut, String title, long date, boolean hasEmoji)
	{
		this.data = new MessageData();
					
		this.messageID=0;
		this.data.userID = 0;
		
		this.data.text=message;
		
		if (attachments!=null)
			this.data.attachments = attachments;
		else this.data.attachments = new Attachment[0];
		
		if (this.data.forwardMessages!=null)
			this.data.forwardMessages = forwardMessages;
		else this.data.forwardMessages = new MessageData[0];
		
		if (forwardMessagesIDs!=null)
			this.forwardMessagesIDs = forwardMessagesIDs;	
		else this.forwardMessagesIDs = new Integer[0];
		
		this.isOut = isOut;
		
		if (title!=null)
			this.data.title=title;
		
		this.data.date=date;
		
		this.hasEmoji=hasEmoji;
	}
	
	public Message(String message, MediaAttachment[]attachments, Integer[] forwardMessagesIDs, String title)
	{
		this.construct(message, attachments, null, forwardMessagesIDs, true, title, 0, true);
	}
		
	public Message (String message, MediaAttachment[]attachments, Integer[] forwardMessagesIDs)
	{
		this.construct( message, attachments, null, forwardMessagesIDs, true, null, 0, true);
	}
		
	public Message (String message)
	{
		construct (message, null, null, null, true, null, 0, true);
	}
	
	public Message (MessageData data, Integer[] forwardMessagesIDs)
	{
		construct (data.text, data.mediaAttachments(), data.forwardMessages, forwardMessagesIDs, true, data.title, 0, true);
	}
	
	public Message (MessageData data)
	{
		construct (data.text, data.mediaAttachments(), data.forwardMessages, null, true, data.title, 0, true);	
	}
}
