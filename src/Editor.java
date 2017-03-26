
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * This class handles creation of editor tab and related 
 * functions.
 * 
 * @author Swayam
 */
public class Editor implements Runnable
{
    private final JDialog editorFrame;
    private final JPanel editorPanel;
    private final JTextArea editorTextArea;
    private final JScrollPane scrollPane;
    private final JButton saveButton;
    private final JButton exitButton;
    private int x;
    private int y;
    private final JPanel parentPanel;
    private final ImageIcon icon;
    
    /**
     * Parameterized constructor for Editor class
     * 
     * @param parentPanel main panel
     * @param icon open editor icon
     */
    public Editor(final JPanel parentPanel, final ImageIcon icon)
    {
        editorFrame = new JDialog();
        editorPanel = new JPanel();
        editorTextArea = new JTextArea(18,46);
        saveButton = new JButton();
        exitButton = new JButton();
        scrollPane = new JScrollPane(editorPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.parentPanel = parentPanel;
        this.icon = icon;
    }
    
    /**
     * this method is responsible for setting up
     * of the editor tab frame and placing it on 
     * bottom right corner.
     * 
     */
    private void setEditorFrame()
    {
        editorFrame.setSize(600, 400);
        editorFrame.setUndecorated(true);
        editorFrame.setVisible(true);
        editorFrame.setResizable(false);
        editorFrame.setAlwaysOnTop(true);
        
        x = Toolkit.getDefaultToolkit().getScreenSize().width - 400 - 600;
        y = Toolkit.getDefaultToolkit().getScreenSize().height - 440;
        editorFrame.setLocation(x, y);
        
        editorFrame.add(editorPanel);
    }
    
    /**
     * This method is responsible for setting up 
     * editor panel which will hold other components 
     * for the editor to function.
     * 
     */
    private void setEditorPanel()
    {
        editorPanel.setLayout(new FlowLayout());
        editorPanel.setBackground(Color.BLACK);
        editorPanel.add(editorTextArea);
        editorPanel.add(scrollPane);
        scrollPane.getViewport().add(editorTextArea);
        editorPanel.add(saveButton);
        editorPanel.add(exitButton);
    }
    
    /**
     * This method is responsible for setting up components
     * on the editor panel.
     * 
     */
    private void setComponents()
    {
        editorTextArea.setFont(new Font("Times New Roman",Font.BOLD,16));
        editorTextArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        editorTextArea.setLineWrap(true);
        editorTextArea.setWrapStyleWord(true);
        editorTextArea.setVisible(true);
        editorTextArea.setBackground(Color.LIGHT_GRAY);
        editorTextArea.requestFocus();
        editorTextArea.setOpaque(true);
        
        exitButton.setText("exit");
        exitButton.setOpaque(true);
        exitButton.setBackground(new Color(130, 0, 0));
        exitButton.setForeground(Color.WHITE);
        
        saveButton.setText("save");
        saveButton.setBackground(new Color(0, 130, 0));
        saveButton.setOpaque(true);
        saveButton.setForeground(Color.WHITE);
    }
    
    @Override
    public void run()
    {
        setEditorFrame();
        setEditorPanel();
        setComponents();
        
        exitButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                SmartWatch.updateEditorExists();
                JLabel temp = (JLabel)parentPanel.getComponent(0);
                temp.setIcon(icon);
                editorFrame.dispose();
            }
        });
        
        saveButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                String text = editorTextArea.getText();
                if(text.length() > 0)
                {
                    NameDialogBox name = new NameDialogBox(text, x, y, editorFrame, parentPanel, icon);
                    Thread confirmThread = new Thread(name);
                    confirmThread.setName("confirmThread");
                    confirmThread.start();
                }
                else
                {
                    ShowMessage sm4 = new ShowMessage("Empty file !", x+230, y+150);
                    Thread showMessage = new Thread(sm4);
                    showMessage.start();
                    
                    editorTextArea.requestFocus();
                }
            }
        });
    }
}
