package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.sig4j.signal.Signal1;

public class BrowserDialogWindowController implements Initializable
{
	public final Signal1<Boolean> sendBrowserResult = new Signal1<>();
	
	final static String resourcePath = "resources.locale.BrowserDialogWindow.messages";
	final static String fxmlPath = "BrowserDialogWindow.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;	
	@FXML private WebView webView;
	@FXML private Button cancelButton;
	
	private WebEngine engine;
		
	String URL;
	
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;			
		this.engine = webView.getEngine();
	}
	
	void initWindow()
	{
		Scene scene = root.getScene();
		Window window = scene.getWindow();
		
		window.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
	          public void handle(WindowEvent we) 
	          {
	        	  we.consume();
	          }
	      });
		
		this.engine.locationProperty().addListener(new ChangeListener<String>() 
		{
            public void changed(ObservableValue<? extends String> prop, final String before, final String after) 
            {
            	//true if OK
                Platform.runLater(new Runnable() 
                {
                     public void run() 
                     {
                    	 close();
                     }  
                });
           }
      });
	}

	public void setURL (String URL)
	{
		this.URL = URL;
		this.engine.load(URL);
	}
	
	@FXML private void onCancel()
	{
		sendBrowserResult.emit(false);
		close();
	}
	
	private void close()
	{
		root.getScene().getWindow().hide();
		sendBrowserResult.clear();
	}
}
