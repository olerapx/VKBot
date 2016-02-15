package dialog;

/**
 * 
 * Message in a chat.
 *
 */
public class Message 
{
	int messageID;
	int userID;
	boolean isOut;
	long date;
	boolean hasEmoji;
	
	public String title;
	public String text;
	public Attachment[] attachments;
	public Integer [] fwds;
	
	public int messageID() {return this.messageID;}
	public int userID() {return this.userID;}	
	public boolean isOut() {return this.isOut;}
	public long date() {return this.date;}
	public boolean hasEmoji() {return this.hasEmoji;}
	
	private void construct (String message, Attachment[]attachments, Integer[] fwds, boolean isOut, String title, long date, boolean hasEmoji)
	{
		this.messageID=0;
		this.userID = 0;
		
		this.text=null;
		
		this.text=message;
		if (attachments!=null)
			this.attachments=attachments;
			
		if (fwds!=null)
			this.fwds=fwds;	
		
		this.isOut = isOut;
		
		if (title!=null)
			this.title=title;
		
		this.date=date;
		
		this.hasEmoji=hasEmoji;
	}
	
	public Message(String message, Attachment[]attachments, Integer[] fwds, String title)
	{
		this.construct(message, attachments, fwds, true, title, 0, true);
	}
		
	public Message (String message, Attachment[]attachments, Integer[] fwds)
	{
		this.construct( message, attachments, fwds, true, null, 0, true);
	}
		
	public Message (String message)
	{
		construct (message, null, null, true, null, 0, true);
	}
}
