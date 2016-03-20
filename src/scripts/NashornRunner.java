package scripts;

import java.io.File;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import api.worker.WorkerInterface;

public class NashornRunner implements ScriptRunner 
{
	ScriptEngine en;
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
}
