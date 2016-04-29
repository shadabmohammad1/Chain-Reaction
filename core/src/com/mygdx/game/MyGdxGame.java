package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor{
	int rows=10;
	int columns=6;
	int margin=10;
	int player=1;
	int flagLock;
	int playersToBeSwapped =0;
	int playerBot=2;	//AI Player
	int lock=0;	//so that user will be/not be able to place their moves
	Cell[][] cells;
	SpriteBatch batch;
	Texture bgTexture1;
	Texture bgTexture2;
	Texture[] ballRedTexture = new Texture[3];
	Texture[] ballBlueTexture = new Texture[3];
	Sprite bgSprite1;
	Sprite bgSprite2;
	Sprite[] ballRedSprite = new Sprite[3];
	Sprite[] ballBlueSprite = new Sprite[3];
	int screenWidth, screenHeight;
	int posX=15, posY=15;
	int cellWidth,cellHeight;
	int tempX,tempY;
	ShapeRenderer shapeDraw;
	GamePlayingAgent agent;
	OpponentsAgent agent2;
	
	Random random = new Random();
	
	@Override
	public void create () {
		screenWidth=Gdx.graphics.getWidth();
		screenHeight=Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		bgTexture1 = new Texture("bg1.jpg");
		bgTexture2 = new Texture("bg2.jpg");
		ballRedTexture[0]= new Texture("ballRed.png");
		ballRedTexture[1]= new Texture("ballRed2.png");
		ballRedTexture[2]= new Texture("ballRed3.png");
		ballBlueTexture[0]= new Texture("ballBlue.png");
		ballBlueTexture[1]= new Texture("ballBlue2.png");
		ballBlueTexture[2]= new Texture("ballBlue3.png");
		bgSprite1 = new Sprite(bgTexture1);
		bgSprite2= new Sprite(bgTexture2);
		ballRedSprite[0]= new Sprite(ballRedTexture[0]);
		ballRedSprite[1]= new Sprite(ballRedTexture[1]);
		ballRedSprite[2]= new Sprite(ballRedTexture[2]);
		ballBlueSprite[0]= new Sprite(ballBlueTexture[0]);
		ballBlueSprite[1]= new Sprite(ballBlueTexture[1]);
		ballBlueSprite[2]= new Sprite(ballBlueTexture[2]);
		shapeDraw = new ShapeRenderer();
		agent= new GamePlayingAgent(rows, columns, playerBot);
		agent2 = new OpponentsAgent(rows, columns, playerBot);
		cellWidth=(screenWidth-margin*2)/columns;
		cellHeight=(screenHeight-margin*2)/rows;

		cells = new Cell[rows][columns];
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++)
				cells[i][j]=new Cell();
		Gdx.input.setInputProcessor(this);
		}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(getCurrentPlayer()==1)
			batch.draw(bgSprite1, 0, 0);
		else batch.draw(bgSprite2, 0, 0);
		//batch.draw(ballRedSprite, posX, posY);
		flagLock=0;
		for(int i=0; i <rows;i++)
			for(int j=0;j<columns;j++)
			{
				posX=cellWidth*j+margin+ (int)(cellWidth-ballRedSprite[0].getWidth())/2;
				posY=cellHeight*i+margin+ (int)(cellHeight-ballRedSprite[0].getHeight())/2;
				if(cells[i][j].moveLeft!=0)
				{
					flagLock=1;
					batch.draw(getBall(0,cells[i][j].player), posX-cells[i][j].moveLeft, posY);
					cells[i][j].moveLeft+=4;
					if(cells[i][j].moveLeft>=80)
					{
						cells[i][j].setMoveLeft(0);
						cells[i][j-1].no_of_balls+=1;
						cells[i][j-1].player=cells[i][j].player;
						if(cells[i][j-1].no_of_balls==criticalMass(j-1,i))
						{
							explode(j-1,i);
						}
					}
				}
				if(cells[i][j].moveRight!=0)
				{
					flagLock=1;
					batch.draw(getBall(0,cells[i][j].player), posX+cells[i][j].moveRight, posY);
					cells[i][j].moveRight+=4;
					if(cells[i][j].moveRight>=80)
					{
						cells[i][j].setMoveRight(0);
						cells[i][j+1].no_of_balls+=1;
						cells[i][j+1].player=cells[i][j].player;
						if(cells[i][j+1].no_of_balls==criticalMass(j+1,i))
						{
							explode(j+1,i);
						}
					}
				}

				if(cells[i][j].moveUp!=0)
				{
					flagLock=1;
					batch.draw(getBall(0,cells[i][j].player), posX, posY+cells[i][j].moveUp);
					cells[i][j].moveUp+=4;
					if(cells[i][j].moveUp>=80)
					{
						cells[i][j].setMoveUp(0);
						cells[i+1][j].no_of_balls+=1;
						cells[i+1][j].player=cells[i][j].player;
						if(cells[i+1][j].no_of_balls==criticalMass(j,i+1))
						{
							explode(j,i+1);
						}
					}
				}
				if(cells[i][j].moveDown!=0)
				{
					flagLock=1;
					batch.draw(getBall(0,cells[i][j].player), posX, posY-cells[i][j].moveDown);
					cells[i][j].moveDown+=4;
					if(cells[i][j].moveDown>=80)
					{
						cells[i][j].setMoveDown(0);
						cells[i-1][j].no_of_balls+=1;
						cells[i-1][j].player=cells[i][j].player;
						if(cells[i-1][j].no_of_balls==criticalMass(j,i-1))
						{
							explode(j,i-1);
						}
					}
				}
				if(cells[i][j].no_of_balls==0)
				{
					if(cells[i][j].moveUp==0 && cells[i][j].moveDown==0 && cells[i][j].moveLeft==0 && cells[i][j].moveRight==0)
					{
						cells[i][j].player=0;
					}
					continue;
				}
				else {
					switch(cells[i][j].no_of_balls) {

						case 1: batch.draw(getBall(0,cells[i][j].player), posX, posY);break;
						case 2: batch.draw(getBall(1,cells[i][j].player), posX, posY);break;
						case 3: batch.draw(getBall(2,cells[i][j].player), posX, posY);break;

					}

				}

			}
		if(flagLock==0)
		{
			releaseLock();
			if(playersToBeSwapped==1)
			{
				swapPlayers();
			}
		}
		batch.end();

		shapeDraw.begin(ShapeRenderer.ShapeType.Line);
		shapeDraw.setColor(0, 0, 0, 1);
		shapeDraw.rect(margin,margin,screenWidth-2*margin,screenHeight-2*margin);
		for(int i=1;i*cellWidth+20<screenWidth-2*margin;i++)
		{
			shapeDraw.rectLine(i*cellWidth+margin,margin,i*cellWidth+margin,screenHeight-margin,2);
		}

		for(int i=1;i*cellHeight+20<screenHeight-2*margin;i++)
		{
			shapeDraw.rectLine(margin,i*cellHeight+margin,screenWidth-margin,i*cellHeight+margin,2);
		}

		shapeDraw.end();
		
	}

	private Sprite getBall(int index, int player) {
		if(player==1)
			return ballRedSprite[index];
		if(player==2)
			return ballBlueSprite[index];
		return null;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;

	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(isLock()==1)
		{
			return false;
		}
		tempX=(int)(screenX/cellWidth);
		tempY=(int)((screenHeight-screenY)/cellHeight);
		placeMove(tempX,tempY);
		return true;
	}

	private boolean placeMove(int X, int Y) {
		if(X>=0 && Y>=0 && X<columns && Y<rows) {	//If touch is within the boundary
			if(cells[Y][X].player!=0 && getCurrentPlayer()!=cells[Y][X].player)
			{
				System.out.println("Calling from here");
				if(getCurrentPlayer()==playerBot)
					makeIntelligentMove1();
				if(getCurrentPlayer()==1)	
					makeIntelligentMove2();
				
				return true;	//If another player has played on a cell before, don't let this player place his ball here
			}
			cells[Y][X].no_of_balls++;
			cells[Y][X].player=getCurrentPlayer();
			if (cells[Y][X].no_of_balls == criticalMass(X, Y)) {
				explode(X, Y);
			}
			//swapPlayers();
			playersToBeSwapped =1;
		}
		return true;
	}

	private void swapPlayers() {
		if(player==2)
			player=1;
		else player=2;
		playersToBeSwapped=0;
		if(player==playerBot)
		{
			makeIntelligentMove1();
		}
		if(player==1)
			makeIntelligentMove2();
	
		System.out.println();
		System.out.println();

	}

	private void makeIntelligentMove1() {
	
		agent.setGridContent(cells);
		agent. intelligentMove(getCurrentPlayer());
		int x = agent.getX();
		int y = agent.getY();
		System.out.println("Placing move for player blue at ["+y+","+x+"]");
		placeMove(x,y);
	}
	
	private void makeIntelligentMove2() {

		System.out.println("Calling opponent");
		agent2.setGridContent(cells);
		agent2.intelligentMove(getCurrentPlayer());
		int x = agent2.getX();
		int y = agent2.getY();
		System.out.println("Placing move for player red at ["+y+","+x+"]");
		placeMove(x,y);
		}
		

	private int criticalMass(int tempX, int tempY) {
		int mass=2;
		if(tempX!=0 && tempX!=columns-1)
			mass++;
		if(tempY!=0 && tempY!=rows-1)
			mass++;
		return mass;
	}

	private void explode(int tempX, int tempY) {
		cells[tempY][tempX].no_of_balls=0;
		putLock();	//So that user will not be able to place their moves untill we relase the lock, when explosion completes.
		if(tempX>=1)	//Exploding point is not aligned to the left wall
		{
			cells[tempY][tempX].moveLeft=1;
		}
		if(tempX<=columns-2)	//Exploding point is not aligned to the right wall
		{
			cells[tempY][tempX].moveRight=1;
		}
		if(tempY>=1)
		{
			cells[tempY][tempX].moveDown=1;
		}
		if(tempY<=rows-2)
		{
			cells[tempY][tempX].moveUp=1;
		}
		//Checking if any player is won
		//System.out.println("Checking Status...");
		checkStatus();
	}
	private void putLock()
	{
		lock=1;
	}
	private void releaseLock()
	{
		lock=0;
	}
	private int isLock()
	{
		return lock;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public int getCurrentPlayer() {
		return player;
	}
	public void checkStatus()
	{
		int current_player = getCurrentPlayer();
		boolean isWon=true;
		int opponent = (current_player==1?2:1);
		for(int i=0; i <rows;i++)
			for(int j=0;j<columns;j++)
			{
				if(cells[i][j].player==opponent)
					isWon = false;
			}
		//System.out.println("...flag is"+isWon);
		if(isWon)
			Won();
	}
	
	public void Won()
	{
		
		System.out.println("Player "+getCurrentPlayer()+" won..!!");
		System.exit(0);
	}
	
}