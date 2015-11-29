package com.soat.exosSgcib.model;


public class TricTrac {

	private Boolean gameOver;
	
	private Boolean winner;
	
	private Boolean[][] tricTracTab;

	public Boolean[][] getTricTracTab() {
		return tricTracTab;
	}

	public void setTricTracTab(Boolean[][] tricTracTab) {
		this.tricTracTab = tricTracTab;
	}

	public Boolean getGameOver() {
		return gameOver;
	}

	public void setGameOver(Boolean gameOver) {
		this.gameOver = gameOver;
	}

	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}
	
	
}
