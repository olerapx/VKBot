package message;

/**
 * 
 * @class Message
 * @brief VK private message.
 *
 */
public class Message 
{
	public int messageID;
	public int userID, fromID;
	public boolean out;
	public Long date;
	public String title;
	public String body;
	public Attachment[] attachments;
	public Integer [] fwds;
	public boolean emoji;
	
	private void construct (String message, Attachment[]attachments, Integer[] fwds, boolean isOut, String title, Long date, boolean hasEmoji)
	{
		this.messageID=0;
		this.userID = 0;
		this.fromID=0;
		
		this.title=null;
		this.body=null;
		this.attachments=null;
		this.fwds=null;
		
		this.body=message;
		if (attachments!=null)
			this.attachments=attachments;
			
		if (fwds!=null)
			this.fwds=fwds;	
		
		this.out = isOut;
		
		if (title!=null)
			this.title=title;
		
		this.date=date;
		
		this.emoji=hasEmoji;
	}
	
	public Message(String message, Attachment[]attachments, Integer[] fwds, String title)
	{
		this.construct(message, attachments, fwds, true, title, null, true);
	}
		
	public Message (String message, Attachment[]attachments, Integer[] fwds)
	{
		this.construct( message, attachments, fwds, true, null, null, true);
	}
		
	public Message (String message)
	{
		construct (message, null, null, true, null, null, true);
	}
}
