package api.media;


import api.client.Client;
import api.worker.Worker;

public abstract class MediaWorker extends Worker 
{
	public MediaWorker(Client client) 
	{
		super(client);
	}
	
	protected abstract Media[] get (String IDs) throws Exception;
	
	public Media getByID (MediaID ID) throws Exception
	{
		String id = ""+ID.ownerID()+"_"+ID.mediaID();
		if (!ID.accessKey().equals(""))
			id+="_"+ID.accessKey();
		
		Media[] media = this.get(id);

		return media[0]; //TODO:safety
	}
	
	public Media[] getByIDs (MediaID[] IDs) throws Exception
	{
		if (IDs.length<=0) return new Media[0];
		
		String ids="";
		for (int i=0;i<IDs.length;i++)
		{
			MediaID ID = IDs[i];
			ids+=ID.ownerID()+"_" + ID.mediaID();
			if (!ID.accessKey().equals("")) ids+="_" + ID.accessKey();
			
			if (i<IDs.length-1)	ids+=",";
		}		

		return this.get(ids);
	}
}
