/******************************************************************************
 * Name: Wang Kun
 * ID: DG1333031
 * Email: nju.wangkun@gmail.com
 * Date: 2014-7-25
 ******************************************************************************/
package ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

public class GuideArea extends TitledPanel
{
    
    private static final long   serialVersionUID = 1L;
    private static final String TITLE            = "Guide";
    
    private JLabel              guideText        = new JLabel();
    private StringBuilder       builder          = new StringBuilder();
    
    public GuideArea()
    {
        super(TITLE);
        
        builder.append("<html><body>");
        builder.append("How to use: <br>");
        builder.append("Step1. Load pre or self defined machine from menu \"Load Machine...\"<br>");
        builder.append("Step2. Set input in the \"input Selector\". "
                + "You can select the predefined input or input your preferred input.<br>");
        builder.append("Step3. Press \"Run Button\" to run the machine.<br>");
        builder.append("Step4. Slide \"Speed Slider\" to adjust the speed of machine.");
        builder.append("<body></html>");
        
        this.setLayout(new BorderLayout());
        this.add(guideText, BorderLayout.NORTH);
        guideText.setText(builder.toString());
        // guideText.setEditable(false);
    }
}
