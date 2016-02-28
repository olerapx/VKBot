package gui;
import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;

import org.json.JSONObject;
import org.json.JSONWriter;

import api.attachment.Attachment;
import api.attachment.MediaAttachment;
import api.client.Client;
import api.dialog.Message;
import api.dialog.MessageWorker;
import api.media.Audio;
import api.media.AudioWorker;
import api.media.DocWorker;
import api.media.MediaID;
import api.media.Photo;
import api.media.PhotoWorker;
import api.media.VideoWorker;
import api.media.WallPost;
import api.media.WallPostWorker;
import api.media.comment.Comment;
import api.media.comment.CommentWorker;
import api.media.comment.WallComment;
import api.user.UserWorker;
import crypto.Decryptor;
import crypto.Encryptor;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class MainWindow 
{
	private JFrame frmUchanVkbot;
	private JTextField textField;
	private JLabel label;
	
	private static Client client;

	public static void main(String[] args) throws Exception  
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					MainWindow window = new MainWindow();
					window.frmUchanVkbot.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		login();
		test();
	}
	
	public MainWindow() throws Exception
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize()
	{
		frmUchanVkbot = new JFrame();
		frmUchanVkbot.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/logo.jpg")));
		frmUchanVkbot.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		textField = new JTextField();
		frmUchanVkbot.getContentPane().add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		frmUchanVkbot.setTitle("UChan VKBot");
		frmUchanVkbot.setBounds(100, 100, 897, 598);
		frmUchanVkbot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		label = new JLabel();
		frmUchanVkbot.getContentPane().add(label, BorderLayout.CENTER);
	}
	
	private static void login() throws Exception
	{
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
	
	public static void test() throws Exception
	{	
		UserWorker uw = new UserWorker(client);
		
		MessageWorker mw = new MessageWorker(client);
		AudioWorker aw = new AudioWorker(client);
		PhotoWorker pw = new PhotoWorker(client);
		VideoWorker vw = new VideoWorker(client);
		DocWorker dw = new DocWorker(client);
		WallPostWorker ww = new WallPostWorker(client);	
		CommentWorker cw = new CommentWorker(client);
	}

	public static String gen()
	{
        String arrNoVowel ="абвгдеёжзийклмнопрстуфхцчшщьыъэюя";
        int numberLetters=10;
 
        Random rand = new Random();
        String out = new String();
        for(int i = 0; i < numberLetters; i++) {
           out+=""+arrNoVowel.charAt(rand.nextInt( arrNoVowel.length() ));
        }
        return out;
	}
}
