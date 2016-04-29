package com.mygdx.game;



import static com.badlogic.gdx.math.MathUtils.random;

import java.util.Date;

import quicktime.std.clocks.Clock;

/**
 * Created by ATD on 23-02-2016.
 */
public class OpponentsAgent
{
    Cell2[][] cells;
    int rows, columns;
    int playerBot;
    int x,y;
    int Player;
    int tempX,tempY;
    boolean isFirst;
    int count=0;
    int setDepth=3;
    int avgResponseTime = 0;
	int avgNodes = 0;
	int n=0;
	Nodes[] node;
	int nodePtr;
    
    public OpponentsAgent(int rows, int columns, int playerBot) {
        cells = new Cell2[rows][columns];
        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++)
                cells[i][j]=new Cell2();
        this.columns=columns;
        this.rows=rows;
        this.playerBot=playerBot;
        isFirst=true;
        node = new Nodes[10000];
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
    
    private void explode2(Cell2[][] C,int current_player)
    {
//    	System.out.println("i = "+i+",j = "+j+",curr player = "+current_player);
    	boolean isAgain = true;
    	
    	while(isAgain)
    	{			
    		isAgain = false;
    		for(int i=0;i<rows;i++)
    		{
    			for(int j=0;j<columns;j++)
    			{
    				if(C[i][j].player == current_player && C[i][j].no_of_balls == criticalMass(j,i))
    				{
    					isAgain = true;
    					C[i][j].no_of_balls=0;
    		    		C[i][j].player=0;
    		    		
    		    		//Explosion upward
    		    		if(i<rows-1)
    		    		{
    		    			//	System.out.println("upward");
    		    			C[i+1][j].no_of_balls++;
    		    			C[i+1][j].player = current_player;
    		    			
    		    	
    		    		}
    		    		//Explosion Downward
    		    		if(i>0)
    		    		{
    		    			//	System.out.println("downward");
    		    		C[i-1][j].no_of_balls++;
    		    		C[i-1][j].player = current_player;
    	
    		    		}
    		    	
    		    		//Explosion Right side
    		    		if(j<columns-1)
    		    		{
    		    			//	System.out.println("right");
    		    			C[i][j+1].no_of_balls++;
    		    			C[i][j+1].player = current_player;
    		    		}
    		    	
    		    	
    		    		//Explosion Left side
    		    		if(j>0)
    		    		{
    		    			//	System.out.println("left");
    		    		C[i][j-1].no_of_balls++;
    		    		C[i][j-1].player = current_player;
    		    		
    		    		} 		
    				}
    			}
    		}		
    	}
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
    	count++;
    	
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		if(C[i][j].player==Player)
        		{
        			switch(C[i][j].no_of_balls)
        			{
        				case 1: possesed_cells = possesed_cells + 1;
        				break;
        				case 2: possesed_cells = possesed_cells + 4;
        				break;
        				case 3: possesed_cells = possesed_cells + 6;
        				break;
        				
        			}
        		}
        	}
        }
    	return(possesed_cells);
    	
    }
    
    private boolean isWon(Cell2[][] C)
    {
    	int opponent = 3 - Player;
    	boolean won = true;
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		if(C[i][j].player==opponent)
        			won  = false;
        	}
        }
    	return(won);
    }
    
    private boolean isLose(Cell2[][] C)
    {
    	boolean lose = true;
    	for(int i=0;i<rows;i++)
        {
        	for(int j=0;j<columns;j++)
        	{
        		if(C[i][j].player==Player)
        			lose  = false;
        	}
        }
    	return(lose);
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
			explode2(tempCell,current_player);
		}
		
	//	if(depth==1)
		//	System.out.println("value = "+countBalls(tempCell));
		
		if(isWon(tempCell))
		{
			return(1000); //Win Condition
			
		}
		
		//System.out.println("d= "+setDepth);
		if(depth==3)
		{
			long t = System.currentTimeMillis();
			int value =countBalls(tempCell); 
			//System.out.println("Leaf value in minmax "+value);
			//System.out.println("time in CountBalls func : "+(System.currentTimeMillis()-t));
			return(value);
		}
		long t = System.currentTimeMillis();
		int value =searchInDepth(tempCell,depth+1,(3-current_player));
	//	System.out.println("time in Searchin Depth func : "+(System.currentTimeMillis()-t));
		return(value);
    }
    
    public int searchInDepth(Cell2[][] C,int depth,int current_player)
    {
  //  	System.out.println("Depth = "+depth);
  //  	printCell(C);
    	int opponent = (3-current_player),min=999,max=0,returnedValue;
    	long t = System.currentTimeMillis();
    	Cell2[][] tempCell = new Cell2[rows][columns];
    	
    	for(int i=0;i<=rows-1;i++)
        {
        	for(int j=0;j<=columns-1;j++)
        	{
        		if(C[i][j].player== current_player || (i!=rows-1 && C[i+1][j].player==opponent || i!=0 && C[i-1][j].player==opponent || j!=columns-1 && C[i][j+1].player==opponent || j!=0 && C[i][j-1].player==opponent))
        		{
        			
        		if(C[i][j].player==0 || C[i][j].player==current_player)
        		{     			
        			
        			
        			if(current_player==2)
        				{
        					
        					returnedValue = updateCell(i,j,C,current_player,depth);
        				//	System.out.println("time in updateCell func : "+(System.currentTimeMillis()-t));
        					if(returnedValue>max)
        					{
        						if(depth==0)
        	        			{
        	        				x=j;y=i;
        	        				
        	        				// Calculation of node value at level 0
        	        				
        	            			copyCell(C,tempCell);
        	            		/*	tempCell[i][j].no_of_balls++;
        	            			tempCell[i][j].player=current_player;
        	            			if (tempCell[i][j].no_of_balls == criticalMass(j, i)) {
        	            				explode2(tempCell,current_player);
        	            			}
        	        				System.out.println("at depth 0 selected node value = "+countBalls(tempCell));
        	        				*/
        	        			}
        						max=returnedValue;
        					}
        				}
        			if(current_player==1)
        				{
        				returnedValue = updateCell(i,j,C,current_player,depth);
        			//	System.out.println("time in updateCell func : "+(System.currentTimeMillis()-t));
    					if(returnedValue<min)
    					{
    						if(depth==1)
    	        			{
    	        			
    	        				
    	        				// Calculation of node value at level 0
    	        				
    	            			copyCell(C,tempCell);
    	            			tempCell[i][j].no_of_balls++;
    	            			tempCell[i][j].player=current_player;
    	            			if (tempCell[i][j].no_of_balls == criticalMass(j, i)) {
    	            				explode2(tempCell,current_player);
    	            			}
    	        				//System.out.println("at depth 1 selected node value = "+countBalls(tempCell));
    	        				
    	        			}
    						
    	        				min=returnedValue;
    	  
    					}
        				}
        		}

        		}
        	}
        }
    	//System.out.println("time in searchindepth func : "+(System.currentTimeMillis()-t));
    	if(current_player==2)
    		return(max);
    	else
    	{
    	//	System.out.println("Returning value = "+min);
    		return(min);
    	}
    }
    
    private void copyCell(Cell2[][] SourceCell,Cell2[][] DestinationCell)
    {
    	for(int i=0;i<=rows-1;i++)
        {
        	for(int j=0;j<=columns-1;j++)
        	{
        		DestinationCell[i][j] = new Cell2();
        		DestinationCell[i][j].no_of_balls=SourceCell[i][j].no_of_balls;
        		DestinationCell[i][j].player=SourceCell[i][j].player;
        	}
        }	
    }
    
    private int getNextPlayer(int current_player)
    {
    	return(3-current_player);
    }
    
    private void addNode(Cell2[][] tempCell,int depth,int alpha,int beta,int current_player,int i,int j)
    {
    	node[nodePtr] = new Nodes();
    	node[nodePtr].cell = new Cell2[rows][columns];
    	copyCell(tempCell, node[nodePtr].cell);
    	node[nodePtr].depth = depth;
    	node[nodePtr].alpha = alpha;
    	node[nodePtr].beta = beta;
    	node[nodePtr].current_player = current_player;
    	node[nodePtr].value = countBalls(tempCell);
    //	System.out.println("Node "+node[nodePtr].value+" added");
    	node[nodePtr].i = i;
    	node[nodePtr].j = j;
    	nodePtr++;	
    }
    
    private void printNodes()
    {
    	System.out.println(nodePtr+" Nodes:\n");
    	for(int i=0;i<nodePtr;i++)
    	{
    		System.out.print("["+node[i].depth+":"+node[i].value+"] ");
    	}
    }
    
    private void deleteNode(int nodeNumber,int depth)
    {
    	int i = nodeNumber;
    //	System.out.println("Node "+node[i].value+" deleted from depth "+depth);
    	nodePtr--;
    	while(i<nodePtr)
    	{
    		node[i] = node[i+1];
    		i++;
    	}
    //	printNodes();
    }
    
    private void  deleteAllNodeOfDepth(int depth)
    {
    	for(int i=0;i<nodePtr;)
    	{
    		if(node[i].depth == depth)
    		{
    			deleteNode(i,depth);
    			
    		}
    		else
    			i++;
    	}
    }
    
    private int alphabeta(Cell2[][] C,int depth,int alpha,int beta, int current_player)
    {
    	int v;
    	boolean isCutOff;
		int opponent = getNextPlayer(current_player);
    	if(depth==4)
    	{
    		
    		int value =countBalls(C);
    	//	System.out.println(" Leaf value "+value);
    		return(value);
    	}
    	if(isWon(C))
		{
			return(1000); //Win Condition
		}
    	if(isLose(C))
    	{
    		return(-1000); //Lose Condition
    	}
    	
    	if(current_player==Player)
    	{
    		//System.out.println("In player = "+current_player);
    		v=-999999;
    		isCutOff = false;
    		int NoOfGeneratedNodes = 0;
    		for(int i=0;i<=rows-1;i++)
            {
            	for(int j=0;j<=columns-1;j++)
            	{
            		if((C[i][j].player==0 || C[i][j].player==current_player)  && (i!=rows-1 && C[i+1][j].player==opponent || i!=0 && C[i-1][j].player==opponent || j!=columns-1 && C[i][j+1].player==opponent || j!=0 && C[i][j-1].player==opponent))
            		{
            			Cell2[][] tempCell = new Cell2[rows][columns];
            			copyCell(C,tempCell);
            			tempCell[i][j].no_of_balls++;
            			tempCell[i][j].player=current_player;
            			if (tempCell[i][j].no_of_balls == criticalMass(j, i)) {
            				explode2(tempCell,current_player);
            			}
            			
            			addNode(tempCell,depth,alpha,beta,current_player,i,j);
            			NoOfGeneratedNodes++;
            		}
            	}
            }
    	//	printNodes();
    		int countNode=MinOf(5,NoOfGeneratedNodes);
    		while(countNode>0)
    		{
    			int maxValuedNode=0;
        		int maxValueOfNode = -9999;
    			for(int k=0;k<nodePtr;k++)
    			{
    				if(node[k].value>maxValueOfNode && node[k].depth==depth)
    				{
    					maxValuedNode = k;
    					maxValueOfNode = node[k].value; 
    				}
    		
    			}
    			
    			Nodes selectedNode = node[maxValuedNode];
    			if(node[maxValuedNode].depth == depth)
    				deleteNode(maxValuedNode,depth);
            	int returnedValue = alphabeta(selectedNode.cell,selectedNode.depth+1,selectedNode.alpha,selectedNode.beta,getNextPlayer(selectedNode.current_player));
            	v = MaxOf(v,returnedValue);
           		if(depth==0 && (v-returnedValue)==0)
           			{
            			x=selectedNode.j;y=selectedNode.i;
            		}
            			alpha = MaxOf(alpha,v);
            			if(beta<=alpha)
            			{
            				isCutOff = true;
            				break;
            			}
            		
            	
            	if(isCutOff)
            	{
            		//System.out.println("Deleted on cutting off");
            		deleteAllNodeOfDepth(depth);
            		break;
            	}
            	countNode--;	
    		}
    		//System.out.println("Deleted on returning at depth "+depth);
    		deleteAllNodeOfDepth(depth);
    		//printNodes();
    		return v;
    	}
    	else
    	{
    		//System.out.println("In player = "+current_player);
    		v=999999;
    		isCutOff = false;
    		int NoOfGeneratedNodes = 0;
    		for(int i=0;i<=rows-1;i++)
            {
            	for(int j=0;j<=columns-1;j++)
            	{
            		if((C[i][j].player==0 || C[i][j].player==current_player)  && (i!=rows-1 && C[i+1][j].player==opponent || i!=0 && C[i-1][j].player==opponent || j!=columns-1 && C[i][j+1].player==opponent || j!=0 && C[i][j-1].player==opponent))
            		{
            			Cell2[][] tempCell = new Cell2[rows][columns];
            			copyCell(C,tempCell);
            			tempCell[i][j].no_of_balls++;
            			tempCell[i][j].player=current_player;
            			if (tempCell[i][j].no_of_balls == criticalMass(j, i)) {
            				explode2(tempCell,current_player);
            			}
            			
            			addNode(tempCell,depth,alpha,beta,current_player,i,j);
            			NoOfGeneratedNodes++;
            		}
            	}
            }
    		//printNodes();
    		//int countNode=MinOf(5,NoOfGeneratedNodes);
    		int countNode = NoOfGeneratedNodes;
    		while(countNode>0)
    		{
    			int maxValuedNode=0;
        		int maxValueOfNode = 9999;
    			for(int k=0;k<nodePtr;k++)
    			{
    				if(node[k].value<maxValueOfNode && node[k].depth==depth)
    				{
    					maxValuedNode = k;
    					maxValueOfNode = node[k].value; 
    				}
    		
    			}
    			Nodes selectedNode = node[maxValuedNode];
    			if(node[maxValuedNode].depth == depth)
    				deleteNode(maxValuedNode,depth);
            	int returnedValue = alphabeta(selectedNode.cell,selectedNode.depth+1,selectedNode.alpha,selectedNode.beta,getNextPlayer(selectedNode.current_player));
            	v = MinOf(v,returnedValue);
           		
            			beta = MinOf(beta,v);
            			if(beta<=alpha)
            			{
            				isCutOff = true;
            				break;
            			}
            		
            	
            	if(isCutOff)
            	{
            	//	System.out.println("Deleted on cutting off");
            		deleteAllNodeOfDepth(depth);
            		break;
            	}
            	countNode--;	
    		}
    	//	System.out.println("Deleted on returning at depth "+depth);
    		deleteAllNodeOfDepth(depth);
    		//printNodes();
    		return v;
    	}
    	/*{
    		//System.out.println("In player = "+current_player);
    		v=999999;
    		isCutOff = false;
    		for(int i=0;i<=rows-1;i++)
            {
            	for(int j=0;j<=columns-1;j++)
            	{
            		if((C[i][j].player==0 || C[i][j].player==current_player)  && (i!=rows-1 && C[i+1][j].player==opponent || i!=0 && C[i-1][j].player==opponent || j!=columns-1 && C[i][j+1].player==opponent || j!=0 && C[i][j-1].player==opponent))
            		{
            			//System.out.println("Calling from depth "+depth);
            			Cell2[][] tempCell = new Cell2[rows][columns];
            			copyCell(C,tempCell);
            			tempCell[i][j].no_of_balls++;
            			tempCell[i][j].player=current_player;
            			if (tempCell[i][j].no_of_balls == criticalMass(j, i)) {
            				explode2(tempCell,current_player);
            			}
            			
            			v = MinOf(v,alphabeta(tempCell,depth+1,alpha,beta,getNextPlayer(current_player)));
            			beta = MinOf(beta,v);
            			if(beta<=alpha)
            			{
            				isCutOff = true;
            				break;
            			}
            		}
            		if(isCutOff)
            		{
            		//	System.out.println("Cutting of");
            			break;
            		}
            	}
            } 	
    		return v;
    	}*/
    }
    
    
    public void intelligentMove(int current_player) {
    	Player = current_player;
    	System.out.println("in opponent... player = "+Player);
    	/*if(current_player==1)
    	{
    	x= random(columns-1);
        y= random(rows-1);
        return;
    	
    	}*/
    	
    	n++;
    	count=0;
    	nodePtr=0;
    	
    	if(isFirst)
    	{
      x= random(columns-1);
        y= random(rows-1);
        isFirst=false;
        return;
    	}
    	
    	
    	
    	long timeBeforeCallingFunction = System.currentTimeMillis();
    	System.out.println("calling alphabeta");
    	System.out.println("selected value by alphabeta = "+alphabeta(cells, 0, -999999, 9999999, Player));
    //	System.out.println("calling minmax");
    //	System.out.println("selected value by minmax= "+searchInDepth(cells,0,current_player));
    	
    
    /*	System.out.println("count "+count+"\nTotal Decision making Time : "+(System.currentTimeMillis()-timeBeforeCallingFunction));
    	avgNodes = (avgNodes * (n-1) + count)/n;
    	avgResponseTime = (avgResponseTime * (n-1) +  (int)(System.currentTimeMillis()-timeBeforeCallingFunction) )/n;
    	System.out.println("\n\navgNodes "+avgNodes+"\nAvg Decision time = "+avgResponseTime);*/
    }
}
