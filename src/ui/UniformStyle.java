package ui;

import java.awt.Font;

public class UniformStyle
{
    public static final String PANEL_FONT_NAME    = Font.DIALOG;
    public static final int    PANEL_FONT_SIZE    = 12;
    public static final int    PANEL_FONT_STYPE   = Font.ITALIC;
    public static final String CAPTION_FONT_NAME  = "Monotype Corsiva";
    public static final int    CAPTION_FONT_SIZE  = 36;
    public static final int    CAPTION_FONT_STYPE = Font.PLAIN;
    
    public static final Font   PANEL_FONT         = new Font(PANEL_FONT_NAME, PANEL_FONT_STYPE,
                                                          PANEL_FONT_SIZE);
    public static final Font   CAPTION_FONT       = new Font(CAPTION_FONT_NAME, CAPTION_FONT_STYPE,
                                                          CAPTION_FONT_SIZE);
}
