/******************************************************************************
 * Name: Wang Kun
 * ID: DG1333031
 * Email: nju.wangkun@gmail.com
 * Date: 2014-7-25
 ******************************************************************************/
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import turing.TuringMachine;
import turing.tape.Tape;

public class ProgramArea extends TitledPanel implements Observer
{
    private static final long   serialVersionUID    = 1L;
    private static final String TITLE               = "Program";
    private static final int    DEFAULT_COLUMN_SIZE = 3;
    private static final int    DEFAULT_ROW_SIZE    = 10;
    private JScrollPane         scrollPane          = new JScrollPane();
    private JTable              table               = new JTable();
    private TapeTableModel      dataModel           = new TapeTableModel();
    private CurrentRuleRenderer renderer            = new CurrentRuleRenderer();
    private String[]            colNames;
    private String[][]          cellValues;
    int                         rowSize;
    
    public ProgramArea()
    {
        super(TITLE);
        this.setLayout(new BorderLayout());
        this.add(scrollPane);
        scrollPane.setViewportView(table);
        /* Set table's header */
        colNames = new String[DEFAULT_COLUMN_SIZE];
        colNames[0] = " ";
        colNames[1] = "0";
        colNames[2] = "1";
        
        /* Object.class means the renderer is activated for all kinds of data in the table */
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setCurrentState(CurrentRuleRenderer.INIT_STATE);
        renderer.setCurrentSymbol(CurrentRuleRenderer.INIT_SYMBOL);
        this.setTableProperties();
        
        setData(null);
        dataModel.setDataVector(cellValues, colNames);
        table.setModel(dataModel);
    }
    
    /**
     * @brief This method is used to set table's data, both column indices and cell values.
     * @param machine
     *            Table values is set to the machine contents. If machine is null,
     *            default table values used.
     * @return void
     */
    private void setData(TuringMachine machine)
    {
        if (machine == null)
        {
            rowSize = DEFAULT_ROW_SIZE;
            cellValues = new String[rowSize][];
            for (int i = 0; i < rowSize; i++)
            {
                cellValues[i] = new String[DEFAULT_COLUMN_SIZE];
                cellValues[i][0] = String.valueOf(i + 1);
                cellValues[i][1] = String.valueOf(" ");
                cellValues[i][2] = String.valueOf(" ");
            }
        }
        else
        {
            rowSize = machine.getMaxState();
            cellValues = new String[rowSize][];
            for (int i = 0; i < rowSize; i++)
            {
                cellValues[i] = new String[DEFAULT_COLUMN_SIZE];
                int state = i + 1;
                cellValues[i][0] = String.valueOf(state);
                cellValues[i][1] = machine.getRuleString(state, Tape.FILL_SYMBOL);
                cellValues[i][2] = machine.getRuleString(state, Tape.COUNTING_SYMBOL);
            }
        }
    }
    
    private void setTableProperties()
    {
        /* Set row height of the table */
        table.setRowHeight(25);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        /* Set column of the table cannot be moved */
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setColumnSelectionAllowed(false);
        /* Set row's font */
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setFont(new Font("Dialog", Font.BOLD, 16));
    }
    
    public void update(Observable o, Object arg)
    {
        TuringMachine machine = (TuringMachine) o;
        /* Set highlight area */
        renderer.setCurrentState(machine.getCurrentState());
        renderer.setCurrentSymbol(machine.getTape().getCurrentSymbol());
        this.setTableProperties();
        
        setData(machine);
        dataModel.setDataVector(cellValues, colNames);
        table.repaint();
    }
    
    class TapeTableModel extends DefaultTableModel
    {
        private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    }
    
    class CurrentRuleRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;
        private static final int  INIT_SYMBOL      = -1;
        private static final int  INIT_STATE       = -1;
        DefaultTableCellRenderer  renderer         = new DefaultTableCellRenderer();
        private int               currentSymbol    = INIT_SYMBOL;
        private int               currentState     = INIT_STATE;
        
        public CurrentRuleRenderer()
        {
            super.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            currentState = INIT_STATE;
            currentSymbol = INIT_SYMBOL;
        }
        
        public int getCurrentSymbol()
        {
            return currentSymbol;
        }
        
        public void setCurrentSymbol(int currentSymbol)
        {
            this.currentSymbol = currentSymbol;
        }
        
        public int getCurrentState()
        {
            return currentState;
        }
        
        public void setCurrentState(int currentState)
        {
            this.currentState = currentState;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            /* Pay attention to the offset! */
            if (currentState == INIT_STATE && currentSymbol == INIT_SYMBOL)
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
            else if (row == (currentState - 1) && column == (currentSymbol + 1))
            {
                this.setBackground(new Color(128, 128, 255));
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
            }
            else
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
        }
    }
}
