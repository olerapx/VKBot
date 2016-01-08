import java.awt.AWTException;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

import media.AudioWorker;
import message.Attachment;
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
	private VKClient client;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws AWTException, IOException, URISyntaxException, BadLocationException  {
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
	private void initialize() throws Exception
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
		
		client = new VKClient();

		
	/*	MessageWorker mw = new MessageWorker(client.httpClient, client.token);
		int i=0;
		while(true)
		{
			Thread.sleep(25000);
			mw.sendMessage(new Message(323086251, "&#127814;  #"+i));
			i++;
		}
		*/
	}
	
	public String gen()
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
