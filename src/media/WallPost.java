package media;

import dialog.Attachment;

public class WallPost extends Media 
{
	int fromID;
	long date;
	
	String text;
	String type;
	Attachment[] atts;
	
	boolean friendsOnly;
	boolean canComment;
	int commentsCount;
	int repostsCount;
	boolean isReposted;
	
	Like likes;
	
	public int fromID() {return this.fromID;}
	public long date() {return this.date;}
	
	public String text() {return this.text;}
	public String type() {return this.type;}
	public Attachment[] atts() {return this.atts;}
	
	public boolean friendsOnly() {return this.friendsOnly;}
	public boolean canComment() {return this.canComment;}
	public int commentsCount() {return this.commentsCount;}
	public int repostsCount() {return this.repostsCount;}
	public boolean isReposted() {return this.isReposted;}
	
	public Like likes() {return this.likes;}
}
