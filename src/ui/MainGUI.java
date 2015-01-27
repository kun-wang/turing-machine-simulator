/********************************************************************
 * Author Name: WangKun
 * Author ID: DG1333031
 * Mail Address: nju.wangkun@gmail.com
 * Last Modified: 2014-07-20 20:18
 * Filename: TuringMain.java
 ********************************************************************/
package ui;

/* TuringMain: Coordinates GUI components, events, etc.
 * with logical aspects of TM (Machine, Tape, etc.) */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileView;

import turing.TuringMachine;
import util.MachineLoader;
import util.MethodFactory;

public class MainGUI extends JFrame
{
    /* ==========================================================================================
     * Constants definition begins
     * ========================================================================================== */
    private static final long    serialVersionUID  = 1L;
    private static final boolean RUNNING_STATE     = false;
    private static final boolean PAUSED_STATE      = true;
    static final int             MIN_SPEED         = 0;
    static final int             MAX_SPEED         = 100;
    static final int             INIT_SPEED        = 50;
    static final int             FADE_IN           = 0;
    static final int             FADE_OUT          = 1;
    static final int             GROW_TAPE_LEFT    = 2;
    static final int             GROW_TAPE_RIGHT   = 3;
    static final String          MACHINE_DIRECTORY = "files/";
    static final int             RIGHT_PADDING     = 6;
    static final int             DOWN_PADDING      = 10;
    /* ==========================================================================================
     * Members definition begins
     * ========================================================================================== */
    /* Menu bar and its elements */
    private JMenuBar             menuBar           = new JMenuBar();
    private JMenu                fileMenu          = new JMenu("File");
    private JMenuItem            openItem          = new JMenuItem("Load Machine...   ");
    private JMenuItem            quitItem          = new JMenuItem("Quit");
    private JMenu                controlMenu       = new JMenu("Controls");
    private JMenuItem            runItem           = new JMenuItem("Run/Pause");
    private JMenuItem            backItem          = new JMenuItem("Step Back");
    private JMenuItem            forwItem          = new JMenuItem("Step Forward");
    private JMenuItem            resetItem         = new JMenuItem("Reset");
    private JMenuItem            jumpItem          = new JMenuItem("Jump To Step...   ");
    /* CaptionArea, TapeArea, InformationArea */
    private CaptionArea          captionArea       = new CaptionArea();
    private TapeArea             tapeArea          = new TapeArea();
    private InformationArea      informationArea   = new InformationArea();
    /* control bar and its elements */
    private ImageIcon            runIcon;
    private ImageIcon            pauseIcon;
    private ImageIcon            backIcon;
    private ImageIcon            stepIcon;
    private ImageIcon            resetIcon;
    private JButton              runPauseButton;
    private JButton              backButton;
    private JButton              stepButton;
    private JButton              resetButton;
    private TitledPanel          controlPanel      = new TitledPanel("Control");
    private JSlider              speedSlider       = new JSlider(JSlider.HORIZONTAL, MIN_SPEED,
                                                           MAX_SPEED, INIT_SPEED);
    private TitledPanel          speedPanel        = new TitledPanel("Speed");
    
    private JComboBox            inputSelector;
    private TitledPanel          inputPanel        = new TitledPanel("Input");
    
    private GuideArea            guideArea         = new GuideArea();
    
    /* program container */
    private ProgramArea          programArea       = new ProgramArea();
    
    private JPanel               leftRightPanel    = new JPanel();
    
    private JFileChooser         machineChooser;
    private JumpDialog           jumpDialog;
    
    private javax.swing.Timer    stepTimer;
    private int                  delay;
    
    private TuringMachine        machine;
    private Vector<String>       inputVector;
    
    /* The Turing machine is paused or not */
    static boolean               state             = PAUSED_STATE;
    
