package media;

public class Photo extends Media 
{
	int albumId;
	int userId=100; ///< who uploaded. For groups only
	String text;
	String url;
	long date;
	
	public int albumId() {return this.albumId;}
	public int userId() {return this.userId;}
	public String text() {return this.text;}
	public String url() {return this.url;}
	public long date() {return this.date;}
}
