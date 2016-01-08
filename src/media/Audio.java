package media;

public class Audio extends Media 
{
	String artist;
	String title;
	int duration;
	int lyricsId;
	String lyrics;
	int albumId;
	int genreId; //TODO: genre list
	int date;
	
	public Audio(){ 
		super();
	}
	
	public String artist() {return this.artist;}
	public String title() {return this.title;}
	public int duration() {return this.duration;}
	public int lyricsId(){return this.lyricsId;}
	public String lyrics(){ return lyrics;}
	public int albumId(){return this.albumId;}
	public int genreId() {return this.genreId;}
	public int date() {return this.date;}
}
