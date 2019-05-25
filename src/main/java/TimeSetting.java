public class TimeSetting {

    private RealTime realTime;
    private int blink;

    // Constructor
    public TimeSetting(RealTime realTime){ this.realTime = realTime; }

    // [TimeSetting] System Methods
    public void requestPointNextTimeSection(){ this.realTime.nextSection(); }
    public void requestIncreaseTimeSection(){ this.realTime.increaseTime(); }
    public void requestDecreaseTimeSection(){ this.realTime.decreaseTime(); }
    public void requestResetSecond(){ this.realTime.setSecond(0); }
    public void requestExitTimeSetting() { this.realTime.setCurrSection(0); }
    /* [Remove] public void realTimeTaskTimeSetting(){ }*/

    // [WatchGUI]
    // void -> String

    // Getters and Setters
    //public RealTime getRealTime() { return realTime; }
    //public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
    // 0: Second, 1: Minute, 2: Hour, 3: Day, 4: Month, 5: Year
    public String showTimeSetting() {
        if(blink++ > 100) blink = 0;
        if (blink > 50) return realTime.showRealTime() + (getCurrSection());
        else return realTime.showRealTime() + " ";
    }
}
