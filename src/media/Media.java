package media;

/**
 * 
 * Base class for all objects which can be attach to a message.
 *
 */
public abstract class Media 
{
	int ID, ownerID; //TODO: write getFromGroup in all child classes
	
	public Media() {}
	
	public int ID() {return this.ID;}
	public int ownerID() { return this.ownerID;}
	
	public boolean doesBelongToGroup()
	{
		return (this.ownerID<0? true: false);
	}
}
