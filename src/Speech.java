
import com.sun.speech.freetts.*;

/**
 * This class handles text to speech(tts) functionality.
 * 
 * @author Swayam
 */
public class Speech implements Runnable 
{
    private String textToSay;
    
    /**
     * This method finalizes what the output text would be.
     * 
     * @param txt it is the text that is to be spoken
     */
    public void setTextToSay(final String txt)
    {
        this.textToSay = txt;
    }
    
    /**
     * This method will return the text that is to be spoken.
     * 
     * @return text that will be spoken.
     */
    public String getTextToSay()
    {
        return this.textToSay;
    }
    
    /**
     * This method gets an instance of voice manager assigns a voice
     * and various properties related to the voice and speaks the 
     * desired text.
     * 
     */
    private void say()
    {
        Voice voice;
        System.setProperty("mbrola.base", "C:\\Users\\RAINA\\Downloads\\Compressed\\mbrola");
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice("mbrola_us1");
        voice.setPitch(200);
        voice.setRate(140);
        voice.allocate();
        voice.speak(getTextToSay());
    }
    
    @Override
    public void run()
    {
        say();
    }
}
