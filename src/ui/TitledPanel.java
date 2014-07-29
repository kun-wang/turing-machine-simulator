/******************************************************************************
 * Name: Wang Kun
 * ID: DG1333031
 * Email: nju.wangkun@gmail.com
 * Date: 2014-7-23
 ******************************************************************************/
package ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TitledPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    private TitledBorder      titledBorder     = null;
    private String            titleString      = null;
    
    public TitledPanel()
    {
        titleString = new String("");
        titledBorder = BorderFactory.createTitledBorder(titleString);
        
        titledBorder.setTitleFont(UniformStyle.PANEL_FONT);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitlePosition(TitledBorder.TOP);
        
        this.setBorder(titledBorder);
    }
    
    public TitledPanel(String title)
    {
        super();
        titleString = new String(title);
        titledBorder = BorderFactory.createTitledBorder(this.getBorder(), titleString);
        titledBorder.setTitleFont(UniformStyle.PANEL_FONT);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitlePosition(TitledBorder.TOP);
        this.setBorder(titledBorder);
    }
    
}
