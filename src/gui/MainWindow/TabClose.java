package gui.MainWindow;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Insets;

public class TabClose extends JPanel 
{
	private static final long serialVersionUID = 2692837309951117084L;
	private static ResourceBundle locale = ResourceBundle.getBundle("resources.locale.TabClose.messages");

	private String name;
	
	JButton buttonClose;
		
	public TabClose(String tabName) 
	{
		name = tabName;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel(name);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblNewLabel);
		
		buttonClose = new JButton("x");
		buttonClose.setMargin(new Insets(0, 2, 0, 2));
		buttonClose.setHorizontalAlignment(SwingConstants.LEFT);
		buttonClose.setToolTipText(locale.getString("buttonCloseToolTip"));
		add(buttonClose);
	}
}
