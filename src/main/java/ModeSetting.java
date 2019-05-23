import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class ModeSetting {

    private ArrayList<String> menu_all;
    private ArrayList<String> currMode;
    private ArrayList<String> newMode;
    private ArrayList oldMode;

    private ModeDB db;
    private WatchSystem sys;
    private int currIndex;

    public ModeSetting(WatchSystem sys, ArrayList currList) {
        this.sys = sys;
        this.db = new ModeDB();

        this.menu_all = new ArrayList<String>();
        this.menu_all.add("Stopwatch");
        this.menu_all.add("Timer");
        this.menu_all.add("Alarm");
        this.menu_all.add("Worldtime");
        this.menu_all.add("Sun");
        this.menu_all.add("TimeSetting");

        for (Object menu : currList) {
            switch (menu.getClass().getTypeName()) {

                case "Stopwatch":
                    this.currMode.add("Stopwatch");
                    break;

                case "Timer":
                    this.currMode.add("Timer");
                    break;

                case "Alarm":
                    this.currMode.add("Alarm");
                    break;

                case "Worldtime":
                    this.currMode.add("Worldtime");
                    break;

                case "Sun":
                    this.currMode.add("Sun");
                    break;


                case "TimeSetting":
                    this.currMode.add("TimeSetting");
                    break;

                default:
                    break;
            }
        }

        this.oldMode.addAll(currList);
        this.currIndex = 0;
    }

    //public void requestModeSetting(){}
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
    }
    //public void getUnselectedMode(){}
    /*
    public boolean hasNewMode(){
        for(String temp : this.newMode)
            if(this.currMode.indexOf(temp) == -1)
                return false;
        return true;
    }
    */

    //public void createNewMode(){}
    //public Mode getUnselectedMode(){}
    //public void sendOldMode(){}
    //public void requestDeleteMode(){}
    public ArrayList confirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList confirmMode = new ArrayList();
        for (String newMode : this.newMode) {
            boolean flag = false; // True: Match, False: Not Match
            for (String oldMode : this.currMode) {
                if (oldMode.equals(newMode)) flag = true;
            }

            if (flag == true) {
                confirmMode.add(this.oldMode.get(this.oldMode.indexOf(this.currMode.get(this.currMode.indexOf(newMode)))));
                this.currMode.remove(this.currMode.indexOf(newMode));
            }

            else {
                    //Load 하고 새로 만들어야지
                switch(newMode) {
                    case "TimeSetting":
                        confirmMode.add(new TimeSetting());
                        break;

                    case "Stopwatch":
                        confirmMode.add(new Stopwatch(db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Timer":
                        confirmMode.add(new Timer(db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Alarm":
                        confirmMode.add(new Alarm((RealTime)this.sys.getMenu(0),db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Worldtime":
                        confirmMode.add(new Worldtime((RealTime)this.sys.getMenu(0), db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    case "Sun":
                        confirmMode.add(new Sun((RealTime)this.sys.getMenu(0),db.loadData(this.menu_all.indexOf(newMode))));
                        break;

                    default:
                        //System.out.println("{Exception}[WatchSystem][realTimeTask] NotValidModeException");
                        break;
                }
            }
        }


        for (String oldMode : this.currMode) {
        //    db.saveData(int j, ArrayList arr);
        }

        return confirmMode;
    }
}

