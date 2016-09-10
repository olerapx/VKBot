package api.attachment;

import api.media.Photo;

public class Link extends Attachment 
{
	private static final long serialVersionUID = 3589440176838968500L;
	
	String URL;
	String previewURL;
	
	String title;
	String caption;
	String description;
	
	Photo photo;
	
	boolean isExternal;
	
	public String URL() {return this.URL;}
	public String previewURL() {return this.previewURL;}
	
	public String title() {return this.title;}
	public String caption() {return this.caption;}
	public String description() {return this.description;}
	
	public Photo photo() {return this.photo;} 
	
	public boolean isExternal() {return this.isExternal;}
}
