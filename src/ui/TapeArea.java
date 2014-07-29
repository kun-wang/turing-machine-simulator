/******************************************************************************
 * Name: Wang Kun
 * ID: DG1333031
 * Email: nju.wangkun@gmail.com
 * Date: 2014-7-23
 ******************************************************************************/
package ui;

/* TuringTapeArea: Draws and animates the ticker tape */

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

public class TapeArea extends TitledPanel implements Observer
{
    private static final long     serialVersionUID    = 1L;
    private static final String   TITLE               = "Tape";
    private static final int      ROW_SIZE            = 1;
    private static final int      DEFAULT_COLUMN_SIZE = 21;
    private JScrollPane           scrollPane          = new JScrollPane();
    private JTable                table               = new JTable();
    private TapeTableModel        dataModel           = new TapeTableModel();
    private CurrentSymbolRenderer renderer            = new CurrentSymbolRenderer();
    private String[]              colNames;
    private String[][]            cellValues;
    int                           colSize;
    
    public TapeArea()
    {
        super(TITLE);
        this.setLayout(new BorderLayout());
        this.add(scrollPane);
        scrollPane.setViewportView(table);
        
        table.setDefaultRenderer(Object.class, renderer);
        this.setTableProperties();
        
        this.setData(null);
        dataModel.setDataVector(cellValues, colNames);
        table.setModel(dataModel);
    }
    
    /**
     * @brief This method is used to set table's data, both column indices and cell values.
     * @param tape
     *            Table values is set to the tape contents. If tape is null, default table values
     *            used.
     * @return void
     */
    private void setData(Tape tape)
    {
        if (null == tape)
        {
            colSize = DEFAULT_COLUMN_SIZE;
            /* Set table's header */
            colNames = new String[colSize];
            colNames[0] = " ";
            for (int i = 1; i < colSize; i++)
                colNames[i] = String.valueOf(i);
            /* Set table's first row */
            cellValues = new String[ROW_SIZE][];
            cellValues[0] = new String[colSize];
            cellValues[0][0] = String.valueOf('#');
            for (int i = 1; i < colSize; i++)
                cellValues[0][i] = String.valueOf(' ');
        }
        else
        {
            this.colSize = tape.getSize();
            /* Set table's header */
            colNames = new String[colSize];
            colNames[0] = " ";
            for (int i = 1; i < colSize; i++)
                colNames[i] = String.valueOf(i);
            /* Set table's first row */
            cellValues = new String[ROW_SIZE][];
            cellValues[0] = new String[colSize];
            cellValues[0][0] = String.valueOf('#');
            for (int i = 1; i < colSize; i++)
                cellValues[0][i] = tape.getSymbolAt(i);
            
        }
    }
    
    private void setTableProperties()
    {
        /* Set row height of the table */
        table.setRowHeight(40);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        /* Set column of the table auto resized based on the contents */
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        /* Set column of the table cannot be moved */
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setColumnSelectionAllowed(false);
        /* Set row's font */
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setFont(new Font("Dialog", Font.BOLD, 16));
        
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            table.getColumnModel().getColumn(i).setPreferredWidth(20);
            table.getColumnModel().getColumn(i).setMinWidth(40);
        }
    }
    
    public void update(Observable o, Object arg)
    {
        Tape tape = ((TuringMachine) o).getTape();
        renderer.setPosition(tape.getCurrentPosition());
        this.setTableProperties();
        
        setData(tape);
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
    
    class CurrentSymbolRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;
        private static final int  INIT_POSITION    = -1;
        private static final int  INIT_ROW_NUMBER  = 0;
        DefaultTableCellRenderer  renderer         = new DefaultTableCellRenderer();
        private int               position         = INIT_POSITION;
        
        public CurrentSymbolRenderer()
        {
            super.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            position = INIT_POSITION;
        }
        
        public int getPosition()
        {
            return position;
        }
        
        public void setPosition(int position)
        {
            this.position = position;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (position == INIT_POSITION)
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
            else if (row == INIT_ROW_NUMBER && column == position)
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
