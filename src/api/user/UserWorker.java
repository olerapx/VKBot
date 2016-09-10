package api.user;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import api.client.Client;
import api.database.DatabaseWorker;
import api.media.Audio;
import api.media.AudioWorker;
import api.media.MediaID;
import api.user.User.Online;
import api.user.User.Relation;
import api.user.User.Sex;
import api.worker.Worker;

public class UserWorker extends Worker 
{	
	private static final long serialVersionUID = -6185181964142331296L;
	private String fields;
	
	public UserWorker(Client client) 
	{
		super(client);
		
		this.fields = "&fields=domain,nickname,maiden_name,wall_comments,"
				+ "can_post,can_see_all_posts,can_see_audio,can_write_private_message,"
				+ "is_friend,can_send_friend_request,has_photo,photo_id,"
				+ "photo_max_orig,sex,bdate,city,country,home_town,online,last_seen,"
				+ "followers_count,common_count,education,schools,relation,personal,connections";
	}
		
	private User[] get(String ids) throws Exception
	{
		String command="users.get?";
		if(ids!=null)
				command += "&user_ids="+ids;

		command+=fields;
		
		String str = client.executeCommand(command); 
		
		JSONObject obj = new JSONObject(str);		
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();	
		
		User[] users = new User[count];
		
		for(int i=0;i<count;i++)						
			users[i] = getFromJSON(response.getJSONObject(i));
				
		return users;
	}
	
	public static User getFromJSON(JSONObject data) throws Exception
	{
		User user = new User();
		
		user.ID = getIntFromJSON(data, "id");		
		user.domain= getStringFromJSON(data, "domain");
		
		user.firstName = getStringFromJSON(data, "first_name");
		user.lastName = getStringFromJSON(data, "last_name");		
		user.nickname= getStringFromJSON(data, "nickname");
		user.maidenName= getStringFromJSON(data, "maiden_name");
		
		if (data.has("deactivated"))
			user.isDeactivated= true;
					
		user.canComment = getBooleanFromJSON(data, "wall_comments");		
		user.canPost = getBooleanFromJSON(data, "can_post");		
		user.canSeeAllPosts= getBooleanFromJSON(data, "can_see_all_posts");		
		user.canSeeAudio= getBooleanFromJSON(data, "can_see_audio");		
		user.canWriteMessage= getBooleanFromJSON(data, "can_write_private_message");

		user.isFriend = getBooleanFromJSON(data, "is_friend");
		
		user.canAddToFriends = getBooleanFromJSON(data, "can_send_friend_request");
		
		user.hasPhoto = getBooleanFromJSON(data, "has_photo");
				
		if (data.has("photo_id"))
			user.photoID =Integer.parseInt(getStringFromJSON(data, "photo_id").split("_")[1]);
		
		user.sex = Sex.values()[getIntFromJSON(data, "sex")];
		
		if (data.has("city"))
		{
			JSONObject city = getObjectFromJSON(data, "city");
			user.cityName = getStringFromJSON(city, "title");
			user.cityID = getIntFromJSON(city, "id");
		}
		
		if (data.has("country"))
		{
			JSONObject country = getObjectFromJSON(data, "country");
			user.countryName = getStringFromJSON(country, "title");
			user.countryID = getIntFromJSON(country, "id");
		}
		
		user.homeTown = getStringFromJSON(data, "home_town");
		
		user.universityID = getIntFromJSON(data, "university");
		user.universityName = getStringFromJSON(data, "university_name");
		user.universityGraduationYear = getIntFromJSON(data, "graduation");
		
		if (data.has("schools"))
		{
			JSONObject school = getObjectFromJSONArray(getArrayFromJSON(data, "schools"), 0);
			if (school!=null)
			{
				user.schoolID = getIntFromJSON(school, "id");
				user.schoolName = getStringFromJSON(school, "name");
				user.schoolGraduationYear = getIntFromJSON(school, "year_graduated");
			}
		}
		user.relation = Relation.values()[getIntFromJSON(data, "relation")];
		user.skype = getStringFromJSON(data, "skype");
		
		JSONObject p = data.optJSONObject("personal");
		if (p!=null)
			user.religion = getStringFromJSON(p, "religion");
				
		user.birthDate = getStringFromJSON(data, "bdate");
		
		boolean isOnline = getBooleanFromJSON(data, "online");
		user.onlineAppID = -1;
		
		if(isOnline)
			user.online = Online.PC;
		else user.online = Online.OFFLINE;
		
		if (data.has("online_app"))
		{
			user.online = Online.APP;
			user.onlineAppID = getIntFromJSON(data, "online_app");
		}
		else if (data.has("online_mobile"))
			user.online = Online.MOBILE;
		
		if (data.has("last_seen"))
		{
			JSONObject last = getObjectFromJSON(data, "last_seen");
			user.lastOnline = getLongFromJSON(last, "time");
		}
		
		user.followersCount = getIntFromJSON(data, "followers_count");
		user.commonCount = getIntFromJSON(data, "common_count");
					
		return user;
	}

