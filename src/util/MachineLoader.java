/******************************************************************************
 * Name:        Wang Kun
 * ID:          DG1333031
 * Email:       nju.wangkun@gmail.com
 * Date:        2014-7-23
 ******************************************************************************/
package util;

/* TuringIOProcessor: a group of methods used for reading
 * information about the TM out of a file */

import java.io.*;
import java.util.*;
import turing.StateSymbolPair;
import turing.TransitionRule;
import turing.TuringMachine;
import turing.tape.Tape;

public class MachineLoader
{
    
    BufferedReader fin;
    Vector<String> inputVector = null;
    
    /**
     * getMachineName: returns the String in the 'title' field of the Turing Machine file f If the
     * file has no such field, it returns the system's name for the file.
     */
    public String getMachineName(File f) throws IOException
    {
        try
        {
            fin = new BufferedReader(new FileReader(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        
        String s;
        do
        {
            s = fin.readLine();
            if (s == null) /* no 'title' field encountered */
                return f.getName().trim();
        }
        while (!s.startsWith("title"));
        return fin.readLine().trim();
    }
    
    /* getMachineDescription: returns the String in the 'description' field of the input file, or
     * null if no such field exists. */
    
    public String getMachineDescription(File f) throws IOException
    {
        try
        {
            fin = new BufferedReader(new FileReader(f));
            
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        
        String s = null;
        do
        {
            s = fin.readLine();
            if (s == null) /* no 'description' field encountered */
                return "No description for this machine.";
        }
        while (!s.startsWith("description"));
        
        StringBuilder desc = new StringBuilder("");
        s = fin.readLine();
        if (s != null)
        {
            while (!s.startsWith("description end"))
            {
                desc = desc.append(s.trim() + "\n");
                s = fin.readLine();
                if (s == null)
                    break;
            }
        }
        return desc.toString();
    }
    
    /** getMachine: converts the information about in the input file into a TuringMachine */
    public TuringMachine getMachine(File file) throws IOException
    {
        try
        {
            fin = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        
        String s = null;
        HashMap<StateSymbolPair, TransitionRule> rules = new HashMap<StateSymbolPair, TransitionRule>();
        int state;
        String ruleOnSymbol0, ruleOnSymbol1;
        StateSymbolPair key = null;
        StringTokenizer stok;
        // scan past comments and inital whitespace, detecting optional fill symbol
        do
        {
            s = fin.readLine();
            if (s == null) /* no 'rule' field encountered */
                return null;
        }
        while (!s.startsWith("rules"));
        s = fin.readLine();
        if (s == null)
            return null;
        stok = new StringTokenizer(s);
        if (stok.countTokens() != TuringMachine.NUMBER_OF_VARIABLES)
        {
            System.err.println("Error: number of variables unmatched.");
            return null;
        }
        /*******************************************************************************
         * read in rules
         *******************************************************************************/
        s = fin.readLine();
        if (s == null)
            return null;
        while (!s.toLowerCase().startsWith("rules end"))
        {
            /* If current entry is not composed of three tokens:
             * + state,
             * + transition rule on symbol 0
             * + transition rule on symbol 1
             * Just stop the parse procedure. */
            stok = new StringTokenizer(s);
            if (stok.countTokens() != 3)
            {
                System.err.println("Error: wrong number of parameters.");
                return null;
            }
            // get state
            state = Integer.parseInt(stok.nextToken());
            // get rule on symbol 0, symbol 1
            ruleOnSymbol0 = stok.nextToken();
            key = new StateSymbolPair(state, Tape.FILL_SYMBOL);
            if (TransitionRule.isValidRule(ruleOnSymbol0))
                rules.put(key, new TransitionRule(ruleOnSymbol0));
            ruleOnSymbol1 = stok.nextToken();
            key = new StateSymbolPair(state, Tape.COUNTING_SYMBOL);
            if (TransitionRule.isValidRule(ruleOnSymbol1))
                rules.put(key, new TransitionRule(ruleOnSymbol1));
            
            s = fin.readLine();
            if (s == null)
                break;
        }
        
        Vector<String> inputVector = getInputVector(file);
        TuringMachine machine = new TuringMachine(rules, inputVector.get(0));
        
        return machine;
    }
    
    public Vector<String> getInputVector(File file) throws IOException
    {
        try
        {
            fin = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        
        Vector<String> inputVector = new Vector<String>();
        String s = null;
        do
        {
            s = fin.readLine();
            if (s == null) /* no 'inputs' field encountered */
                break;
        }
        while (!s.startsWith("inputs"));
        
        s = fin.readLine();
        if (s != null)
        {
            while (!s.startsWith("inputs end"))
            {
                inputVector.add(s);
                s = fin.readLine();
                if (s == null)
                    break;
            }
        }
        
        if (inputVector.size() == 0)
            inputVector.add(new String("010000"));
        
        return inputVector;
    }
}
