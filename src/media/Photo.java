package media;

public class Photo extends Media 
{
	int albumID;
	
	/**
	 * ID of user who uploaded the photo. Applicable only if the photo belongs to group, equals to 100 in other cases.
	 */
	int userID=100;
	String text;
	String URL;
	long date;
	
	public int albumID() {return this.albumID;}
	public int userID() {return this.userID;}
	public String text() {return this.text;}
	public String URL() {return this.URL;}
	public long date() {return this.date;}
}
