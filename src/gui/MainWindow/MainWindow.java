package gui.MainWindow;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONObject;
import org.json.JSONWriter;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import api.attachment.Link;
import api.client.Client;
import api.dialog.Message;
import api.user.User;
import api.user.UserSearchParameters;
import api.worker.WorkerInterface;
import crypto.Decryptor;
import crypto.Encryptor;


import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;

import javax.swing.UIManager;

import javax.swing.border.CompoundBorder;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;

public class MainWindow 
{
	private static ResourceBundle locale = ResourceBundle.getBundle("resources.locale.MainWindow.messages"); //$NON-NLS-1$

	private JFrame frmUchanVkbot;

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
		test();
	}
	
	public MainWindow() throws Exception
	{
		initialize();
	}

	private void initialize() throws Exception
	{	
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    
	    initFrame();	
	    
	    addMenuBar();			
		addTabPane();

		frmUchanVkbot.setTitle("UChan VKBot");
		frmUchanVkbot.setBounds(100, 100, 999, 638);
		frmUchanVkbot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void loadLocale(String language, String country)
	{
		Locale l = new Locale(language, country);
		Locale.setDefault(l);
		locale = ResourceBundle.getBundle("locale.MainWindow.messages");
	}
	
	private void initFrame()
	{
		frmUchanVkbot = new JFrame();
		frmUchanVkbot.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/logo.jpg")));
		frmUchanVkbot.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
	}
	
	private void addMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		frmUchanVkbot.setJMenuBar(menuBar);

		addMenuFile(menuBar);
		addMenuBot(menuBar);
		addMenuHelp(menuBar);
	}
	
	private void addMenuFile(JMenuBar menuBar)
	{
		JMenu menuFile = new JMenu(locale.getString("MainWindow.menuFile.text")); //$NON-NLS-1$
		menuBar.add(menuFile);
		
		JMenuItem fileItemNew = new JMenuItem(locale.getString("MainWindow.fileItemNew.text")); //$NON-NLS-1$
		fileItemNew.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				addNewBot();
			}
		});
		menuFile.add(fileItemNew);
		
		JMenuItem fileItemOpen = new JMenuItem(locale.getString("MainWindow.fileItemOpen.text")); //$NON-NLS-1$
		menuFile.add(fileItemOpen);
		
		JMenuItem fileItemExit = new JMenuItem(locale.getString("MainWindow.fileItemExit.text")); //$NON-NLS-1$
		fileItemExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				frmUchanVkbot.dispatchEvent(new WindowEvent(frmUchanVkbot, WindowEvent.WINDOW_CLOSING));
			}
		});
		menuFile.add(fileItemExit);
	}
	
	private void addMenuBot(JMenuBar menuBar)
	{
		JMenu menuBot = new JMenu(locale.getString("MainWindow.menuBot.text")); //$NON-NLS-1$
		menuBar.add(menuBot);
		
		JMenuItem botItemToggle = new JMenuItem(locale.getString("MainWindow.botItemToggle.text")); //$NON-NLS-1$
		menuBot.add(botItemToggle);
		
		JMenuItem botItemTask = new JMenuItem(locale.getString("MainWindow.botItemTask.text")); //$NON-NLS-1$
		menuBot.add(botItemTask);
		
		JMenuItem botItemRemove = new JMenuItem(locale.getString("MainWindow.botItemRemove.text")); //$NON-NLS-1$
		menuBot.add(botItemRemove);
		
		JMenuItem botItemDelete = new JMenuItem(locale.getString("MainWindow.botItemDelete.text")); //$NON-NLS-1$
		menuBot.add(botItemDelete);
	}
	
	
	private void addMenuHelp(JMenuBar menuBar)
	{
		JMenu menuHelp = new JMenu(locale.getString("MainWindow.menuHelp.text")); //$NON-NLS-1$
		menuBar.add(menuHelp);
		
		JMenu helpMenuLang = new JMenu(locale.getString("MainWindow.helpMenuLang.text")); //$NON-NLS-1$
		menuHelp.add(helpMenuLang);
		
		JRadioButtonMenuItem langItemEn = new JRadioButtonMenuItem(locale.getString("MainWindow.langItemEn.text")); //$NON-NLS-1$
		helpMenuLang.add(langItemEn);
		
		JRadioButtonMenuItem langItemRu = new JRadioButtonMenuItem(locale.getString("MainWindow.langItemRu.text")); //$NON-NLS-1$
		langItemRu.setSelected(true);
		helpMenuLang.add(langItemRu);
		
		JMenuItem helpItemAbout = new JMenuItem(locale.getString("MainWindow.helpItemAbout.text")); //$NON-NLS-1$
		menuHelp.add(helpItemAbout);
	}
	
	private void addTabPane()
	{
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);	
		
		addMainTab(tabbedPane);
		
		tabbedPane.add(new BotTab());
		tabbedPane.add(new BotTab());	
		addCloseToTab(tabbedPane, tabbedPane.getComponentAt(1), "Woo");
		addCloseToTab(tabbedPane, tabbedPane.getComponentAt(2), "Woo1");
		
		tabbedPane.setBorder(new CompoundBorder());
		GroupLayout groupLayout = new GroupLayout(frmUchanVkbot.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
					.addGap(5))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
					.addGap(5))
		);
		frmUchanVkbot.getContentPane().setLayout(groupLayout);
	}
	
	private void addMainTab(JTabbedPane pane)
	{
		if (pane.getComponentCount()>0) return;
		
		MainTab mainTab = new MainTab();
		FlowLayout flowLayout = (FlowLayout) mainTab.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pane.addTab(locale.getString("MainWindow.mainTab.title"), mainTab);
	}
	
	private void addCloseToTab(JTabbedPane pane, Component tab, String name)
	{	
		TabClose tabClose = new TabClose(name);
		pane.setTabComponentAt(pane.indexOfComponent(tab), tabClose);
		
		tabClose.buttonClose.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
		        int index = pane.indexOfComponent(tab);
		        if (index>=0) 
		            pane.remove(index);
			}
		});
	}

	private void addNewBot()
	{
		
	}
	
	private static void login() throws Exception
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
	
	public static void test() throws Exception
	{	
		/*
		
		WorkerInterface wi = new WorkerInterface(client);
		WorkerInterface wi1 = new WorkerInterface(c1);		
		
		long startTime = System.currentTimeMillis();
		for (int i=0;i<30;i++)
		{
			wi.messageWorker().sendMessageToUser(new Message(gen() + " "+i), client.me);
			wi1.messageWorker().sendMessageToUser(new Message(gen() + " "+i), client.me);
			long endTime = System.currentTimeMillis();
			System.out.println("query# "+i+": " + (endTime-startTime));
			if (i%3==0)
			{
				startTime = endTime;
				System.out.println("pass");
			}
			
		}
		*/

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