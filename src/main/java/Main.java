<<<<<<< HEAD
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        WatchSystem watchSystem = new WatchSystem();
        TimeThread timeThread = new TimeThread(watchSystem);
        timeThread.run();

    }
}
=======
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        WatchSystem watchSystem = new WatchSystem();
        TimeThread timeThread = new TimeThread(watchSystem);
        timeThread.run();

    }
}
>>>>>>> 4438be0b9f34d8ba11db7f514eef8ef45ce44b52
