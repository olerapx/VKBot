package media;

/**
 * 
 * @class Media
 * @brief Base class for all objects which can be attach to a message
 *
 */
public class Media {
	
	int id, ownerId; //TODO: write getFromGroup in all child classes
	public Media()
	{	
	}
	
	public int id(){return this.id;}
	public int ownerId(){ return this.ownerId;}
	
	public boolean doesBelongToGroup()
	{
		return (this.ownerId<0? true: false);
	}
}
