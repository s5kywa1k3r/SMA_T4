import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        WatchSystem watchSystem = new WatchSystem();
        TimeThread timeThread = new TimeThread(watchSystem);
        timeThread.run();

        /*
        Calendar.YEAR // 1
        Calendar.MONTH // 2
        Calendar.DATE // 5
        Calendar.HOUR_OF_DAY // 11
        Calendar.MINUTE // 12
        Calendar.SECOND // 13
        Calendar.MILLISECOND // 14
        */

    }
}
