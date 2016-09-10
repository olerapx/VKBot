package scripts;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import api.worker.WorkerInterface;

/**
 * Provides an evaluation and an execution of a javascript file.
 */
public class NashornRunner implements ScriptRunner 
{
	private static final long serialVersionUID = 3307134212267034370L;
	
	transient ScriptEngine en;
	Bindings bindings;
	WorkerInterface wi;
	
	String stopVariableName = "stop";
	boolean isRun = false;
	
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

	public void execFile(File file) throws Exception
	{
		this.isRun = true;
		
		en.eval(new FileReader(file.getAbsolutePath()), bindings);
		
		this.isRun = false;
	}

	public void stop() 
	{
		if (isRun)
		{
			bindings.put(stopVariableName, true);
			isRun = false;
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
	}
}
