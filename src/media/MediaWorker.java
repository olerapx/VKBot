package media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public abstract class MediaWorker extends Worker 
{

	public MediaWorker(VKClient client) 
	{
		super(client);
	}
	
	protected abstract Media[] get (String IDs)  throws ClientProtocolException, IOException, JSONException;
	
	public Media getByID (MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		String id = ""+ID.ownerID()+"_"+ID.mediaID();
		if (!ID.accessKey().equals(""))
			id+="_"+ID.accessKey();

		return this.get(id)[0];
	}
	
	public Media[] getByIDs (MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		if (IDs.length<=0) return new Media[0];
		
		String ids="";
		for (int i=0;i<IDs.length-1;i++)
		{
			MediaID ID = IDs[i];
			ids+=ID.ownerID()+"_" + ID.mediaID();
			if (!ID.accessKey().equals("")) ids+="_" + ID.accessKey();
			ids+=",";
		}
		MediaID ID = IDs[IDs.length-1];
		ids+=ID.ownerID()+"_" + ID.mediaID();
		if (!ID.accessKey().equals("")) ids+="_" + ID.accessKey();
		
		return this.get(ids);
	}
	
	protected Like getLike (JSONObject like) throws JSONException
	{
		Like likes = new Like();
		likes.number = like.getInt("count");
		
		if(like.has("can_like"))
			likes.canLike = like.getInt("can_like")!=0;
		else likes.canLike = true;
		
		likes.isLiked = like.getInt("user_likes")!=0;
		likes.canRepost = true;
		
		return likes;
	}
}
