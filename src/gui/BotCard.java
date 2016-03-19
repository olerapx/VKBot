package gui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class BotCard extends JPanel 
{

	private static final long serialVersionUID = -7514894457484952214L;
	private JTextField txtBooblieck;
	private JTextField txtOnline;

	public BotCard()
	{
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		txtBooblieck = new JTextField();
		txtBooblieck.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtBooblieck.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtBooblieck.setText("Booblieck ");
		txtBooblieck.setEditable(false);
		txtBooblieck.setColumns(10);
		
		txtOnline = new JTextField();
		txtOnline.setEditable(false);
		txtOnline.setText("online");
		txtOnline.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("F:\\Program Files\\Eclipse\\projects\\VKBot\\src\\em4.png"));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 101, Short.MAX_VALUE)
						.addComponent(txtBooblieck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtOnline, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtBooblieck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(41)
							.addComponent(txtOnline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(130, Short.MAX_VALUE))
		);
		
		JButton btnNewButton = new JButton("ON");
		toolBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("LOAD");
		toolBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("SET");
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("RM");
		toolBar.add(btnNewButton_3);
		setLayout(groupLayout);

	}
}
