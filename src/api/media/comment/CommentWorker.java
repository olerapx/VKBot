package api.media.comment;

import java.net.URLEncoder;

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
		
		comment.setMediaID(new MediaID(ID.ownerID(), data.getInt("id")));	
		comment.fromID = data.getInt("from_id");
		comment.date = data.getLong("date");
		comment.text = data.getString("text");
		
		if (data.has("likes"))
		{
			JSONObject like = data.getJSONObject("likes");		
			comment.likes = new LikeWorker(client).getLike (like);
		} 
		else comment.likes = new Like();
		
		if (data.has("attachments"))
		{
			JSONArray att = data.getJSONArray("attachments");
			comment.atts = new AttachmentWorker(this.client).getFromJSONArray(att);
		}
		else comment.atts = new Attachment[0];
		
		return comment;
	}
	
	public void repost (WallComment comment, String message) throws Exception
	{		
		client.executeCommand("wall.repost?"+
							  "&object=wall"+comment.ID().ownerID()+"_"+comment.ID().mediaID()+
							  "&message="+URLEncoder.encode(message, "UTF-8".replace(".", "&#046;")));
	}
}
