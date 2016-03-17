package com.mygdx.game;



import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by ATD on 23-02-2016.
 */
public class GamePlayingAgent
{
    Cell2[][] cells;
    int rows, columns;
    int playerBot;
    int x,y;
    int Player;
    int tempX,tempY;
    boolean isFirst;
    
    public GamePlayingAgent(int rows, int columns, int playerBot) {
        cells = new Cell2[rows][columns];
        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++)
                cells[i][j]=new Cell2();
        this.columns=columns;
        this.rows=rows;
        this.playerBot=playerBot;
        isFirst=true;
    }

    public void setGridContent(Cell[][] cells) {
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
            this.cells[i][j].no_of_balls=cells[i][j].no_of_balls;
            this.cells[i][j].player=cells[i][j].player;
        	}
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
    private void explode(int i,int j,Cell2[][] C,int current_player)
    {
//    	System.out.println("i = "+i+",j = "+j+",curr player = "+current_player);
    	C[i][j].no_of_balls=0;
    	C[i][j].player=0;
    	//Explosion upward
    	if(i<rows-1)
    	{
    	//	System.out.println("upward");
    		C[i+1][j].no_of_balls++;
    		C[i+1][j].player = current_player;
    		if (C[i+1][j].no_of_balls == criticalMass(j, i+1)) {
    			explode(i+1, j,C,current_player);
    		}
    	
		}
    	//Explosion Downward
    	if(i>0)
    	{
    	//	System.out.println("downward");

    		C[i-1][j].no_of_balls++;
    		C[i-1][j].player = current_player;
    		
    		if (C[i-1][j].no_of_balls == criticalMass(j, i-1)) {
    			explode(i-1, j,C,current_player);
    		}
    	
    	}
    	
    	//Explosion Right side
    	if(j<columns-1)
    	{
    	//	System.out.println("right");

    		C[i][j+1].no_of_balls++;
    		C[i][j+1].player = current_player;
    		if (C[i][j+1].no_of_balls == criticalMass(j+1, i)) {
    			explode(i, j+1,C,current_player);
    		}
    	}
    	
    	
    	//Explosion Left side
    	if(j>0)
    	{
    	//	System.out.println("left");

    		C[i][j-1].no_of_balls++;
    		C[i][j-1].player = current_player;
    		if (C[i][j-1].no_of_balls == criticalMass(j-1, i)) {
    			explode(i, j-1,C,current_player);
    		}
    	}
    	
    	
    }
    
    private int criticalMass(int tempX, int tempY) {
		int mass=2;
		if(tempX!=0 && tempX!=columns-1)
			mass++;
		if(tempY!=0 && tempY!=rows-1)
			mass++;
		return mass;
	}

    private void printCell(Cell2[][] C)
    {
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		System.out.print(" "+C[i][j].player+"."+C[i][j].no_of_balls);
        	}
        	System.out.println();
        }	
    }
    
    private int countBalls(Cell2[][] C )
    {
    	int possesed_cells=0;
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		if(C[i][j].player==Player)
        			possesed_cells = possesed_cells + C[i][j].no_of_balls;
        	}
        }
    	return(possesed_cells);
    	
    }
    
    private int MaxOf(int a,int b)
    {
    	return((a>b)?a:b);
    }
    
    private int MinOf(int a,int b)
    {
    	return((a<b)?a:b);
    }
    
    public int updateCell(int r,int c,Cell2[][] C,int current_player,int depth)
    {
    	Cell2[][] tempCell= new Cell2[rows][columns];
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		tempCell[i][j] = new Cell2();
        		tempCell[i][j].no_of_balls = C[i][j].no_of_balls;
        		tempCell[i][j].player=C[i][j].player;
        	}
        }	
        
    	tempCell[r][c].no_of_balls++;
    	tempCell[r][c].player=current_player;
//        System.out.println("Critical Mass "+criticalMass(c, r));
		if (tempCell[r][c].no_of_balls == criticalMass(c, r)) {
			explode(r, c,tempCell,current_player);
		}
		
		if(depth==1)
		{
			return(countBalls(tempCell));
		}
		return(searchInDepth(tempCell,depth+1,(current_player==1)?2:1));
    }
    
    public int searchInDepth(Cell2[][] C,int depth,int current_player)
    {
  //  	System.out.println("Depth = "+depth);
  //  	printCell(C);
    	int opponent = (current_player==2?1:2),min=999,max=0,returnedValue;
    //	if(depth==3)
    	{
   // 		System.out.println("count = "+countBalls(C)+" min = "+min+" tempX="+tempX+" tempY = "+tempY+" x="+x+" y="+y);
    		
    	//	return(ount);
    	}
  
    	
    	for(int i=0;i<=rows-1;i++)
        {
        	for(int j=0;j<=columns-1;j++)
        	{
        		if(C[i][j].player== current_player || (i!=rows-1 && C[i+1][j].player==opponent || i!=0 && C[i-1][j].player==opponent || j!=columns-1 && C[i][j+1].player==opponent || j!=0 && C[i][j-1].player==opponent))
        		{
        			
        		if(C[i][j].player==0 || C[i][j].player==current_player)
        		{     			
        		//	System.out.println("i = "+i+",j = "+j);
        			
        			if(current_player==2)
        				{
        					returnedValue = updateCell(i,j,C,current_player,depth);
        					if(returnedValue>max)
        					{
        						if(depth==0)
        	        			{
        	        				x=j;y=i;
        	        			}
        						max=returnedValue;
        					}
        				}
        			if(current_player==1)
        				{
        				returnedValue = updateCell(i,j,C,current_player,depth);
    					if(returnedValue<min)
    					{
    						
    	        				min=returnedValue;
    	  
    					}
        				}
        		}

        		}
        	}
        }
    	if(current_player==2)
    		return(max);
    	else
    		return(min);
    }
    
    public void intelligentMove(int current_player) {
    	/*if(current_player==1)
    	{
    		x= random(columns-1);
            y= random(rows-1);
            return;
    	}
    	*/
    	if(isFirst)
    	{
      x= random(columns-1);
        y= random(rows-1);
        isFirst=false;
        return;
    	}
    	
    	
    	Player = current_player;
    	
    	searchInDepth(cells,0,current_player);
        
    }
}
