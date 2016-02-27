package api.media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
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
			post.likes = getLike (like);
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
		
	public Comment[] getComments (MediaID ID, int offset, int count)  throws ClientProtocolException, IOException, JSONException
	{
		if (count>100 || count <0) count = 100;
		
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
		
		int commentsCount = data.getInt("count");
		
		JSONArray items = data.getJSONArray("items");		
		Comment[] replies = new Comment[commentsCount];
		
		for (int i=0;i<commentsCount;i++)						
			replies[i]=getCommentFromJSON(items.getJSONObject(i), ID);
		
		return replies;
	}
	
	public Comment getCommentFromJSON(JSONObject data, MediaID ID) throws JSONException
	{
		Comment reply = new Comment();
		
		reply.ID = new MediaID(ID.ownerID(), data.getInt("id"));	
		reply.fromID = data.getInt("from_id");
		reply.date = data.getLong("date");
		reply.text = data.getString("text");
		
		if (data.has("likes"))
		{
			JSONObject like = data.getJSONObject("likes");		
			reply.likes = getLike (like);
		} 
		else reply.likes = new Like();
		
		if (data.has("attachments"))
		{
			JSONArray att = data.getJSONArray("attachments");
			reply.atts = new AttachmentWorker(this.client).getFromJSONArray(att);
		}
		else reply.atts = new Attachment[0];
		
		return reply;
	}
			
	public WallPost getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (WallPost)super.getByID(ID);
	}
	
	public WallPost[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (WallPost[])super.getByIDs(IDs);
	}
}