public class TimeSetting implements Mode {

    private  RealTime realTime;

    public TimeSetting(){
        this.realTime = null;
    }
    public TimeSetting(RealTime realTime){ this.realTime = realTime; }

    public void requestPointNextTimeSection(){ this.realTime.nextSection(); }
    public void requestIncreaseTimeSection(){ this.realTime.increaseTime(); }
    public void requestDecreaseTimeSection(){ this.realTime.decreaseTime(); }
    public void requestResetSecond(){ this.realTime.setSecond(0); }
    public void realTimeTaskTimeSetting(){ System.out.println("[TimeSetting]"); }

    // Getters and Setters
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
}
