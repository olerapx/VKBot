package api.dialog;

import api.user.User;

/**
 * Conference.
 */
public class ConferenceDialog extends Dialog 
{
	private static final long serialVersionUID = -1008421433782672760L;
	
	User admin;
	User [] users;
	String photoURL;
	
	public User admin() {return this.admin;}
	public User[] users() {return this.users;}
	public String photoURL() {return this.photoURL;}
}
