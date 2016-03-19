package gui.MainWindow;

import javax.swing.JPanel;

import gui.BotCard;
import gui.WrapLayout;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class MainTab extends JPanel 
{
	private static final long serialVersionUID = 4657131159060870118L;
	
	public MainTab() 
	{
		setLayout(new BorderLayout(0, 0));
			
			JScrollPane scrollPane = new JScrollPane();
			add(scrollPane);			
			
			JPanel panel = new JPanel();
			scrollPane.setViewportView(panel);
			
			WrapLayout wl_panel = new WrapLayout();
			wl_panel.setVgap(1);
			panel.setLayout(wl_panel);
			
			BotCard botCard_1 = new BotCard();
			botCard_1.setPreferredSize(new Dimension(280, 300));
			botCard_1.setMinimumSize(new Dimension(0, 0));
			panel.add(botCard_1);			

			BotCard botCard = new BotCard();
			botCard.setPreferredSize(new Dimension(280, 300));
			botCard.setMinimumSize(new Dimension(0, 0));
			panel.add(botCard);
			BotCard botCard_2 = new BotCard();
			botCard_2.setPreferredSize(new Dimension(280, 300));
			botCard_2.setMinimumSize(new Dimension(0, 0));
			panel.add(botCard_2);
	}

}
