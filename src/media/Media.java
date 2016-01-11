package media;

/**
 * 
 * @class Media
 * @brief Base class for all objects which can be attach to a message
 *
 */
public class Media {
	
	int ID, ownerID; //TODO: write getFromGroup in all child classes
	
	public Media() {}
	
	public int ID(){return this.ID;}
	public int ownerID(){ return this.ownerID;}
	
	public boolean doesBelongToGroup()
	{
		return (this.ownerID<0? true: false);
	}
}
