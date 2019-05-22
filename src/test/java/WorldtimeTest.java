import org.junit.Test;

import static org.junit.Assert.*;

public class WorldtimeTest {

    @Test
    public void nextNation() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
        // Test NextCity at Last City
        worldtime.setCurrNation(worldtime.getMaxNation() - 1);
        worldtime.nextNation(); // max-1 -> 0
        worldtime.nextNation(); // 0 -> 1

        assertEquals(1, worldtime.getCurrNation());
    }

    @Test
    public void prevNation() {
        RealTime realTime = new RealTime();
        Worldtime worldtime = new Worldtime(realTime);
        // Test Prev City at First City
        worldtime.setCurrNation(0);
        worldtime.prevNation(); // 0 -> max-1
        worldtime.prevNation(); // max-1 -> max-2

        assertEquals(worldtime.getMaxNation() - 2, worldtime.getCurrNation());
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