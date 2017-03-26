
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class handles the popups to display messages to the user.
 * The popup messages mainly consists of informative messages and 
 * error messages.
 * 
 * @author Swayam
 */
public class ShowMessage implements Runnable
{
    private final JDialog popup;
    private final JLabel message;
    private final JPanel panel;
    private final String content;
    private final int x;
    private final int y;
    
    /**
     * Default constructor for ShowMessage class
     * Constructor chaining has been used. 
     * Default constructor calls the parameterized constructor.
     * 
     */
    public ShowMessage()
    {
        this(null,0,0);
    }
    
    /**
     * Parameterized constructor for ShowMessage
     * 
     * @param msg message to be displayed 
     * @param x x-coordinate of the popup 
     * @param y y-coordinate of the popup
     */
    public ShowMessage(String msg, int x, int y)
    {
        popup = new JDialog();
        message = new JLabel();
        panel = new JPanel();
        content = msg;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void run()
    {
        display(content, x, y);
    }
    
    /**
     * This method sets various properties of popup window
     * and adds a panel to the popup for other components 
     * to be added on the panel.
     * 
     */
    private void setupPopupDialog()
    {
        popup.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        popup.setSize(150, 50);
        popup.setUndecorated(true);
        popup.setVisible(true);
        popup.setAlwaysOnTop(true);
        
        popup.add(panel);
        popup.setLocation(x, y);
    }
    
    /**
     * This method sets up the popup panel properties and
     * adds message component to the panel to be displayed.
     * 
     */
    private void setupPopupPanel()
    {
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(130, 0, 0));
        message.setForeground(Color.WHITE);
        panel.add(message);
    }
    
    /**
     * This method sets up the popup dialog and displays 
     * the message on the screen for the user to read.
     * 
     * @param msg message to be displayed
     * @param x x-coordinate of the popup window
     * @param y y-coordinate of the popup window
     */
    private void display(final String msg, final int x, final int y)
    {
        this.setupPopupDialog();
        this.setupPopupPanel();
        
        message.setText(msg);
        try
        {
            Thread.sleep(1500);
            popup.dispose();
        }
        catch(InterruptedException ex)
        {
            System.err.println(ex);
        }
    }
}
