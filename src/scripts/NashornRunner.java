package scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import api.worker.WorkerInterface;

/**
 * Provides an evaluation and an execution of a javascript file.
 */
public class NashornRunner implements ScriptRunner 
{
	private static final long serialVersionUID = 3307134212267034370L;
	
	transient ScriptEngine en;
	transient Bindings bindings;
	WorkerInterface wi;
	
	String stopVariableName = "stop";
	boolean isRunning = false;
	
	public NashornRunner(WorkerInterface wi) 
	{
		this.wi = wi;		
		
		en = new ScriptEngineManager().getEngineByName("nashorn");		
		setNewEnvironment();
	}

	/**
	 * @see ScriptRunner#setNewEnvironment()
	 */
	public void setNewEnvironment() 
	{
		bindings = new SimpleBindings();
		bindings.put("workerInterface", wi);
		bindings.put(stopVariableName, false);
	}

	public void execFile(File file) throws FileNotFoundException, ScriptException 
	{
		this.isRunning = true;
		
		en.eval(new FileReader(file.getAbsolutePath()), bindings);
		
		this.isRunning = false;
	}

	public void stop() 
	{
		if (isRunning)
		{
			bindings.put(stopVariableName, true);
		}
	}
	
	public ScriptEngine js() {return this.en;}
	public Bindings bindings() {return this.bindings;}
	
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		oos.defaultWriteObject();	
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		ois.defaultReadObject();
		
		en = new ScriptEngineManager().getEngineByName("nashorn");		
		setNewEnvironment();
	}
}
