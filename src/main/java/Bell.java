import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bell extends Thread{
    private Clip clip;
    private boolean isContinue = false;
    private int sec;
    private AudioInputStream audioInputStream;

    public Bell(int index) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        // index is order of alarm
        audioInputStream = AudioSystem.getAudioInputStream(new File(Bell.class.getResource("").getPath()+"sounds/Alarm"+index+".wav"));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void play() {
        this.run();
    }
    public void pause() {
        clip.stop();
    }
    public void run() {
        clip.start();
    }
}