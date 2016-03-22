package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LoginWindowController implements Initializable
{
	final static String resourcePath = "resources.locale.LoginWindow.messages";
	final static String fxmlPath = "LoginWindow.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	
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
		
	}
}
