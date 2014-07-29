/******************************************************************************
 * @author Wang Kun
 *         ID: DG1333031
 *         Email: nju.wangkun@gmail.com
 *         Date: 2014-7-26
 * @file MachineCaretaker.java
 * @brief This is the memento caretaker of MachineMemento.
 *        It holds a stack of mementos.
 *        Each time pushMemento() is executed, the latest snapshot
 *        of current machine state is push into the top of stack,
 *        and each time popMemento() is executed, the top
 *        snapshot will be pop out. The 'step back' operation
 *        will always get the most recent pushed in.
 ******************************************************************************/
package turing.memento;

import java.util.Stack;

public class MachineCaretaker
{
    private Stack<MachineMemento> mementos = null;
    
    public MachineCaretaker()
    {
        mementos = new Stack<MachineMemento>();
    }
    
    public void pushMemento(MachineMemento memento)
    {
        mementos.add(memento);
    }
    
    public MachineMemento popMemento()
    {
        return mementos.pop();
    }
    
    public int getMementoSize()
    {
        if (mementos == null)
            return 0;
        return mementos.size();
    }
    
    public void clearMementos()
    {
        if (mementos != null)
            mementos.clear();
    }
}
