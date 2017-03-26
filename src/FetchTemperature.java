
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is responsible for handling of weather
 * information to be displayed to the user.
 * It uses openweatherAPI and fetches temperature and
 * humidity values for the selected city.
 * 
 * @author Swayam
 */
public class FetchTemperature implements Runnable
{
    private final String apikey = "532d313d6a9ec4ea93eb89696983e369";   // 13cf259bf09bd6a170106b4fc2bb67c2
    private final String city = "faridabad";
    
    private final JDialog tempDialog;
    private final JPanel tempPanel;
    private final JLabel tempLabel;
    private final JLabel humidityLabel;
    private final JLabel cityLabel;
    private final int x;
    private final int y;
    
    /**
     * Parameterized constructor for FetchTemperature class
     * 
     * @param x x-coordinate where the info will be displayed
     * @param y y-coordinate where the info will be displayed
     */
    public FetchTemperature(final int x, final int y)
    {
        tempDialog = new JDialog();
        tempPanel = new JPanel(new GridBagLayout());
        tempLabel = new JLabel();
        humidityLabel = new JLabel();
        cityLabel = new JLabel();
        this.x = x;
        this.y = y - 200;
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
     * This method is responsible for giving advice 
     * to user according to the temperature of the 
     * city.
     * 
     * @param temperature temperature of the city
     */
    private void temperatureAdvice(final int temperature)
    {
        if(temperature >= 35)
        {
            speakOutMessage("it is really hot outside. you should avoid going out.");
        }
        else    
        {
            if(temperature < 15)
            {
                speakOutMessage("it is chilling outside. keep youself warm.");
            }
            else
            {
                if(temperature > 24 && temperature < 30)
                {
                    speakOutMessage("the weather is pleasent. you should probably go out for a walk");
                }
            }
        }
    }
    
    /**
     * This method is responsible for giving 
     * advice to the user about the humidity levels
     * in the city.
     * 
     * @param humidity humidity percentage in the city
     */
    private void humidityAdvice(final int humidity)
    {
        if(humidity > 60)
        {
            speakOutMessage("it is really humid, you should drink a glass of water.");
        }
    }
    
    /**
     * This method is responsible for setting
     * weather panel for other components to be added on.
     * 
     */
    private void setTempPanel()
    {
        tempDialog.add(tempPanel);
        tempPanel.add(tempLabel);
        tempPanel.add(cityLabel);
        tempPanel.add(humidityLabel);
        tempPanel.setBackground(Color.BLACK);
    }
    
    /**
     * This method is responsible for setting up
     * the weather frame.
     * 
     */
    private void setTempFrame()
    {
        tempDialog.setSize(400, 200);
        tempDialog.setLocation(x, y);
        tempDialog.setAlwaysOnTop(true);
        tempDialog.setUndecorated(true);
        tempDialog.setVisible(true);
    }
    
    /**
     * This method is responsible for setting
     * up various components onto the weather 
     * panel.
     * 
     */
    private void setLabels()
    {
        tempLabel.setForeground(Color.WHITE);
        humidityLabel.setForeground(Color.WHITE);
        cityLabel.setForeground(Color.WHITE);
        cityLabel.setText("Faridabad");
        cityLabel.setFont(new Font("Geoma Thin Demo",Font.PLAIN,40));
        humidityLabel.setFont(new Font("Geoma Thin Demo",Font.PLAIN,40));
        tempLabel.setFont(new Font("Geoma Thin Demo",Font.PLAIN,40));
    }
    
    @Override
    public void run()
    {
        setTempFrame();
        setTempPanel();
        setLabels();
        fetchTemp();
        
        try
        {
            Thread.sleep(5000);
            tempDialog.dispose();
            SmartWatch.updateTempExists();
        }
        catch(InterruptedException ex)
        {
            displayErrorMessage("something went wrong !");
        }
    }
    
    /**
     * This method sends a GET request to the server
     * fetches the temperature and humidity values 
     * and displays them to the user.
     * 
     */
    private void fetchTemp()
    {
        URL url;
        try 
        {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine = in.readLine();
            int cod = inputLine.indexOf("cod");
            if(Integer.parseInt(inputLine.substring(cod+5, cod+8)) == 200)
            {
                int startIndexOfTemperature = inputLine.indexOf("temp") + 6;
                int endIndexOfTemperature = inputLine.indexOf(".", startIndexOfTemperature);
                int temperature = Integer.parseInt(inputLine.substring(startIndexOfTemperature, endIndexOfTemperature)) - 273;
                tempLabel.setText(temperature + " °C");
                int startIndexOfHumidity = inputLine.indexOf("humidity") + 10;
                int endIndexOfHumidity = inputLine.indexOf(",", startIndexOfHumidity);
                int humidity = Integer.parseInt(inputLine.substring(startIndexOfHumidity, endIndexOfHumidity));
                humidityLabel.setText(humidity + "%");
                cityLabel.setText("    < >    ");
                temperatureAdvice(temperature);
                humidityAdvice(humidity);
            }
            else
            {
                displayErrorMessage("server busy !");
            }
        } 
        catch(MalformedURLException ex) 
        {
            displayErrorMessage("something went wrong !");
        } 
        catch (IOException ex) 
        {
            cityLabel.setText("    < >    ");
            displayErrorMessage("no internet connection !");
        }
    }
}