	public User getByID(int ID) throws Exception
	{
		String id = "" + ID;
		return this.get(id)[0];
	}
	
	public User getByDomain(String domain) throws Exception
	{
		return this.get(domain)[0];
	}

	public User[] getByIDs(Integer[] IDs) throws Exception
	{
		String ids="";
		
		for (int i=0;i<IDs.length-1;i++)
			ids+=IDs[i].toString()+",";
		ids+=IDs[IDs.length-1].toString();
			
		return this.get(ids);
	}
	
	public User getMe() throws Exception
	{
		return this.get(null)[0];
	}
		
	

	public User[] getFriends(User user) throws Exception //TODO:sort
	{		
		String str = client.executeCommand("friends.get?"+
				"&user_id="+user.ID+
				"&order=hints");
		
		JSONObject obj = new JSONObject(str);
		JSONObject data = getObjectFromJSON(obj, "response");
		JSONArray array = getArrayFromJSON(data, "items");
		
		int count = array.length();
		
		String ids="";
		
		for (int i=0;i<count-1;i++)
			ids+=""+array.getInt(i)+",";		
		ids+=""+array.getInt(count-1);
		
		User[] friends = this.get(ids);
		return friends;
	}
		
	public int addToFriends(User user, String text) throws Exception
	{
		if (!user.canAddToFriends) return -1;
		
		String str = client.executeCommand("friends.add?"+
				"&user_id="+user.ID+
				"&text"+encodeStringToURL(text)+
				"&follow=0");
				
		JSONObject obj = new JSONObject(str);
		
		return getIntFromJSON(obj, "response");
	}
	
	
	
	/**
	 * Sets status of user client logged in.
	 * @param status
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void setStatus (String status) throws Exception
	{
		client.executeCommand("status.set?"+
				"text="+encodeStringToURL(status));
	}
		
	public Status getStatus (User user) throws Exception
	{
		Status status = new Status();
		
		String command = "status.get?"+
				"&user_id=" + user.ID();
				
		String str = client.executeCommand(command);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");
		status.text =  getStringFromJSON(data, "text");
		
		if(data.has("audio"))
		{
			data = getObjectFromJSON(data, "audio");
			status.audio = new AudioWorker(client).getByID(new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id")));
		}
		else
			status.audio=new Audio();
		
		return status;
	}	

	

	public User getRelationPartner (User user) throws Exception
	{
		String command="users.get?";
				command += "&user_ids="+user.ID;
		command+="&fields=relation";
		
		String str = client.executeCommand(command); 
		
		JSONObject obj = new JSONObject(str);		
		JSONArray response = obj.getJSONArray("response");
		
		if (response.length()==0) return new User();
		
		JSONObject data = response.getJSONObject(0);
		
		if (data.has("relation_partner"))
		{
			JSONObject rel = getObjectFromJSON(data, "relation_partner");
			return getByID(getIntFromJSON(rel, "id"));
		}
		else return new User();
	}
	
	
	
	/**
	 * @param Object with all necessary search parameters.
	 * @return Found users.
	 */
	public User[] search (UserSearchParameters param) throws Exception
	{						 
		JSONObject obj = new JSONObject(client.executeCommand(searchParametersToCommand(param)));
		JSONObject data = getObjectFromJSON(obj, "response");
		JSONArray array = getArrayFromJSON(data, "items");
		
	    int count = array.length();
	    
	    User[] user = new User[count];
	    for (int i=0;i<count;i++)
	    	user[i] = getFromJSON(getObjectFromJSONArray(array, i));
	    
	    return user;		
	}

	/**
	 * @return Recommended users.
	 */
	public User[] searchRecommendations (int offset, int count) throws Exception
	{
		UserSearchParameters param = new UserSearchParameters();
		param.offset = offset;
		param.count = count;
		
		return search(param);
	}
	
