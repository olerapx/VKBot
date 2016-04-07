package api.user;

import api.object.VKObject;

/**
 * Any person.
 */
public class User extends VKObject
{
	public enum Online
	{
		PC,
		MOBILE,
		APP,
		OFFLINE
	}
	
	public enum Sex
	{
		ANY,
		WOMAN,
		MAN
	}
	
	public enum Relation
	{
		NO_INFORMATION,
		SINGLE,
		HAS_RELATIONSHIP,
		ENGAGED,
		MARRIED,
		COMPLICATED,
		SEARCHING,
		IN_LOVE
	}
	
	int ID;
	String domain;
	String firstName, lastName, nickname, maidenName;
	
	String countryName, cityName;
	int countryID, cityID;
	String homeTown;
	
	String universityName;
	int universityID;
	int universityGraduationYear;	
	
	String schoolName;
	int schoolID;
	int schoolGraduationYear;
	
	Relation relation;
	String religion;
	String skype;
	
	boolean isDeactivated;
	boolean canComment;
	boolean canPost;
	boolean canSeeAllPosts;
	boolean canSeeAudio;
	boolean canWriteMessage;
	boolean isFriend;
	boolean canAddToFriends;
	
	boolean hasPhoto;
	int photoID;
	
	Sex sex;
	String birthDate;
	
	Online online;
	int onlineAppID;
	long lastOnline;
	
	int followersCount, commonCount;
	
	public int ID(){return this.ID;}
	public String domain(){return this.domain;}
	public String firstName() {return this.firstName;}
	public String lastName() {return this.lastName;}
	public String nickname() {return this.nickname;}
	public String maidenName() {return this.maidenName;}
	
	public String countryName() {return this.countryName;}
	public String cityName() {return this.cityName;}
	public int countryID() {return this.countryID;}
	public int cityID() {return this.cityID;}
	public String homeTown() {return this.homeTown;}
	
	public String universityName() {return this.universityName;}
	public int universityID() {return this.universityID;}
	public int universityGraduationYear() {return this.universityGraduationYear;}
	
	public String schoolName() {return this.schoolName;}
	public int schoolID() {return this.schoolID;}
	public int schoolGraduationYear() {return this.schoolGraduationYear;}
	
	public Relation relation() {return this.relation;}
	public String religion () {return this.religion;}
	public String skype() {return this.skype;}

	public boolean isDeactivated() {return this.isDeactivated;}
	public boolean canComment() {return this.canComment;}
	public boolean canPost() {return this.canPost;}
	public boolean canSeeAllPosts() {return this.canSeeAllPosts;}
	public boolean canSeeAudio() {return this.canSeeAudio;}
	public boolean canWriteMessage() {return this.canWriteMessage;}
	public boolean isFriend() {return this.isFriend;}
	public boolean canAddToFriends() {return this.canAddToFriends;}
	
	public boolean hasPhoto () {return this.hasPhoto;}
	public int photoID() {return this.photoID;}
	
	public Sex sex() {return this.sex;}
	public String birthDate() {return this.birthDate;}
	
	public Online online() {return this.online;}
	public int onlineAppID() {return this.onlineAppID;}
	public long lastOnline() {return this.lastOnline;}
	
	public int followersCount(){return this.followersCount;}
	public int commonCount() {return this.commonCount;}
}
