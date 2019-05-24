import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class ModeSetting {
    private WatchSystem sys;
    private ModeDB db;
    private RealTime realTime;

    private ArrayList<String> menu_all;
    private ArrayList<String> currMode;
    private ArrayList<String> newMode;
    private ArrayList oldMode;

    private String[] shortNameMode;

    private int currIndex;

    public ModeSetting(){
        this.sys = null;
        this.db = new ModeDB();

        this.menu_all = new ArrayList<String>();
        this.currMode = new ArrayList<String>();
        this.newMode = new ArrayList<String>();
        this.oldMode = new ArrayList();
        this.shortNameMode = new String[] {"SW", "TM", "AL", "WT", "SU", "TS"};

        this.menu_all.add("Stopwatch");
        this.menu_all.add("Timer");
        this.menu_all.add("Alarm");
        this.menu_all.add("Worldtime");
        this.menu_all.add("Sun");
        this.menu_all.add("TimeSetting");


        this.currIndex = 0;
    }

    public ModeSetting(WatchSystem sys) {
        this();
        this.sys = sys;
        ArrayList currList = this.sys.getMenu();
        for (Object menu : currList)
            if(this.menu_all.indexOf(menu.getClass().getTypeName()) != -1)
                this.currMode.add(menu.getClass().getTypeName());

        this.oldMode.addAll(currList);
    }

    public void requestNextMode() {
        if (++this.currIndex == 6)
            this.currIndex = 0;
        for (String temp : this.newMode)
            if (this.menu_all.get(this.currIndex).equals(temp)) {
                this.requestNextMode();
                return;
            }
    }

    public void requestSelectMode() {
        if (this.newMode.size() == 4)
            this.newMode.remove(0);
        this.newMode.add(this.menu_all.get(this.currIndex));
        for (String temp : this.newMode)
            if (this.menu_all.get(this.currIndex).equals(temp)) {
                this.requestNextMode();
                return;
            }
    }

    public ArrayList confirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList confirmMode = new ArrayList();
        for (String newMode : this.newMode) {
            boolean flag = false; // True: Match, False: Not Match
            for (String oldMode : this.currMode)
                if (oldMode.equals(newMode)) flag = true;

            if (flag == true) {
                confirmMode.add(this.oldMode.get(this.oldMode.indexOf(this.currMode.get(this.currMode.indexOf(newMode)))));
                this.currMode.set(this.currMode.indexOf(newMode), null);
            }

            else {
                switch(newMode) { // 0: Stopwatch, 1: Timer, 2: Alarm, 3: Worldtime, 4: Sun, 5: TimeSetting
                    case "Stopwatch":
                        confirmMode.add(new Stopwatch(db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Timer":
                        confirmMode.add(new Timer(db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Alarm":
                        confirmMode.add(new Alarm((RealTime)this.sys.getMenu(1),db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Worldtime":
                        confirmMode.add(new Worldtime((RealTime)this.sys.getMenu(1), db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Sun":
                        confirmMode.add(new Sun((RealTime)this.sys.getMenu(1),db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "TimeSetting":
                        confirmMode.add(new TimeSetting());
                        break;

                    default:
                        //System.out.println("{Exception}[WatchSystem][realTimeTask] NotValidModeException");
                        break;
                }
            }
        }


        for (String oldMode : this.currMode) {
            switch(oldMode) { // 0: Stopwatch, 1: Timer, 2: Alarm, 3: Worldtime, 4: Sun
                case "Stopwatch":
                    this.db.saveData(0, ((Stopwatch)this.oldMode.get(this.currMode.indexOf(oldMode))).getStopwatchData());
                    break;

                case "Timer":
                    this.db.saveData(1, ((Timer)this.oldMode.get(this.currMode.indexOf(oldMode))).getTimerData());
                    break;

                case "Alarm":
                    this.db.saveData(2, ((Alarm)this.oldMode.get(this.currMode.indexOf(oldMode))).getAlarmData());
                    break;

                case "Worldtime":
                    this.db.saveData(3, ((Worldtime)this.oldMode.get(this.currMode.indexOf(oldMode))).getWorldtimeData());
                    break;

                case "Sun":
                    this.db.saveData(4, ((Sun)this.oldMode.get(this.currMode.indexOf(oldMode))).getSunData());
                    break;

                default:
                    //System.out.println("{Exception}[WatchSystem][realTimeTask] NotValidModeException");
                    break;
            }
        }

        return confirmMode;
    }

    public String showModeSetting() {
        String data = "";
        for(int i = 0; i< newMode.size(); i++) {
            data += shortNameMode[menu_all.indexOf(newMode.get(i))];
        }
        for(int i = newMode.size(); i < 4; i++) {
            data += "__";
        }
        if(menu_all.get(currIndex).length() > 6) {
            data += menu_all.get(currIndex).substring(0, 6);
        }
        else if(menu_all.get(currIndex).length() < 4) {
            data += menu_all.get(currIndex);
            for(int i =0 ;  i <(6 - menu_all.get(currIndex).length()); i++) {
                data += " ";
            }
        }
        else data += menu_all.get(currIndex);
        return data;
    }

    public ArrayList exitSetMode(){ return oldMode; }

    // Getters and Setters
    public ArrayList<String> getMenu_all() { return menu_all; }
    public void setMenu_all(ArrayList<String> menu_all) { this.menu_all = menu_all; }
    public ArrayList<String> getCurrMode() { return currMode; }
    public void setCurrMode(ArrayList<String> currMode) { this.currMode = currMode; }
    public ArrayList<String> getNewMode() { return newMode; }
    public void setNewMode(ArrayList<String> newMode) { this.newMode = newMode; }
    public ArrayList getOldMode() { return oldMode; }
    public void setOldMode(ArrayList oldMode) { this.oldMode = oldMode; }
    public ModeDB getDb() { return db; }
    public void setDb(ModeDB db) { this.db = db; }
    public WatchSystem getSys() { return sys; }
    public void setSys(WatchSystem sys) { this.sys = sys; }
    public int getCurrIndex() { return currIndex; }
    public void setCurrIndex(int currIndex) { this.currIndex = currIndex; }

}

