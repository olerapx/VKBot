package gui.MainWindow;

import javax.swing.JPanel;

import gui.BotCard;
import gui.WrapLayout;


public class MainTab extends JPanel 
{
	private static final long serialVersionUID = 4657131159060870118L;
	
	public MainTab() 
	{
		setLayout(new WrapLayout());
		WrapLayout wrapLayout = (WrapLayout) getLayout();
		wrapLayout.setAlignment(WrapLayout.LEFT);
		BotCard botCard = new BotCard();
		add(botCard);
		add(new BotCard());
		add(new BotCard());
	}

}
