package api.media;

public class Photo extends Media 
{
	private static final long serialVersionUID = -8273733745791294933L;

	int albumID;
	
	/**
	 * ID of user which uploaded the photo. Applicable only if the photo belongs to group, equals to 100 in other case.
	 */
	int userID = 100;
	
	String text;
	String URL;
	
	long date;
	
	Like likes;
	
	boolean canComment;
	boolean canRepost;
	int commentsCount;
	int repostsCount;
	
	public int albumID() {return this.albumID;}
	public int userID() {return this.userID;}
	
	public String text() {return this.text;}
	public String URL() {return this.URL;}
	
	public long date() {return this.date;}
	
	public Like likes() {return this.likes;}
	
	public boolean canComment() {return this.canComment;}
	public boolean canRepost() {return this.canRepost;}
	public int commentsCount() {return this.commentsCount;}
	public int repostsCount() {return this.repostsCount;}
}
