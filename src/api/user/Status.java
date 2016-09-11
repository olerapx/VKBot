package api.user;

import api.media.Audio;
import api.object.VKObject;

/**
 * An user status. Can contain text or audio but not both.
 */
public class Status extends VKObject
{
	private static final long serialVersionUID = 5779735789373627264L;
	
	String text;
	Audio audio;
	
	public String text() {return this.text;}
	public Audio audio() {return this.audio;}
}
