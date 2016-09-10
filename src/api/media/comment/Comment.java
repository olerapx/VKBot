package api.media.comment;

import api.attachment.Attachment;
import api.media.Like;
import api.media.Media;

/**
 * Base class for comments.
 */
public abstract class Comment extends Media
{
	private static final long serialVersionUID = 2534779726114047791L;

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
