package scripts;

import java.io.File;

public interface ScriptRunner 
{
	/**
	 * Creates a new interpreter and initializes his environment.
	 */
	public void setNewEnvironment();
	
	public void execFile (File file) throws Exception;
		
	public void stop();
}
