package user;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import media.AudioWorker;
import media.MediaID;
import user.User.Online;
import worker.Worker;

public class UserWorker extends Worker 
{
	public UserWorker(VKClient client) 
	{
		super(client);
	}
	
	private User[] get(String ids) throws JSONException, UnsupportedOperationException, IOException
	{
		String command="https://api.vk.com/method/"+
				"users.get?";
		
		if (ids!=null)
			command+="&user_ids="+ids;
		
		command+="&fields=domain,nickname,maiden_name,wall_comments,"
				+ "can_post,can_see_all_posts,can_see_audio,can_write_private_message,"
				+ "is_friend,can_send_friend_request,has_photo,photo_id,"
				+ "photo_max_orig,sex,bdate,online,followers_count,common_count"+
				"&v=5.45"+
				"&access_token="+client.token;

		String str = client.executeCommand(command); 
		JSONObject obj = new JSONObject(str);
		
		int count = obj.getJSONArray("response").length();		
		User[] users = new User[count];
		
		for(int i=0;i<count;i++)
		{
			User user = new User();
			JSONObject data= obj.getJSONArray("response").getJSONObject(i);
			
			user.ID = data.getInt("id");
			
			if (data.has("domain"))
				user.domain= data.getString("domain");
			else user.domain="";
			
			user.firstName = data.getString("first_name");
			user.lastName=data.getString("last_name");
	
			if (data.has("nickname"))
				user.nickname= data.getString("nickname");
			else user.nickname="";
			
			if (data.has("maiden_name")) 
				user.maidenName= data.getString("maiden_name");
			else user.maidenName="";
			
			if (data.has("deactivated"))
				user.isDeactivated= true;
			else user.isDeactivated=false;
						
			if (data.has("wall_comments"))
				user.canComment =(data.getInt("wall_comments")!=0);
			else user.canComment = false;
			
			user.canPost = (data.getInt("can_post")!=0);
			
			if (data.has("can_see_all_posts"))
				user.canSeeAllPosts= (data.getInt("can_see_all_posts")!=0);
			else user.canSeeAllPosts=false;
			
			if(data.has("can_see_audio"))
				user.canSeeAudio= (data.getInt("can_see_audio")!=0);
			else user.canSeeAudio=false;
			
			user.canWriteMessage= (data.getInt("can_write_private_message")!=0);
			
			if (data.has("is_friend"))
				user.isFriend = (data.getInt("is_friend")!=0);
			else user.isFriend=false;
			
			if (data.has("can_send_friend_request"))
				user.canAddToFriends =(data.getInt("can_send_friend_request")!=0);
			else user.canAddToFriends=false;
			
			user.hasPhoto = (data.getInt("has_photo")!=0);
					
			if (data.has("photo_id"))
				user.photoID =Integer.parseInt(data.getString("photo_id").split("_")[1]);
			else user.photoID=0;
			
			user.sex = data.getInt("sex");
			
			if (data.has("bdate"))
				user.birthDate = data.getString("bdate");
			else user.birthDate="";
			
			boolean isOnline = (data.getInt("online")!=0);
			user.onlineAppID = -1;
			
			if(isOnline)
				user.online = Online.PC;
			else user.online = Online.OFFLINE;
			
			if (data.has("online_app"))
			{
				user.online = Online.APP;
				user.onlineAppID = data.getInt("online_app");
			}
			else if (data.has("online_mobile"))
				user.online = Online.MOBILE;
			
			if (data.has("followers_count"))
				user.followersCount = data.getInt("followers_count");
			else user.followersCount=0;
			
			if (data.has("common_count"))
				user.commonCount = data.getInt("common_count");
			else user.commonCount=0;
						
			users[i] = user;
		}
		
		return users;
	}

	public User getByID(int ID) throws ClientProtocolException, IOException, JSONException
	{
		String id = ""+ID;
		return this.get(id)[0];
	}
	
	public User getByDomain(String domain) throws UnsupportedOperationException, JSONException, IOException
	{
		return this.get(domain)[0];
	}
	
	public User[] getByIDs(Integer[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		if (IDs.length<=0) return new User[0];
		String ids="";
		
		for (int i=0;i<IDs.length-1;i++)
			ids+=IDs[i].toString()+",";
		ids+=IDs[IDs.length-1].toString();
			
		return this.get(ids);
	}
		
	public User getMe() throws UnsupportedOperationException, JSONException, IOException
	{
		return this.get(null)[0];
	}
	
	public User[] getFriends(User user) throws ClientProtocolException, IOException //TODO:sort
, JSONException
	{		
		String str = client.executeCommand("https://api.vk.com/method/"+
				"friends.get?"+
				"&user_id="+user.ID+
				"&order=hints"+
				"&v=5.45"+
				"&access_token="+client.token);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data= obj.getJSONObject("response");
		JSONArray array = data.getJSONArray("items");
		
		int count = data.getInt("count");
		
		String ids="";
		
		for (int i=0;i<count-1;i++)
			ids+=""+array.getInt(i)+",";		
		ids+=""+array.getInt(count-1);
		
		User[] friends = this.get(ids);
		return friends;
	}
	
	public int addToFriends(User user, String text) throws ClientProtocolException, IOException, JSONException
	{
		if (user.canAddToFriends== false) return -1;
		
		String str = client.executeCommand("https://api.vk.com/method/"+
				"friends.add?"+
				"&user_id="+user.ID+
				"&text"+URLEncoder.encode(text, "UTF-8")+
				"&follow=0"+
				"&v=5.45"+
				"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(str);
		
		return obj.getInt("response");
	}
	
	/**
	 * Sets status of user client logged in.
	 * @param status
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void setStatus (String status) throws ClientProtocolException, IOException
	{
		client.executeCommand("https://api.vk.com/method/"+
				"status.set?"+
				"text="+URLEncoder.encode(status,"UTF-8".replace(".", "&#046;"))+
				"&v=5.45"+
				"&access_token="+client.token);
	}
		
	public Status getStatus (User user) throws ClientProtocolException, IOException, JSONException
	{
		Status status = new Status();
		
		String command = "https://api.vk.com/method/"+
				"status.get?";
		if (user!=null) command+="&user_id="+user.ID();
		command+="&v=5.45";
		command+="&access_token="+client.token;
				
		String str = client.executeCommand(command);
		JSONObject obj = new JSONObject(str);
		JSONObject data =  obj.getJSONObject("response");
		status.text =  data.getString("text");
		
		if(data.has("audio"))
		{
			data = data.getJSONObject("audio");
			status.audio = new AudioWorker(client).getByID(new MediaID(data.getInt("owner_id"), data.getInt("id")));
		}
		else
			status.audio=null;
		
		return status;
	}	
}
