package api.exceptions;

/**
 * Represents VK invalid responses.
 */
public class VKException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String error;
	private int code;
	
	public VKException (String what, int code)
	{
		this.error = what;
		this.code = code;
	}
	public String error() {return error;}
	public int code() {return code;}
}
