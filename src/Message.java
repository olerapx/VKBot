/**
 * 
 * @class Message
 * @brief VK private message.
 *
 */
public class Message 
{
	public Integer messageId;
	public Integer userId, fromId;
	public boolean out;
	public Long date;
	public String title;
	public String body;
	public Attachment[] attachments;
	public Integer [] fwds;
	public boolean emoji;
	
	public Message(int receiverId, String message, Attachment[]attachments, Integer[] fwds, boolean isOut, String title, Long date, boolean hasEmoji)
	{
		this.messageId=null;
		this.userId=null;
		this.fromId=null;
		this.date=System.currentTimeMillis()/1000L;
		this.title=null;
		this.body=null;
		this.attachments=null;
		this.fwds=null;
		
		this.userId=receiverId;
		this.body=message;
		if (attachments!=null)
			this.attachments=attachments;
			
		if (fwds!=null)
			this.fwds=fwds;
		this.out=isOut;
		
		if (title!=null)
			this.title=title;
		
		if (date!=null)
			this.date=date;
		
		this.emoji=hasEmoji;
	}
	
	public Message (int receiverId, String message)
	{
		this.messageId=null;
		this.userId=receiverId;
		this.fromId=null;
		this.date=System.currentTimeMillis()/1000L;
		this.title=null;
		this.body=message;
		this.attachments=null;
		this.fwds=null;
		this.out=true;
		this.emoji=true;
	}
}
