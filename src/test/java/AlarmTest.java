import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class AlarmTest {

    @Test
    public void realTimeTaskAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException{

    }

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
        alarm.increaseSection();    // Alarm2.wav
        alarm.increaseSection();    // Alarm3.wav
        alarm.increaseSection();    // Alarm4.wav
        alarm.increaseSection();    // Alarm5.wav
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
        alarm.decreaseSection();    // Alarm3.wav
        alarm.decreaseSection();    // Alarm2.wav
        alarm.decreaseSection();    // Alarm1.wav
        alarm.decreaseSection();    // Alarm4.wav
    }

    /*@Test
    public void requestSettingBellAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    }

    @Test
    public void requestAlarmPrevSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    }*/

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
        alarm.getBell(1).play(5);
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
}