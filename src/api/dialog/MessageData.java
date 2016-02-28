package api.dialog;

import api.attachment.Attachment;
import api.attachment.MediaAttachment;
import api.object.VKObject;

public class MessageData extends VKObject 
{
	int userID;
	long date;
	
	String title;
	String text;
	Attachment[] attachments;
	MessageData[] forwardMessages;
	
	public int userID() {return this.userID;}	
	public long date() {return this.date;}
	
	public String title() {return this.title;}
	public String text() {return this.text;}
	public Attachment[] attachments() {return this.attachments;}
	public MessageData[] forwardMessages() {return this.forwardMessages;}
	
	public MediaAttachment[] mediaAttachments()
	{
		int count = 0;
		
		for (Attachment att: attachments)
			if (att instanceof MediaAttachment)
				count ++;
		
		MediaAttachment[] mediaAtts = new MediaAttachment[count];
		
		count = 0;
		
		for (Attachment att: attachments)
			if (att instanceof MediaAttachment)
			{
				mediaAtts[count] = (MediaAttachment) att;
				count ++;
			}
		
		return mediaAtts;		
	}
}
