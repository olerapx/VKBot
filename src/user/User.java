package user;
/**
 * 
 * @class User
 * @brief Any person
 *
 */
public class User 
{
	int id;
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
	Integer photoId;
	String photoURL;
	
	Integer sex;
	String birthDate;
	
	boolean isOnline; //TODO: mobile/app
	
	Integer followersCount, commonCount;

	int timeZone;
	
	public int id(){return this.id;}
	public String domain(){return this.domain==null?"" : this.domain;}
	public String firstName() {return this.firstName;}
	public String lastName() {return this.lastName;}
	public String nickname() {return this.nickname==null? "" :this.nickname;}
	public String maidenName() {return (this.maidenName==null? "" : this.maidenName);}
	public Boolean isDeactivated() {return this.isDeactivated;}
	public boolean canComment() {return this.canComment;}
	public boolean canPost() {return this.canPost;}
	public boolean canSeeAllPosts() {return this.canSeeAllPosts;}
	public boolean canSeeAudio() {return this.canSeeAudio;}
	public boolean canWriteMessage() {return this.canWriteMessage;}
	public boolean isFriend() {return this.isFriend;}
	public boolean canAddToFriends () {return this.canAddToFriends;}
	public boolean hasPhoto () {return this.hasPhoto;}
	public Integer photoId() {return this.photoId==null? 0: this.photoId;}
	public String photoURL () {return this.photoURL==null? "" : this.photoURL;}
	public Integer sex() {return this.sex;}
	public String birthDate() {return this.birthDate==null? "" : this.birthDate;}
	public boolean isOnline() {return this.isOnline;}
	public Integer followersCount(){return this.followersCount;}
	public Integer commonCount() {return this.commonCount;}
}
