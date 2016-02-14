package media;

import dialog.Attachment;

public class WallPostReply extends Media
{
	int fromID;
	long date;
	String text;
	Like likes;
	Attachment[] atts; 
	
	public int fromID() {return this.fromID;}
	public long date() {return this.date;}
	public String text() {return this.text;}
	public Like likes() {return this.likes;}
	public Attachment[] atts() {return this.atts;}
}
