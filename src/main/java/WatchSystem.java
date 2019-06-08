import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class WatchSystem {

    private static ArrayList menu;
    private static int currMode;
    private static int maxCnt;
    private static WatchGUI watchGUI;

    //private static WatchSystem instance;

    private static class Singleton{
        private static final WatchSystem instance = new WatchSystem();
    }

    public static WatchSystem getInstance(){
        return Singleton.instance;
    }

    public WatchSystem(){
        WatchSystem.menu = new ArrayList(){};

        menu.add(new ModeSetting());
        menu.add(new RealTime());
        menu.add(new SettingTime());
        menu.add(new Stopwatch());

        /* [sonarqube][Bug #9] */
        try{

            menu.add(new Timer());
            menu.add(new Alarm());
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }

        WatchSystem.currMode = 1; // [currMode] 1: Always RealTime
        this.maxCnt = 4;

        watchGUI = new WatchGUI(this);
        watchGUI.setMode(menu.get(1));
        watchGUI.designMode(true);
    }

    // [WatchSystem] System Method
    // Worked by thread
    public static void realTimeTask() {
        if(WatchSystem.currMode == 0)
            watchGUI.realtimeGUI(((ModeSetting) menu.get(0)).showModeSetting());

        for (int i = 1; i <= WatchSystem.maxCnt + 1; i++) {
            //if((menu = WatchSystem.menu.get(i)) == null) continue;

            try {
                Object menu = WatchSystem.menu.get(i);
                switch (menu.getClass().getTypeName()) {
                    case "RealTime":
                        ((RealTime) WatchSystem.menu.get(1)).calculateTime();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((RealTime) menu).showRealTime());
                        break;

                    case "SettingTime":
                        // ((SettingTime) menu).realTimeTaskSettingTime();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((SettingTime) menu).showSettingTime());
                        break;

                    case "Stopwatch":
                        ((Stopwatch) menu).realTimeTaskStopwatch();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((Stopwatch) menu).showStopwatch());
                        break;

                    case "Timer":
                        ((Timer) menu).realTimeTimerTask();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((Timer) menu).showTimer());
                        break;

                    case "Alarm":
                        ((Alarm) menu).realTimeTaskAlarm();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((Alarm) menu).showAlarm());
                        break;

                    case "Worldtime":
                        ((Worldtime) menu).realTimeTaskWorldtime();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((Worldtime) menu).showWorldTime());
                        break;

                    case "Sun":
                        ((Sun) menu).realTimeTaskSun();
                        if (WatchSystem.currMode == i) watchGUI.realtimeGUI(((Sun) menu).showSun());
                        break;

                    default:
                        break;
                }
            }
            catch(NullPointerException e){}
        }
    }

    public static void pressChangeMode() {
        watchGUI.designMode(false);
        if(++WatchSystem.currMode == WatchSystem.maxCnt + 2)
            WatchSystem.currMode = 1; // 1: RealTime
        watchGUI.setMode(menu.get(WatchSystem.currMode));
        watchGUI.designMode(true);
    }

    // RealTime
    public static void pressShowType() { ((RealTime)WatchSystem.menu.get(1)).requestChangeType();}

    // Mode Setting
    public static void enterModeSetting(){
        watchGUI.designMode(false);
        WatchSystem.currMode = 0;
        ((ModeSetting)WatchSystem.menu.get(0)).requestModeSetting();
        watchGUI.setMode(menu.get(WatchSystem.currMode));
        watchGUI.designMode(true);
    }

    public static void pressNextMode()  { ((ModeSetting)WatchSystem.menu.get(0)).requestNextMode(); }
    public static void pressSelectMode(){ ((ModeSetting)WatchSystem.menu.get(0)).requestSelectMode(); }
    public static void pressConfirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList newList = ((ModeSetting)WatchSystem.menu.get(0)).confirmSelectMode();
        WatchSystem.maxCnt = 0;
        for(int i = 0; i < 4; i++) {
            try{
                WatchSystem.maxCnt++;
                WatchSystem.menu.set(i+2, newList.get(i));
            }
            /* [sonarqube][Catching 'Exception' is not allowed.] */
            catch(IndexOutOfBoundsException e){
                WatchSystem.maxCnt--;
                WatchSystem.menu.set(i+2, null);
            }
        }
        WatchSystem.pressChangeMode();
    }

    // Setting Time
    public static void nextTimeSection()    { ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).requestPointNextTimeSection(); }
    public static void increaseTimeSection(){ ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).requestIncreaseTimeSection(); }
    public static void decreaseTimeSection(){ ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).requestDecreaseTimeSection(); }
    public static void pressResetSecond()   { ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).requestResetSecond(); }
    public static void exitSettingTime()    { ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).requestExitSettingTime(); }
    public static int  getSettingTimeFlag() { return ((SettingTime)WatchSystem.menu.get(WatchSystem.currMode)).getCurrSection(); }

    // Stopwatch
    public static void pressStartStopwatch(){ ((Stopwatch)WatchSystem.menu.get(WatchSystem.currMode)).requestStartStopwatch(); }
    public static void pressStopStopwatch() { ((Stopwatch)WatchSystem.menu.get(WatchSystem.currMode)).requestStopStopwatch(); }
    public static void pressSplitStopwatch(){ ((Stopwatch)WatchSystem.menu.get(WatchSystem.currMode)).requestSplitStopwatch(); }
    public static void pressResetStopwatch(){ ((Stopwatch)WatchSystem.menu.get(WatchSystem.currMode)).requestResetStopwatch(); }
    public static int  getStopwatchFlag()   { return ((Stopwatch)WatchSystem.menu.get(WatchSystem.currMode)).requestStopwatchFlag(); }

    // Timer
    public static void enterSetTimerTime()       { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestTimerTime(); }
    public static void nextTimerTimeSection()    { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestNextTimerTimeSection(); }
    public static void increaseTimerTimeSection(){ ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestIncreaseTimerTimeSection(); }
    public static void decreaseTimerTimeSection(){ ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestDecreaseTimerTimeSection(); }
    public static void exitSetTimerTime()        { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestExitSetTimerTime(); }
    public static void pressStartTimer()         { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).changeStatus(1); }
    public static void pressStopTimer()          { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).changeStatus(0); }
    public static void pressResetTimer()         { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestResetTimer(); }
    public static void pressStopRingingTimer()   { ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).ringOff(); }
    public static int  getTimerFlag()            { return ((Timer)WatchSystem.menu.get(WatchSystem.currMode)).requestTimerFlag(); }

    // Alarm
    public static void enterSetAlarmTime()    { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestSettingAlarm(); }
    public static void nextAlarmTimeSection() { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestAlarmNextSection();}
    public static void increaseAlarmTime()    { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).increaseSection(); }
    public static void decreaseAlarmTime()    { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).decreaseSection(); }
    public static void pressNextAlarm()       { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestNextAlarm(); }
    public static void pressStopRingingAlarm(){ ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestStopRinging(); }
    public static void pressAlarmOnOff()      { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestAlarmOnOff(); }
    public static void exitSetAlarmSetting()  { ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestExitAlarmSetting(); }
    public static int  getAlarmFlag()         { return ((Alarm)WatchSystem.menu.get(WatchSystem.currMode)).requestAlarmFlag(); }

    // Worldtime
    public static void nextWorldtimeNation(){ ((Worldtime)WatchSystem.menu.get(WatchSystem.currMode)).nextNation(); }
    public static void prevWorldtimeNation(){ ((Worldtime)WatchSystem.menu.get(WatchSystem.currMode)).prevNation(); }
    /* [Remove] public void pressSummerTime(){} */

    // Sun
    public static void pressSetRise() { ((Sun)WatchSystem.menu.get(WatchSystem.currMode)).requestSetRise(); }
    public static void nextSunNation(){ ((Sun)WatchSystem.menu.get(WatchSystem.currMode)).requestNextNation(); }
    public static void prevSunNation(){ ((Sun)WatchSystem.menu.get(WatchSystem.currMode)).requestPrevNation(); }

    // Getters and Setters
    public static ArrayList getMenu(){ return WatchSystem.menu; }
    public static Object getMenu(int i){ return WatchSystem.menu.get(i); }
    public static void setMenu(int i, Object o){ WatchSystem.menu.set(i, o); }
    public static int getMaxCnt(){ return WatchSystem.maxCnt; }
}
