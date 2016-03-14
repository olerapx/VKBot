package api.media;

import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.media.comment.PhotoComment;
import api.media.comment.VideoComment;
import api.media.comment.WallComment;
import api.worker.Worker;

public class LikeWorker extends Worker 
{
	public LikeWorker(Client client) 
	{
		super(client);
	}

	public static Like getLike (JSONObject data) throws JSONException
	{
		Like likes = new Like();
		likes.number = getIntFromJSON(data, "count");
		
	    likes.canLike = getBooleanFromJSON(data, "can_like");		
		likes.isLiked = getBooleanFromJSON(data, "user_likes");
		
		likes.canRepost = true;
		
		return likes;
	}
	
	public void like (Media media) throws Exception
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
