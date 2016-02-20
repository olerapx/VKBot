package media;

import dialog.Attachment;

/**
 * Represents a photo, video or wall comment.  
 */
public class Comment extends Media
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
