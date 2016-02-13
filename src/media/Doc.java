package media;

public class Doc extends Media 
{
	String title;
	String extention;
	long size;
	String URL;
	String photoURL;
	long addingDate;
	int type;
	
	public String title() {return this.title;}
	public String extention() {return this.extention;}
	public long size() {return this.size;}
	public String URL() {return this.URL;}
	public String photoURL() {return this.photoURL;}
	public long addingDate() {return this.addingDate;}
	public int type() {return this.type;}	
}
