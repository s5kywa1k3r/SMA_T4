import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class SunTest {

    @Test
    public void realTimeTaskSun() {
        RealTime realTime = new RealTime();
        realTime.requestRealTime().set(2019, 5,23,03,59,30);
        Sun sun = new Sun(realTime);

        // Before realTimeTaskSun
        assertEquals(23, sun.getSun(0).get(Calendar.DATE)); // Today(23)'s Sun Rise
        assertEquals(23, sun.getSun(1).get(Calendar.DATE)); // Today(23)'s Sun Set

        realTime.requestRealTime().set(2019, 5,23,12,59,30);
        sun.realTimeTaskSun(); // [Changed] this.currTime.getTimeInMillis() >= this.sun[0].getTimeInMillis()
        assertEquals(24, sun.getSun(0).get(Calendar.DATE)); // Tomorrow(23->24)'s Sun Rise
        assertEquals(23, sun.getSun(1).get(Calendar.DATE)); // Today(23)'s Sun Set

        realTime.requestRealTime().set(2019, 5,23,20,59,30);
        sun.realTimeTaskSun(); // [Changed] this.currTime.getTimeInMillis() >= this.sun[1].getTimeInMillis()
        assertEquals(24, sun.getSun(0).get(Calendar.DATE)); // Tomorrow(24)'s Sun Rise
        assertEquals(24, sun.getSun(1).get(Calendar.DATE)); // Tomorrow(23 -> 24)'s Sun Set
    }

    @Test
    public void requestSetRise() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);

        assertEquals(0, sun.getCurrMode()); // [currMode] 0: Sun Rise
        sun.requestSetRise(); // [currMode] 0: Sun Rise -> 1: Sun Set
        assertEquals(1, sun.getCurrMode()); // [currMode] 1: Sun Set
        sun.requestSetRise(); // [currMode] 1: Sun Set -> 0: Sun Rise
        assertEquals(0, sun.getCurrMode()); // [currMode] 0: Sun Rise
    }

    @Test
    public void requestNextNation() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);

        // Test requestNextCity at Last City
        sun.setCurrNation(sun.getMaxNation() - 1); // [currNation] 19 -> max-1
        sun.requestNextNation(); // [currNation] max-1 -> 0
        sun.requestNextNation(); // [currNation] 0 -> 1

        assertEquals(1, sun.getCurrNation()); // [currNation] 1
    }

    @Test
    public void requestPrevNation() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);

        // Test requestPrevCity at First City
        sun.setCurrNation(0); // [currNation] 19 -> 0
        sun.requestPrevNation(); // [currNation] 0 -> max-1
        sun.requestPrevNation(); // [currNation] max-1 -> max-2

        assertEquals(sun.getMaxNation() - 2, sun.getCurrNation()); // [currNation] max-2
    }

    @Test
    public void initSun() {
        RealTime realTime = new RealTime();
        Sun sun = null;

        // 1. Today's sun rise and Today's sun set
        realTime.requestRealTime().set(2019, Calendar.MAY, 23, 03, 59, 30);
        sun = new Sun(realTime);

        assertEquals(23, sun.getSun(0).get(Calendar.DATE)); // Today(23)'s Sun Rise
        assertEquals(23, sun.getSun(1).get(Calendar.DATE)); // Today(23)'s Sun Set

        // 2. Tomorrow's sun rise and Today's sun set
        realTime.requestRealTime().set(2019, Calendar.MAY, 23, 12, 59, 30);
        sun = new Sun(realTime);

        assertEquals(24, sun.getSun(0).get(Calendar.DATE)); // Tomorrow(24)'s Sun Rise
        assertEquals(23, sun.getSun(1).get(Calendar.DATE)); // Today(23)'s Sun Set

        // 3. Tomorrow's sun rise and Tomorrow's sun set
        realTime.requestRealTime().set(2019, Calendar.MAY, 23, 20, 59, 30);
        sun = new Sun(realTime);

        assertEquals(24, sun.getSun(0).get(Calendar.DATE)); // Tomorrow(24)'s Sun Rise
        assertEquals(24, sun.getSun(1).get(Calendar.DATE)); // Tomorrow(24)'s Sun Set
    }
}