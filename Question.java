public class Question {
	
	private String question;
	private String answer;
	
	public Question() {
		question = "";
		answer = "";
	}
	public Question(String q, String a) {
		question = q;
		answer = a;
	}
	public String toString() {
		StringBuilder S = new StringBuilder();
		S.append("Q: " + question);
		S.append(" A: " + answer);
		return S.toString();
	}
	public String getQuestion() {
		return question;
	}
	public String getAnswer() {
		return answer;
	}
	//checks to see if both the question and answer match
	public boolean equals(Question que) {
		if (que != null && (question.equals(que.getQuestion())) && (answer.equals(que.getAnswer()))) {
			return true;
		}
		else {
			return false;
		}
	}
}