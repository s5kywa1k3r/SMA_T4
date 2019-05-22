import java.util.Calendar;
import java.util.TimeZone;

public class Sun implements Mode {

    private RealTime realTime;
    private Calendar[] sun;
    private String[] city;
    private int currCity;
    private boolean currMode; // true: rise, false: set

    public Sun(){
        this.realTime = null;
        this.sun = new Calendar[2];
        for(Calendar cal : this.sun){
            cal = Calendar.getInstance();
            cal.clear();
        }

        this.city = TimeZone.getAvailableIDs();
        this.currCity = 0;
        this.currMode = true;
    }

    public void setRealTime(RealTime realTime){
        this.realTime = realTime;

    }

    public void realTimeTaskSun(){

    }

    public void requestSetRise(){
        this.currMode = !this.currMode;
    }

    public void requestNextCity(){

    }

    public void requestPrevCity(){

    }

    public void showSun(){

    }
    //private Calendar[] calculateSun(){}


}
