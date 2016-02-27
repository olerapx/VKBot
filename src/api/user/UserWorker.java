
package api.user;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.media.Audio;
import api.media.AudioWorker;
import api.media.MediaID;
import api.user.User.Online;
import api.worker.Worker;

public class UserWorker extends Worker 
{
	public UserWorker(Client client) 
	{
		super(client);
	}
	
	private User[] get(String ids) throws JSONException, UnsupportedOperationException, IOException
	{
		String command="users.get?";
		if(ids!=null)
				command += "&user_ids="+ids;

		command+="&fields=domain,nickname,maiden_name,wall_comments,"
				+ "can_post,can_see_all_posts,can_see_audio,can_write_private_message,"
				+ "is_friend,can_send_friend_request,has_photo,photo_id,"
				+ "photo_max_orig,sex,bdate,online,followers_count,common_count";

		String str = client.executeCommand(command); 
		
		JSONObject obj = new JSONObject(str);		
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();		
		User[] users = new User[count];
		
		for(int i=0;i<count;i++)						
			users[i] = getFromJSON(response.getJSONObject(i));
				
		return users;
	}
	
	public User getFromJSON(JSONObject data) throws JSONException
	{
		User user = new User();
		
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
					
		if (data.has("wall_comments"))
			user.canComment =(data.getInt("wall_comments")!=0);
		
		user.canPost = (data.getInt("can_post")!=0);
		
		if (data.has("can_see_all_posts"))
			user.canSeeAllPosts= (data.getInt("can_see_all_posts")!=0);
		
		if(data.has("can_see_audio"))
			user.canSeeAudio= (data.getInt("can_see_audio")!=0);
		
		user.canWriteMessage= (data.getInt("can_write_private_message")!=0);
		
		if (data.has("is_friend"))
			user.isFriend = (data.getInt("is_friend")!=0);
		
		if (data.has("can_send_friend_request"))
			user.canAddToFriends =(data.getInt("can_send_friend_request")!=0);
		
		user.hasPhoto = (data.getInt("has_photo")!=0);
				
		if (data.has("photo_id"))
			user.photoID =Integer.parseInt(data.getString("photo_id").split("_")[1]);
		
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
		
		if (data.has("common_count"))
			user.commonCount = data.getInt("common_count");
					
		return user;
	}

	public User getByID(int ID) throws ClientProtocolException, IOException, JSONException
	{
		String id = "" + ID;
		return this.get(id)[0];
	}
	
	public User getByDomain(String domain) throws UnsupportedOperationException, JSONException, IOException
	{
		return this.get(domain)[0];
	}
	
	public User[] getByIDs(Integer[] IDs) throws ClientProtocolException, IOException, JSONException
	{
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
		String str = client.executeCommand("friends.get?"+
				"&user_id="+user.ID+
				"&order=hints");
		
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
		if (!user.canAddToFriends) return -1;
		
		String str = client.executeCommand("friends.add?"+
				"&user_id="+user.ID+
				"&text"+URLEncoder.encode(text, "UTF-8")+
				"&follow=0");
				
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
		client.executeCommand("status.set?"+
				"text="+URLEncoder.encode(status,"UTF-8".replace(".", "&#046;")));
	}
		
	public Status getStatus (User user) throws ClientProtocolException, IOException, JSONException
	{
		Status status = new Status();
		
		String command = "status.get?"+
				"&user_id=" + user.ID();
				
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
			status.audio=new Audio();
		
		return status;
	}	
}