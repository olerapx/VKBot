package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import message.Attachment;
import worker.Worker;

public class WallPostWorker extends Worker 
{

	public WallPostWorker(VKClient client) {
		super(client);
	}
	
	public WallPost getByID(int ownerID, int wallPostID) throws ClientProtocolException, IOException, JSONException
	{
		String wid = "" + ownerID + "_" + wallPostID;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
					"wall.getById?"+
					"&posts="+wid+
					"&extended=0"+
					"&v=5.45"+
					"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		
		WallPost post = new WallPost();
		
		post.ID = data.getInt("id");
		post.ownerID = data.getInt("owner_id");
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
			post.repostsCount = data.getJSONObject("reposts").getInt("count");
			post.isReposted = data.getJSONObject("reposts").getInt("user_reposted")!=0;
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
			post.atts = getAttachments(att);
		}
		else post.atts = null;
					
		return post;
	}
	
	public WallPostReply[] getReplies (int ownerID, int wallPostReplyID, int offset, int count)  throws ClientProtocolException, IOException, JSONException
	{
		if (count>100 || count <0) count = 100;
		
		InputStream stream = executeCommand("https://api.vk.com/method/"+
					"wall.getComments?"+
					"&owner_id="+ownerID+
					"&post_id="+wallPostReplyID+
					"&need_likes=1"+
					"&offset="+offset+
					"&count="+count+
					"&preview_length=0"+
					"&extended=0"+
					"&v=5.45"+
					"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONObject("response");
		
		int commentsCount = data.getInt("count");
		
		JSONArray items = data.getJSONArray("items");		
		WallPostReply[] replies = new WallPostReply[commentsCount];
		
		for (int i=0;i<commentsCount;i++)
		{
			JSONObject comment = items.getJSONObject(i);
			WallPostReply reply = new WallPostReply();
			reply.ID = comment.getInt("id");
			reply.ownerID = ownerID;
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
				reply.atts = getAttachments(att);
			}
			else reply.atts = null;
			
			replies[i]=reply;
		}
		
		return replies;
	}
	
	Attachment[] getAttachments(JSONArray att) throws JSONException
	{
		int attCount = att.length();
		
		Attachment[] atts = new Attachment[attCount];
		
		for (int j=0;j<attCount;j++)
		{
			JSONObject currAttachment = att.getJSONObject(j);
			String currAttachmentID="";
			String type = currAttachment.getString("type");
			currAttachmentID+=type;
			
			currAttachment = currAttachment.getJSONObject(type);
			currAttachmentID+=""+currAttachment.getInt("owner_id")+"_" + currAttachment.getInt("id");
			
			atts[j] = new Attachment(currAttachmentID);
		}
		return atts;
	}
	
	Like getLike (JSONObject like) throws JSONException
	{
		Like likes = new Like();
		likes.count = like.getInt("count");
		likes.canLike = like.getInt("can_like")!=0;
		likes.isLiked = like.getInt("user_likes")!=0;
		likes.canRepost = true;
		
		return likes;
	}
}