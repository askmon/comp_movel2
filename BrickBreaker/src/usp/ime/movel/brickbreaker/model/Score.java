package usp.ime.movel.brickbreaker.model;

public class Score {
	private Long id = null;
	private int score = 0;

	public Score() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}