package api.media;

public class Audio extends Media 
{
	private static final long serialVersionUID = -315193787050043233L;
	
	String artist;
	String title;
	
	long duration;
	int lyricsID;
	String lyrics;
	
	int albumID;
	int genreID;
	String genre;
	
	long date;
	
	public String artist() {return this.artist;}
	public String title() {return this.title;}
	
	public long duration() {return this.duration;}
	public int lyricsID(){return this.lyricsID;}
	public String lyrics(){ return lyrics;}
	
	public int albumID(){return this.albumID;}
	public int genreID() {return this.genreID;}
	public String genre() {return this.genre;}
	
	public long date() {return this.date;}
}
