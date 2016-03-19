package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;
import org.json.JSONWriter;

import api.client.Client;
import crypto.Decryptor;
import crypto.Encryptor;

public class NewBotDialog extends JDialog 
{
	private static final long serialVersionUID = -4730031659819032635L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewBotDialog dialog = new NewBotDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewBotDialog() throws Exception 
	{
		setResizable(false);
		setModal(true);
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		setBounds(100, 100, 400, 501);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
	
	@SuppressWarnings("unused")
	private void login() throws Exception
	{
		Client client;
		File file = new File("cache/user.dat");
		
		if(!file.exists())
		{
			file.createNewFile();
			StringWriter string = new StringWriter();
			
			JSONWriter writer = new JSONWriter(string);
	        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	        
			writer.object();
			
			System.out.println("Input email");
			writer.key("email");
			writer.value(input.readLine());
			
			System.out.println("Input pass");
			writer.key("pass");
			writer.value(input.readLine());
			
			writer.endObject();
			
			string.close();
			
			Encryptor en = new Encryptor();
			en.encrypt(string.toString(), file, "cache/user.cr");
		}
		
		Decryptor de = new Decryptor();
		String userdata = de.decrypt(file, new File("cache/user.cr"));
		
		JSONObject data = new JSONObject(userdata);
		
		client = new Client(data.getString("email"), data.getString("pass"));
		System.out.println("Logged in under token:\n"+client.token);
	}

}
