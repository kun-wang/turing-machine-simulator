package turing;

import turing.tape.Tape;


public class TransitionRule
{
    public static final String UNDEFINED_RULE  = "XXX";
    public static final int    LENGTH_OF_RULE   = 3;
    public static final int    NEW_SYMBOL_INDEX = 0;
    public static final int    DIRECTION_INDEX  = 1;
    public static final int    NEW_STATE_INDEX  = 2;
    
    private int                newSymbol        = 0;
    private int                direction        = 0;
    private int                newState         = 0;
    
    public TransitionRule()
    {
    }
    
    public TransitionRule(String rule)
    {
        if (LENGTH_OF_RULE != rule.length())
        {
            System.err.println("getNewSymbol(): error length of transition rule!");
            System.exit(-1);
        }
        this.newSymbol = Integer.parseInt(rule.substring(NEW_SYMBOL_INDEX, NEW_SYMBOL_INDEX + 1));
        
        String dirString = rule.substring(DIRECTION_INDEX, DIRECTION_INDEX + 1);
        if (dirString.equals(Tape.LEFT_DIRECTION_FLAG))
            this.direction = Tape.LEFT_DIRECTION;
        else if (dirString.equals(Tape.RIGHT_DIRECTION_FLAG))
            this.direction = Tape.RIGHT_DIRECTION;
        else if (dirString.equals(Tape.STAY_DIRECTION_FLAG))
            this.direction = Tape.STAY_DIRECTION;
        else 
        {
            System.err.println("TransitionRule(): error direction flag!");
            System.exit(-1);
        }
        this.newState = Integer.parseInt(rule.substring(NEW_STATE_INDEX, NEW_STATE_INDEX + 1));

    }
    
    public static boolean isValidRule(String rule)
    {
        if (rule == null)
            return false;
        else if (rule.equals(UNDEFINED_RULE))
            return false;
        else
            return true;
    }
    
    public int getNewSymbol()
    {
        return this.newSymbol;
    }
    
    public int getDirection()
    {
        return this.direction;
    }
    
    public int getNewState()
    {
        return this.newState;
    }
    
    public String getRuleString()
    {
        StringBuilder rule = new StringBuilder();
        rule.append(newSymbol);
        switch ( direction )
        {
            case Tape.LEFT_DIRECTION:
                rule.append(Tape.LEFT_DIRECTION_FLAG);
                break;
            case Tape.RIGHT_DIRECTION:
                rule.append(Tape.RIGHT_DIRECTION_FLAG);
                break;
            case Tape.STAY_DIRECTION:
                rule.append(Tape.STAY_DIRECTION_FLAG);
                break;
        }
        rule.append(newState);
        return rule.toString();
    }
    
    public static String getUndifinedRule()
    {
        return UNDEFINED_RULE;
    }
}
