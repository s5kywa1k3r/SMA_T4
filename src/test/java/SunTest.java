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
        //assertEquals(true, sun.isFlag()); // True
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

        assertEquals(0, sun.getCurrMode());
        sun.requestSetRise();
        assertEquals(1, sun.getCurrMode());
        sun.requestSetRise();
        assertEquals(0, sun.getCurrMode());
    }

    @Test
    public void requestNextNation() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);
        // Test requestNextCity at Last City
        sun.setCurrNation(sun.getMaxNation() - 1);
        sun.requestNextNation();
        sun.requestNextNation();
        assertEquals(1, sun.getCurrNation());
    }

    @Test
    public void requestPrevNation() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);
        // Test requestPrevCity at First City
        sun.setCurrNation(0);
        sun.requestPrevNation();
        sun.requestPrevNation();
        assertEquals(sun.getMaxNation() - 2, sun.getCurrNation());
    }
}