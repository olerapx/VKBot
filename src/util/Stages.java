package util;


import javafx.application.Platform;
import javafx.stage.Stage;

public class Stages 
{	
	public static void close (Stage stage)
	{
		Platform.runLater(new Runnable()
		{
			public void run() 
			{
				stage.close();						
			}			
		});
	}
}
