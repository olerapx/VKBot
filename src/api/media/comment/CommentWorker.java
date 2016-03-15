package api.media.comment;

import org.json.JSONArray;
import org.json.JSONObject;

import api.attachment.Attachment;
import api.attachment.AttachmentWorker;
import api.client.Client;
import api.media.Like;
import api.media.LikeWorker;
import api.media.Media;
import api.media.MediaID;
import api.media.Photo;
import api.media.Video;
import api.media.WallPost;
import api.worker.Worker;

public class CommentWorker extends Worker 
{
	public CommentWorker(Client client) 
	{
		super(client);
	}
	
	public Comment getFromJSON(JSONObject data, Media media) throws Exception
	{
		Comment comment;
		
		if (media instanceof Video)
			comment = new VideoComment();
		else if (media instanceof Photo)
			comment = new PhotoComment();
		else if (media instanceof WallPost)
			comment = new WallComment();
		else throw new IllegalArgumentException("Invalid media type. Only Photo, Video or WallPost are allowed.");
		
		MediaID ID = media.ID();
		
		comment.setMediaID(new MediaID(ID.ownerID(), getIntFromJSON(data, "id")));	
		comment.fromID = getIntFromJSON(data, "from_id");
		comment.date = getLongFromJSON(data, "date");
		comment.text = getStringFromJSON(data, "text");
		
		JSONObject like = getObjectFromJSON(data, "likes");		
		if (like != null)
			comment.likes = LikeWorker.getLike(like);
		else comment.likes = new Like();
		
		JSONArray att = getArrayFromJSON(data, "attachments");
		if (att != null)
			comment.atts = AttachmentWorker.getFromJSONArray(att);
		else comment.atts = new Attachment[0];
		
		return comment;
	}
	
	public void repost (WallComment comment, String message) throws Exception
	{		
		client.executeCommand("wall.repost?"+
							  "&object=wall"+comment.ID().ownerID()+"_"+comment.ID().mediaID()+
							  "&message="+encodeStringToURL(message));
	}
}
