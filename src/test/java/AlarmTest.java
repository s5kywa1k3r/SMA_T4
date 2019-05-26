import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class AlarmTest {

    /*
    @Test
    public void realTimeTaskAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException{

    }
    */

    @Test
    public void requestSettingAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.requestSettingAlarm();
        assertEquals(1, alarm.getStatus());
    }

    @Test
    public void requestAlarmNextSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);

        alarm.requestSettingAlarm();
        assertEquals(0, alarm.getCurrSection());    // 0 : Minute
        assertEquals(1, alarm.getStatus());         // Alarm Setting

        alarm.requestAlarmNextSection();
        assertEquals(1, alarm.getCurrSection());    // 1 : Hour

        alarm.requestAlarmNextSection();
        assertEquals(2, alarm.getCurrSection());    // 2 : Frequency_Second
        assertEquals(2, alarm.getStatus());         // Alarm Frequency

        alarm.requestAlarmNextSection();
        assertEquals(3, alarm.getCurrSection());    // 3 : Frequency_Minute

        alarm.requestAlarmNextSection();
        assertEquals(4, alarm.getCurrSection());    // 4 : Count

        alarm.requestAlarmNextSection();
        assertEquals(5, alarm.getCurrSection());    // 5 : Bell

        alarm.requestAlarmNextSection();
        assertEquals(0, alarm.getCurrSection());     // 0 : Minute
        assertEquals(1, alarm.getStatus());
    }

    @Test
    public void requestExitAlarmSetting()throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.setStatus(1);
        alarm.setCurrSection(2);
        assertEquals(1, alarm.getStatus());
        assertEquals(2, alarm.getCurrSection());

        alarm.requestExitAlarmSetting();
        assertEquals(0, alarm.getStatus());
        assertEquals(0, alarm.getCurrSection());
    }

    @Test
    public void increaseSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        assertEquals(0, alarm.getCurrSection()); // 0: Minutes

        alarm.requestSettingAlarm();
        alarm.increaseSection();
        assertEquals(1, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.MINUTE));

        // 1: Max Minute Increase Test
        alarm.getReservated(alarm.getCurrAlarm()).set(Calendar.MINUTE, 59);
        alarm.increaseSection();
        assertEquals(0, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.MINUTE));
        assertEquals(0, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.HOUR_OF_DAY));

        // 2: Max Hour Increase Test
        alarm.getReservated(alarm.getCurrAlarm()).set(Calendar.HOUR_OF_DAY, 23);
        alarm.getReservated(alarm.getCurrAlarm()).set(Calendar.MINUTE, 59);

        alarm.requestAlarmNextSection();
        alarm.increaseSection();

        assertEquals(0, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.HOUR_OF_DAY));
        assertEquals(59, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.MINUTE));
        assertEquals(1, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.DATE));

        // 3: Max Frequency_Second Increase Test
        alarm.requestAlarmNextSection();
        alarm.getFrequency(alarm.getCurrAlarm()).set(Calendar.SECOND, 59);
        alarm.getFrequency(alarm.getCurrAlarm()).set(Calendar.MINUTE, 8);
        alarm.increaseSection();
        alarm.increaseSection();

        assertEquals(0, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MILLISECOND));
        assertEquals(1, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.SECOND));
        assertEquals(8, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MINUTE));

        // 4: Max Frequency_Minute Increase Test
        alarm.requestAlarmNextSection();

        // 5: Max Repeat Increase Test
        alarm.requestAlarmNextSection();
        alarm.setRepeat(alarm.getCurrAlarm(), 5);
        alarm.increaseSection();

        assertEquals(0, alarm.getRepeat(alarm.getCurrAlarm()));

        // 6: Max Bell Increase Test
        alarm.requestAlarmNextSection();
        alarm.increaseSection();    // Alarm1.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.increaseSection();    // Alarm2.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.increaseSection();    // Alarm3.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.increaseSection();    // Alarm4.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.increaseSection();    // Alarm1.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();
    }

    @Test
    public void decreaseSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        assertEquals(0, alarm.getCurrSection()); // 0: Minutes

        // 1: Min Minute Decrease Test
        alarm.requestSettingAlarm();
        alarm.decreaseSection();
        assertEquals(59, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.MINUTE));
        assertEquals(0, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.HOUR_OF_DAY));

        // 2: Min Hour Decrease Test
        alarm.requestAlarmNextSection();
        alarm.decreaseSection();
        assertEquals(23, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.HOUR_OF_DAY));
        assertEquals(1, alarm.getReservated(alarm.getCurrAlarm()).get(Calendar.DATE));

        // 3: Min Frequency_Second Decrease Test
        alarm.requestAlarmNextSection();
        alarm.decreaseSection();
        assertEquals(59, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.SECOND));
        assertEquals(0, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MINUTE));

        // 4: Min Frequency_Minute Decrease Test
        alarm.requestAlarmNextSection();
        alarm.decreaseSection();
        assertEquals(9, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MINUTE));

        // 5: Min Repeat Decrease Test
        alarm.requestAlarmNextSection();
        alarm.decreaseSection();
        alarm.decreaseSection();
        assertEquals(5, alarm.getRepeat(alarm.getCurrAlarm()));

        // 6: Max Bell Increase Test
        alarm.requestAlarmNextSection();
        alarm.decreaseSection();    // Alarm4.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.decreaseSection();    // Alarm3.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.decreaseSection();    // Alarm2.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.decreaseSection();    // Alarm1.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();

        alarm.decreaseSection();    // Alarm4.wav
        alarm.getBell(alarm.getBellIndex()-1).play();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) {e.printStackTrace();}
        alarm.getBell(alarm.getBellIndex()-1).pause();
    }

    @Test
    public void requestNextAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.setCurrAlarm(3); // [currAlarm] Point last alarm
        alarm.requestNextAlarm(); // [currAlarm] 3 -> 0
        alarm.requestNextAlarm(); // [currAlarm] 0 -> 1
        assertEquals(1, alarm.getCurrAlarm());
    }

    @Test
    public void requestStopRinging() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        // Alarm Ringing Index not -1
        alarm.getBell(1).play();
        assertTrue(alarm.isRinging());
        // Alarm Ringing Index -1
        alarm.requestStopRinging();
        assertFalse(alarm.isRinging());
    }

    @Test
    public void requestAlarmOnOff() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.requestAlarmOnOff();
        assertTrue(alarm.getAlarmCurrAlarmStatus());

        alarm.requestAlarmOnOff();
        assertFalse(alarm.getAlarmCurrAlarmStatus());
    }

    @Test
    public void ringProperly() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        RealTime realTime = new RealTime();
        realTime.requestRealTime().set(2000, Calendar.JANUARY, 1, 1, 1, 30);
        Alarm alarm = new Alarm(realTime);
        Calendar tmp = Calendar.getInstance();
        Calendar tmpFre = Calendar.getInstance();
        tmp.clear();
        tmp.set(2000, Calendar.JANUARY, 1, 1, 1, 30);
        Calendar compare = (Calendar)tmp.clone();
        // Frequency is set by 1 min 30 sec
        tmpFre.set(1970, Calendar.JANUARY, 0, 0, 1, 30);
        // Reputation is 2
        alarm.setRepeat(0, 2);
        alarm.setFrequency(0, tmpFre);
        alarm.requestAlarmOnOff();
        alarm.setAlarm(tmp);

        assertEquals(realTime.requestRealTime().getTime(), alarm.getAlarm().getTime());

        // First Reputation
        for(int i = 0; i < 90; i++) {
            alarm.realTimeTaskAlarm();
            realTime.requestRealTime().add(Calendar.SECOND, 1);
        }
        assertEquals(realTime.requestRealTime().getTime(), alarm.getAlarm().getTime());

        // Second Reputation
        for(int i = 0 ; i<90; i++) {
            alarm.realTimeTaskAlarm();
            realTime.requestRealTime().add(Calendar.SECOND, 1);
        }
        assertEquals(realTime.requestRealTime().getTime(), alarm.getAlarm().getTime());

        // Reset To Init Alarm
        alarm.realTimeTaskAlarm();
        assertEquals(compare.getTime(), alarm.getAlarm().getTime());
    }
}