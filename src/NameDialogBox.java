
import java.awt.Color;
import java.awt.Dialog;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * This class handles the file saving option for the editor 
 * functionality.
 * 
 * @author Swayam
 */
public class NameDialogBox implements Runnable
{
    
    private final JDialog confirmBox;
    private final JPanel confirmPanel;
    private final JButton confirmButton;
    private final JButton nopeButton;
    private final TextField filename;
    private final String content;
    private final int x;
    private final int y;
    private final JDialog editorDialog;
    private final JPanel smartWatchPanel;
    private final ImageIcon icon;
    
    /**
     * Default constructor for NameDialogBox class
     * 
     */
    public NameDialogBox()
    {
        this(null,0,0,null,null,null);
    }
    
    /**
     * Parameterized constructor for NameDialogBox class.
     * 
     * @param text content to be saved in the file
     * @param x x-coordinate of the save file dialog box
     * @param y y-coordinate of the save file dialog box
     * @param ed editor frame
     * @param smartWatchPanel main panel
     * @param icon editor open icon
     */
    public NameDialogBox(final String text, final int x, final int y, final JDialog ed, final JPanel smartWatchPanel, final ImageIcon icon)
    {
        confirmBox = new JDialog(ed, "", Dialog.ModalityType.DOCUMENT_MODAL);
        confirmPanel = new JPanel();
        confirmButton = new JButton();
        nopeButton = new JButton();
        filename = new TextField(30);
        editorDialog = ed;
        content = text;
        this.icon = icon;
        this.smartWatchPanel = smartWatchPanel;
        this.x = x;
        this.y = y;
    }
    
    /**
     * This method saves the content of the editor into a file 
     * and closes the save dialog box.
     * 
     */
    private void saveFileAndDispose()
    {
        if(filename.getText().length() > 0)
        {
            byte data[] = content.getBytes();
            Path p = Paths.get("./"+filename.getText()+".txt");
            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) 
            {
                out.write(data, 0, data.length);
                confirmBox.dispose();
                editorDialog.dispose();
            }
            catch(IOException ex)
            {
                System.err.println(ex);
            }
        }
        else
        {
            ShowMessage sm = new ShowMessage("Enter file name !", x+80+confirmBox.getWidth()/2, y+100+confirmBox.getHeight()/2);
            Thread showMessage = new Thread(sm);
            showMessage.start();
            
            filename.requestFocus();
        }
    }
    
    /**
     * This method sets up the save dialog box.
     * Also, adds a panel for other components to be added on.
     * 
     */
    private void setConfirmBox()
    {
        confirmBox.setSize(300, 100);
        confirmBox.setAlwaysOnTop(true);
        confirmBox.setUndecorated(true);
        confirmBox.setLocation(x+confirmBox.getWidth()/2, y+confirmBox.getHeight()/2);
        
        confirmBox.add(confirmPanel);
    }
    
    /**
     * This method sets up the save box panel for 
     * filename textbar and other buttons to be added on the
     * panel.
     * 
     */
    private void setConfirmPanel()
    {
        confirmPanel.add(filename);
        confirmPanel.add(confirmButton);
        confirmPanel.add(nopeButton);
        
        confirmPanel.setBackground(Color.LIGHT_GRAY);
    }
    
    /**
     * This method sets up the properties of the buttons
     * added on the save file dialog.
     * 
     */
    private void setComponents()
    {
        confirmButton.setText("confirm");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(0, 120, 0));
        
        nopeButton.setText(" leave ");
        nopeButton.setForeground(Color.WHITE);
        nopeButton.setBackground(new Color(120, 0, 0));
    }
    
    @Override
    public void run()
    {
        setConfirmBox();
        setConfirmPanel();
        setComponents();
        
        confirmButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                saveFileAndDispose();
                JLabel temp = (JLabel)smartWatchPanel.getComponent(0);
                temp.setIcon(icon);
                SmartWatch.updateEditorExists();
            }
        });
        
        nopeButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                confirmBox.dispose();
            }
        });
        
        confirmBox.setVisible(true);
    }
}