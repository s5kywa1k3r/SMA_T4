import java.util.Calendar;
import java.util.TimeZone;

public class Worldtime implements Mode{

    private RealTime realTime;
    private Calendar worldTime;
    private Calendar currTime;
    private String[] nation;
    private String[] nationTimeZone;
    private int currNation;
    private int maxNation = 27;

    public Worldtime(RealTime realTime){
        this.worldTime = Calendar.getInstance();
        this.worldTime.clear();

        this.realTime = realTime;
        this.currTime = realTime.requestRealTime();
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

        this.currNation = 19;

        this.worldTime.set(Calendar.MILLISECOND, this.currTime.get(Calendar.MILLISECOND));
        this.worldTime.set(
                this.currTime.get(Calendar.YEAR),
                this.currTime.get(Calendar.DATE),
                this.currTime.get(Calendar.HOUR_OF_DAY),
                this.currTime.get(Calendar.MINUTE),
                this.currTime.get(Calendar.SECOND)
        );
    }

    public void nextNation(){
        if(++this.currNation == this.maxNation)
            this.currNation = 0;
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[this.currNation]));
    }

    public void prevNation(){
        if(--this.currNation == -1)
            this.currNation = this.maxNation - 1;
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[this.currNation]));
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
        this.worldTime.setTimeZone(TimeZone.getTimeZone(this.nationTimeZone[this.currNation]));
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
    public String[] getNation() { return nation; }
    public void setNation(String[] nation) { this.nation = nation; }
    public int getCurrNation() { return currNation; }
    public void setCurrNation(int currNation) { this.currNation = currNation; }
    public int getMaxNation() { return maxNation; }
    public void setMaxNation(int maxNation) { this.maxNation = maxNation; }
}
