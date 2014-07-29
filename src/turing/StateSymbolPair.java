package turing;

public class StateSymbolPair
{
    private int state;
    private int symbol;
    
    public StateSymbolPair()
    {
        setState(TuringMachine.INIT_STATE);
        setSymbol(0);
    }
    
    public StateSymbolPair(int state, int symbol)
    {
        if (symbol != 0 && symbol != 1)
        {
            System.err.println("StateSymbolPair(): error type of symbol, 0 and 1 supported!");
            System.exit(-1);
        }
        
        this.setState(state);
        this.setSymbol(symbol);
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getSymbol()
    {
        return symbol;
    }

    public void setSymbol(int symbol)
    {
        this.symbol = symbol;
    }
    
    public int hashCode()
    {  
        return (int)Math.pow(2, state) + (int)Math.pow(3, symbol);
    }
    
    public boolean equals(Object o)
    {
        if ( o instanceof StateSymbolPair )
        {
            StateSymbolPair obj = (StateSymbolPair) o;
            if ( this.state == obj.state && this.symbol == obj.symbol )
                return true;
        }
        return false;
    }
}