	private String searchParametersToCommand(UserSearchParameters param) throws Exception
	{
		String sort = "&sort=" + (param.sortByPopularity ? 0 : 1);
		String userSex = "&sex="+param.sex.ordinal();
		String userRelation = (param.relation == User.Relation.NO_INFORMATION ? "" : "&status="+param.relation.ordinal());
		String userOnline = "&online=" + (param.needOnline ? 1 : 0);
		String photo = "&has_photo=" + (param.needPhoto ? 1 : 0);
		
		String from = "&from_list=";
		if (param.searchFriends) 
		{
			from+="friends";
			if (param.searchSubscriptions) from+=",subscriptions";
		}
		else if (param.searchSubscriptions) from+="subscriptions";
		else from = "";
		
		int country=0, city=0, universityCountry=0, schoolCountry=0, schoolCity=0, universityCity=0;
		
		DatabaseWorker dw = new DatabaseWorker(client);

		if (param.country!="")
			country = dw.getCountryID(param.country);
		
		if (param.schoolCountry!="")
			schoolCountry = dw.getCountryID(param.schoolCountry);
		
		if (param.universityCountry!="")
		{
			universityCountry = dw.getCountryID(param.universityCountry);
			if (param.universityCity!="")
				universityCity =dw.getCityID(universityCountry, param.universityCity);
		}

		if (param.city!="")
		{
			if (param.region!="")
			{
				int region = dw.getRegionID(country, param.region);
				city = dw.getCityID(country, region, param.city);
			}
			else city = dw.getCityID(country, 0 ,param.city);
		}
		
		if (param.schoolCity!="")
		{
			if (param.schoolRegion!="")
			{
				int region = dw.getRegionID(schoolCountry, param.schoolRegion);
				schoolCity = dw.getCityID(schoolCountry, region, param.schoolCity);
			}
			else schoolCity = dw.getCityID(schoolCountry, 0, param.schoolCity);
		}
		
		String school="";
		String university="";
		
		if (param.school!="") school = "&school="+dw.getSchoolID(schoolCity, param.school);
		
		if (param.university!="")
		{
			if (universityCity!=0)
				university = "&university=" + dw.getUniversityIDByCity(universityCity, param.university);	
			else 
				university = "&university=" + dw.getUniversityIDByCountry(universityCountry, param.university);
		}
		
		String countryName = (country==0? "" : "&country="+country);
		String cityName = (city==0? "" : "&city="+city);	
		String schoolCityName = (schoolCity ==0? "" : "&school_city="+schoolCity);	
		String universityCountryName = (universityCountry==0? "" : "&university_country="+ universityCountry);
		
				
		String str = "users.search?"+
				 (param.query!="" ? "&q="+ param.query : "")+
				 sort+
				 ( param.offset!=0 ? "&offset="+  param.offset : "")+
				 ( param.count!=0 ? "&count="+  param.count : "")+
				 	fields+
					countryName+
					cityName+
					school+
					university+
				 ( param.homeTown!="" ? "&hometown="+ param.homeTown : "")+	
				 universityCountryName+ 
				 ( param.universityGraduationYear!=0 ? "&university_year="+ param.universityGraduationYear : "")+		
				 ( param.schoolGraduationYear!=0 ? "&school_year="+ param.schoolGraduationYear : "")+							 
				 userSex+
				 userRelation+
				 ( param.startAge!=0 ? "&age_from="+ param.startAge : "")+
				 ( param.endAge!=0 ? "&age_to="+ param.endAge : "")+
				 ( param.birthDay!=0 ? "&birth_day="+ param.birthDay : "")+
				 ( param.birthMonth!=0 ? "&birth_month="+ param.birthMonth : "")+
				 ( param.birthYear!=0 ? "&birth_year="+ param.birthYear : "")+
				 userOnline+
				 photo+
				 schoolCityName+
				 ( param.religion!="" ? "&religion="+ param.religion : "")+
				 ( param.interests!="" ? "&interests="+ param.interests : "")+
				 ( param.company!="" ? "&company="+ param.company : "")+
				 ( param.position!="" ? "&position="+ param.position : "")+
				 ( param.groupID!=0 ? "&group_id="+ param.groupID : "")+						 
				 from;
		
		return str;
	}

	public void setOnline() throws Exception
	{
		client.executeCommand("account.setOnline?+"
							  + "&voip=0");
	}
	
	public void setOffline() throws Exception
	{
		client.executeCommand("account.setOffline?");
	}
}