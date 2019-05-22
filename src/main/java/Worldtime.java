import java.util.Calendar;
import java.util.TimeZone;

public class Worldtime implements Mode{

    private RealTime realTime;
    private Calendar worldTime;
    private Calendar currTime;
    private String[] city;
    private int currCity;

    public Worldtime(){
        this.worldTime = Calendar.getInstance();
        this.worldTime.clear();

        this.realTime = null;
        this.currTime = null;

        this.city = TimeZone.getAvailableIDs();
        this.currCity = 0;
    }

    public void setRealTime(RealTime realTime){
        this.realTime = realTime;
        this.currTime = this.realTime.requestRealTime();
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
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.city[this.currCity]));
    }

    public void prevCity(){
        if(--this.currCity < 0)
            this.currCity = this.city.length - 1;
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.city[this.currCity]));
    }

    public void changeSummerTime(){

    }

    public void realTimeTaskWorldtime(){
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

}
