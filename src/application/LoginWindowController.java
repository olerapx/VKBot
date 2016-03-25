package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LoginWindowController implements Initializable
{
	final static String resourcePath = "resources.locale.LoginWindow.messages";
	final static String fxmlPath = "LoginWindow.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private GridPane grid;
	
	@FXML private Pane statusPane;
	
	@FXML private TextField loginText;
	@FXML private PasswordField passText;
	
	@FXML private ImageView captchaImage;
	@FXML private TextField captchaKey;
	
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;
	}
	
	@FXML private void onLogin()
	{	
		showCaptchaAnimation();
	}
	
	private void showCaptchaAnimation()
	{
        KeyFrame start;
        KeyFrame end;        
        
        boolean expanded =  captchaImage.visibleProperty().get();     

        if (expanded) 
        {
            start = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(2).prefHeightProperty(), 111));
            end = new KeyFrame(Duration.millis(200), new KeyValue(grid.getRowConstraints().get(2).prefHeightProperty(), 0));
            
            captchaImage.setVisible(false);
    		captchaImage.setManaged(false);
    		captchaKey.setVisible(false);
    		captchaKey.setManaged(false);
        } 
        else 
        {
            start = new KeyFrame(Duration.ZERO, new KeyValue(grid.getRowConstraints().get(2).prefHeightProperty(), 0));
            end = new KeyFrame(Duration.millis(200), new KeyValue(grid.getRowConstraints().get(2).prefHeightProperty(), 111));      
        }
     
        Timeline timeline = new Timeline(start, end);

        timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent event) 
            {
                if (!expanded)
                {
                    captchaImage.setVisible(true);
                	captchaImage.setManaged(true);
            		captchaKey.setVisible(true);
            		captchaKey.setManaged(true);
                }
            }
        });
        timeline.play();    
	}
}
