package scripts;

import java.io.File;

public interface ScriptRunner 
{
	public void setNewEnvironment();
	
	public void execFile (File file) throws Exception;
		
	public void stop();
}
