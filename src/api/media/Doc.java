package api.media;

public class Doc extends Media 
{
	private static final long serialVersionUID = -2008163632808288272L;
	
	String title;
	String extention;
	int type;
	
	long size;
	
	String URL;
	String photoURL;
	
	long addingDate;	
	
	public String title() {return this.title;}
	public String extention() {return this.extention;}
	public int type() {return this.type;}	
	
	public long size() {return this.size;}
	
	public String URL() {return this.URL;}
	public String photoURL() {return this.photoURL;}
	
	public long addingDate() {return this.addingDate;}
}
