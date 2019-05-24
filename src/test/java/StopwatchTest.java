import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class StopwatchTest {

    @Test
    public void realTimeTaskStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // [status] 0: Stopped -> 1: Continued

        Calendar temp = Calendar.getInstance();
        temp.clear();

        assertEquals(temp, stp.getStpTime());

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
        stp.requestStartStopwatch(); // [status] 0: Stopped -> 1: Continued
        assertEquals(1, stp.getStatus()); // [status] 1: Continued
    }

    @Test
    public void requestStopStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // [status] 0: Stopped -> 1: Continued
        stp.requestStopStopwatch(); // [status] 1: Continued -> 0: Stopped
        assertEquals(0, stp.getStatus()); // [status] 0: Stopped
    }

    @Test
    public void requestSplitStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // [status] 0: Stopped -> 1: Continued

        for(int i = 0; i < 10002; i++)
            stp.realTimeTaskStopwatch();

        stp.requestSplitStopwatch();

        assertEquals(-32400000 + 10 * 10002, stp.getSplitTime().getTimeInMillis());
        assertEquals(20, stp.getSplitTime().get(Calendar.MILLISECOND));
        assertEquals(40, stp.getSplitTime().get(Calendar.SECOND));
        assertEquals(1, stp.getSplitTime().get(Calendar.MINUTE));


        for(int i = 0; i < 10002; i++)
            stp.realTimeTaskStopwatch();

        stp.requestStopStopwatch(); // [status] 1: Continued -> 0: Stopped
        stp.requestSplitStopwatch(); // Not Changed

        assertNotEquals(-32400000 + 10 * 10002 + 10 * 10002, stp.getSplitTime().getTimeInMillis());
        assertEquals(20, stp.getSplitTime().get(Calendar.MILLISECOND));
        assertEquals(40, stp.getSplitTime().get(Calendar.SECOND));
        assertEquals(1, stp.getSplitTime().get(Calendar.MINUTE));
    }

    @Test
    public void requestResetStopwatch() {
        Stopwatch stp = new Stopwatch();
        stp.requestStartStopwatch(); // [status] 0: Stopped -> 1: Continued

        for(int i = 0; i < 10; i++)
            stp.realTimeTaskStopwatch();
        stp.requestStopStopwatch(); // [status] 1: Continued -> 0: Stopped
        stp.requestResetStopwatch();

        assertEquals(-32400000, stp.getStpTime().getTimeInMillis());
        assertEquals(-32400000, stp.getSplitTime().getTimeInMillis());
    }

    /*
    @Test
    public void showStopwatch() {
    }
    */
}