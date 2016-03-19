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
	private JTextField txtImmaDoin;

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
		lblNewLabel.setIcon(new ImageIcon("F:\\Program Files\\Eclipse\\projects\\VKBot\\src\\Everlasting Summer 2015-04-13 23-19-59-453.jpg"));
		
		txtImmaDoin = new JTextField();
		txtImmaDoin.setText("IMMA DOIN");
		txtImmaDoin.setEditable(false);
		txtImmaDoin.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txtBooblieck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtOnline, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18, 18, Short.MAX_VALUE)
							.addComponent(lblNewLabel)
							.addGap(34))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(txtImmaDoin, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(191, Short.MAX_VALUE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtBooblieck, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(41)
							.addComponent(txtOnline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addComponent(txtImmaDoin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
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
