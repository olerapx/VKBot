package api.dialog;

import api.user.User;

public class PrivateDialog extends Dialog 
{
	private static final long serialVersionUID = -2968128791632411369L;
	
	User user;
	
	public User user() {return this.user;}
}
