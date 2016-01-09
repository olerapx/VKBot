package message;

/**
 * 
 * @class Message
 * @brief VK private message.
 *
 */
public class Message 
{
	public Integer messageID;
	public Integer userID, fromID;
	public boolean out;
	public Long date;
	public String title;
	public String body;
	public Attachment[] attachments;
	public Integer [] fwds;
	public boolean emoji;
	
	//TODO: user in constructor
	public Message(int receiverID, String message, Attachment[]attachments, Integer[] fwds, boolean isOut, String title, Long date, boolean hasEmoji)
	{
		this.messageID=null;
		this.userID=null;
		this.fromID=null;
		this.date=System.currentTimeMillis()/1000L;
		this.title=null;
		this.body=null;
		this.attachments=null;
		this.fwds=null;
		
		this.userID=receiverID;
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
	
	public Message (int receiverID, String message)
	{
		this.messageID=null;
		this.userID=receiverID;
		this.fromID=null;
		this.date=System.currentTimeMillis()/1000L;
		this.title=null;
		this.body=message;
		this.attachments=null;
		this.fwds=null;
		this.out=true;
		this.emoji=true;
	}
}
