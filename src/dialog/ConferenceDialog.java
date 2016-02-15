package dialog;

import user.User;

/**
 * 
 * Conference.
 *
 */
public class ConferenceDialog extends Dialog 
{
	User admin;
	User [] users;
	String photoURL;
	
	public User admin() {return this.admin;}
	public User[] users() {return this.users;}
	public String photoURL() {return this.photoURL;}
}
