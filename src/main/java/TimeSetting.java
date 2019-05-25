public class TimeSetting {

    private RealTime realTime;
    private int blink;
    public TimeSetting(){
        this.realTime = null;
    }
    public TimeSetting(RealTime realTime){
        this.realTime = realTime;
    }

    public void requestPointNextTimeSection(){
        this.realTime.nextSection();
    }
    public void requestIncreaseTimeSection(){ this.realTime.increaseTime(); }
    public void requestDecreaseTimeSection(){ this.realTime.decreaseTime(); }
    public void requestResetSecond(){ this.realTime.setSecond(0); }
    public void requestExitTimeSetting() {
        this.realTime.setCurrSection(0);
    }
    public void realTimeTaskTimeSetting(){ }

    // Getters and Setters
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public int getCurrSection() { return this.realTime.getCurrSection(); }
    public String showTimeSetting() {
        String data = realTime.showRealTime();
        if(blink++ > 100) blink = 0;
        if (blink > 50) {
            switch (getCurrSection()) {
                case 0: data += 
            }
        }
        return data;
    }
}
