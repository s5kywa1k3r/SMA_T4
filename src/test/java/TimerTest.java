import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class TimerTest {


    @Test
    public void requestTimerTime() {
        Timer timer = new Timer();
        timer.requestTimerTime();
        assertEquals(2, timer.getStatus());
    }

    @Test
    public void requestNextTimerTimeSection() {
        Timer timer = new Timer();
        timer.setCurrSection(2); // Last Section
        timer.requestNextTimerTimeSection();
        timer.requestNextTimerTimeSection();
        assertEquals(1, timer.getCurrSection());
    }

    @Test
    public void requestIncreaseTimerTimeSection() {
        Timer timer = new Timer();

        // 1: Max Second Increase Test
        timer.getRsvTime().set(Calendar.SECOND, 59);

        timer.requestIncreaseTimerTimeSection(); // 59 -> 0
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(0, timer.getRsvTime().get(Calendar.MINUTE));

        // 2: Max Minute Increase Test
        timer.setCurrSection(1);
        timer.getRsvTime().set(Calendar.MINUTE, 59);

        timer.requestIncreaseTimerTimeSection(); // 59 -> 0
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(0, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 3: Max Minute Increase Test
        timer.setCurrSection(2);
        timer.getRsvTime().set(Calendar.HOUR_OF_DAY, 23);

        timer.requestIncreaseTimerTimeSection(); // 23 -> 24(0)
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(1, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, timer.getRsvTime().get(Calendar.DATE));
    }

    @Test
    public void requestDecreaseTimerTimeSection() {
        Timer timer = new Timer();
        Calendar testTime = Calendar.getInstance();
        testTime.set(1970, 1-1, 1, 22, 37, 0);
        testTime.set(Calendar.MILLISECOND, 0);
        timer.setRsvTime(testTime);

        // 1: Min Second Decrease Test
        timer.requestDecreaseTimerTimeSection(); // 0 -> 59
        timer.requestDecreaseTimerTimeSection(); // 59 -> 58

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(37, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 2: Min Minute Decrease Test
        timer.setCurrSection(1);
        timer.getRsvTime().set(Calendar.MINUTE, 0);
        timer.requestDecreaseTimerTimeSection(); // 0 -> 59
        timer.requestDecreaseTimerTimeSection(); // 59 -> 58

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 3: Max Minute Increase Test
        timer.setCurrSection(2);
        timer.getRsvTime().set(Calendar.HOUR_OF_DAY, 0);
        timer.requestDecreaseTimerTimeSection(); // 0 -> 23
        timer.requestDecreaseTimerTimeSection(); // 23 -> 22

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, timer.getRsvTime().get(Calendar.DATE));
    }

    @Test
    public void changeStatus() {
        Timer timer = new Timer();
        timer.changeStatus(1);
        assertEquals(1, timer.getStatus());

        timer.changeStatus(-1);
        assertEquals(1, timer.getStatus());

    }

    @Test
    public void requestResetTimer() {
        Timer timer = new Timer();
        Calendar testTime = Calendar.getInstance();
        testTime.set(1970, 1-1, 1, 22, 34, 30);
        testTime.set(Calendar.MILLISECOND, 0);

        timer.setRsvTime(testTime);
        timer.requestResetTimer();

        assertEquals(0, timer.getTimerTime().get(Calendar.MILLISECOND));
        assertEquals(30, timer.getTimerTime().get(Calendar.SECOND));
        assertEquals(34, timer.getTimerTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getTimerTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, timer.getTimerTime().get(Calendar.DATE));
        assertEquals(1-1, timer.getTimerTime().get(Calendar.MONTH));
        assertEquals(1970, timer.getTimerTime().get(Calendar.YEAR));

    }

    @Test
    public void ringOff() {
    }

    @Test
    public void realTimeTimerTask() {
        Timer timer = new Timer();
        timer.getRsvTime().set(1970, 1-1, 1, 10, 30, 12);
        timer.getRsvTime().set(Calendar.MILLISECOND, 0);

        timer.requestResetTimer();
        assertEquals(timer.getRsvTime().getTimeInMillis(), timer.getTimerTime().getTimeInMillis());

        timer.changeStatus(1); // [Status] 0: Stopped -> 1: Continued
        timer.realTimeTimerTask();
        assertEquals(timer.getRsvTime().getTimeInMillis() - 10, timer.getTimerTime().getTimeInMillis());

        while(timer.getTimerTime().getTimeInMillis() > -32400000)
            timer.realTimeTimerTask();

        assertEquals(0, timer.getStatus()); // Timer Expired
    }

    @Test
    public void showTimer() {
    }

    @Test
    public void requestExitSetTimerTime() {
        Timer timer = new Timer();

        timer.changeStatus(2); // [Status] 0: Stopped -> 2: Setting
        timer.requestNextTimerTimeSection(); // [CurrSection] 0: Second -> 1: Minute
        timer.requestExitSetTimerTime();

        assertEquals(0, timer.getStatus());
        assertEquals(0, timer.getCurrSection());
    }
}