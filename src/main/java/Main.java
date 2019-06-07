import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {

    /* [sonarqube][Class should define a constructor] */
    public Main(){}

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        WatchSystem watchSystem = new WatchSystem();
        TimeThread timeThread = new TimeThread(watchSystem);
        timeThread.run();
    }
}
