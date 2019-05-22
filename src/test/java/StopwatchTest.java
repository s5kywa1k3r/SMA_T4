import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class StopwatchTest {

    @Test
    public void realTimeTaskStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // 0: Stopped -> 1: Continued

        assertEquals(0, stp.getStpTime().get(Calendar.MILLISECOND));
        assertEquals(0, stp.getStpTime().get(Calendar.SECOND));
        assertEquals(0, stp.getStpTime().get(Calendar.MINUTE));
        assertEquals(0, stp.getStpTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, stp.getStpTime().get(Calendar.DATE));
        assertEquals(0, stp.getStpTime().get(Calendar.MONTH));
        assertEquals(1970, stp.getStpTime().get(Calendar.YEAR));
        assertEquals(-32400000, stp.getStpTime().getTimeInMillis());

        for(int i = 0; i < 10002; i++)
            stp.realTimeTaskStopwatch();

        assertEquals(-32400000 + 10 * 10002, stp.getStpTime().getTimeInMillis());
        assertEquals(20, stp.getStpTime().get(Calendar.MILLISECOND));
        assertEquals(40, stp.getStpTime().get(Calendar.SECOND));
        assertEquals(1, stp.getStpTime().get(Calendar.MINUTE));
    }

    @Test
    public void requestStartStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch();
        assertEquals(1, stp.getStatus()); // 0: Stopped -> 1: Continued
    }

    @Test
    public void requestStopStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch();
        stp.requestStopStopwatch();
        assertEquals(0, stp.getStatus());
    }

    @Test
    public void requestSplitStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // 0: Stopped -> 1: Continued

        for(int i = 0; i < 10002; i++)
            stp.realTimeTaskStopwatch();

        stp.requestSplitStopwatch();

        assertEquals(-32400000 + 10 * 10002, stp.getSplitTime().getTimeInMillis());
        assertEquals(20, stp.getSplitTime().get(Calendar.MILLISECOND));
        assertEquals(40, stp.getSplitTime().get(Calendar.SECOND));
        assertEquals(1, stp.getSplitTime().get(Calendar.MINUTE));


        for(int i = 0; i < 10002; i++)
            stp.realTimeTaskStopwatch();

        stp.requestStopStopwatch(); // 1: Continued -> 0: Stopped
        stp.requestSplitStopwatch(); // Not Changed

        assertEquals(-32400000 + 10 * 10002, stp.getSplitTime().getTimeInMillis());
        assertEquals(20, stp.getSplitTime().get(Calendar.MILLISECOND));
        assertEquals(40, stp.getSplitTime().get(Calendar.SECOND));
        assertEquals(1, stp.getSplitTime().get(Calendar.MINUTE));
    }

    @Test
    public void requestResetStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch();
        for(int i = 0; i < 10; i++)
            stp.realTimeTaskStopwatch();
        stp.requestStopStopwatch();
        stp.requestResetStopwatch();

        assertEquals(-32400000, stp.getStpTime().getTimeInMillis());
        assertEquals(-32400000, stp.getSplitTime().getTimeInMillis());
    }

    @Test
    public void showStopwatch() {
    }
}