package scripts;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import api.worker.WorkerInterface;

/**
 * Provides an evaluation and an execution of a python script.
 */
public class JythonRunner implements ScriptRunner
{
	private static final long serialVersionUID = 5570515376625071223L;
	
	transient PythonInterpreter py;
	WorkerInterface wi;
	
	String stopVariableName = "stop";
	boolean isRunning = false;
	
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
		this.isRunning = true;
		
		py.execfile(file.getPath());
		py.close();
		
		this.isRunning = false;	
	}
		
	public void stop()
	{
		if (isRunning)
		{
			py.set(stopVariableName, true);
		}
	}
	
	public PythonInterpreter py() {return this.py;}
	
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		oos.defaultWriteObject();	
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		ois.defaultReadObject();
		
		PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
		setNewEnvironment();
	}
}
