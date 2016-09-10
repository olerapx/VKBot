package api.media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.media.comment.CommentWorker;
import api.media.comment.VideoComment;

public class VideoWorker extends MediaWorker 
{
	private static final long serialVersionUID = -467839669278688310L;

	public VideoWorker(Client client) 
	{
		super(client);
	}
	
	protected Video[] get (String IDs) throws Exception
	{
		String str  = client.executeCommand("video.get?"+
				"&videos="+IDs+
				"&extended=1");
		
		JSONObject obj = new JSONObject(str);
		JSONObject response = obj.getJSONObject("response");
		JSONArray items = response.getJSONArray("items");
		
		if (items.length()==0) return new Video[0]; //not found or don't have key
		
		int count = items.length();
		Video[] videos = new Video[count];
		
		for (int i=0;i<count;i++)		
			videos[i] = getFromJSON(items.getJSONObject(i));;

		return videos;
	}
	
	public Video getFromJSON(JSONObject data) throws JSONException
	{
		Video video = new Video();
		
		video.ID = new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id"));	
		video.title = getStringFromJSON(data, "title");
		video.description = getStringFromJSON(data, "description");
		video.duration = getLongFromJSON(data, "duration");
		
		if(data.has("photo_640"))
			video.photoURL = data.getString("photo_640");
		else if (data.has("photo_320"))
			video.photoURL = data.getString("photo_320");
		else if (data.has("photo_130"))
			video.photoURL = data.getString("photo_130");
		else video.photoURL = "";
		
		video.creationDate = getLongFromJSON(data, "date");		
		video.addingDate = getLongFromJSON(data, "adding_date");
		
		video.viewsNumber = getIntFromJSON(data, "views");
		video.commentsNumber = getIntFromJSON(data, "comments");
		
		video.playURL = getStringFromJSON(data, "player");
		
		video.isLive = getBooleanFromJSON(data, "live");
		
		JSONObject like = getObjectFromJSON(data, "likes");		
		if (like!=null)
			video.likes = LikeWorker.getLike (like);
		else video.likes = new Like();
		
		video.canComment = getBooleanFromJSON(data, "can_comment");
		video.canRepost = getBooleanFromJSON(data, "can_repost");
		
		video.commentsCount = getIntFromJSON(data, "comments");
		
		return video;
	}
	
	public Video getByID(MediaID ID) throws Exception
	{
		return (Video)super.getByID(ID);
	}
	
	public Video[] getByIDs(MediaID[] IDs) throws Exception
	{
		return (Video[])super.getByIDs(IDs);
	}
	
	public VideoComment[] getComments (Video video, int offset, int count)  throws Exception
	{
		if (count>100 || count <0) count = 100;
		
		MediaID ID = video.ID();
		
		String str  = client.executeCommand("video.getComments?"+
					"&owner_id="+ID.ownerID()+
					"&video_id="+ID.mediaID()+
					"&need_likes=1"+
					"&offset="+offset+
					"&count="+count+
					"&preview_length=0"+
					"&extended=0");
				
		JSONObject obj = new JSONObject(str);
		JSONObject data = obj.getJSONObject("response");
				
		JSONArray items = data.getJSONArray("items");		
		int commentsCount = items.length();
		
		VideoComment[] comments = new VideoComment[commentsCount];
		
		for (int i=0;i<commentsCount;i++)						
			comments[i] = (VideoComment) CommentWorker.getFromJSON(items.getJSONObject(i), video);
		
		return comments;
	}
}
