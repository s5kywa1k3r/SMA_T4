import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.Calendar;
import java.util.TimeZone;

public class Sun implements Mode {

    private RealTime realTime;
    private Calendar currTime;
    private Calendar[] sun; // 0: Sun Rise, 1: Sun set

    private String[] city;
    private double[] city_latitude;
    private double[] city_longitude;

    private Location location;
    private SunriseSunsetCalculator calculatorSun;
    private int currCity;
    private int maxCity = 100;
    private int currMode; // 0: Sun Rise, 1: Sun Set
    private boolean flag;



    public Sun(RealTime realTime) {
        this.realTime = realTime;
        this.sun = new Calendar[2];
        for (int i = 0; i < 2; i++) {
            this.sun[i] = Calendar.getInstance();
            this.sun[i].clear();
        }

        this.location = new Location("37.571303", "127.018495");
        this.calculatorSun = new SunriseSunsetCalculator(location, "Asia/Seoul");

        this.city = new String[this.maxCity];
        this.city_latitude = new double[this.maxCity];
        this.city_longitude = new double[this.maxCity];

        this.currCity = 0;
        this.currMode = 0;
    }

    public void realTimeTaskSun(){
        if(this.flag == true){
            if(this.currTime.getTimeInMillis() > this.sun[1].getTimeInMillis()){
                this.flag = false;
                this.calculateSun(); // Tomorrow's sunrise and sunset
            }
        }

        else{
            if(this.currTime.getTimeInMillis() > this.sun[0].getTimeInMillis()){
                this.flag = true;
                this.calculateSun(); // Today's sunset and tomorrow's sunrise
            }
        }
    }

    public void requestSetRise(){ this.currMode = this.currMode == 0 ? 1 : 0; }

    public void requestNextCity(){
        if(++this.currCity == this.maxCity)
            this.currCity = 0;
    }

    public void requestPrevCity(){
        if(--this.currCity == -1)
            this.currCity = this.maxCity - 1;
    }

    public void showSun(){

    }

    private void calculateSun(){
        this.location.setLocation(this.city_latitude[this.currCity], this.city_longitude[this.currCity]);
        this.calculatorSun = new SunriseSunsetCalculator(this.location, this.city[this.currCity]);
        // 0: Sun Rise, 1: Sun set
        // 0: if currTime is more than today's sunrise
        // print Today's sunset and Tomorrow's sunrise
        this.currTime.add(Calendar.DATE, 1);
        if(this.flag == true){
            // Tomorrow's sunrise
            this.sun[0] = this.calculatorSun.getOfficialSunriseCalendarForDate(this.currTime);
            // Today's sunset
            this.currTime.add(Calendar.DATE, -1);
            this.sun[1] = this.calculatorSun.getOfficialSunsetCalendarForDate(this.currTime);
        }

        // 1: if currTime is more than today's sunset
        // print Tomorrow's sun rise and sunset
        else{
            // Tomorrow's sunrise
            this.sun[0] = this.calculatorSun.getOfficialSunriseCalendarForDate(this.currTime);
            // Tomorrow's sunset
            this.sun[1] = this.calculatorSun.getOfficialSunsetCalendarForDate(this.currTime);
            this.currTime.add(Calendar.DATE, -1);
        }
    }

    // Getters and Setters
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public Calendar getCurrTime() { return currTime; }
    public void setCurrTime(Calendar currTime) { this.currTime = currTime; }
    public Calendar[] getSun() { return sun; }
    public void setSun(Calendar[] sun) { this.sun = sun; }
    public String[] getCity() { return city; }
    public void setCity(String[] city) { this.city = city; }
    public double[] getCity_latitude() { return city_latitude; }
    public void setCity_latitude(double[] city_latitude) { this.city_latitude = city_latitude; }
    public double[] getCity_longitude() { return city_longitude; }
    public void setCity_longitude(double[] city_longitude) { this.city_longitude = city_longitude; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public SunriseSunsetCalculator getCalculatorSun() { return calculatorSun; }


    public void setCalculatorSun(SunriseSunsetCalculator calculatorSun) { this.calculatorSun = calculatorSun; }
    public int getCurrCity() { return currCity; }
    public void setCurrCity ( int currCity){ this.currCity = currCity; }
    public int getMaxCity () { return maxCity; }
    public void setMaxCity (int maxCity){ this.maxCity = maxCity; }
    public int getCurrMode () { return currMode; }
    public void setCurrMode ( int currMode){ this.currMode = currMode; }
    public boolean isFlag () { return flag; }
    public void setFlag ( boolean flag){ this.flag = flag; }
}
