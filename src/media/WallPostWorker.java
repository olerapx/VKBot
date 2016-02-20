package media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;

public class WallPostWorker extends MediaWorker 
{
	public WallPostWorker(VKClient client) 
	{
		super(client);
	}
	
	protected WallPost[] get (String IDs) throws ClientProtocolException, IOException, JSONException
	{
		String str  = client.executeCommand("https://api.vk.com/method/"+
				"wall.getById?"+
				"&posts="+IDs+
				"&extended=0"+
				"&v=5.45"+
				"&access_token="+client.token);
			
		JSONObject obj = new JSONObject(str);
		
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		WallPost[] posts = new WallPost[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = response.getJSONObject(i);
			WallPost post = new WallPost();
			
			post.ID = new MediaID(data.getInt("owner_id"), data.getInt("id"));	
			post.fromID = data.getInt("from_id");
			post.date = data.getLong("date");
			
			if (data.has("friends_only"))
				post.friendsOnly = data.getInt("friends_only")!=0;
			else post.friendsOnly = false;
			
			if (data.has("likes"))
			{
				JSONObject like = data.getJSONObject("likes");
				post.likes = getLike (like);
			}
			else post.likes = null;
			
			if (data.has("reposts"))
			{
				JSONObject reposts = data.getJSONObject("reposts");
				post.repostsCount = reposts.getInt("count");
				
				if (reposts.has("user_reposted"))
					post.isReposted = reposts.getInt("user_reposted")!=0;
				else post.isReposted = false;
			}
			else
			{
				post.repostsCount = 0;
				post.isReposted = false;
			}
			
			if (data.has("comments"))
			{
				post.commentsCount = data.getJSONObject("comments").getInt("count");
				post.canComment = data.getJSONObject("comments").getInt("can_post")!=0;
			}
			else
			{
				post.commentsCount = 0;
				post.canComment = false;
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
				post.atts = getAttachmentsFromJSON(att);
			}
			else post.atts = null;
			
			posts[i] = post;
		}
		return posts;
	}
		
	public Comment[] getReplies (int ownerID, int wallPostID, int offset, int count)  throws ClientProtocolException, IOException, JSONException
	{
		if (count>100 || count <0) count = 100;
		
		String str  = client.executeCommand("https://api.vk.com/method/"+
					"wall.getComments?"+
					"&owner_id="+ownerID+
					"&post_id="+wallPostID+
					"&need_likes=1"+
					"&offset="+offset+
					"&count="+count+
					"&preview_length=0"+
					"&extended=0"+
					"&v=5.45"+
					"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(str);
		JSONObject data = obj.getJSONObject("response");
		
		int commentsCount = data.getInt("count");
		
		JSONArray items = data.getJSONArray("items");		
		Comment[] replies = new Comment[commentsCount];
		
		for (int i=0;i<commentsCount;i++)
		{
			JSONObject comment = items.getJSONObject(i);
			Comment reply = new Comment();
			reply.ID = new MediaID(ownerID, data.getInt("id"));	
			reply.fromID = comment.getInt("from_id");
			reply.date = comment.getLong("date");
			reply.text = comment.getString("text");
			
			if (comment.has("likes"))
			{
				JSONObject like = comment.getJSONObject("likes");		
				reply.likes = getLike (like);
			} 
			else reply.likes = null;
			
			if (comment.has("attachments"))
			{
				JSONArray att = comment.getJSONArray("attachments");
				reply.atts = getAttachmentsFromJSON(att);
			}
			else reply.atts = null;
			
			replies[i]=reply;
		}
		
		return replies;
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