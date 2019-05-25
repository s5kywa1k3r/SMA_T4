import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        WatchSystem watchSystem = new WatchSystem();
        TimeThread timeThread = new TimeThread(watchSystem);
        timeThread.run();

    }
}
