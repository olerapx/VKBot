package scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.script.ScriptException;

public interface ScriptRunner extends Serializable
{
	/**
	 * Creates a new interpreter and initializes his environment.
	 */
	public void setNewEnvironment();
	
	public void execFile (File file) throws FileNotFoundException, ScriptException;
		
	public void stop();
}
