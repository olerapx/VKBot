package media;

import message.Attachment;

public class WallPost extends Media 
{
	int fromID;
	long date;
	String text;
	boolean friendsOnly;
	boolean canComment;
	int commentsCount;
	Like likes;
	int repostsCount;
	boolean isReposted;
	String type;
	Attachment[] atts;
	
	public int fromID() {return this.fromID;}
	public long date() {return this.date;}
	public String text() {return this.text;}
	public boolean friendsOnly() {return this.friendsOnly;}
	public boolean canComment() {return this.canComment;}
	public int commentsCount() {return this.commentsCount;}
	public Like likes() {return this.likes;}
	public int repostsCount() {return this.repostsCount;}
	public boolean isReposted() {return this.isReposted;}
	public String type() {return this.type;}
	public Attachment[] atts() {return this.atts;}
}
