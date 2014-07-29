/********************************************************************
 * Author Name: WangKun
 * Author ID: DG1333031
 * Mail Address: nju.wangkun@gmail.com
 * Last Modified: 2014-07-20 20:18
 * Filename: TuringMain.java
 ********************************************************************/
package turing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Observable;

import turing.memento.MachineCaretaker;
import turing.memento.MachineMemento;
import turing.tape.Tape;
import turing.tape.TapeCaretaker;

public class TuringMachine extends Observable
{
    public static final int                         INIT_STATE          = 1;
    public static final int                         NUMBER_OF_VARIABLES = 2;
    public static final boolean                     NOT_HALTED          = Boolean.FALSE;
    public static final boolean                     HALTED              = Boolean.TRUE;
    
    /* transition rules */
    public HashMap<StateSymbolPair, TransitionRule> rules;
    /* steps have been taken */
    public int                                      steps;
    /* total steps needed to halt the machine */
    public int                                      totalSteps;
    public int                                      currentState;
    private boolean                                 halted;
    private Tape                                    tape;
    private TapeCaretaker                           tapeCaretaker;
    private String                                  status;
    private MachineCaretaker                        machineCaretaker;
    
    public TuringMachine(HashMap<StateSymbolPair, TransitionRule> rules, String input)
    {
        this.rules = rules;
        this.steps = 0;
        this.totalSteps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        this.tape = new Tape(input);
        this.tapeCaretaker = new TapeCaretaker();
        tapeCaretaker.saveMemento(tape.createTapeMemento());
        this.machineCaretaker = new MachineCaretaker();
        this.setTotalSteps();
    }
    
    public TuringMachine(HashMap<StateSymbolPair, TransitionRule> rules, Tape tape)
    {
        this.rules = rules;
        this.steps = 0;
        this.totalSteps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        this.tape = new Tape(tape);
        this.tapeCaretaker = new TapeCaretaker();
        tapeCaretaker.saveMemento(tape.createTapeMemento());
        this.machineCaretaker = new MachineCaretaker();
        this.setTotalSteps();
    }
    
    public int getCurrentState()
    {
        return currentState;
    }
    
    public void setCurrentState(int currentState)
    {
        this.currentState = currentState;
    }
    
    public Tape getTape()
    {
        return this.tape;
    }
    
    public void setTape(Tape t)
    {
        tape = t;
    }
    
    public boolean getHaltState()
    {
        return halted;
    }
    
