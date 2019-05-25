import org.junit.Test;
import java.util.Calendar;

import static org.junit.Assert.*;

public class WorldtimeTest {

    @Test
    public void nextNation() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);

        // Test NextCity at Last City
        worldtime.setCurrNation(worldtime.getMaxNation() - 1); // [currNation] 19 -> max-1
        worldtime.nextNation(); // [currNation] max-1 -> 0
        worldtime.nextNation(); // [currNation] 0 -> 1

        assertEquals(1, worldtime.getCurrNation()); // [currNation] 1
    }

    @Test
    public void prevNation() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);

        // Test Prev City at First City
        worldtime.setCurrNation(0); // [currNation] 19 -> 0
        worldtime.prevNation(); // [currNation] 0 -> max-1
        worldtime.prevNation(); // [currNation] max-1 -> max-2

        assertEquals(worldtime.getMaxNation() - 2, worldtime.getCurrNation()); // [currNation] max-2
    }

    @Test
    public void realTimeTaskWorldtime() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
        realTime.requestRealTime().set(2019, Calendar.MAY,23,18,20,0); // Seoul(GMT+9)
        assertEquals(realTime.requestRealTime().getTimeZone().getID(),worldtime.getNationTimeZone(worldtime.getCurrNation())); // Seoul(GMT+9) == Seoul(GMT+9)
        worldtime.setCurrNation(4); // Seoul(GMT+9) -> Paris(GMT+2)
        worldtime.prevNation(); // Paris(GMT+2) -> London(GMT)

        // Increase Time
        for(int i = 0; i < 100; i++){
            realTime.calculateTime();
            worldtime.realTimeTaskWorldtime();
        }

        assertNotEquals(worldtime.getWorldTime().get(Calendar.HOUR_OF_DAY), worldtime.getCurrTime().get(Calendar.HOUR_OF_DAY)); // Seoul(GMT+9) != London(GMT)
        assertEquals(worldtime.getWorldTime().get(Calendar.MILLISECOND), worldtime.getCurrTime().get(Calendar.MILLISECOND)); // Seoul(GMT+9) == London(GMT)
        assertEquals(worldtime.getWorldTime().get(Calendar.SECOND), worldtime.getCurrTime().get(Calendar.SECOND)); // Seoul(GMT+9) == London(GMT)
        assertEquals(worldtime.getWorldTime().get(Calendar.MINUTE), worldtime.getCurrTime().get(Calendar.MINUTE)); // Seoul(GMT+9) == London(GMT)
    }

    /*
    @Test
    public void showWorldTime() { }
    */
}