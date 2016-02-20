package dialog;

/**
 * Dialog with a single user.
 */
import user.User;

public class PrivateDialog extends Dialog 
{
	User user;
	
	public User user() {return this.user;}
}
