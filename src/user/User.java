package user;
/**
 * 
 * @class User
 * @brief Any person
 *
 */
public class User 
{
	int ID;
	String domain;
	String firstName, lastName, nickname, maidenName;
	
	Boolean isDeactivated;
	boolean canComment;
	boolean canPost;
	boolean canSeeAllPosts;
	boolean canSeeAudio;
	boolean canWriteMessage;
	boolean isFriend;
	boolean canAddToFriends;
	boolean hasPhoto;
	int photoID;
	
	int sex;
	String birthDate;
	
	boolean isOnline; //TODO: mobile/app
	
	int followersCount, commonCount;

	int timeZone;
	
	public int ID(){return this.ID;}
	public String domain(){return this.domain;}
	public String firstName() {return this.firstName;}
	public String lastName() {return this.lastName;}
	public String nickname() {return this.nickname;}
	public String maidenName() {return this.maidenName;}
	public Boolean isDeactivated() {return this.isDeactivated;}
	public boolean canComment() {return this.canComment;}
	public boolean canPost() {return this.canPost;}
	public boolean canSeeAllPosts() {return this.canSeeAllPosts;}
	public boolean canSeeAudio() {return this.canSeeAudio;}
	public boolean canWriteMessage() {return this.canWriteMessage;}
	public boolean isFriend() {return this.isFriend;}
	public boolean canAddToFriends () {return this.canAddToFriends;}
	public boolean hasPhoto () {return this.hasPhoto;}
	public int photoID() {return this.photoID;}
	public int sex() {return this.sex;}
	public String birthDate() {return this.birthDate;}
	public boolean isOnline() {return this.isOnline;}
	public int followersCount(){return this.followersCount;}
	public int commonCount() {return this.commonCount;}
}
