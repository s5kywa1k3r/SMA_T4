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
    public void requestNextCity() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);
        // Test requestNextCity at Last City
        sun.setCurrCity(sun.getMaxCity() - 1);
        sun.requestNextCity();
        sun.requestNextCity();
        assertEquals(1, sun.getCurrCity());
    }

    @Test
    public void requestPrevCity() {
        RealTime realTime = new RealTime();
        Sun sun = new Sun(realTime);
        // Test requestPrevCity at First City
        sun.setCurrCity(0);
        sun.requestPrevCity();
        sun.requestPrevCity();
        assertEquals(sun.getMaxCity() - 2, sun.getCurrCity());
    }
}