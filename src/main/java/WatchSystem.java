import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class WatchSystem {

    private ArrayList menu;
    private int currMode;
    private int maxCnt;
    private WatchGUI watchGUI;
    private int flag;
    public WatchSystem() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this.menu = new ArrayList(){};
        menu.add(new ModeSetting(this));
        menu.add(new RealTime());
        menu.add(new TimeSetting((RealTime)menu.get(1)));
        menu.add(new Stopwatch());
        menu.add(new Timer());
        menu.add(new Alarm((RealTime)menu.get(1)));
       // menu.add(new Worldtime((RealTime)menu.get(1)));
       // menu.add(new Sun((RealTime)menu.get(1)));
        this.currMode = 1; // [currMode] 0: Always RealTime
        this.maxCnt = 4;
        watchGUI = new WatchGUI(this);
        watchGUI.setMode(menu.get(1));
        watchGUI.designMode(true);
        this.flag = -1;
    }


    // Worked by thread
    public void realTimeTask() {
        if(this.currMode == 0){
            watchGUI.realtimeGUI(((ModeSetting) menu.get(0)).showModeSetting());
        }

        for (int i = 1; i < this.maxCnt + 1; i++) {
            Object menu = this.menu.get(i);
            switch (menu.getClass().getTypeName()) {
                case "RealTime":
                    ((RealTime) this.menu.get(1)).calculateTime();
                    if (this.currMode == i) watchGUI.realtimeGUI(((RealTime) menu).showRealTime());
                    break;

                case "TimeSetting":
                    ((TimeSetting) menu).realTimeTaskTimeSetting();
                    if (this.currMode == i) watchGUI.realtimeGUI(((TimeSetting) menu).showTimeSetting());
                    break;

                case "Stopwatch":
                    ((Stopwatch) menu).realTimeTaskStopwatch();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Stopwatch) menu).showStopwatch());
                    break;

                case "Timer":
                    ((Timer) menu).realTimeTimerTask();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Timer) menu).showTimer());
                    break;

                case "Alarm":
                    ((Alarm) menu).realTimeTaskAlarm();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Alarm) menu).showAlarm());
                    break;

                case "Worldtime":
                    ((Worldtime) menu).realTimeTaskWorldtime();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Worldtime) menu).showWorldTime());
                    break;

                case "Sun":
                    ((Sun) menu).realTimeTaskSun();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Sun) menu).showSun());
                    break;

                default:
                    System.out.println("{Exception}[WatchSystem][realTimeTask] NotValidModeException");
                    break;
            }
        }
    }

    public void pressChangeMode() {
        watchGUI.designMode(false);
        if(++this.currMode == this.maxCnt + 2)
            this.currMode = 1; // 1: RealTime
        System.out.println("this.currMode= " + this.currMode + ", " + this.menu.get(this.currMode).getClass().getTypeName());
        watchGUI.setMode(menu.get(this.currMode));
        watchGUI.designMode(true);
    }

    /*
    public void callNextMode(){
        if(++this.currMode == this.maxCnt)
            this.currMode = 0; // 0: RealTime
    }
    */
    // RealTime
    public void pressShowType() { ((RealTime)this.menu.get(1)).requestChangeType();}
    // Mode Setting
    public void enterModeSetting(){ this.currMode = 0; }
    public void pressNextMode(){ ((ModeSetting)this.menu.get(0)).requestNextMode(); }
    public void pressSelectMode(){ ((ModeSetting)this.menu.get(0)).requestSelectMode(); }
    public void confirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList newList = ((ModeSetting)this.menu.get(0)).confirmSelectMode();
        for(int i = 0; i < newList.size(); i++)
            this.menu.set(i, newList.get(i));
        this.exitSelectMode();
    }
    public void exitSelectMode(){ this.currMode = 1; }

    // Time Setting
    public void nextTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestPointNextTimeSection(); }
    public void increaseTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestIncreaseTimeSection(); }
    public void decreaseTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestDecreaseTimeSection(); }
    public void pressResetSecond(){ ((TimeSetting)this.menu.get(this.currMode)).requestResetSecond(); }
    public void exitTimeSetting(){ ((TimeSetting)this.menu.get(this.currMode)).requestExitTimeSetting(); }
    public int getTimeSettingFlag(){ return ((TimeSetting)this.menu.get(this.currMode)).getCurrSection(); }

    // Stopwatch
    public void pressStartStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestStartStopwatch(); }
    public void pressStopStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestStopStopwatch(); }
    public void pressSplitStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestSplitStopwatch(); }
    public void pressResetStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestResetStopwatch(); }
    public int getStopwatchFlag(){ return ((Stopwatch)this.menu.get(this.currMode)).requestStopwatchFlag(); }

    // Timer
    public void enterSetTimerTime(){ ((Timer)this.menu.get(this.currMode)).requestTimerTime(); }
    public void nextTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestNextTimerTimeSection(); }
    public void increaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestIncreaseTimerTimeSection(); }
    public void decreaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestDecreaseTimerTimeSection(); }
    public void exitSetTimerTime(){ ((Timer)this.menu.get(this.currMode)).requestExitSetTimerTime(); }
    public void pressStartTimer(){ ((Timer)this.menu.get(this.currMode)).changeStatus(1); }
    public void pressStopTimer(){ ((Timer)this.menu.get(this.currMode)).changeStatus(0); }
    public void pressResetTimer(){ ((Timer)this.menu.get(this.currMode)).requestResetTimer(); }
    public void pressStopRingingTimer(){ ((Timer)this.menu.get(this.currMode)).ringOff(); }
    public int getTimerFlag(){ return ((Timer)this.menu.get(this.currMode)).requestTimerFlag(); }

    // Alarm
    public void enterSetAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).requestSettingAlarm(); }
    public void nextAlarmTimeSection(){ ((Alarm)this.menu.get(this.currMode)).requestAlarmNextSection();}
    public void increaseAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).increaseSection(); }
    public void decreaseAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).decreaseSection(); }
    public void pressNextAlarm(){ ((Alarm)this.menu.get(this.currMode)).requestNextAlarm(); }
    public void pressStopRingingAlarm(){ ((Alarm)this.menu.get(this.currMode)).requestStopRinging(); }
    public void pressAlarmOnOff(){ ((Alarm)this.menu.get(this.currMode)).requestAlarmOnOff(); }
    public void exitSetAlarmSetting(){ ((Alarm)this.menu.get(this.currMode)).requestExitAlarmSetting(); }
    public int getAlarmFlag(){ return ((Alarm)this.menu.get(this.currMode)).requestAlarmFlag(); }
    public int getAlarmSection(){ return ((Alarm)this.menu.get(this.currMode)).getCurrSection(); }

    // Worldtime
    public void nextWorldtimeCity(){ ((Worldtime)this.menu.get(this.currMode)).nextNation(); }
    public void prevWorldtimeCity(){ ((Worldtime)this.menu.get(this.currMode)).prevNation(); }
    //public void pressSummerTime(){ ((Worldtime)this.menu.get(this.currMode)).changeSummerTime(); }

    // Sun
    public void pressSetRise(){ ((Sun)this.menu.get(this.currMode)).requestSetRise(); }
    public void nextSunCity(){ ((Sun)this.menu.get(this.currMode)).requestNextNation(); }
    public void prevSunCity(){ ((Sun)this.menu.get(this.currMode)).requestPrevNation(); }

    // Getters and Setters
    public ArrayList getMenu(){ return this.menu; }
    public Object getMenu(int i){ return this.menu.get(i); }
}
