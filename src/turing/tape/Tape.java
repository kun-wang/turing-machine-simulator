/******************************************************************************
 * @author Wang Kun
 * @email nju.wangkun@gmail.com
 * @date 2014/7/25
 * @file Tape.java
 * @brief This object represents the logical aspects of the tape.
 *        (The class 'TuringTapeArea' handles the graphical aspects).
 *        The tape is an array list of 0s and 1s, and an index to
 *        the current position on the tape.
 *        !!!Attention!!! The position begins in 1 while the array list index
 *        begins in 0, so the 0 element of the array list is set to 0 always.
 ******************************************************************************/
package turing.tape;

import java.util.ArrayList;
import java.util.Iterator;

import util.MethodFactory;

public class Tape implements Cloneable
{
    public static final int    LEFT_DIRECTION       = -1;
    public static final int    STAY_DIRECTION       = 0;
    public static final int    RIGHT_DIRECTION      = 1;
    public static final String LEFT_DIRECTION_FLAG  = "L";
    public static final String STAY_DIRECTION_FLAG  = "C";
    public static final String RIGHT_DIRECTION_FLAG = "R";
    public static final int    INIT_POSITION        = 2;
    public static final int    LEFT_MOST_POSITION   = 1;
    public static final char   FILL_SYMBOL_FLAG     = '0';
    public static final int    FILL_SYMBOL          = 0;
    public static final char   COUNTING_SYMBOL_FLAG = '1';
    public static final int    COUNTING_SYMBOL      = 1;
    
    private int                position             = INIT_POSITION;
    private ArrayList<Integer> cells;
    
    /* This constructor creates a tape from a string. The symbols must be separated
     * by at least one space. A pair of square brackets should be placed around
     * the starting position. If no symbol has square brackets on each side, the
     * position of the tape reader will be the default position. If more than
     * one symbol is bracketed, the last one will be the starting position. */
    public Tape(String s)
    {
        this.position = INIT_POSITION;
        cells = new ArrayList<Integer>();
        char ch;
        cells.add(FILL_SYMBOL); /* leave out the 0 indices */
        for (int i = 0; i < s.length(); i++)
        {
            ch = s.charAt(i);
            if (ch == FILL_SYMBOL_FLAG)
                cells.add(FILL_SYMBOL);
            else if (ch == COUNTING_SYMBOL_FLAG)
                cells.add(COUNTING_SYMBOL);
        }
    }
    
    // This constructor simply clones the tape passed to it.
    @SuppressWarnings("unchecked")
    public Tape(Tape t)
    {
        position = t.position;
        cells = (ArrayList<Integer>) t.cells.clone();
    }
    
    public int getCurrentPosition()
    {
        return position;
    }
    
    public int getCurrentSymbol()
    {
        return cells.get(position).intValue();
    }
    
    public void updateCurrentSymbol(int symbol)
    {
        cells.set(position, symbol);
    }
    
    public boolean updateCurrentPosition(int direction)
    {
        boolean flag = true;
        if (position == LEFT_MOST_POSITION && direction == Tape.LEFT_DIRECTION)
            flag = false;
        else
        {
            switch (direction)
            {
            case Tape.LEFT_DIRECTION:
                position--;
                break;
            case Tape.STAY_DIRECTION:
                break;
            case Tape.RIGHT_DIRECTION:
                position++;
                break;
            default:
                System.err.println("updateCurrentPosition(): error direction value!");
                break;
            }
            if (position >= cells.size())
            {
                for (int i = 0; i < 5; i++)
                    cells.add(FILL_SYMBOL);
            }
            flag = true;
        }
        return flag;
    }
    
    public String getSymbolAt(int i)
    {
        return cells.get(i).toString();
    }
    
    public int getSize()
    {
        return cells.size();
    }
    
    public void printTape()
    {
        StringBuilder info = new StringBuilder();
        Iterator<Integer> it = cells.iterator();
        while (it.hasNext())
            info.append(it.next().toString() + " ");
        info.append(", position is " + position);
        System.out.println("printTape(): tape is " + info);
    }
    
    public TapeMemento createTapeMemento()
    {
        return new TapeMemento(cells, position);
    }
    
    @SuppressWarnings("unchecked")
    public void restoreTapeMemento(TapeMemento memento)
    {
        this.position = memento.getPosition();
        this.cells = (ArrayList<Integer>) MethodFactory.deepCopy(memento.getCells());
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        Tape tapeClone = null;
        try
        {
            tapeClone = (Tape) super.clone();
            tapeClone.position = this.position;
            tapeClone.cells = (ArrayList<Integer>) MethodFactory.deepCopy(this.cells);
        }
        catch (CloneNotSupportedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tapeClone;
    }
}
