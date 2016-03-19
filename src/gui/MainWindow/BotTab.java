package gui.MainWindow;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.border.MatteBorder;

import gui.BotCard;

import java.awt.Color;
import javax.swing.Box;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class BotTab extends JPanel 
{
	private static final long serialVersionUID = 3937755224955412216L;

	/**
	 * Create the panel.
	 */
	public BotTab() 
	{
		
		JPanel botCard = new JPanel();
		botCard.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panel = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(botCard, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(4)
									.addComponent(botCard, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addContainerGap()
									.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		JTextPane txtpnLgg = new JTextPane();
		scrollPane_1.setViewportView(txtpnLgg);
		
		
		JTextPane txtpnChatique = new JTextPane();
		scrollPane.setViewportView(txtpnChatique);
		txtpnChatique.setFont(new Font("Verdana", Font.PLAIN, 12));
		BotCard botCard_1 = new BotCard();
		botCard_1.setPreferredSize(new Dimension(0, 0));
		botCard_1.setMinimumSize(new Dimension(0, 0));
		GroupLayout gl_botCard = new GroupLayout(botCard);
		gl_botCard.setHorizontalGroup(
			gl_botCard.createParallelGroup(Alignment.LEADING)
				.addComponent(botCard_1, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
		);
		gl_botCard.setVerticalGroup(
			gl_botCard.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_botCard.createSequentialGroup()
					.addGap(5)
					.addComponent(botCard_1, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
		);
		botCard.setLayout(gl_botCard);
		
		Box dialogList = Box.createVerticalBox();
		dialogList.add(new DialogListEntry());
		
		panel.add(dialogList);
		setLayout(groupLayout);

	}
}
