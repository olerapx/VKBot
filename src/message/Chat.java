package message;

import user.User;

public class Chat 
{
	long ID;
	String title;
	User admin;
	User [] users;
		
	public long ID() {return this.ID;}
	public String title() {return this.title;}
	public User admin() {return this.admin;}
	public User[] users() {return this.users;}
}
