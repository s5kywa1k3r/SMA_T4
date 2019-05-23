import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bell {
    private Clip clip;
    private AudioInputStream audioInputStream;

    public Bell(int index) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        // index is order of alarm
        audioInputStream = AudioSystem.getAudioInputStream(new File(Bell.class.getResource("").getPath()+"sounds/Alarm"+index+".wav"));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        //clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play(int sec) {
        long time = System.currentTimeMillis();
        clip.start();
        while(System.currentTimeMillis() - time < (sec * 1000));
        pause();
    }
    public void pause() {
        clip.stop();
    }
}