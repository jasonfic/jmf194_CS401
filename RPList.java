import java.util.*;
import java.io.*;
public class RPList {

	private RoulettePlayer [] playerList;
	private int playerNum;
	private String fileName;
	
	public RPList() {
		fileName = "";
		playerNum = 0;
		playerList = new RoulettePlayer[0];
	}
	public RPList(String fn) {
		try {
			fileName = fn;
			Scanner inScan = new Scanner(new File(fileName));
			playerNum = Integer.parseInt(inScan.nextLine());
			playerList = new RoulettePlayer[playerNum];
			for (int i = 0; i < playerNum; i++) {
				String newPlayer = inScan.nextLine();
				String [] newPlayerData = newPlayer.split(",");
				String name = newPlayerData[0];
				String password = newPlayerData[1];
				double money = Double.parseDouble(newPlayerData[2]);
				double debt = Double.parseDouble(newPlayerData[3]);
				//if the array of data retrieved from the file has
				//more than 4 variables, then the rest of them must
				//be security questions
				if (newPlayerData.length > 4) {
					String q1q = newPlayerData[4];
					String q1a = newPlayerData[5];
					String q2q = newPlayerData[6];
					String q2a = newPlayerData[7];
					playerList[i] = new RoulettePlayer(name, password, money, debt, q1q, q1a, q2q, q2a);
				}
				else {
					playerList[i] = new RoulettePlayer(name, password, money, debt);
				}
			}
			inScan.close();
		}
		catch (IOException e) {
			System.out.println("Problem reading file");
		}
	}
	public String toString() {
		StringBuilder P = new StringBuilder();
		P.append("Players:");
		if (playerList.length > 0) {
			for (int i = 0; i < playerList.length; i++) {
				if (playerList[i] != null) {
					P.append("\n        " + playerList[i].toString());
				}
			}
		}
		return P.toString();
	}
	//returns the logical size of the RPList
	public int getSize() {
		int count = 0;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null) {
				count++;
			}
		}
		return count;
	}
	//returns the physical size of the RPList
	public int getASize() {
		return playerList.length;
	}
	//returns "true" if the given ID matches the name
	//of one of the RoulettePlayers in the RPList
	public boolean checkId(String id) {
		boolean match = false;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null) {
				if (id.equalsIgnoreCase(playerList[i].getName())) {
					match = true;
					break;
				}
				else {
					match = false;
				}
			}
		}
		return match;
	}
	//returns the RoulettePlayer within the RPLIST
	//that matches the given ID and password
	public RoulettePlayer getPlayerPassword(String id, String p) {
		RoulettePlayer rp = new RoulettePlayer();
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && id.equals(playerList[i].getName())) {
				if (p.equals(playerList[i].getPassword())) {
					rp = playerList[i];
				}
				else {
					rp = null;
				}
			}
		}
		return rp;
	}
	public RoulettePlayer getPlayerId(String id) {
		RoulettePlayer rp = new RoulettePlayer();
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && id.equals(playerList[i].getName())) {
				rp = playerList[i];
			}
			else {
				rp = null;
			}
		}
		return rp;
	}
	//This method takes in a player's name and an array of Question objects,
	//then returns the RoulettePlayer object that matches on both fields.
	//If none of the RoulettePlayers within the RPList match, the it returns null.
	public RoulettePlayer getPlayerQuestions(String id, Question [] q) {
		RoulettePlayer rp = new RoulettePlayer();
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && id.equals(playerList[i].getName())) {
				Question [] myQuestions = playerList[i].getWholeQuestions();
				boolean match = ((q[0].equals(myQuestions[0])) && q[1].equals(myQuestions[1]));
				if (match) {
					rp = playerList[i];
					return rp;
				}
				else {
					rp = null;
				}
			}
			else {
				rp = null;
			}
		}
		return rp;
	}
	//This method takes in a RoulettePlayer as an argument and
	//checks to see if it already exists within the RPList.
	//If it doesn't, it then checks to see if the RPList is full.
	//If it is, it then doubles the size of the array and adds
	//the new RoulettePlayer at the first empty index. It then
	//returns "true." If the array isn't full, it adds the new
	//RoulettePlayer without doubling the size.
	//Otherwise, it returns "false" and does nothing.
	public boolean add(RoulettePlayer rp) {
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && playerList[i].equals(rp)) {
				//found a match, terminates by declaring false
				return false;				
			}
		}
		if (playerNum == playerList.length) {
			//doubles size of array when physical size is same as logical size
			//copies data from playerList into a temporary array that's twice as big
			RoulettePlayer [] temp = new RoulettePlayer [2 * playerList.length];
			for (int i = 0; i < playerList.length; i++) {
				temp[i] = playerList[i];
			}
			playerList = temp;
		}
		//adds RoulettePlayer to first empty spot
		playerList[playerNum] = rp;
		playerNum++;
		return true;
	}
	public void overwrite(RoulettePlayer rp) {
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && playerList[i].equals(rp)) {
				playerList[i] = rp;
				return;
			}
		}
	}
	//obtains all the security questions for the RoulettePlayer with
	//the given name, returning them in an array of Question objects
	public String [] getQuestions(String id) {
		String [] myQuestions = null;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && id.equals(playerList[i].getName())) {
				if (playerList[i].getQuestions() != null) {
					myQuestions = playerList[i].getQuestions();
				}
			}
		}
		return myQuestions;
	}
	public String [] getAnswers(String id) {
		String [] myAnswers = null;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null && id.equals(playerList[i].getName())) {
				if (playerList[i].getAnswers() != null) {
					myAnswers = playerList[i].getAnswers();
				}
			}
		}
		return myAnswers;
	}
	//saves the RPList to a file with the values for each
	//RoulettePlayer separated by commas
	public void saveList() {
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(fileName);
			out = new BufferedWriter(fw);
			out.write(Integer.toString(playerNum));
			for (int i = 0; i < playerList.length; i++) {
				if (playerList[i] != null) { //avoids NullPointerException
					out.write("\n" + playerList[i].saveString());
				}
			}
			out.close(); //without this the file will be printed out blank
		}
		catch (IOException e) {
			System.out.println("Problem reading file");
		}
	}
}