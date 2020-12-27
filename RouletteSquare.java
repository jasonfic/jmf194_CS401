import java.awt.*;
import javax.swing.*;
import java.awt.event.*; 

public class RouletteSquare extends JLabel {

	private int value;
	private boolean selected;
	private RColors color;
	private RParities evo;
	private RRanges range;
	private RouletteResult result;
	
	public RouletteSquare(int num) {
		value = num;
		this.setText("" + value); //empty String added to convert from int to String
		this.setFont(new Font("Serif", 0, 40));
		this.setHorizontalAlignment(SwingConstants.CENTER);	//so that number isn't on left side of square
		if (value == 0) {
			color = RColors.valueOf("Green");
			evo = RParities.valueOf("None");
			range = RRanges.valueOf("None");
			this.setForeground(Color.GREEN);
		}
		else if (value > 0 && value <= 18){
			range = RRanges.valueOf("Low");
			if ((value % 2) == 0) {
				evo = RParities.valueOf("Even");
				if (value <= 10) {
					color = RColors.valueOf("Black");
				}
				else {
					color = RColors.valueOf("Red");
					this.setForeground(Color.RED);
				}
			}
			else if ((value % 2) != 0) {
				evo = RParities.valueOf("Odd");
				if (value < 10) {
					color = RColors.valueOf("Red");
					this.setForeground(Color.RED);
				}
				else {
					color = RColors.valueOf("Black");
				}
			}
		}
		else if (value > 18 && value <= 36) {
			range = RRanges.valueOf("High");
			if ((value % 2) == 0) {
				evo = RParities.valueOf("Even");
				if (value <= 28) {
					color = RColors.valueOf("Black");
				}
				else {
					color = RColors.valueOf("Red");
					this.setForeground(Color.RED);
				}
			}
			else if ((value % 2) != 0) {
				evo = RParities.valueOf("Odd");
				if (value < 28) {
					color = RColors.valueOf("Red");
					this.setForeground(Color.RED);
				}
				else {
					color = RColors.valueOf("Black");
				}
			}
		}
		//storing result so that it can be checked against a bet later
		result = new RouletteResult(color, range, evo, value);
	}
	public void choose() {
		selected = true;
		this.setOpaque(true);
		//couldn't find exact shade used in example, cyan  is the closest thing
		this.setBackground(Color.CYAN);
		this.repaint();
	}
	public void unChoose() {
		selected = false;
		this.setOpaque(false); //this removes any background color
		this.repaint();
	}
	//various accessors to give value of squares
	public boolean isChosen() {
		return selected;
	}
	public int getValue() {
		return value;
	}
	public RColors getColor() {
		return color;
	}
	public RParities getParity() {
		return evo;
	}
	public RRanges getRange() {
		return range;
	}
	public RouletteResult getResult() {
		return result;
	}
}