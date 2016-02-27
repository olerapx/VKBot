package api.media;

import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.worker.Worker;

public class LikeWorker extends Worker 
{
	public LikeWorker(Client client) 
	{
		super(client);
	}

	public Like getLike (JSONObject like) throws JSONException
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
