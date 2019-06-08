import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bell extends Thread{
    private Clip clip;
    private AudioInputStream audioInputStream;

    public Bell(int index) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        // index is order of alarm
        String fileName = "/sounds/Alarm" + index + ".wav";
        InputStream audioSrc = getClass().getResourceAsStream(fileName);
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        try{
            // Make Jar File
            audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        }catch(Exception e){
            e.printStackTrace();
            /* [sonarqube][Vuln #14] */
            // IDE Test
            /*
            System.out.println(getClass().getResource("").getPath()+"sounds/Alarm"+index+".wav");
            audioInputStream = AudioSystem.getAudioInputStream(new File(getClass().getResource("").getPath()+"sounds/Alarm"+index+".wav"));
            */
        }
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public void play() {
        clip.loop(1);
        //this.run();

        /* [sonarqube][Bug #7] */
        if(this.getState() == State.NEW)
            this.start();
    }

    public void pause() {
        clip.stop();
    }
    public void run() { clip.start(); }
}