    public void setHaltState(boolean halted)
    {
        this.halted = halted;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public int getSteps()
    {
        return steps;
    }
    
    public void setSteps(int steps)
    {
        this.steps = steps;
    }
    
    public int getTotalSteps()
    {
        return totalSteps;
    }
    
    public void setTotalSteps(int totalSteps)
    {
        this.totalSteps = totalSteps;
    }
    
    /** Reset the whole machine, original backup shall be used. */
    public void resetMachine(String input)
    {
        this.steps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        if (null == input)
            tape.restoreTapeMemento(tapeCaretaker.retrieveMemento());
        else
        {
            this.tape = new Tape(input);
            tapeCaretaker.saveMemento(tape.createTapeMemento());
            this.setTotalSteps();
        }
        machineCaretaker.clearMementos();
        /* Following components must be repainted:
         * + TapeArea (changed to original input)
         * + InformationArea
         * + ProgramArea (mainly the highlight) */
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * stepForward: This method will perform one transition based on the current state and tape
     * symbol. This method focus on the internal states of the machine, so it will not
     * repaint related components.
     */
    private boolean stepForward()
    {
        /* No matter what will happen in the following codes, save snapshot first */
        if (machineCaretaker != null)
            machineCaretaker.pushMemento(this.createMachineMemento());
        
        int currentSymbol = tape.getCurrentSymbol();
        StateSymbolPair key = new StateSymbolPair(currentState, currentSymbol);
        TransitionRule rule = rules.get(key);
        
        if (rule == null)
        {
            halted = HALTED;
            this.status = "machine halted";
            return false;
        }
        else
        {
            int newSymbol = rule.getNewSymbol();
            int direction = rule.getDirection();
            int newState = rule.getNewState();
            currentState = newState;
            tape.updateCurrentSymbol(newSymbol);
            if (!tape.updateCurrentPosition(direction))
            {
                halted = HALTED;
                this.status = "machine halted";
                return false;
            }
            this.status = "machine ready";
            steps++;
        }
        return true;
    }
    
    /* Run the current Turing machine n steps.
     * @return
     * * true If after stepping forward n steps, the machine is not halted
     * * false Otherwise */
    public void stepForwardN(int n)
    {
        for (int i = 0; i < n; i++)
        {
            if (!this.stepForward())
            {
                this.status = new String("halted before " + n + " steps");
                break;
            }
        }
        /* After moving, several components must be repainted:
         * + TapeArea
         * + InformationArea (step)
         * + ProgramArea (current hightlights)
         * There are two cases in which the machine will halt:
         * Case1. No transition rule defined for current (state, symbol) pair
         * Case2. The header moves to the left of the boundary */
        this.setChanged();
        this.notifyObservers();
    }
    
    /* stepBack: restores the machine and tape to the state they were in in the last step. Returns
     * false if the stepBack was unsuccessful (because the history was empty). */
    public boolean stepBack()
    {
        if (machineCaretaker != null && machineCaretaker.getMementoSize() > 0)
        {
            this.restoreMachineMemento(machineCaretaker.popMemento());
            return true;
        }
        else
        {
            this.status = "cannot step back";
            return false;
        }
    }
    
    public void stepBackwardN(int n)
    {
        for (int i = 0; i < n; i++)
        {
            if (!this.stepBack())
            {
                this.status = new String("cannot step back");
                break;
            }
        }
        /* After moving, several components must be repainted:
         * + TapeArea
         * + InformationArea (step)
         * + ProgramArea (current hightlights)
         * There are two cases in which the machine will halt:
         * Case1. No transition rule defined for current (state, symbol) pair
         * Case2. The header moves to the left of the boundary */
        this.setChanged();
        this.notifyObservers();
    }
    
    private void setTotalSteps()
    {
        while (this.stepForward())
            ;
        this.totalSteps = this.steps;
        /** We should reset machine */
        this.steps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        tape.restoreTapeMemento(tapeCaretaker.retrieveMemento());
        machineCaretaker.clearMementos();
    }
    
    public void printRules()
    {
        System.out.println("printRules(): ******************************************************");
        Iterator<Entry<StateSymbolPair, TransitionRule>> it = rules.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<StateSymbolPair, TransitionRule> entry = it.next();
            StateSymbolPair key = entry.getKey();
            TransitionRule value = entry.getValue();
            System.out.println(key.getState() + "\t" + key.getSymbol() + ": "
                    + value.getRuleString());
        }
        System.out.println("********************************************************************");
    }
    
    public int getMaxState()
    {
        int maxState = 0;
        Iterator<Entry<StateSymbolPair, TransitionRule>> it = rules.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<StateSymbolPair, TransitionRule> entry = it.next();
            TransitionRule rule = entry.getValue();
            int state = rule.getNewState();
            if (maxState < state)
                maxState = state;
        }
        return maxState;
    }
    
    public String getRuleString(int state, int symbol)
    {
        String ruleString = null;
        StateSymbolPair key = new StateSymbolPair(state, symbol);
        TransitionRule rule = rules.get(key);
        if (null == rule)
            ruleString = TransitionRule.getUndifinedRule();
        else
            ruleString = rule.getRuleString();
        return ruleString;
    }
    
    public MachineMemento createMachineMemento()
    {
        return new MachineMemento(steps, currentState, halted, tape, status);
    }
    
    public void restoreMachineMemento(MachineMemento memento)
    {
        this.steps = memento.getSteps();
        this.currentState = memento.getCurrentState();
        this.halted = memento.isHalted();
        this.tape = (Tape) memento.getTape().clone();
        this.status = memento.getStatus();
    }
}
