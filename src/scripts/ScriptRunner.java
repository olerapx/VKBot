package scripts;

import java.io.File;
import java.io.Serializable;

public interface ScriptRunner extends Serializable
{
	/**
	 * Creates a new interpreter and initializes his environment.
	 */
	public void setNewEnvironment();
	
	public void execFile (File file) throws Exception;
		
	public void stop();
}
