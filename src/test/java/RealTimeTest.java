import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class RealTimeTest {

    @Test
    public void setSecond() {
        RealTime realTime = new RealTime();
        realTime.setSecond(31); // 0 -> 31
        assertEquals(31,realTime.requestRealTime().get(Calendar.SECOND));
        assertEquals(0, realTime.requestRealTime().get(Calendar.MILLISECOND));
    }

    @Test
    public void nextSection() {
        RealTime realTime = new RealTime();
        realTime.setCurrSection(5); // [currSection] 0: Second -> 5: Year / Last Section
        realTime.nextSection(); // [currSection] 5: Year -> 0: Second
        realTime.nextSection(); // [currSection] 0: Second -> 1: Minute
        assertEquals(1, realTime.getCurrSection()); // [currSection] 1: Minute
    }

    @Test
    public void increaseTime() {
        RealTime realTime = new RealTime();

        // 1: Max Minute Increase Test
        realTime.nextSection(); // 0: Second -> 1: Minute
        realTime.setRealTime(Calendar.MINUTE, 59);

        realTime.increaseTime(); // 59 -> 0
        realTime.increaseTime(); // 0 -> 1

        assertEquals(1, realTime.requestRealTime().get(Calendar.MINUTE));
        assertEquals(0, realTime.requestRealTime().get(Calendar.HOUR));

        // 2: Max Hour Increase Test
        realTime.nextSection(); // 1: Minute -> 2: HOUR
        realTime.setRealTime(Calendar.HOUR_OF_DAY, 23);

        realTime.increaseTime(); // 23 -> 0
        realTime.increaseTime(); // 0 -> 1

        assertEquals(1, realTime.requestRealTime().get(Calendar.HOUR));
        assertEquals(1, realTime.requestRealTime().get(Calendar.DATE));

        // 3: Max Date Increase Test
        realTime.nextSection(); // 2: HOUR -> 3: DATE
        realTime.setRealTime(Calendar.MONTH, Calendar.FEBRUARY);
        realTime.setRealTime(Calendar.DATE, realTime.requestRealTime().getLeastMaximum(Calendar.DATE));

        realTime.increaseTime(); // 28 -> 1
        realTime.increaseTime(); // 1 -> 2

        assertEquals(2, realTime.requestRealTime().get(Calendar.DATE));
        assertEquals(Calendar.FEBRUARY, realTime.requestRealTime().get(Calendar.MONTH));

        // 4: Max Month Increase Test
        realTime.nextSection(); // 3: DATE -> 4: MONTH
        realTime.setRealTime(Calendar.MONTH, Calendar.DECEMBER);

        realTime.increaseTime(); // 12 (DECEMBER) -> 1 (JANUARY)
        realTime.increaseTime(); // 1 (JANUARY) -> 2 (FEBRUARY)

        assertEquals(Calendar.FEBRUARY, realTime.requestRealTime().get(Calendar.MONTH));
        assertEquals(1970, realTime.requestRealTime().get(Calendar.YEAR));
    }

    @Test
    public void decreaseTime() {
        RealTime realTime = new RealTime();

        // 1: Min Minute Decrease Test
        realTime.nextSection(); // 0: Second -> 1: Minute
        realTime.setRealTime(Calendar.MINUTE, 0);

        realTime.decreaseTime(); // 0 -> 59
        realTime.decreaseTime(); // 59 -> 58

        assertEquals(58, realTime.requestRealTime().get(Calendar.MINUTE));
        assertEquals(0, realTime.requestRealTime().get(Calendar.HOUR_OF_DAY));

        // 2: Min Hour Decrease Test
        realTime.nextSection(); // 1: Minute -> 2: HOUR
        realTime.setRealTime(Calendar.HOUR_OF_DAY, 0);

        realTime.decreaseTime(); // 0 -> 23
        realTime.decreaseTime(); // 23 -> 22

        assertEquals(22, realTime.requestRealTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, realTime.requestRealTime().get(Calendar.DATE));

        // 3: Min Date Decrease Test
        realTime.nextSection(); // 2: HOUR -> 3: DATE
        realTime.setRealTime(Calendar.MONTH, Calendar.FEBRUARY);
        realTime.setRealTime(Calendar.DATE, 1);

        realTime.decreaseTime(); // 1 -> 28
        realTime.decreaseTime(); // 28 -> 27

        assertEquals(27, realTime.requestRealTime().get(Calendar.DATE));
        assertEquals(Calendar.FEBRUARY, realTime.requestRealTime().get(Calendar.MONTH));

        // 4: Min Month Decrease Test
        realTime.nextSection(); // 3: DATE -> 4: MONTH
        realTime.setRealTime(Calendar.MONTH, Calendar.JANUARY);

        realTime.decreaseTime(); // 1 (JANUARY) -> 12 (DECEMBER)
        realTime.decreaseTime(); // 12 (DECEMBER) -> 11 (NOVEMBER)

        assertEquals(Calendar.NOVEMBER, realTime.requestRealTime().get(Calendar.MONTH));
        assertEquals(1970, realTime.requestRealTime().get(Calendar.YEAR));

        // 5: Min Year Decrease Test
        realTime.nextSection(); // 4: MONTH -> 5: YEAR
        realTime.setRealTime(Calendar.YEAR, 1971);

        realTime.decreaseTime(); // 1971 -> 1970
        realTime.decreaseTime(); // 1970 -> 1970 => Not Changed

        assertEquals(1970, realTime.requestRealTime().get(Calendar.YEAR));
    }


    @Test
    public void calculateTime() {
        RealTime realTime = new RealTime();
        realTime.requestRealTime().set(2019, 5, 22, 15, 30, 20);
        for(int i = 0; i < 20; i++)
            realTime.calculateTime();

        assertEquals(10 * 20, realTime.requestRealTime().get(Calendar.MILLISECOND));
        assertEquals(20, realTime.requestRealTime().get(Calendar.SECOND));
        assertEquals(30, realTime.requestRealTime().get(Calendar.MINUTE));
        assertEquals(15, realTime.requestRealTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(22, realTime.requestRealTime().get(Calendar.DATE));
        assertEquals(5, realTime.requestRealTime().get(Calendar.MONTH));
        assertEquals(2019, realTime.requestRealTime().get(Calendar.YEAR));
    }

    /*
    @Test
    public void showRealTime() {
    }
    */
}