package api.user;

import api.media.Audio;
import api.object.VKObject;

public class Status extends VKObject
{
	String text;
	Audio audio;
	
	public String text() {return this.text;}
	public Audio audio() {return this.audio;}
}
