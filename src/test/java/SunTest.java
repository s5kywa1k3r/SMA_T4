import org.junit.Test;

import static org.junit.Assert.*;

public class SunTest {

    @Test
    public void realTimeTaskSun() {
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
    public void requestPrevCity() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);
        // Test requestPrevCity at First City
        sun.setCurrNation(0);
        sun.requestPrevNation();
        sun.requestPrevNation();
        assertEquals(sun.getMaxNation() - 2, sun.getCurrNation());
    }
}