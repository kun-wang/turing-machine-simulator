/******************************************************************************
 * Name:		Wang Kun
 * ID:			DG1333031
 * Email:		nju.wangkun@gmail.com
 * Date:		2014-7-23
 ******************************************************************************/
package ui;


import java.awt.Color;

import javax.swing.JLabel;

public class CaptionArea extends TitledPanel
{

    private static final long serialVersionUID = 1L;
    JLabel titleLabel = new JLabel("Turing Machine Simulator   ");
    public CaptionArea()
    {
        super(" ");
        /* Set title "Turing Machine Simulator" format */
        titleLabel.setFont(UniformStyle.CAPTION_FONT);
        titleLabel.setBackground(new Color(255, 255, 255));
        this.add(titleLabel);
    }

}
