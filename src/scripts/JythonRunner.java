package scripts;

import java.io.File;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import api.worker.WorkerInterface;

/**
 * Provides an evaluation and an execution of a python script.
 */
public class JythonRunner implements ScriptRunner
{
	PythonInterpreter py;
	WorkerInterface wi;
	
	String stopVariableName = "stop";
	boolean isRun = false;
	
	public JythonRunner(WorkerInterface wi)
	{
		this.wi = wi;
		
		PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);		
		setNewEnvironment();
	}
	
	/**
	 * @see ScriptRunner#setNewEnvironment()
	 */
	public void setNewEnvironment()
	{
		this.py = new PythonInterpreter(null, new PySystemState());
		py.set("workerInterface", wi);
		py.set(stopVariableName, false);
	}
	
	public void execFile (File file)
	{		
		this.isRun = true;
		
		py.execfile(file.getPath());
		py.close();
		
		this.isRun = false;	
	}
		
	public void stop()
	{
		if (isRun)
		{
			py.set(stopVariableName, true);
			isRun = false;
		}
	}
	
	public PythonInterpreter py() {return this.py;}
}
