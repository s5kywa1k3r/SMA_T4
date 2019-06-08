import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Sun {

    private RealTime realTime;
    private Calendar currTime;
    private Calendar[] sun; // 0: Sun Rise, 1: Sun Set

    // All City -> Nation
    private String[] nation;
    private String[] nationTimeZone;
    private String[] displaySunDisplay;
    private double[] nationLatitude;
    private double[] nationLongitude;

    private Location location;
    private SunriseSunsetCalculator calculatorSun;

    private int currNation;
    private int maxNation = 27;
    private int currMode; // 0: Sun Rise, 1: Sun Set

    // Constructors
    public Sun() {
        this.realTime = null;
        this.currTime = null;
        this.sun = new Calendar[2];
        for (int i = 0; i < 2; i++) {
            this.sun[i] = Calendar.getInstance();
            this.sun[i].clear();
        }

        /* [sonarqube][Array should contain trailing comma.] */
        this.nation = new String[]{
                "MAR", "GHA", "ESP", "UK", "FRA", "NGR", "GER", "ITA", "GRE",
                "RSA", "UKR", "EGY", "KEN", "AF", "PAK", "IND", "THA", "CHN",
                "MAS", "KOR", "AUS", "JPN", "CAN", "USA", "MEX", "CHI", "BRA",
        };

        /* [sonarqube][Array should contain trailing comma.] */
        this.nationTimeZone = new String[]{
                "Etc/GMT", "Africa/Accra", "Europe/Madrid", "Europe/London", "Europe/Paris", "Etc/GMT-1", "Europe/Berlin",
                "Europe/Rome", "Europe/Athens", "Etc/GMT-2", "Etc/GMT-3", "Africa/Cairo", "Etc/GMT-3", "Asia/Kabul",
                "Etc/GMT-5", "IST", "Asia/Bangkok", "Etc/GMT-8", "Asia/Kuala_Lumpur", "Asia/Seoul", "Australia/Canberra",
                "Asia/Tokyo", "Etc/GMT+4", "Etc/GMT+4", "Mexico/General", "America/Santiago", "Etc/GMT+3",
        };

        /* [sonarqube][Array should contain trailing comma.] */
        this.nationLatitude = new double[]{
                33.977595, 5.61168, 40.421095, 51.480065, 48.855972, 9.073947, 52.517944, 41.9054, 37.98512,
                -25.753113, 50.457425, 30.045495, -1.285904, 34.560963, 33.688766, 28.615838, 13.767087, 39.906642,
                3.135744, 37.551079, -35.280708, 35.716626, 45.417095, 38.907882, 19.435852, -33.430989 ,-15.787272,
        };

        /* [sonarqube][Array should contain trailing comma.] */
        this.nationLongitude = new double[]{
                -6.850382, -0.184533, -3.711156, -0.129337, 2.352544, 7.393677, 13.398693, 12.492016, 23.727004,
                28.224044, 30.517889, 31.235796, 36.827738, 69.207369, 73.047844, 77.209795, 100.506751, 116.408181,
                101.687675, 126.991333, 149.1298, 139.768531, -75.696552, -77.035156, -99.125796, -70.665665, -47.89328,
        };

        this.currNation = 19;
        this.location = new Location(this.nationLatitude[this.currNation], this.nationLongitude[this.currNation]);
        this.calculatorSun = new SunriseSunsetCalculator(this.location, this.nationTimeZone[this.currNation]);
        this.displaySunDisplay = new String[9];
        this.currMode = 0;
    }

    public Sun(RealTime realTime) {
        this();
        this.realTime = realTime;
        this.currTime = this.realTime.requestRealTime();
        this.initSun();
    }

    // [ModeDB] Methods
    // LoadData from ModeDB
    public Sun(RealTime realTime, ArrayList db){
        this(realTime);
        if(db != null){
            this.sun = (Calendar[])db.get(0);
            this.currNation = (int)db.get(1);
            this.currMode = (int)db.get(2);
        }
    }

    // Save Data to ModeDB
    public ArrayList getSunData(){
        ArrayList data = new ArrayList();

        data.add(this.sun);
        data.add(this.currNation);
        data.add(this.currMode);

        return data;
    }

    // [Sun] System Methods
    public void realTimeTaskSun(){
        this.currTime = this.realTime.requestRealTime();

        /* [sonarqube][Bug #4] */
        if(this.currTime.getTimeInMillis() >= this.getSun(1).getTimeInMillis()){
            // Tomorrow's Sun Set
            this.currTime.add(Calendar.DATE, 1);
            this.sun[1] = this.calculatorSun.getOfficialSunsetCalendarForDate(this.currTime);
            this.currTime.add(Calendar.DATE, -1);
        }

        else if(this.currTime.getTimeInMillis() >= this.sun[0].getTimeInMillis()){
            // Tomorrow's Sun Rise
            this.currTime.add(Calendar.DATE, 1);
            this.sun[0] = this.calculatorSun.getOfficialSunriseCalendarForDate(this.currTime);
            this.currTime.add(Calendar.DATE, -1);
        }

        /* [sonarqube]["if ... else if" constructs should end with "else" clauses.] */
        else{}
    }

    public void requestSetRise(){ this.currMode = this.currMode == 0 ? 1 : 0; }

    public void requestNextNation(){
        if(++this.currNation == this.maxNation)
            this.currNation = 0;
        this.initSun();
    }

    public void requestPrevNation(){
        if(--this.currNation == -1)
            this.currNation = this.maxNation - 1;
        this.initSun();
    }

    // Calculate sun at constructor, requestNextNation, requestPrevNation

    /* [sonarqube][Bug #10] */
    public final void initSun(){
        // Apply World Time Zone
        this.location.setLocation(this.nationLatitude[this.currNation], this.nationLongitude[this.currNation]);
        this.calculatorSun = new SunriseSunsetCalculator(this.location, this.nationTimeZone[this.currNation]);

        // Today's Sun Rise and Today's Sun Set
        this.sun[0] = this.calculatorSun.getOfficialSunriseCalendarForDate(this.currTime);
        this.sun[1] = this.calculatorSun.getOfficialSunsetCalendarForDate(this.currTime);

        /* [sonarqube][Bug #5] */
        if(this.currTime.getTimeInMillis() > this.getSun(0).getTimeInMillis()){
            // Tomorrow's Sun Rise
            this.currTime.add(Calendar.DATE, 1);
            this.sun[0] = this.calculatorSun.getOfficialSunriseCalendarForDate(this.currTime);
            this.currTime.add(Calendar.DATE, -1);
        }

        /* [sonarqube][Bug #5] */
        if(this.currTime.getTimeInMillis() > this.getSun(1).getTimeInMillis()){
            // Tomorrow's Sun Set
            this.currTime.add(Calendar.DATE, 1);
            this.sun[1] = this.calculatorSun.getOfficialSunsetCalendarForDate(this.currTime);
            this.currTime.add(Calendar.DATE, -1);
        }
     }

    // [WatchGUI]
    // String -> String[]
    public String[] showSun(){
        displaySunDisplay[0] = this.sun[currMode].get(Calendar.YEAR) + "";
        displaySunDisplay[1] = this.sun[currMode].getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH).substring(0, 2);
        displaySunDisplay[2] = (this.sun[currMode].get(Calendar.MONTH) < 9 ? "0" : "") + (this.sun[currMode].get(Calendar.MONTH)+1);
        displaySunDisplay[3] = (this.sun[currMode].get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + this.sun[currMode].get(Calendar.DAY_OF_MONTH);
        // 0 Rise, 1 Set
        displaySunDisplay[4] = currMode + "";
        if(realTime.isIs24H()) {
            displaySunDisplay[5] = "";
            displaySunDisplay[6] = (this.sun[currMode].get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.sun[currMode].get(Calendar.HOUR_OF_DAY);
        }
        else {
            displaySunDisplay[5] = (sun[currMode].get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
            displaySunDisplay[6] = (sun[currMode].get(Calendar.HOUR) < 10 ? "0" : "")+sun[currMode].get(Calendar.HOUR);
        }
        displaySunDisplay[7] = (this.sun[currMode].get(Calendar.MINUTE) < 10 ? "0" : "") + this.sun[currMode].get(Calendar.MINUTE);
        displaySunDisplay[8] = (this.nation[currNation])+ "";
        /* [sonarqube][Vuln #5] */
        return displaySunDisplay.clone();
    }

    // Getters and Setters
    //public RealTime getRealTime() { return realTime; }
    //public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    //public Calendar getCurrTime() { return currTime; }
    //public void setCurrTime(Calendar currTime) { this.currTime = currTime; }
    //public Calendar[] getSun() { return sun; }
    //public void setSun(Calendar[] sun) { this.sun = sun; }
    public Calendar getSun(int i) { return sun[i]; }
    //public void setSun(int i, Calendar sun) { this.sun[i] = sun; }
    //public String[] getNation() { return nation; }
    //public void setNation(String[] nation) { this.nation = nation; }
    //public double[] getNationLatitude() { return nationLatitude; }
    //public void setNationLatitude(double[] nationLatitude) { this.nationLatitude = nationLatitude; }
    //public double[] getNationLongitude() { return nationLongitude; }
    //public void setNationLongitude(double[] city_longitude) { this.nationLongitude = city_longitude; }
    //public Location getLocation() { return location; }
    //public void setLocation(Location location) { this.location = location; }
    //public SunriseSunsetCalculator getCalculatorSun() { return calculatorSun; }
    //public void setCalculatorSun(SunriseSunsetCalculator calculatorSun) { this.calculatorSun = calculatorSun; }
    public int getCurrNation() { return currNation; }
    public void setCurrNation ( int currNation){ this.currNation = currNation; }
    public int getMaxNation () { return maxNation; }
    //public void setMaxNation (int maxNation){ this.maxNation = maxNation; }
    public int getCurrMode () { return currMode; }
    //public void setCurrMode ( int currMode){ this.currMode = currMode; }
}
