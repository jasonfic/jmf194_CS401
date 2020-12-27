import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoulettePanel extends JPanel implements Activatable {

	private RoulettePlayer player;
	//these 2 declarations below avoid errors about variables not being initialized
	private RouletteBet bet = null;
	private String betValue = "";
	private double betAmount;
	private GameInterface game;
	private RouletteWheel wheel;
	private JPanel gameButtons, wheelPanel;
	private JLabel display;
	private JButton betButton, spinButton, infoButton, quitButton;
	private final Font myFont = new Font("TimesRoman", Font.BOLD, 40);
	private final Font regFont = new Font("Helvetica", 0, 30);
	
	public RoulettePanel(RoulettePlayer rp, GameInterface G) {
		player = rp;
		game = G;	//allows access to gameOver() method
		this.setLayout(new GridLayout(1, 2));
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		JOptionPane.showMessageDialog(null, ("Welcome to Roulette " + rp.getName() + "!"));
		ActionListener play = new RouletteListener();
		
		//creates a subpanel for the JLabel and various buttons
		gameButtons = new JPanel();
		gameButtons.setLayout(new GridLayout(5, 1));
		gameButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		display = new JLabel("Ready to Play, " + rp.getName());	//this JLabel changes throughout game
		display.setFont(regFont);
		gameButtons.add(display);
		betButton = new JButton("Make Bet");
		betButton.setFont(myFont);
		betButton.addActionListener(play);
		if (!player.hasMoney()) {
			display.setText("Sorry, but you are out of money!");
			betButton.setEnabled(false); //doesn't allow user to access button to place
		}								 //a bet if they have no money in their account
		gameButtons.add(betButton);
		spinButton = new JButton("Spin Wheel");
		spinButton.setFont(myFont);
		spinButton.addActionListener(play);
		spinButton.setEnabled(false);
		gameButtons.add(spinButton);
		infoButton = new JButton("Show My Info");
		infoButton.setFont(myFont);
		infoButton.addActionListener(play);
		gameButtons.add(infoButton);
		quitButton = new JButton("Quit");
		quitButton.setFont(myFont);
		quitButton.addActionListener(play);
		gameButtons.add(quitButton);
		
		wheelPanel = new JPanel();
		wheel = new RouletteWheel(this);
		wheel.setPreferredSize(new Dimension (500, 500)); //if I don't do this,
		wheelPanel.add(wheel);							  //RouletteWheel looks squished
		
		this.add(gameButtons);
		this.add(wheelPanel);
	}
	private class RouletteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == betButton) {
				boolean illegal = true;
				//do-while loop repeats until user enters a legal amount for their bet
				do {
					String amountStr = JOptionPane.showInputDialog("How much to bet? (<=" + player.getMoney() + ")");
					betAmount = Double.parseDouble(amountStr);
					if (betAmount > 0 && betAmount <= player.getMoney()) {
						illegal = false;
					}
				} while (illegal == true);
				String typeStr = JOptionPane.showInputDialog("Type of your bet? [Value, Color, Range, Parity]");
				RBets betType = RBets.valueOf(typeStr);
				if (betType == RBets.Color) {
					betValue = JOptionPane.showInputDialog("Enter your color [Red, Black]: ");
				}
				else if (betType == RBets.Parity) {
					betValue = JOptionPane.showInputDialog("Enter your parity [Even, Odd]: ");
				}
				else if (betType == RBets.Range) {
					betValue = JOptionPane.showInputDialog("Enter your range [Low, High]: ");
				}
				else if (betType == RBets.Value) {
					betValue = JOptionPane.showInputDialog("Enter your number [0-36]: ");
				}
				display.setText("You have bet $" + betAmount + " on " + betValue);
				bet = new RouletteBet(betType, betValue);
				//after bet is placed, winning number from previous round
				//of the game is unchosen and wheel can be spun again
				spinButton.setEnabled(true);
				wheel.set();
			}
			else if (e.getSource() == spinButton) {
				//removes access to all buttons other than Show Info
				//and then calls spin() method from RouletteWheel
				betButton.setEnabled(false);
				spinButton.setEnabled(false);
				quitButton.setEnabled(false);
				wheel.spin();
			}
			else if (e.getSource() == infoButton) {
				JOptionPane.showMessageDialog(null, "Here is your info:\n" + player.toString());
			}
			else if (e.getSource() == quitButton) {
				game.gameOver();
			}
		}
	}
	//when a spin is completed, this method displays the result of the spin,
	//shows whether or not the user's bet won, and updates their money
	//it then greys out the Make Bet button if the user runs out of money
	public void activate() {
		JOptionPane.showMessageDialog(null, ("Result:\n" + wheel.getResult()));
		StringBuilder str1 = new StringBuilder("Your bet on " + betValue);
		StringBuilder str2 = new StringBuilder("");
		int res = wheel.checkBet(bet);
		if (res == 0) {
			str1.append(" has lost");
			str2.append("Sorry, but you lose $" + betAmount);
			player.updateMoney(-betAmount);
		}
		else if (res == 1) {
			str1.append(" has won!");
			str2.append("Even money winner of $" + betAmount);
			player.updateMoney(betAmount);
		}
		else if (res == 35) {
			str1.append(" has won big!");
			str2.append("Big money winner of $" + betAmount);
			player.updateMoney(35 * betAmount);
		}
		JOptionPane.showMessageDialog(null, str1.toString());
		JOptionPane.showMessageDialog(null, str2.toString());
		if (player.hasMoney()) {
			betButton.setEnabled(true);
			display.setText("You now have $" + player.getMoney() + " left");
		}
		else {
			display.setText("Sorry, but you are out of money!");
			betButton.setEnabled(false);
		}
		spinButton.setEnabled(false);
		infoButton.setEnabled(true);
		quitButton.setEnabled(true);
	}
}