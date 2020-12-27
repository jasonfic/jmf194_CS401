import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//ThreadLocalRandom used to createn a random object within a range of values
import java.util.concurrent.ThreadLocalRandom;

//Runnable is commented out because it is only used
//in the class below which is used to create threads
public class RouletteWheel extends JPanel //implements Runnable 
{
	
	private Activatable active;
	private final RouletteSquare [] wheelNumbers = new RouletteSquare[37];
	//winning number initialized here so it can be accessed throughout
	//the class, including in the thread
	private RouletteSquare winNumber = null;
	
	public RouletteWheel(Activatable A) {
		//Activatable object passed in so that activate() method can be called later
		active = A;
		this.setLayout(new GridLayout(5, 8));
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//this loop iterates through full array of RouletteSquare numbers,
		//0 through 36, and adds them to the JPanel
		for (int i = 0; i < wheelNumbers.length; i++) {
			wheelNumbers[i] = new RouletteSquare(i);
			this.add(wheelNumbers[i]);
		}
	}
	
	//if player wins bet on color, range, or parity, result changes to 1
	//if player wins bet on value, result changes to 35
	//if player doesn't win anything, result stays at its initialized value of 0
	public int checkBet(RouletteBet b) {
		int res = 0;
		if (b.getBetType() == RBets.Color) {
			RColors colorBet = RColors.valueOf(b.getBetValue());
			if (colorBet == winNumber.getColor()) {
				res = 1;
			}
		}
		else if (b.getBetType() == RBets.Range) {
			RRanges rangeBet = RRanges.valueOf(b.getBetValue());
			if (rangeBet == winNumber.getRange()) {
				res = 1;
			}
		}
		else if (b.getBetType() == RBets.Parity) {
			RParities parityBet = RParities.valueOf(b.getBetValue());
			if (parityBet == winNumber.getParity()) {
				res = 1;
			}
		}
		else if (b.getBetType() == RBets.Value) {
			int valBet = Integer.parseInt(b.getBetValue());
			if (valBet == winNumber.getValue()) {
				res = 35;
			}
		}
		return res;
	}
	//if the wheel has been spun (and a winning number therefore exists), the
	//getResult() method from RouletteSquare is called. Otherwise returns null
	public RouletteResult getResult() {
		if (winNumber != null) {
			return winNumber.getResult();
		}
		else {
			return null;
		}
	}
	//resets winning number and iterates through array of RouletteWheel
	//objects, unchoosing each of them (even though only winning number
	//should be chosen in the first place, this is just a failsafe)
	public void set() {
		winNumber = null;
		for (int i = 0; i < wheelNumbers.length; i++) {
			wheelNumbers[i].unChoose();
		}
	}
	//creates a thread from the class below and tells the wheel to spin asynchronously
	public void spin() {
		//delay between selection of RouletteSquares is set to a random
		//amount of time between a 10th of a second and 1 second
		long delay = ThreadLocalRandom.current().nextLong(100, 1000);
		//sets duration of spin to a random amount of time between 6
		//seconds and 12 seconds
		long duration = ThreadLocalRandom.current().nextLong(6000, 12000);
		Runnable spinner = new RouletteSpin(delay, duration);
		new Thread(spinner).start();
	}
	//class that contains thread, draws heavily from RunnableTest.java
	//will choose and unchoose RouletteSquare objects within the RouletteWheel
	//for as long as the spin() method tells it to
	class RouletteSpin implements Runnable {
		
		int index = 0;
		long delay, duration;
		//creates thread based on arguments passed to constructor in spin()
		public RouletteSpin(long del, long dur) {
			delay = del;
			duration = dur;
		}
		public void run() {
			long start = System.nanoTime();
			long end = System.nanoTime();
			long delta = end - start;
			long durNano = duration * 1000000;
			while (delta <= durNano) {
				index = ThreadLocalRandom.current().nextInt(0, wheelNumbers.length);
				wheelNumbers[index].choose();
				try {
					Thread.sleep(delay);
				}
				catch (InterruptedException e) {
					System.out.println("Problem with Thread!");
				}
				end = System.nanoTime();
				delta = end - start;
				//checks if there is any time remaining to spin (i.e. if the difference
				//between duration and elapsed time is greater than zero)
				//if there is, then the current RouletteSquare is unchosen and the
				//loop repeats again
				if (durNano - delta > 0) {
					wheelNumbers[index].unChoose();
				}
				//when time runs out, the last selected RouletteSquare is kept
				//highlighted and saved as an instance variable
				else {
					winNumber = wheelNumbers[index];
				}
			}
			//once the spin is complete, the activate() method is called to 
			//allow access to buttons that were previously greyed out
			active.activate();
		}
	}
}