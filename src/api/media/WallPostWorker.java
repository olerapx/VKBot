package api.media;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
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
	
	protected WallPost[] get (String IDs) throws ClientProtocolException, IOException, JSONException
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
	
	public WallPost getFromJSON(JSONObject data) throws JSONException
	{
		WallPost post = new WallPost();
		
		post.ID = new MediaID(data.getInt("owner_id"), data.getInt("id"));	
		post.fromID = data.getInt("from_id");
		post.date = data.getLong("date");
		
		if (data.has("friends_only"))
			post.friendsOnly = data.getInt("friends_only")!=0;
		
		if (data.has("likes"))
		{
			JSONObject like = data.getJSONObject("likes");
			post.likes = new LikeWorker(client).getLike (like);
		}
		else post.likes = new Like();
		
		if (data.has("reposts"))
		{
			JSONObject reposts = data.getJSONObject("reposts");
			post.repostsCount = reposts.getInt("count");
			
			if (reposts.has("user_reposted"))
				post.isReposted = reposts.getInt("user_reposted")!=0;
		}
		
		if (data.has("comments"))
		{
			post.commentsCount = data.getJSONObject("comments").getInt("count");
			post.canComment = data.getJSONObject("comments").getInt("can_post")!=0;
		}
		
		if (data.has("copy_history")) 
		{
			JSONArray array = data.getJSONArray("copy_history");
			data = array.getJSONObject(array.length()-1);
		}

		post.type = data.getString("post_type");
		post.text = data.getString("text");
		
		if(data.has("attachments"))
		{
			JSONArray att = data.getJSONArray("attachments");
			post.atts =  new AttachmentWorker(this.client).getFromJSONArray(att);
		}
		else post.atts = new Attachment[0];
		
		return post;
	}
	
	public WallPost getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (WallPost)super.getByID(ID);
	}
	
	public WallPost[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (WallPost[])super.getByIDs(IDs);
	}
	
	public WallComment[] getComments (WallPost post, int offset, int count)  throws ClientProtocolException, IOException, JSONException
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
		
		CommentWorker cw = new CommentWorker(client);
		
		for (int i=0;i<commentsCount;i++)						
			comments[i] = (WallComment) cw.getFromJSON(items.getJSONObject(i), post);
		
		return comments;
	}
	
	public void repost (WallPost post, String message) throws ClientProtocolException, IOException
	{		
		client.executeCommand("wall.repost?"+
							  "&object=wall"+post.ID.ownerID+"_"+post.ID.mediaID+
							  "&message="+URLEncoder.encode(message, "UTF-8".replace(".", "&#046;")));
	}

}