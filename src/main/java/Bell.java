import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bell extends Thread{
    private Clip clip;
    private int sec;
    private boolean isContinue = false;
    private AudioInputStream audioInputStream;

    public Bell(int index) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        // index is order of alarm
        audioInputStream = AudioSystem.getAudioInputStream(new File(Bell.class.getResource("").getPath()+"sounds/Alarm"+index+".wav"));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void play(int sec) {
        this.sec = sec;
        isContinue = true;
        this.run();
    }
    public void pause() {
        System.out.println("is Paused?");
        isContinue = false;
        clip.stop();
    }
    public void run() {
        long time = System.currentTimeMillis();
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        while(System.currentTimeMillis() - time < (1000 * sec) && isContinue) { }
    }
}