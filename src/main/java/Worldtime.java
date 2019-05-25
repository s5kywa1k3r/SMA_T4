import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Worldtime {

    private RealTime realTime;

    private Calendar worldTime;
    private Calendar currTime;

    // All City -> Nation
    private String[] nation;
    private String[] nationTimeZone;
    private String [] displayWTData;

    private int blink;
    private int currNation;
    private int maxNation = 27;

    // Constructors
    public Worldtime(){
        this.worldTime = null;
        this.realTime = null;
        this.currTime = null;

        this.nation = new String[]{
                "MAR", "GHA", "ESP", "UK", "FRA", "NGR", "GER", "ITA", "GRE",
                "RSA", "UKR", "EGY", "KEN", "AF", "PAK", "IND", "THA", "CHN",
                "MAS", "KOR", "AUS", "JPN", "CAN", "USA", "MEX", "CHI", "BRA"
        };

        this.nationTimeZone = new String[]{
                "Etc/GMT", "Africa/Accra", "Europe/Madrid", "Europe/London", "Europe/Paris", "Etc/GMT-1", "Europe/Berlin",
                "Europe/Rome", "Europe/Athens", "Etc/GMT-2", "Etc/GMT-3", "Africa/Cairo", "Etc/GMT-3", "Asia/Kabul",
                "Etc/GMT-5", "IST", "Asia/Bangkok", "Etc/GMT-8", "Asia/Kuala_Lumpur", "Asia/Seoul", "Australia/Canberra",
                "Asia/Tokyo", "Etc/GMT+4", "Etc/GMT+4", "Mexico/General", "America/Santiago", "Etc/GMT+3"
        };

        this.displayWTData = new String[9];
        this.currNation = 19; // Initial City => 19: Asia/Seoul
    }

    public Worldtime(RealTime realTime) {
        this();

        this.worldTime = Calendar.getInstance();
        this.worldTime.clear();

        this.realTime = realTime;
        this.currTime = realTime.requestRealTime();

        this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
        this.worldTime.set(
                this.currTime.get(Calendar.YEAR),
                this.currTime.get(Calendar.MONTH),
                this.currTime.get(Calendar.HOUR_OF_DAY),
                this.currTime.get(Calendar.MINUTE),
                this.currTime.get(Calendar.SECOND)
        );

        this.currNation = 19;
    }

    // [ModeDB] Methods
    // Load Data from ModeDB
    public Worldtime(RealTime realTime, ArrayList db){
        this(realTime);
        if(db != null){
            this.currNation = (int)db.get(0);
            this.worldTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[this.currNation]));
            this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
            this.worldTime.set(
                    this.currTime.get(Calendar.YEAR),
                    this.currTime.get(Calendar.MONTH),
                    this.currTime.get(Calendar.HOUR_OF_DAY),
                    this.currTime.get(Calendar.MINUTE),
                    this.currTime.get(Calendar.SECOND)
            );
        }
    }

    // Save Data to ModeDB
    public ArrayList getWorldtimeData(){
        ArrayList data = new ArrayList();

        data.add(this.currNation);

        return data;
    }

    // [Worldtime] System Methods
    public void nextNation() {
        if (++this.currNation == this.maxNation)
            this.currNation = 0;
    }

    public void prevNation() {
        if (--this.currNation == -1)
            this.currNation = this.maxNation - 1;
    }

    /* [Remove] public void changeSummerTime(){ } */

    public void realTimeTaskWorldtime() {
        this.currTime = this.realTime.requestRealTime();
        this.currTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[this.currNation]));
        this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
        this.worldTime.set(
                this.currTime.get(Calendar.YEAR),
                this.currTime.get(Calendar.MONTH),
                this.currTime.get(Calendar.DATE),
                this.currTime.get(Calendar.HOUR_OF_DAY),
                this.currTime.get(Calendar.MINUTE),
                this.currTime.get(Calendar.SECOND)
        );

        this.currTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[19]));
    }

    // [WatchGUI]
    // void -> String
    public String[] showWorldTime() {

        if(blink++ > 100) blink = 0;

        displayWTData[0] = this.worldTime.get(Calendar.YEAR) + "";
        displayWTData[1] = this.worldTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH).substring(0, 2);
        displayWTData[2] = (this.worldTime.get(Calendar.MONTH) < 9 ? "0" : "") + (this.worldTime.get(Calendar.MONTH)+1);
        displayWTData[3] = (this.worldTime.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + this.worldTime.get(Calendar.DAY_OF_MONTH);
        displayWTData[4] = (this.worldTime.get(Calendar.MINUTE) < 10 ? "0" : "")+this.worldTime.get(Calendar.MINUTE);
        displayWTData[5] = (this.worldTime.get(Calendar.SECOND) < 10 ? "0" : "")+this.worldTime.get(Calendar.SECOND);
        if(this.realTime.isIs24H()) {
            displayWTData[6] = (this.worldTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")+this.worldTime.get(Calendar.HOUR_OF_DAY);
            displayWTData[7] = "";
        }
        else {
            displayWTData[6] = (this.worldTime.get(Calendar.HOUR) < 10 ? "0" : "")+this.worldTime.get(Calendar.HOUR);
            displayWTData[7] = (this.worldTime.get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
        }
        displayWTData[8] = (this.nation[this.currNation]);
        return displayWTData;
    }

    // Getters and Setters for Unit Test
    //public RealTime getRealTime() { return realTime; }
    //public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public Calendar getWorldTime() { return worldTime; }
    //public void setWorldTime(Calendar worldTime) { this.worldTime = worldTime; }
    public Calendar getCurrTime() { return currTime; }
    //public void setCurrTime(Calendar currTime) { this.currTime = currTime; }
    //public String[] getNation() { return nation; }
    //public void setNation(String[] nation) { this.nation = nation; }
    //public String getNation(int i) { return nation[i]; }
    //public void setNation(int i, String nation) { this.nation[i] = nation; }
    //public String[] getNationTimeZone() { return nationTimeZone; }
    //public void setNationTimeZone(String[] nationTimeZone) { this.nationTimeZone = nationTimeZone; }
    public String getNationTimeZone(int i) { return nationTimeZone[i]; }
    //public void setNationTimeZone(int i, String nationTimeZone) { this.nationTimeZone[i] = nationTimeZone; }
    public int getCurrNation() { return currNation; }
    public void setCurrNation(int currNation) { this.currNation = currNation; }
    public int getMaxNation() { return maxNation; }
    //public void setMaxNation(int maxNation) { this.maxNation = maxNation; }
}
