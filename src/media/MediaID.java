package media;

public class MediaID
{
	int ownerID;
	int mediaID;
	String accessKey;
	
	public int ownerID() {return this.ownerID;}
	public int mediaID() {return this.mediaID;}
	public String accessKey() {return this.accessKey;}
	
	public MediaID (int ownerID, int mediaID)
	{
		this.ownerID = ownerID;
		this.mediaID = mediaID;
		this.accessKey = "";
	}
	
	public MediaID (int ownerID, int mediaID, String accessKey)
	{
		this.ownerID = ownerID;
		this.mediaID = mediaID;
		this.accessKey = accessKey;
	}
}
