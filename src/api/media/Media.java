package api.media;

import api.object.VKObject;

/**
 * Base class for all objects which can be attach to a message.
 */
public abstract class Media extends VKObject
{	
	MediaID ID;
	
	public MediaID ID() {return this.ID;} 
	public void setMediaID (MediaID ID) {this.ID = ID;}
}
