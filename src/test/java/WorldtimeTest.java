import org.junit.Test;

import static org.junit.Assert.*;

public class WorldtimeTest {

    @Test
    public void nextCity() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
        // Test NextCity at Last City
        worldtime.setCurrCity(worldtime.getMaxCity() - 1);
        worldtime.nextCity(); // max-1 -> 0
        worldtime.nextCity(); // 0 -> 1

        assertEquals(1, worldtime.getCurrCity());
    }

    @Test
    public void prevCity() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
        // Test Prev City at First City
        worldtime.setCurrCity(0);
        worldtime.prevCity(); // 0 -> max-1
        worldtime.prevCity(); // max-1 -> max-2

        assertEquals(worldtime.getMaxCity() - 2, worldtime.getCurrCity());
    }

    @Test
    public void changeSummerTime() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
    }

    @Test
    public void realTimeTaskWorldtime() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
    }

    @Test
    public void showWorldTime() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
    }
}