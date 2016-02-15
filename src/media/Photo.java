package media;

public class Photo extends Media 
{
	int albumID;
	int userID=100; ///< who uploaded. For groups only
	String text;
	String URL;
	long date;
	String accessKey;
	
	public int albumID() {return this.albumID;}
	public int userID() {return this.userID;}
	public String text() {return this.text;}
	public String URL() {return this.URL;}
	public long date() {return this.date;}
	public String accessKey() {return this.accessKey;}
}
