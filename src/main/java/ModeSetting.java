import java.util.ArrayList;

public class ModeSetting implements Mode {

    private ArrayList<String> menu_all;
    private ArrayList<String> currMode;
    private ArrayList<String> newMode;
    private ArrayList<Mode> oldMode;
    private int currIndex;

    public ModeSetting(ArrayList<Mode> currList){
        this.menu_all = new ArrayList<String>();
        this.menu_all.add("TimeSetting");
        this.menu_all.add("Stopwatch");
        this.menu_all.add("Timer");
        this.menu_all.add("Alarm");
        this.menu_all.add("Worldtime");
        this.menu_all.add("Sun");

        for(Mode menu : currList){
            switch(menu.getClass().getTypeName()) {
                case "TimeSetting":
                    this.currMode.add("TimeSetting");
                    break;

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

                default:
                    break;
            }
        }

        this.oldMode.addAll(currList);
        this.currIndex = 0;
    }

    public void requestModeSetting(){}
    public void requestNextMode(){
        if(++this.currIndex == 6)
            this.currIndex = 0;
    }
    public void getUnselectedMode(){}
    public boolean hasNewMode(){
        for(String temp : this.newMode)
            if(this.currMode.indexOf(temp) == -1)
                return false;
        return true;
    }
    public void createNewMode(){}
    //public Mode getUnselectedMode(){}
    public void sendOldMode(){}
    public void requestDeleteMode(){}
}
