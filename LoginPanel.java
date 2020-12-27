import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {

	private RPList playerList;
	private RoulettePlayer player;
	//passing a LoginInterface object allows access to setPlayer() method
	private LoginInterface logIn;
	private JPanel middle;
	private JLabel theMessage, labelID, labelPass;
	private TextField enterID, enterPass;
	private JButton submit;
	private final Font regFont = new Font("Serif", Font.BOLD, 35);
	
	public LoginPanel(RPList pl, LoginInterface L) {
		playerList = pl;
		logIn = L;
		this.setLayout(new GridLayout(3, 1));
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		theMessage = new JLabel("Please log into the site");
		theMessage.setFont(regFont);
		this.add(theMessage);
		
		//a subpanel with 2x2 dimensions is created to hold
		//the JLabels and JTextFields needed for username
		//and password input
		middle = new JPanel();
		middle.setLayout(new GridLayout(2, 2));
		middle.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		labelID = new JLabel("User ID: ");
		labelID.setFont(regFont);
		middle.add(labelID);
		enterID = new TextField(8);
		enterID.setFont(regFont);
		middle.add(enterID);
		labelPass = new JLabel("Password: ");
		labelPass.setFont(regFont);
		middle.add(labelPass);
		enterPass = new TextField(8);
		enterPass.setEchoChar('*');	//this replaces the text the user enters with asterisks
		enterPass.setFont(regFont);
		middle.add(enterPass);
		this.add(middle);
		
		submit = new JButton("Submit");
		submit.setFont(regFont);
		submit.addActionListener(new SubmitListener());
		this.add(submit);
	}
	private class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String id = enterID.getText();
			String password = enterPass.getText();
			//checking to see if user ID exists within the RPList
			if (playerList.checkId(id)) {
				player = playerList.getPlayerPassword(id, password);
				if (player == null) {
					JOptionPane.showMessageDialog(null,
						("Password doesn't match for " + id), 
						"Message", 
						JOptionPane.WARNING_MESSAGE);
					enterPass.setText("");	//this makes the JTextField for password
				}							//empty, allowing the user to try to log
											//in again under the same username
				else {
					JOptionPane.showMessageDialog(null, ("Welcome " + id + "!"));
					logIn.setPlayer(player); //calling setPlayer() method to
				}							 //save player to memory
			}
			else {
				JOptionPane.showMessageDialog(null,
					("ID " + id + " was not found"), 
					"Message", 
					JOptionPane.WARNING_MESSAGE);
				//sets both JTextFields to be blank so user can try to log in
				//again with under an actually existing username
				enterID.setText("");
				enterPass.setText("");
			}
		}
	}
}