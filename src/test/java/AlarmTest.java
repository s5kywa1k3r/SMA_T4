import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class AlarmTest {

    @Test
    public void realTimeTaskAlarm() {
    }

    @Test
    public void requestSettingAlarm() {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.requestSettingAlarm();
        assertEquals(1, alarm.getStatus());
    }

    @Test
    public void requestAlarmNextSection() {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        //alarm.req
    }

    @Test
    public void increaseSection() {
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

        // 3: Max Frequency_Minute Increase Test
        alarm.requestAlarmNextSection();
        alarm.getFrequency(alarm.getCurrAlarm()).set(Calendar.SECOND, 59);
        alarm.getFrequency(alarm.getCurrAlarm()).set(Calendar.MINUTE, 8);
        alarm.increaseSection();
        alarm.increaseSection();

        assertEquals(0, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MILLISECOND));
        assertEquals(1, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.SECOND));
        assertEquals(8, alarm.getFrequency(alarm.getCurrAlarm()).get(Calendar.MINUTE));

        // 4: Max Frequency_Hour Increase Test
        alarm.requestAlarmNextSection();

        // 5: Max Repeat Increase Test
        alarm.requestAlarmNextSection();
        alarm.setRepeat(alarm.getCurrAlarm(), 5);
        alarm.increaseSection();

        assertEquals(0, alarm.getRepeat(alarm.getCurrAlarm()));

        // 6: Max Bell Increase Test


    }

    @Test
    public void decreaseSection() {
    }

    @Test
    public void requestSettingBellAlarm() {
    }

    @Test
    public void requestAlarmPrevSection() {
    }

    @Test
    public void requestNextAlarm() {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.setCurrAlarm(3); // [currAlarm] Point last alarm
        alarm.requestNextAlarm(); // [currAlarm] 3 -> 0
        alarm.requestNextAlarm(); // [currAlarm] 0 -> 1
        assertEquals(1, alarm.getCurrAlarm());
    }

    @Test
    public void requestStopRinging() {
    }

    @Test
    public void requestAlarmOnOff() {
        RealTime realTime = new RealTime();
        Alarm alarm = new Alarm(realTime);
        alarm.requestAlarmOnOff();
        assertEquals(true, alarm.getAlarmCurrAlarmStatus());

        alarm.requestAlarmOnOff();
        assertEquals(false, alarm.getAlarmCurrAlarmStatus());
    }
}