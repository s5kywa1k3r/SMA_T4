import java.util.Calendar;
import java.util.TimeZone;

public class Worldtime implements Mode{

    private RealTime realTime;
    private Calendar worldTime;
    private Calendar currTime;
    private String[] city;
    private int currCity;
    private int maxCity;

    public Worldtime(RealTime realTime){
        this.worldTime = Calendar.getInstance();
        this.worldTime.clear();

        this.realTime = realTime;
        this.currTime = realTime.requestRealTime();

        this.maxCity = 100;
        this.city = new String[this.maxCity];
        this.currCity = 0;

        this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
        this.worldTime.set(
                this.currTime.get(Calendar.YEAR),
                this.currTime.get(Calendar.DATE),
                this.currTime.get(Calendar.HOUR_OF_DAY),
                this.currTime.get(Calendar.MINUTE),
                this.currTime.get(Calendar.SECOND)
        );
    }

    public void nextCity(){
        if(++this.currCity == this.city.length)
            this.currCity = 0;
        //this.worldTime.setTimeZone(TimeZone.getTimeZone(this.city[this.currCity]));
    }

    public void prevCity(){
        if(--this.currCity < 0)
            this.currCity = this.city.length - 1;
        //this.worldTime.setTimeZone(TimeZone.getTimeZone(this.city[this.currCity]));
    }

    public void changeSummerTime(){

    }

    public void realTimeTaskWorldtime(){

        System.out.println("[Worldtime]");
        this.currTime = this.realTime.requestRealTime();
        this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
        this.worldTime.set(
                this.currTime.get(Calendar.YEAR),
                this.currTime.get(Calendar.DATE),
                this.currTime.get(Calendar.HOUR_OF_DAY),
                this.currTime.get(Calendar.MINUTE),
                this.currTime.get(Calendar.SECOND)
        );
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.city[this.currCity]));
    }

    public void showWorldTime(){

    }

    // Getters and Setters
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public Calendar getWorldTime() { return worldTime; }
    public void setWorldTime(Calendar worldTime) { this.worldTime = worldTime; }
    public Calendar getCurrTime() { return currTime; }
    public void setCurrTime(Calendar currTime) { this.currTime = currTime; }
    public String[] getCity() { return city; }
    public void setCity(String[] city) { this.city = city; }
    public int getCurrCity() { return currCity; }
    public void setCurrCity(int currCity) { this.currCity = currCity; }
    public int getMaxCity() { return maxCity; }
    public void setMaxCity(int maxCity) { this.maxCity = maxCity; }
}
