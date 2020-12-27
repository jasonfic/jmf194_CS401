public class RoulettePlayer {
	
	//variables set to private to maintain data abstraction
	private String name;
	private String password;
	private double money;
	private double debt;
	private double change = 0;
	private Question [] securityQuestions = new Question[2];
	
	//constructors
	public RoulettePlayer() {
		name = "";
		password = "";
		money = 0;
		debt = 0;
	}
	public RoulettePlayer(String n, String p) {
		name = n;
		password = p;
		money = 0;
		debt = 0;
	}
	public RoulettePlayer(String n, String p, double m, double d) {
		name = n;
		password = p;
		money = m;
		debt = d;
	}
	//this constructer only used if player chooses to have security questions
	public RoulettePlayer(String n, String p, double m, double d, String q1q, String q1a, String q2q, String q2a) {
		name = n;
		password = p;
		money = m;
		debt = d;
		Question q1 = new Question(q1q, q1a);
		Question q2 = new Question(q2q, q2a);
		securityQuestions[0] = q1;
		securityQuestions[1] = q2;
	}
	
	//mutator that changes value of money by amount won or lost in bet
	//if bet is lost, value is negative so it subtracts from money
	public void updateMoney(double delta) {
		money += delta;
	}
	public void updateChange(double delta) {
		change += delta;
	}
	//accessors
	public double getMoney() {
		return money;
	}
	public double getChange() {
		return change;
	}
	public double getDebt() {
		return debt;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String delta) {
		password = delta;
	}
	public String toString() {
		StringBuilder S = new StringBuilder();
		S.append("Name: " + getName());
		S.append("  Cash: " + getMoney());
		S.append("  Debt: " + getDebt());
		return S.toString();
	}
	//this method saves contents of RoulettePlayer in a
	//format the separates each variable with a comma
	public String saveString() {
		StringBuilder S = new StringBuilder();
		S.append(name + ",");
		S.append(password + ",");
		S.append(money + ",");
		S.append(debt);
		//only adds questions if they exist
		if (securityQuestions[0] != null && securityQuestions[1] != null) {
			S.append("," + securityQuestions[0].getQuestion() + ",");
			S.append(securityQuestions[0].getAnswer() + ",");
			S.append(securityQuestions[1].getQuestion() + ",");
			S.append(securityQuestions[1].getAnswer());
		}
		return S.toString();
	}
	//while loop in main method never starts if this is false, and will end as soon as it's false
	public boolean hasMoney() {
		if (money > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	public void borrow(double delta) {
		debt += delta;
		updateMoney(delta);
	}
	//controls for user trying to pay back debt in various
	//ways, including ways that would cause an error
	public void payBack(double delta) {
		if ((delta <= debt) && (delta <= money)) {
			debt -= delta;
			updateMoney(-delta);
		}
		else if (delta > debt) {
			System.out.println("Amount: " + delta + " is more than borrowed: " + debt);
			System.out.println("Only paying back: " + debt);
			updateMoney(-debt);
			debt = 0;
		}
		else if (delta > money) {
			System.out.println("Amount: " + delta + " is more than cash: " + money);
			System.out.println("Only paying back: " + money);
			debt -= money;
			money = 0;
		}
	}
	public void addQuestions(Question [] delta) {
		securityQuestions[0] = delta[0];
		securityQuestions[1] = delta[1];
	}
	public void showAllData() {
		System.out.println("Name: " + name);
		System.out.println("Password: " + password);
		System.out.println("Cash: " + money);
		System.out.println("Debt: " + debt);
		boolean empty = true;
		for (Question q : securityQuestions) {
			if (q != null) {
				empty = false;
				break;
			}
		}
		if (empty == true) {
			System.out.println("Questions: None");
		}
		else {
			System.out.println(securityQuestions[0].toString());
			System.out.println(securityQuestions[1].toString());
		}
	}
	public boolean equals(RoulettePlayer rp) {
		if (name.equals(rp.getName()) && password.equals(rp.getPassword())) {
			return true;
		}
		else {
			return false;
		}
	}
	public String [] getQuestions() {
		if (securityQuestions.length == 0) {
			return null;
		}
		String [] qList = new String[2];
		for (Question q : securityQuestions) {
			if (q != null) {
				qList[0] = securityQuestions[0].getQuestion();
				qList[1] = securityQuestions[1].getQuestion();
				break;
			}
		}
		return qList;
	}
	public String [] getAnswers() {
		if (securityQuestions.length == 0) {
			return null;
		}
		String [] aList = new String[2];
		for (Question q : securityQuestions) {
			if (q != null) {
				aList[0] = securityQuestions[0].getAnswer();
				aList[1] = securityQuestions[1].getAnswer();
				break;
			}
		}
		return aList;
	}
	//returns the Question objects as a whole instead
	//of only the questions or answers within
	public Question [] getWholeQuestions() {
		Question [] wqList = new Question[2];
		for (int i = 0; i < securityQuestions.length; i++) {
			if (securityQuestions[i] != null) {
				wqList[i] = securityQuestions[i];
			}
		}
		return wqList;
	}
	//checks to see if the given array of Questions match the
	//security questions within the RoulettePlayer
	public boolean matchQuestions(Question [] q) {
		boolean match = false;
		if (securityQuestions.length == q.length) {
			for(int i = 0; i < securityQuestions.length; i++){
				if ((securityQuestions[i].getQuestion() == q[i].getQuestion()) && (securityQuestions[i].getAnswer() == q[i].getAnswer())){
					match = true;
				}
				else {
					match = false;
				}
			}
		}
		return match;
	}
}