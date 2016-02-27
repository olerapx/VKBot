package api.media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import api.client.Client;
import api.media.comment.PhotoComment;
import api.media.comment.VideoComment;
import api.media.comment.WallComment;
import api.worker.Worker;

public abstract class MediaWorker extends Worker 
{
	public MediaWorker(Client client) 
	{
		super(client);
	}
	
	protected abstract Media[] get (String IDs)  throws ClientProtocolException, IOException, JSONException;
	
	public Media getByID (MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		String id = ""+ID.ownerID()+"_"+ID.mediaID();
		if (!ID.accessKey().equals(""))
			id+="_"+ID.accessKey();
		
		Media[] media = this.get(id);

		return media[0]; //TODO:safety
	}
	
	public Media[] getByIDs (MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
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
	
	public void like (Media media) throws IllegalArgumentException, ClientProtocolException, IOException
	{
		String type;
		
		if (media instanceof WallPost)
			type = "post";
		else if (media instanceof WallComment)
			type = "comment";
		else if (media instanceof PhotoComment)
			type = "photo_comment";
		else if (media instanceof VideoComment)
			type = "video_comment";
		else if (media instanceof Photo)
			type = "photo";
		else if (media instanceof Audio)
			type = "audio";
		else if (media instanceof Video)
			type = "video";
		else throw new IllegalArgumentException ("That object can't be liked.");
		
		String command = "likes.add?"+
						"&type="+type+
						"&owner_id="+media.ID.ownerID+
						"&item_id="+media.ID.mediaID;
		
		if (media.ID.accessKey!="") command += "&access_key="+media.ID.accessKey;
		
		client.executeCommand(command);
	}
}
