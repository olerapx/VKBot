package api.media;

import org.json.JSONArray;
import org.json.JSONObject;

import api.client.Client;
import api.media.comment.CommentWorker;
import api.media.comment.WallComment;
import api.attachment.Attachment;
import api.attachment.AttachmentWorker;

public class WallPostWorker extends MediaWorker 
{
	public WallPostWorker(Client client) 
	{
		super(client);
	}
	
	protected WallPost[] get (String IDs) throws Exception
	{
		String str  = client.executeCommand("wall.getById?"+
				"&posts="+IDs+
				"&extended=0");
			
		JSONObject obj = new JSONObject(str);
		
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		WallPost[] posts = new WallPost[count];
		
		for (int i=0;i<count;i++)		
			posts[i] = getFromJSON(response.getJSONObject(i));
		
		return posts;
	}
	
	public WallPost getFromJSON(JSONObject data) throws Exception
	{
		WallPost post = new WallPost();
		
		post.ID = new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id"));	
		post.fromID = getIntFromJSON(data, "from_id");
		post.date = getLongFromJSON(data, "date");
		
		post.friendsOnly = getBooleanFromJSON(data, "friends_only");
				
		JSONObject like = getObjectFromJSON(data, "likes");
		if(like!=null)
			post.likes = LikeWorker.getLike (like);
		else post.likes = new Like();
		
		JSONObject reposts = getObjectFromJSON(data, "reposts");
		if(reposts!=null)
		{
			post.repostsCount = getIntFromJSON(reposts, "count");
			post.isReposted =getBooleanFromJSON(reposts, "user_reposted");
		}
		
		JSONObject comments = getObjectFromJSON(data, "comments");
		if(comments!=null)
		{
			post.commentsCount = getIntFromJSON(comments, "count");
			post.canComment = getBooleanFromJSON(comments, "can_post");
		}

		JSONArray array = getArrayFromJSON(data, "copy_history");
		if (array != null)
			data = getObjectFromJSONArray(array, array.length()-1);
		else data = null;
		
		if (data!=null)
		{
			post.type = getStringFromJSON(data, "post_type");
			post.text = getStringFromJSON(data, "text");

			JSONArray att = getArrayFromJSON(data, "attachments");
			if(att != null)
				post.atts =  AttachmentWorker.getFromJSONArray(att);
			else post.atts = new Attachment[0];
		}
		else
			post.atts = new Attachment[0];
		
		return post;
	}
	
	public WallPost getByID(MediaID ID) throws Exception
	{
		return (WallPost)super.getByID(ID);
	}
	
	public WallPost[] getByIDs(MediaID[] IDs) throws Exception
	{
		return (WallPost[])super.getByIDs(IDs);
	}
	
	public WallComment[] getComments (WallPost post, int offset, int count)  throws Exception
	{
		if (count>100 || count <0) count = 100;
		
		MediaID ID = post.ID();
		
		String str  = client.executeCommand("wall.getComments?"+
					"&owner_id="+ID.ownerID()+
					"&post_id="+ID.mediaID()+
					"&need_likes=1"+
					"&offset="+offset+
					"&count="+count+
					"&preview_length=0"+
					"&extended=0");
				
		JSONObject obj = new JSONObject(str);
		JSONObject data = obj.getJSONObject("response");
				
		JSONArray items = data.getJSONArray("items");		
		int commentsCount = items.length();
		
		WallComment[] comments = new WallComment[commentsCount];
		
		for (int i=0;i<commentsCount;i++)						
			comments[i] = (WallComment) CommentWorker.getFromJSON(items.getJSONObject(i), post);
		
		return comments;
	}
	
	public void repost (WallPost post, String message) throws Exception
	{		
		client.executeCommand("wall.repost?"+
							  "&object=wall"+post.ID.ownerID+"_"+post.ID.mediaID+
							  "&message="+encodeStringToURL(message));
	}

}