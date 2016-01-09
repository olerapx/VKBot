package media;

public class Audio extends Media 
{
	String artist;
	String title;
	long duration;
	int lyricsID;
	String lyrics;
	int albumID;
	int genreID; //TODO: genre list
	int date;
	
	public Audio(){ 
		super();
	}
	
	public String artist() {return this.artist;}
	public String title() {return this.title;}
	public long duration() {return this.duration;}
	public int lyricsID(){return this.lyricsID;}
	public String lyrics(){ return lyrics;}
	public int albumID(){return this.albumID;}
	public int genreID() {return this.genreID;}
	public int date() {return this.date;}
}
