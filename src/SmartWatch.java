
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SmartWatch implements Runnable
{   
    private final JFrame smartWatchFrame;
    private final JPanel smartWatchPanel;
    private final JLabel timeDisplayLabel;
    private final JLabel addNoteButton;
    private final JLabel weatherButton;
    private final JLabel closeButton;
    private final Font timeFont;
    private int x;
    private int y;
    private static boolean editorExists;
    private static boolean tempExists;
    private ImageIcon icon;
    private ImageIcon subtractIcon;
    private final JTextField search;
    private final JLabel searchButton;
    private final JLabel minimizeButton;
    
    /**
     * This method sets the icons of various
     * functionality buttons.
     * 
     * @param path path to the icon image
     * @return ImageIcon for the button
     */
    private ImageIcon createImageIcon(final String path)
    {
        java.net.URL imageURL = getClass().getResource(path);
        if(imageURL != null)
            return new ImageIcon(imageURL);
        else
        {
            displayErrorMessage("oops! cannot load image");
            //System.err.println("file not found : " + path);
            return null;
        }
    }
    
    /**
     * Default constructor for SmartWatch class
     * 
     */
    public SmartWatch()
    {
        timeFont = new Font("Ticking Timebomb BB",Font.PLAIN,120);     
        smartWatchFrame = new JFrame("SmartWatch");
        smartWatchPanel = new JPanel();
        timeDisplayLabel = new JLabel();
        addNoteButton = new JLabel();
        weatherButton = new JLabel();
        closeButton = new JLabel();
        search = new JTextField(20);
        searchButton = new JLabel();
        minimizeButton = new JLabel();
        x = 0;
        y = 0;
        editorExists = false;
        tempExists = false;
        icon = null;
        subtractIcon = null;
    }
    
    /**
     * This method updates the existence variable 
     * of the editor.
     * 
     */
    public static void updateEditorExists()
    {
        editorExists = false;
    }
    
    /**
     * This method updates the existence variable 
     * of the weather info panel.
     * 
     */
    public static void updateTempExists()
    {
        tempExists = false;
    }
    
    /**
     * This method links icons to their functionality 
     * buttons.
     * 
     */
    private void linkIcons()
    {
        subtractIcon = createImageIcon("images/subtractNoteIcon.png");
        icon = createImageIcon("images/closeIcon.png");
        closeButton.setIcon(icon);
        icon = createImageIcon("images/minimizeIcon.png");
        minimizeButton.setIcon(icon);
        icon = createImageIcon("images/weatherIcon.png");
        weatherButton.setIcon(icon);
        icon = createImageIcon("images/searchIcon.png");
        searchButton.setIcon(icon);
        icon = createImageIcon("images/addNoteIcon.png");
        addNoteButton.setIcon(icon);
    }
    
    /**
     * This method is responsible for placing
     * various components onto the main panel.
     * 
     */
    private void placeComponents()
    {
        addNoteButton.setBounds(0, 0, 40, 40);
        addNoteButton.setOpaque(true);
        weatherButton.setBounds(50, 0, 40, 40);
        weatherButton.setOpaque(true);
        timeDisplayLabel.setBounds(100, 60, 300, 100);
        timeDisplayLabel.setOpaque(true);
        closeButton.setBounds(345, 0, 50, 32);
        closeButton.setOpaque(true);
        searchButton.setBounds(365, 168, 25, 25);
        searchButton.setOpaque(true);
        minimizeButton.setBounds(300, -2, 40, 40);
        minimizeButton.setOpaque(true);
        search.setBounds(10, 170, 350, 25);
        search.setOpaque(true);
    }
    
    /**
     * This method is responsible for placing
     * the main frame to the desired location.
     * 
     */
    private void placeWatch()
    {
        x = Toolkit.getDefaultToolkit().getScreenSize().width - 400;
        y = Toolkit.getDefaultToolkit().getScreenSize().height - 240;
        smartWatchFrame.setLocation(x,y);
    }
    
    /**
     * This method is responsible for setting up 
     * the main watch frame.
     * 
     */
    private void setWatchFrame()
    {
        smartWatchFrame.setSize(400, 200);
        smartWatchFrame.setAlwaysOnTop(true);
        smartWatchFrame.setResizable(false);
        smartWatchFrame.setUndecorated(true);
        smartWatchFrame.setVisible(true);
        smartWatchFrame.add(smartWatchPanel);
    }
    
    /**
     * This method is responsible for setting up
     * the main watch panels for various components
     * to be added on.
     * 
     */
    private void setWatchPanel()
    {
        smartWatchPanel.setLayout(null);
        smartWatchPanel.setBackground(Color.BLACK);
        smartWatchPanel.add(addNoteButton);
        smartWatchPanel.add(weatherButton);
        smartWatchPanel.add(closeButton);
        smartWatchPanel.add(timeDisplayLabel);
        smartWatchPanel.add(search);
        smartWatchPanel.add(searchButton);
        smartWatchPanel.add(minimizeButton);
    }
    
    /**
     * This method is responsible for setting up 
     * various properties of the time label and also 
     * make the search bar on auto request.
     * 
     */
    private void setTimeLabelAndFocus()
    {
        timeDisplayLabel.setBackground(Color.BLACK);
        timeDisplayLabel.setFont(timeFont);
        timeDisplayLabel.setForeground(new Color(44, 117, 255));
        search.requestFocus();
    }
    
    /**
     * This method creates speech object and 
     * speaks out the desired message.
     * 
     * @param txt message to be spoken
     */
    private void speakOutMessage(final String txt)
    {
        Speech speech = new Speech();
        speech.setTextToSay(txt);
        Thread t = new Thread(speech);
        t.start();
    }
    
    /**
     * This method is responsible for displaying
     * error and informative messages to the user.
     * 
     * @param txt message to be displayed
     */
    private void displayErrorMessage(final String txt)
    {
        ShowMessage sm = new ShowMessage(txt, x+140, y+80);
        Thread showMessage = new Thread(sm);
        showMessage.start();
    }
    
    /**
     * This method is responsible for creating the 
     * main watch frame.
     * 
     */
    private void createWatch()
    {   
        smartWatchFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/mainIcon.png")));
        setWatchFrame();
        linkIcons();
        setWatchPanel();
        placeWatch();
        placeComponents();
        setTimeLabelAndFocus();
        
        minimizeButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                smartWatchFrame.setState(Frame.ICONIFIED);
            }
        });
        
        addNoteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                if(!editorExists)
                {
                    editorExists = true;
                    Editor editor = new Editor(smartWatchPanel, icon);
                    Thread editorThread = new Thread(editor);
                    editorThread.setName("editorThread");
                    editorThread.start();
                    addNoteButton.setIcon(subtractIcon);
                }
            }
        });
        
        weatherButton.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                if(!tempExists)
                {
                    tempExists = true;
                    FetchTemperature fetchTemperature = new FetchTemperature(x, y);
                    Thread fetchTemperatureThread = new Thread(fetchTemperature);
                    fetchTemperatureThread.setName("fetchTemperatureThread");
                    fetchTemperatureThread.start();
                }
            }
        });
        
        closeButton.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                System.exit(0);
            }
        });
        
        searchButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                if(search.getText().length() > 0)
                    googleSearch();
            }
        });
        
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt)
            {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    if(search.getText().length() > 0)
                        googleSearch();
                }
            }
        });
    }
    
    /**
     * This method records all the search queries
     * made by the user and saves them in a file.
     * 
     * @param txt the searched string
     */
    private void storeSearch(final String txt)
    {
        Thread t = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                byte data[] = txt.getBytes();
                Path p = Paths.get("./querries.txt");
                try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) 
                {
                    out.write(data, 0, data.length);
                }
                catch(IOException ex)
                {
                    System.err.println(ex);
                }
            }
        });
        t.start();
    }
    
    /**
     * This method opens the browser and searches the 
     * required string.
     * 
     */
    private void googleSearch()
    {
        try
        {
            URL url = new URL("http://www.google.com");
            URLConnection conn = url.openConnection();
            conn.connect();
            String query = search.getText();
            storeSearch(query+"\n");
            query=query.replace(" ","+");
            speakOutMessage("searching google");
            Desktop.getDesktop().browse(URI.create("www.google.co.in/search?q="+query));
            search.setText("");
        }
        catch(IOException e)
        {
            displayErrorMessage("no internet connection!");
        }
    }
    
    @Override
    public void run()
    {
        int val = 0;
        while(true)
        {
            Date d = new Date();
            if(val == 0)
            {
                if(d.getHours() < 10)
                {
                    if(d.getMinutes() < 10)
                        timeDisplayLabel.setText("0"+d.getHours()+":0"+d.getMinutes());
                    else
                        timeDisplayLabel.setText("0"+d.getHours()+":"+d.getMinutes());
                }
                else
                {
                    if(d.getMinutes() < 10)
                        timeDisplayLabel.setText(d.getHours()+":0"+d.getMinutes());
                    else
                        timeDisplayLabel.setText(d.getHours()+":"+d.getMinutes());
                }
                timeDisplayLabel.setForeground(new Color(44, 117, 255));
            }
            else
            {
                timeDisplayLabel.setForeground(Color.BLACK);
            }
            val = 1 - val;
            try 
            {
                Thread.sleep(1000);
            } 
            catch(InterruptedException ex) 
            {
                displayErrorMessage("oops! something went wrong");
            }
        }
    }
    
    public static void main(String[] args) 
    {      
        SmartWatch smartWatch = new SmartWatch();
        smartWatch.createWatch();
        Thread runClock = new Thread(smartWatch);
        runClock.setName("runClock");
        runClock.start();
    }
}
