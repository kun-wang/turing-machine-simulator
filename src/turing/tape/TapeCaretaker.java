/******************************************************************************
 * @author Wang Kun
 *         ID: DG1333031
 *         Email: nju.wangkun@gmail.com
 *         Date: 2014-7-26
 * @file TapeStorager.java
 * @brief Brief Description
 ******************************************************************************/
package turing.tape;

public class TapeCaretaker
{
    private TapeMemento memento;

    public TapeCaretaker()
    {
        this.memento = null;
    }
    
    public TapeMemento retrieveMemento()
    {
        return this.memento;
    }
    
    public void saveMemento(TapeMemento memento)
    {
        this.memento = memento;
    }
}