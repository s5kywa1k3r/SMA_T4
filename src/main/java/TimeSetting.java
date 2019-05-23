public class TimeSetting {

    private RealTime realTime;
    private int flag;
    public TimeSetting(){
        this.realTime = null;
        this.flag = 0; // 0: Second, 1: another
    }
    public TimeSetting(RealTime realTime){
        this.realTime = realTime;
        this.flag = 0;
    }

    public void requestPointNextTimeSection(){
        if(this.realTime.getCurrSection() == 0) this.flag = 1;
        this.realTime.nextSection();
    }
    public void requestIncreaseTimeSection(){ this.realTime.increaseTime(); }
    public void requestDecreaseTimeSection(){ this.realTime.decreaseTime(); }
    public void requestResetSecond(){ this.realTime.setSecond(0); }
    public void requestExitTimeSetting() {
        this.realTime.setCurrSection(0);
        this.flag = 0;
    }
    public void realTimeTaskTimeSetting(){
        //System.out.println("[TimeSetting]");
    }

    public int requestTimeSettingFlag() { return this.flag; }
    // Getters and Setters
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
    public String showTimeSetting() {
        return realTime.showRealTime() + getCurrSection();
    }
}
