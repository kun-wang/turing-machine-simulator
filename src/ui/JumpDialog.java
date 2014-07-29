package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class JumpDialog extends JDialog
{
    private static final long serialVersionUID = 1L;
    private JButton    jumpButton   = new JButton("Jump");
    private JButton    cancelButton = new JButton("Cancel");
    private JLabel     label        = new JLabel(" Jump To Step: ");
    private JTextField jtf          = new JTextField(10);
    private JCheckBox  jcb          = new JCheckBox("Jump to End");
    
    private JPanel     buttonPane   = new JPanel();
    private JPanel     inputPane    = new JPanel();
    
    private Box        b;
    
    private int        j;
    private boolean    valid;
    
    JDialog            dialog       = this;
    
    public JumpDialog(Frame owner, String message)
    {
        super(owner, message, true);
        
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.add(jumpButton);
        buttonPane.add(cancelButton);
        
        jumpButton.addActionListener(new JumpValueListener());
        
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dialog.dispose();
            }
        });
        
        jcb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (jcb.isSelected())
                {
                    jtf.setEnabled(false);
                }
                else
                {
                    jtf.setEnabled(true);
                }
            }
        });
        
        inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.X_AXIS));
        inputPane.add(label);
        inputPane.add(jtf);
        inputPane.setMaximumSize(new Dimension(200, 100));
        
        b = new Box(BoxLayout.Y_AXIS);
        
        b.add(Box.createVerticalGlue());
        b.add(inputPane);
        b.add(Box.createVerticalGlue());
        b.add(jcb);
        b.add(Box.createVerticalGlue());
        b.add(buttonPane);
        getContentPane().add(b);
        setLocationRelativeTo(owner);
        setSize(new Dimension(200, 120));
        setResizable(false);
        getRootPane().setDefaultButton(jumpButton);
    }
    
    @SuppressWarnings("deprecation")
    public void show()
    {
        valid = false;
        super.show();
    }
    
    public int getJump()
    {
        return j;
    }
    
    public boolean isValid()
    {
        return valid;
    }
    
    class JumpValueListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (!jcb.isSelected())
            {
                try
                {
                    j = Integer.valueOf(jtf.getText()).intValue();
                }
                catch (NumberFormatException exc)
                {
                    JOptionPane.showMessageDialog(dialog, "Please enter a positive integer",
                            "Bad Format", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }
                if (j < 1)
                {
                    JOptionPane.showMessageDialog(dialog, "Please enter a positive integer",
                            "Bad Format", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }
            }
            else
            {
                j = -1;
            }
            valid = true;
            dialog.dispose();
        }
    }
}
