package com.mygdx.game;

public class Cell{
	int no_of_balls;
	int player;
	int moveLeft;
	int moveRight;
	int moveUp;
	int moveDown;

	public void setMoveDown(int moveDown) {
		this.moveDown = moveDown;
	}

	public void setMoveRight(int moveRight) {
		this.moveRight = moveRight;
	}

	public void setMoveUp(int moveUp) {
		this.moveUp = moveUp;
	}

	public void setMoveLeft(int moveLeft) {
		this.moveLeft = moveLeft;
	}

	public void setEmpty() {
		this.moveDown = 0;
		this.moveUp = 0;
		this.moveLeft = 0;
		this.moveRight = 0;
		this.player=0;
		this.no_of_balls=0;
	}
}

