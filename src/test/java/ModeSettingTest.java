import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class ModeSettingTest {

    @Test
    public void requestNextMode() {
        ModeSetting ms = new ModeSetting();
        // 1: Max currIndex Test
        ms.setCurrIndex(5);
        ms.requestNextMode(); // 5 -> 0
        ms.requestNextMode(); // 0 -> 1

        assertEquals(1, ms.getCurrIndex());

        // 2: Ignore Selected Mode
        ArrayList<String> selectedMode = new ArrayList<String>();
        selectedMode.add("Timer");
        selectedMode.add("SettingTime");

        ms.setNewMode(selectedMode);
        ms.setCurrIndex(0); // 0: Stopwatch

        // if ms.requestNextMode then it may be 0-> (1) -> 2 -> 3 -> 4 -> (5) -> 0
        ms.requestNextMode(); // 0: Stopwatch -> (1: Timer) -> 2: Alarm
        assertEquals(2, ms.getCurrIndex());
        ms.requestNextMode(); // 2: Alarm -> 3: Worldtime
        assertEquals(3, ms.getCurrIndex());
        ms.requestNextMode(); // 3: Worldtime -> 4: Sun
        assertEquals(4, ms.getCurrIndex());
        ms.requestNextMode(); // 4: Sun -> (5: SettingTime) -> 0: Stopwatch
        assertEquals(0, ms.getCurrIndex());
    }

    @Test
    public void requestSelectMode() {
        ModeSetting ms = new ModeSetting();

        // Make newMode's size 4
        ArrayList<String> selectedMode = new ArrayList<String>();
        selectedMode.add("SettingTime");
        selectedMode.add("Stopwatch");
        selectedMode.add("Timer");
        selectedMode.add("Alarm");

        ms.setNewMode((ArrayList<String>)selectedMode.clone()); // Avoid Call-By-Reference

        ms.setCurrIndex(4); // 4: Sun
        ms.requestSelectMode();
        // {"SettingTime", "Stopwatch", "Timer, "Alarm"}
        //                  !=
        // {"Stopwatch", "Timer", "Alarm, "Sun"}
        assertNotEquals(selectedMode, ms.getNewMode());
    }

    @Test
    public void confirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ModeSetting ms = new ModeSetting();
        ms.setSys(new WatchSystem());
        ms.setOldMode(ms.getSys().getMenu());

        // Make newMode's size 4
        ArrayList<String> selectedMode = new ArrayList<String>();
        selectedMode.add("Timer");
        selectedMode.add("Stopwatch");
        selectedMode.add("SettingTime");

        ms.setNewMode((ArrayList<String>)selectedMode.clone()); // Avoid Call-By-Reference

        // {SettingTime, Timer, Stopwatch} -> {Timer, Stopwatch, SettingTime}
        ArrayList confirmMode = ms.confirmSelectMode();
        assertThat(confirmMode.get(0), instanceOf(Timer.class));
        assertThat(confirmMode.get(1), instanceOf(Stopwatch.class));
        assertThat(confirmMode.get(2), instanceOf(SettingTime.class));
    }
}