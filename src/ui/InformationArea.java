/******************************************************************************
 * Name: Wang Kun
 * ID: DG1333031
 * Email: nju.wangkun@gmail.com
 * Date: 2014-7-23
 ******************************************************************************/
package ui;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import turing.TuringMachine;

public class InformationArea extends TitledPanel implements Observer
{
    private static final long   serialVersionUID         = 1L;
    private static final String TITLE                    = "Information";
    private static final String LEADING_STATUS_STRING    = "status: ";
    private static final String LEADING_STEP_STRING      = "step: ";
    private static final String LEADING_STATE_STRING     = "current state: ";
    private static final String LEADING_TOTALSTEP_STRING = "total steps: ";
    
    public JLabel               statusLabel              = new JLabel();
    public JLabel               stateLabel               = new JLabel();
    public JLabel               stepLabel                = new JLabel();
    public JLabel               totalStepLabel           = new JLabel();
    
    public InformationArea()
    {
        super(TITLE);
        statusLabel.setText(LEADING_STATUS_STRING);
        stateLabel.setText(LEADING_STATE_STRING);
        stepLabel.setText(LEADING_STEP_STRING);
        totalStepLabel.setText(LEADING_TOTALSTEP_STRING);
        GridLayout layout = new GridLayout(1, 4);
        this.setLayout(layout);
        this.add(statusLabel);
        this.add(stateLabel);
        this.add(stepLabel);
        this.add(totalStepLabel);
    }
    
    public void setStatus(String status)
    {
        statusLabel.setText(LEADING_STATUS_STRING + status);
    }
    
    public void setState(int state)
    {
        stateLabel.setText(LEADING_STATE_STRING + state);
    }
    
    public void setSteps(int steps)
    {
        stepLabel.setText(LEADING_STEP_STRING + steps);
    }
    
    public void setTotalSteps(int totalSteps)
    {
        totalStepLabel.setText(LEADING_TOTALSTEP_STRING + totalSteps);
    }
    
    public void update(Observable o, Object arg)
    {
        TuringMachine machine = (TuringMachine) o;
        this.setStatus(machine.getStatus());
        this.setState(machine.getCurrentState());
        this.setSteps(machine.getSteps());
        this.setTotalSteps(machine.getTotalSteps());
    }
}
