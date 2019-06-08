import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class ModeSetting {
    private WatchSystem sys;
    private ModeDB db;

    private ArrayList<String> menu_all;
    private ArrayList<String> prevMode;
    private ArrayList<String> newMode;
    private ArrayList<Object> prevModeObject;

    private String[] shortNameMode;
    private String[] displayModeData;

    private int currIndex;
    private int blink;

    public ModeSetting(){
        this.sys = null;
        this.db = new ModeDB();

        this.menu_all = new ArrayList<String>();
        this.prevMode = new ArrayList<String>();
        this.newMode = new ArrayList<String>();
        this.prevModeObject = new ArrayList();
        this.shortNameMode = new String[] {"SW", "TM", "AL", "WT", "SU", "TS"};
        this.displayModeData = new String[5];

        this.menu_all.add("Stopwatch");
        this.menu_all.add("Timer");
        this.menu_all.add("Alarm");
        this.menu_all.add("Worldtime");
        this.menu_all.add("Sun");
        this.menu_all.add("SettingTime");

        this.currIndex = 0;
    }

    public ModeSetting(WatchSystem sys) {
        this();
        this.sys = sys;
    }

    // [ModeSetting] System Methods
    public void requestModeSetting(){
        this.newMode.clear();
        this.prevMode.clear();
        this.prevModeObject.clear();
        for(int i = 2; i < this.sys.getMaxCnt() + 2; i++) {
            this.prevModeObject.add(this.sys.getMenu(i));
            if (this.menu_all.indexOf(this.sys.getMenu(i).getClass().getTypeName()) != -1)
                this.prevMode.add(this.sys.getMenu(i).getClass().getTypeName());
        }
    }

    public void requestNextMode() {
        if (++this.currIndex == 6)
            this.currIndex = 0;
        for (String temp : this.newMode) {
            if (this.menu_all.get(this.currIndex).equals(temp)) {
                this.requestNextMode();
                return;
            }
        }
    }

    /* [Remove] private void getUnselectedMode(){} */
    public void requestSelectMode() {
        if (this.newMode.size() == 4)
            this.newMode.remove(0);
        this.newMode.add(this.menu_all.get(this.currIndex));
        this.requestNextMode();
    }

    /* [Remove] private boolean hasNewMode(){} */
    /* [Remove] public Mode createNewMode(){} */
    /* [Remove] private void getUnselectedMode(){} */
    /* [Remove] private void sendOldMode(){} */
    /* [Remove] private void requestDeleteMode(){} */

    public ArrayList confirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList confirmMode = new ArrayList();
        for (String newMode : this.newMode) {

            boolean flag = false; // True: Match, False: Not Match

            // Find newMode from prevMode
            for (String oldMode : this.prevMode)
                if (newMode.equals(oldMode)) flag = true;

            /* [sonarqube][Expression can be simplified] */
            if (flag) {
                confirmMode.add(this.prevModeObject.get(this.prevMode.indexOf(newMode)));

                /* [sonarqube][NPath Complexity is 325] */
                //this.prevMode.remove(this.prevMode.indexOf(newMode));
                this.prevMode.set(this.prevMode.indexOf(newMode), null);
            }

            // Load Data from DB
            else {
                switch(newMode) { // 0: Stopwatch, 1: Timer, 2: Alarm, 3: Worldtime, 4: Sun, 5: SettingTime
                    case "Stopwatch":   confirmMode.add(new Stopwatch(db.loadData(this.menu_all.indexOf(newMode))));break;
                    case "Timer":       confirmMode.add(new Timer(db.loadData(this.menu_all.indexOf(newMode))));break;
                    case "Alarm":       confirmMode.add(new Alarm((RealTime)this.sys.getMenu(1),db.loadData(this.menu_all.indexOf(newMode))));break;
                    case "Worldtime":   confirmMode.add(new Worldtime((RealTime)this.sys.getMenu(1), db.loadData(this.menu_all.indexOf(newMode))));break;
                    case "Sun":         confirmMode.add(new Sun((RealTime)this.sys.getMenu(1),db.loadData(this.menu_all.indexOf(newMode))));break;
                    case "SettingTime": confirmMode.add(new SettingTime((RealTime)this.sys.getMenu(1)));break;
                    default: break;
                }
            }
        }

        // Save Data from not existed mode
        for (String oldMode : this.prevMode) {
            if(oldMode == null) continue;
            switch(oldMode) { // 0: Stopwatch, 1: Timer, 2: Alarm, 3: Worldtime, 4: Sun
                case "Stopwatch": this.db.saveData(0, ((Stopwatch)this.prevModeObject.get(this.prevMode.indexOf(oldMode))).getStopwatchData());break;
                case "Timer":     this.db.saveData(1, ((Timer)this.prevModeObject.get(this.prevMode.indexOf(oldMode))).getTimerData());break;
                case "Alarm":     this.db.saveData(2, ((Alarm)this.prevModeObject.get(this.prevMode.indexOf(oldMode))).getAlarmData());break;
                case "Worldtime": this.db.saveData(3, ((Worldtime)this.prevModeObject.get(this.prevMode.indexOf(oldMode))).getWorldtimeData());break;
                case "Sun":       this.db.saveData(4, ((Sun)this.prevModeObject.get(this.prevMode.indexOf(oldMode))).getSunData());break;
                default: break;
            }
        }

        return confirmMode;
    }


    // [WatchGUI]
    // String -> String []
    public String[] showModeSetting() {
        if(blink++ > 100)  blink = 0;

        for(int i = 0; i< 4; i++) {
            if(i < newMode.size()) {
                displayModeData[i] = shortNameMode[menu_all.indexOf(newMode.get(i))];
            }
            else if (blink > 50 && i == newMode.size()){
                displayModeData[i] = "  ";
            }
            else {
                displayModeData[i] = "__";
            }
        }
        displayModeData[4] = menu_all.get(currIndex);

        /* [sonarqube][Vuln #1] */
        return displayModeData.clone();
    }

    // Getters and Setters for Unit Test
    /* [sonarqube][Return a copy of] */
    public ArrayList<String> getNewMode() { return (ArrayList)newMode.clone(); }

    /* [sonarqube][Return a copy of] */
    public void setNewMode(ArrayList<String> newMode) { this.newMode = (ArrayList)newMode.clone(); }

    /* [sonarqube][Store a copy of] */
    //public void setOldMode(ArrayList oldMode) { this.prevModeObject = (ArrayList)oldMode.clone(); }

    /* [sonarqube][Store a copy of] */
    public ArrayList getPrevModeObject(){ return (ArrayList)this.prevModeObject.clone(); }
    public ModeDB getDb() { return db; }
    public void setDb(ModeDB db) { this.db = db; }
    public WatchSystem getSys() { return sys; }
    public void setSys(WatchSystem sys) { this.sys = sys; }
    public int getCurrIndex() { return currIndex; }
    public void setCurrIndex(int currIndex) { this.currIndex = currIndex; }
}

