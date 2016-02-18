package media;

/**
 * 
 * Base class for all objects which can be attach to a message.
 *
 */
public abstract class Media 
{
	//TODO: write getFromGroup in all child classes
	
	MediaID ID;
	
	public MediaID ID() {return this.ID;} 
	
	public boolean doesBelongToGroup()
	{
		return (this.ID.ownerID<0? true: false);
	}
}