    /* ===========================================================================================
     * Methods definition begins
     * =========================================================================================== */
    public MainGUI(String s)
    {
        super(s);
        this.setSize(new Dimension(900, 700));
        this.setResizable(false);
        setForeground(new Color(200, 200, 200));
        setBackground(new Color(200, 200, 200));
        this.addWindowListener(new WindowAdapter()
        {
            /* Invoked when a window has been closed. */
            public void windowClosed(WindowEvent e)
            {
                System.exit(0);
            }
            
            /* Invoked when a window is in the process of being closed. */
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        machineChooser = new JFileChooser();
        machineChooser.addChoosableFileFilter(new TuringFilter());
        machineChooser.setAccessory(new TuringDescription(machineChooser));
        machineChooser.setFileView(new TuringFileView());
        machineChooser.setMultiSelectionEnabled(false);
        machineChooser.setDialogTitle("Load Machine");
        machineChooser.setPreferredSize(new Dimension((int) (getWidth() * 0.9), 500));
        
        jumpDialog = new JumpDialog(this, "Jump To Step...");
        jumpDialog.addWindowListener(new JumpWindowListener());
        
        /* set menuBar to the main GUI, and menuItems into menu, and add menus into menuBar */
        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(openItem);
        fileMenu.add(quitItem);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        openItem.addActionListener(new GetMachinesMenuItemListener());
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        quitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                stepTimer.stop();
                dispose();
            }
        });
        menuBar.add(controlMenu);
        controlMenu.add(runItem);
        controlMenu.add(backItem);
        controlMenu.add(forwItem);
        controlMenu.add(resetItem);
        controlMenu.add(jumpItem);
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        runItem.addActionListener(new RunPauseButtonListener());
        backItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        backItem.addActionListener(new BackButtonListener());
        forwItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        forwItem.addActionListener(new StepButtonListener());
        resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        resetItem.addActionListener(new ResetButtonListener());
        jumpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()));
        jumpItem.addActionListener(new JumpItemListener());
        
        /* Control Panel */
        runIcon = new ImageIcon(this.getClass().getResource("/images/Play24.gif"));
        pauseIcon = new ImageIcon(this.getClass().getResource("/images/Pause24.gif"));
        backIcon = new ImageIcon(this.getClass().getResource("/images/StepBack24.gif"));
        stepIcon = new ImageIcon(this.getClass().getResource("/images/StepForward24.gif"));
        resetIcon = new ImageIcon(this.getClass().getResource("/images/Stop24.gif"));
        
        runPauseButton = new JButton(runIcon);
        runPauseButton.addActionListener(new RunPauseButtonListener());
        runPauseButton.setToolTipText("Run Machine");
        backButton = new JButton(backIcon);
        backButton.addActionListener(new BackButtonListener());
        backButton.setToolTipText("Step Back");
        stepButton = new JButton(stepIcon);
        stepButton.addActionListener(new StepButtonListener());
        stepButton.setToolTipText("Step Forward");
        resetButton = new JButton(resetIcon);
        resetButton.addActionListener(new ResetButtonListener());
        resetButton.setToolTipText("Reset Machine");
        
        controlPanel.setLayout(new GridLayout(1, 4, 5, 0));
        controlPanel.add(runPauseButton);
        controlPanel.add(backButton);
        controlPanel.add(stepButton);
        controlPanel.add(resetButton);
        
        /* Speed Panel */
        speedSlider.addChangeListener(new SpeedSliderListener());
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true); /* Show current number of the slider */
        speedSlider.setToolTipText("Adjust Running Speed");
        speedPanel.setLayout(new BorderLayout());
        speedPanel.add(speedSlider);
        
        /* Input Panel */
        inputSelector = new JComboBox();
        inputSelector.addActionListener(new inputSelectorListener());
        inputSelector.setEnabled(false);
        inputSelector.setEditable(true);
        inputSelector.setToolTipText("Select an input tape, or enter your own");
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputSelector);
        
        /* Group left panel and program panel together */
        leftRightPanel.setLayout(null);
        leftRightPanel.add(controlPanel);
        controlPanel.setBounds(0, 0, 450, 60);
        leftRightPanel.add(speedPanel);
        speedPanel.setBounds(0, 60, 450, 80);
        leftRightPanel.add(inputPanel);
        inputPanel.setBounds(0, 140, 450, 60);
        leftRightPanel.add(guideArea);
        guideArea.setBounds(0, 200, 450, 200 - DOWN_PADDING);
        leftRightPanel.add(programArea);
        programArea.setBounds(450, 0, 450 - RIGHT_PADDING, 400 - DOWN_PADDING);
        
        this.setLayout(null);
        this.getContentPane().add(captionArea);
        captionArea.setBounds(0, 0, this.getWidth() - RIGHT_PADDING, 80);
        this.getContentPane().add(tapeArea);
        tapeArea.setBounds(0, 80, this.getWidth() - RIGHT_PADDING, 120);
        this.getContentPane().add(informationArea);
        informationArea.setBounds(0, 200, this.getWidth() - RIGHT_PADDING, 50);
        this.getContentPane().add(leftRightPanel);
        leftRightPanel.setBounds(0, 250, this.getWidth() - RIGHT_PADDING, this.getHeight() - 250);
        
        /* Set timers */
        delay = (int) ((Math.exp(MAX_SPEED / 10.0) / Math.exp(speedSlider.getValue() / 10.0)) / 3.0);
        stepTimer = new javax.swing.Timer(delay, new StepTimerListener());
        stepTimer.setInitialDelay(delay);
    }
    
    public void run()
    {
        this.informationArea.setStatus("machine not set");
        this.setVisible(true);
    }
    
    /* setHalted: changes the GUI to reflect that the machine is halted. */
    private void setHalted()
    {
        runPauseButton.setIcon(runIcon);
        runPauseButton.setToolTipText("Run Machine");
        state = PAUSED_STATE;
        stepTimer.stop();
        informationArea.setStatus("Halted");
    }
    
    private void setRunning()
    {
        runPauseButton.setIcon(pauseIcon);
        runPauseButton.setToolTipText("Pause Machine");
        state = RUNNING_STATE;
        stepTimer.start();
        informationArea.setStatus("running");
    }
    
    /* setPaused: changes the GUI to reflect that the machine is paused. */
    private void setPaused()
    {
        runPauseButton.setIcon(runIcon);
        runPauseButton.setToolTipText("Run Machine");
        state = PAUSED_STATE;
        stepTimer.stop();
        informationArea.setStatus("paused");
    }
    
    class RunPauseButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            if (machine.getHaltState() == TuringMachine.HALTED)
                resetButton.doClick();
            if (PAUSED_STATE == state)
                setRunning();
            else
                setPaused();
        }
    }
    
    /**
     * Have not been done
     */
    class BackButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            setPaused();
            machine.stepBackwardN(1);
        }
    }
    
    class StepButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            if (machine.getHaltState() == TuringMachine.HALTED)
                return;
            else if (RUNNING_STATE == state)
                setPaused();
            machine.stepForwardN(1);
            if (machine.getHaltState() == TuringMachine.HALTED)
                setHalted();
        }
    }
    
    class ResetButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            setPaused();
            machine.resetMachine(null);
        }
    }
    
    /**
     * Everytime 'Speed Slider' is modified, 'stateChanged' function is called. Invoked when the
     * target of the listener(speedSlider) has changed its state.
     */
    class SpeedSliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            int fps = (int) speedSlider.getValue();
            delay = (int) ((Math.exp(MAX_SPEED / 10.0) / Math.exp(fps / 10.0)) / 3.0);
            stepTimer.setInitialDelay(delay);
            stepTimer.setDelay(delay);
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            if (fps == 0)
                stepTimer.stop();
            else
            {
                informationArea.setStatus("speed " + fps + "%");
                if (RUNNING_STATE == state)
                    stepTimer.start();
            }
        }
    }
    
    class StepTimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            machine.stepForwardN(1);
            informationArea.setSteps(machine.getSteps());
            if (machine.getHaltState() == TuringMachine.HALTED)
                setHalted();
        }
    }
    
    class JumpItemListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (null == machine)
            {
                informationArea.setStatus("machine not set");
                return;
            }
            setPaused();
            jumpDialog.setVisible(true);
        }
    }
    
    /* The following Listener is activated when the 'Load Machine..' option is chosen from the File
     * Menu. It fetches the machine data and forwards it to the drawing areas. */
    class GetMachinesMenuItemListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            setPaused();
            File filesdir = new File(MainGUI.MACHINE_DIRECTORY);
            File filename = null;
            MachineLoader loader = new MachineLoader();
            
            // Pull up file chooser dialog and find name of selected machine.
            machineChooser.setFileFilter(new TuringFilter());
            machineChooser.setCurrentDirectory(filesdir);
            
            if (JFileChooser.APPROVE_OPTION == machineChooser.showOpenDialog(MainGUI.this))
            {
                filename = machineChooser.getSelectedFile();
                try
                {
                    /* remove old observers */
                    if (machine != null)
                        machine.deleteObservers();
                    
                    machine = loader.getMachine(filename);
                    if (machine != null)
                    {
                        /* Add observers */
                        machine.addObserver(tapeArea);
                        machine.addObserver(informationArea);
                        machine.addObserver(programArea);
                        
                        /* Get all predefined inputs */
                        inputVector = loader.getInputVector(filename);
                        inputSelector.setModel(new DefaultComboBoxModel(inputVector));
                        /* This action will activate the InputSelectorListener, and
                         * thus reset the machine. In this case, all observers of machine
                         * will be notified. */
                        inputSelector.setSelectedIndex(0);
                        inputSelector.setEnabled(true);
                        
                        setTitle("Turing Machine:  " + loader.getMachineName(filename));
                        controlPanel.requestFocus();
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }
    
    /** The listener is activated when a input is chosen or entered in the Input Combo Box */
    class inputSelectorListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int i, len;
            boolean match = false;
            String s;
            String inputString = (String) inputSelector.getSelectedItem();
            
            if (inputString == null)
            {
                inputSelector.setSelectedIndex(0);
                inputString = (String) inputSelector.getItemAt(0);
            }
            
            DefaultComboBoxModel model = (DefaultComboBoxModel) inputSelector.getModel();
            // If the entered input is not already, a sample tape,
            // add it to the list of sample tapes
            len = inputSelector.getItemCount();
            for (i = 0; i < len; i++)
            {
                s = (String) model.getElementAt(i);
                if (inputString.equals(s))
                    match = true;
            }
            if (!match)
                model.insertElementAt(inputString, 0);
            
            machine.resetMachine(inputString);
        }
    }
    
    /**
     * The class 'TuringFileView' is used to tell the file chooser how to display the files.
     * Intead of the actual file name, the file chooser will display the name in the 'title'
     * field of the input file.
     */
    public class TuringFileView extends FileView
    {
        public String getTypeDescription(File f)
        {
            return null;
        }
        
        public Icon getIcon(File f)
        {
            return null;
        }
        
        public String getName(File f)
        {
            try
            {
                if (f.getName().endsWith(".tur"))
                {
                    MachineLoader loader = new MachineLoader();
                    return loader.getMachineName(f);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        
        public String getDescription(File f)
        {
            return null;
        }
        
        public Boolean isTraversable(File f)
        {
            return null;
        }
    }
    
    /**
     * The class 'TuringFilter' is used in the file chooser to filter out all files except
     * those with the '.tur' suffix
     */
    class TuringFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(File f)
        {
            if (f.isDirectory())
                return true;
            if (f.getName().endsWith(".tur"))
                return true;
            else
                return false;
        }
        
        public String getDescription()
        {
            return "TUR files";
        }
    }
    
    /**
     * The class 'TuringDescription' is the component that displays the
     * machine descriptions in the file chooser.
     */
    class TuringDescription extends JPanel implements PropertyChangeListener
    {
        private static final long serialVersionUID = 1L;
        JLabel                    label            = new JLabel("  Description:");
        JTextArea                 jta;
        JScrollPane               jsp;
        
        public TuringDescription(JFileChooser fc)
        {
            setPreferredSize(new Dimension(300, 200));
            setLayout(new BorderLayout());
            jta = new JTextArea();
            jsp = new JScrollPane(jta);
            fc.addPropertyChangeListener(this);
            jta.setLineWrap(true);
            jta.setWrapStyleWord(true);
            jta.setBackground(fc.getBackground());
            add(label, BorderLayout.NORTH);
            add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.WEST);
            add(jsp, BorderLayout.CENTER);
        }
        
        public void propertyChange(PropertyChangeEvent e)
        {
            File f;
            MachineLoader loader = new MachineLoader();
            if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
            {
                f = (File) e.getNewValue();
                if (f != null)
                {
                    try
                    {
                        jta.setText(loader.getMachineDescription(f));
                    }
                    catch (IOException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    // scroll to beginning of description
                    jta.moveCaretPosition(0);
                }
            }
        }
    }
    
    /* Everytime when the JumpDialog is disposed, 'windowClose' action will be taken. */
    class JumpWindowListener extends WindowAdapter
    {
        public void windowClosed(WindowEvent e)
        {
            if (jumpDialog.isValid())
            {
                int jumpValue = jumpDialog.getJump();
                if (jumpValue > machine.getTotalSteps())
                {
                    String message = "Jump steps exceeds the total steps of the machine!";
                    JOptionPane
                            .showMessageDialog(null, message, "Alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (jumpValue == -1)
                    jumpValue = Integer.MAX_VALUE;
                
                stepTimer.stop();
                machine.resetMachine(null);
                machine.stepForwardN(jumpValue);
                if (machine.getHaltState() == TuringMachine.HALTED)
                    setHalted();
            }
        }
    }
    
    public void setProgramDirectory()
    {
        File dir = new File(MACHINE_DIRECTORY);
        if (!dir.isDirectory())
            dir.mkdir();
        File programDir = new File(this.getClass().getResource("/files/").getPath());
        File[] programFiles = programDir.listFiles();
        System.out.println("Copying files... ");
        for (int i = 0; i < programFiles.length; i++)
        {
            String filename = programFiles[i].getName();
            if (!filename.endsWith(".tur"))
                continue;
            /* Copy file to the new directory */
            System.out.println("Copying files... ");
            
            MethodFactory.copyFile(programFiles[i].getPath(), MACHINE_DIRECTORY + filename);
        }
    }
}
