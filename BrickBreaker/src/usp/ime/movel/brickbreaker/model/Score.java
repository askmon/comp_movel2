package usp.ime.movel.brickbreaker.model;

public class Score {
	private Long id = null;
	private Integer score = 0;

	public Score() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}