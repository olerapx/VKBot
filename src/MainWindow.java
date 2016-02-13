import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;

import org.json.JSONException;

import client.VKClient;
import media.Audio;
import media.AudioWorker;
import media.Photo;
import media.PhotoWorker;
import media.Video;
import media.VideoWorker;
import media.WallPost;
import media.WallPostReply;
import media.WallPostWorker;
import media.Doc;
import media.DocWorker;
import message.Attachment;
import message.Attachment.Type;
import message.Message;
import message.MessageWorker;
import user.User;
import user.UserWorker;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class MainWindow {

	private JFrame frmUchanVkbot;
	private JTextField textField;
	
	private static VKClient client;

	/**
	 * Launch the application.
	 * @throws Exception 
	 * @throws JSONException 
	 * @throws UnsupportedOperationException 
	 */
	public static void main(String[] args) throws Exception  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmUchanVkbot.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		test();
	}
	
	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public MainWindow() throws Exception{
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
		frmUchanVkbot.setTitle("Uchan VKBot");
		frmUchanVkbot.setBounds(100, 100, 897, 598);
		frmUchanVkbot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void test() throws Exception
	{
		client = new VKClient("xxxxxx", "xxxxxx");
		
		UserWorker uw = new UserWorker(client);
		
		MessageWorker mw = new MessageWorker(client);
		AudioWorker aw = new AudioWorker(client);
		PhotoWorker pw = new PhotoWorker(client);
		VideoWorker vw = new VideoWorker(client);
		DocWorker dw = new DocWorker(client);
		WallPostWorker ww = new WallPostWorker(client);
				
		System.out.println(client.token);
	}
	
	public static String gen()
	{
        String arrNoVowel ="אבגדהו¸זחטיךכלםמןנסעףפץצקרשüûת‎‏ÿ";
        int numberLetters=10;
 
        Random rand = new Random();
        String out = new String();
        for(int i = 0; i < numberLetters; i++) {
           out+=""+arrNoVowel.charAt(rand.nextInt( arrNoVowel.length() ));
        }
        return out;
	}

}
