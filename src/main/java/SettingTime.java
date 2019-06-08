// Make Simplify
public class SettingTime {

    private RealTime realTime;
    private int blink;
    private String [] displaySettingTimeData;
    // Constructor
    public SettingTime(RealTime realTime){
        this.realTime = realTime;
    }

    // [SettingTime] System Methods
    public void requestPointNextTimeSection(){ this.realTime.nextSection(); }
    public void requestIncreaseTimeSection(){ this.realTime.increaseTime(); }
    public void requestDecreaseTimeSection(){ this.realTime.decreaseTime(); }
    public void requestResetSecond(){ this.realTime.setSecond(0); }
    public void requestExitSettingTime() { this.realTime.setCurrSection(0); }
    /* [Remove] public void realTimeTaskSettingTime(){ }*/

    // [WatchGUI]
    // String -> String []
    public String[] showSettingTime() {
        if(blink ++ > 100) blink = 0;
        if(blink > 50) {
            displaySettingTimeData =  realTime.showRealTime();
            switch (realTime.getCurrSection()) {
                case 0: displaySettingTimeData[5] = ""; break;
                case 1: displaySettingTimeData[4] = ""; break;
                case 2: displaySettingTimeData[6] = ""; break;
                case 3: displaySettingTimeData[3] = ""; break;
                case 4: displaySettingTimeData[2] = ""; break;
                case 5: displaySettingTimeData[0] = ""; break;
            }

            /* [sonarqube][Vuln #3] */
            return displaySettingTimeData.clone();
        }
        else return realTime.showRealTime();
    }

    // Getters and Setters
    //public RealTime getRealTime() { return realTime; }
    //public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
}
