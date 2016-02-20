package exceptions;

/**
 * Represents VK invalid responses
 */
public class VKException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String error;
	
	public VKException (String what)
	{
		this.error=what;
	}
	public String error() {return error;}
}
