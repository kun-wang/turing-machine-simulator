/******************************************************************************
 * @author Wang Kun
 *         ID: DG1333031
 *         Email: nju.wangkun@gmail.com
 *         Date: 2014-7-26
 * @file TapeMemento.java
 * @brief Brief Description
 ******************************************************************************/
package turing.tape;

import java.util.ArrayList;

import util.MethodFactory;

public class TapeMemento
{
    private int                position;
    private ArrayList<Integer> cells;
    
    @SuppressWarnings("unchecked")
    public TapeMemento(ArrayList<Integer> cells, int position)
    {
        this.setPosition(position);
        this.setCells((ArrayList<Integer>) MethodFactory.deepCopy(cells));
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public ArrayList<Integer> getCells()
    {
        return cells;
    }

    public void setCells(ArrayList<Integer> cells)
    {
        this.cells = cells;
    }
}
