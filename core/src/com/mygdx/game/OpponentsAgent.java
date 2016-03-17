package com.mygdx.game;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by ATD on 23-02-2016.
 */
public class OpponentsAgent
{
    Cell[][] cells;
    int rows, columns;
    int x,y;
    public OpponentsAgent(int columns, int rows) {
        cells = new Cell[rows][columns];
        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++)
                cells[i][j]=new Cell();
        this.columns=columns;
        this.rows=rows;
    }

    public void setGridContent(Cell[][] cells) {
        for(int i=0;i<rows;i++)
        {
            this.cells[i]=cells[i].clone();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void intelligentMove() {
        x=random(columns-1);
        y=random(rows-1);
        /*if(cells[y][x].no_of_balls>0 && cells[y][x].player==1)
        {
            x=random(columns-1);
            y=random(rows-1);

        }
        */

    }
}
