import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.Calendar;
import java.util.TimeZone;

public class Sun implements Mode {

    private RealTime realTime;
    private Calendar currTime;
    private Calendar[] sun; // 0: Sun Rise, 1: Sun set

    private String[] nation;
    private String[] nationTimeZone;
    private double[] nationLatitude;
    private double[] nationLongitude;

    private Location location;
    private SunriseSunsetCalculator calculatorSun;
    private int currNation;
    private int maxNation = 27;
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

        this.nation = new String[]{
                "MAR", "GHA", "ESP", "UK", "FRA", "NGR", "GER", "ITA", "GRE",
                "RSA", "UKR", "EGY", "KEN", "AF", "PAK", "IND", "THA", "CHN",
                "MAS", "KOR", "AUS", "JPN", "CAN", "USA", "MEX", "CHI", "BRA"
        };

        this.nationTimeZone = new String[]{
          "Etc/GMT", "Africa/Accra", "Europe/Madrid", "Europe/London", "Europe/Paris", "Etc/GMT+1", "Europe/Berlin",
          "Europe/Rome", "Europe/Athens", "Etc/GMT+2", "Etc/GMT+2", "Africa/Cairo", "Etc/GMT+3", "Asia/Kabul",
          "Etc/GMT+5", "Etc/GMT+5", "Asia/Bangkok", "Etc/GMT+8", "Asia/Kuala_Lumpur", "Asia/Seoul", "Australia/Canberra",
          "Asia/Tokyo", "Canada/Central", "Etc/GMT-5", "Mexico/General", "America/Santiago", "Etc/GMT-3"
        };

        this.nationLatitude = new double[]{
                33.977595, 5.61168, 40.421095, 51.480065, 48.855972, 9.073947, 52.517944, 41.9054, 37.98512,
                -25.753113, 50.457425, 30.045495, -1.285904, 34.560963, 33.688766, 28.615838, 13.767087, 39.906642,
                3.135744, 37.551079, -35.280708, 35.716626, 45.417095, 38.907882, 23.963784, -33.430989 ,-15.787272
        };

        this.nationLongitude = new double[]{
                -6.850382, -0.184533, -3.711156, -0.129337, 2.352544, 7.393677, 13.398693, 12.492016, 23.727004,
                28.224044, 30.517889, 31.235796, 36.827738, 69.207369, 73.047844, 77.209795, 100.506751, 116.408181,
                101.687675, 126.991333, 149.1298, 139.768531, -75.696552, -77.035156, -102.177707, -70.665665, -47.89328
        };

        this.currNation = 19;
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

    public void requestNextNation(){
        if(++this.currNation == this.maxNation)
            this.currNation = 0;
    }

    public void requestPrevNation(){
        if(--this.currNation == -1)
            this.currNation = this.maxNation - 1;
    }

    public void showSun(){

    }

    private void calculateSun(){
        this.location.setLocation(this.nationLatitude[this.currNation], this.nationLongitude[this.currNation]);
        this.calculatorSun = new SunriseSunsetCalculator(this.location, this.nationTimeZone[this.currNation]);
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
    public String[] getNation() { return nation; }
    public void setNation(String[] nation) { this.nation = nation; }
    public double[] getNationLatitude() { return nationLatitude; }
    public void setNationLatitude(double[] nationLatitude) { this.nationLatitude = nationLatitude; }
    public double[] getNationLongitude() { return nationLongitude; }
    public void setNationLongitude(double[] city_longitude) { this.nationLongitude = city_longitude; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public SunriseSunsetCalculator getCalculatorSun() { return calculatorSun; }
    public void setCalculatorSun(SunriseSunsetCalculator calculatorSun) { this.calculatorSun = calculatorSun; }
    public int getCurrNation() { return currNation; }
    public void setCurrNation ( int currNation){ this.currNation = currNation; }
    public int getMaxNation () { return maxNation; }
    public void setMaxNation (int maxNation){ this.maxNation = maxNation; }
    public int getCurrMode () { return currMode; }
    public void setCurrMode ( int currMode){ this.currMode = currMode; }
    public boolean isFlag () { return flag; }
    public void setFlag ( boolean flag){ this.flag = flag; }
}
