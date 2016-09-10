package api.media;

public class Video extends Media 
{
	private static final long serialVersionUID = 2407478598722060568L;
	
	String title;
	String description;
	long duration;
	
	String photoURL;
	String playURL;
	
	long creationDate;
	long addingDate;
	
	int viewsNumber;
	int commentsNumber;
	
	boolean isLive;
	
	Like likes;
	
	boolean canComment;
	boolean canRepost;
	int commentsCount;
	
	public String title(){return this.title;}
	public String description() {return this.description;}
	public long duration() {return this.duration;}
	
	public String photoURL() {return this.photoURL;}
	public String playURL() {return this.playURL;}
	
	public long creationDate() {return this.creationDate;}
	public long addingDate() {return this.addingDate;}
	
	public int viewsNumber() {return this.viewsNumber;}
	public int commentsNumber() {return this.commentsNumber;}
	
	public boolean isLive() {return this.isLive;}
	
	public Like likes() {return this.likes;}
	
	public boolean canComment() {return this.canComment;}
	public boolean canRepost() {return this.canRepost;}
	public int commentsCount() {return this.commentsCount;}
}
