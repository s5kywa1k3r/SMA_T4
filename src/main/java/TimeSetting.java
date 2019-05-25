public class TimeSetting {

    private RealTime realTime;

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
    public String showTimeSetting() { return realTime.showRealTime() + this.getCurrSection(); }

    // Getters and Setters
    //public RealTime getRealTime() { return realTime; }
    //public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
}
