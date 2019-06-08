/* RealTime Class */

import java.util.Calendar;
import java.util.Locale;

public class RealTime {
    private Calendar realTime; // day, month, year

    private String[] displayRealTimeData;

    private int currSection;
    private boolean is24H;

    // Constructor
    public RealTime(){
        this.realTime = Calendar.getInstance();
        // Calendar Month start 0 => 0: January, 1: February, 2: March, ... , 11: December
        displayRealTimeData = new String[8];
        this.realTime.clear(); // Initialized to 1970. 1. 1 00:00:00.000
        this.currSection = 0; // 0: Second, 1: Minute, 2: Hour, 3: Day, 4: Month, 5: Year
        this.is24H = true;
    }

    // [RealTime] System Methods
    public Calendar requestRealTime(){ return this.realTime; }

    public void setSecond(int i){
        this.realTime.set(Calendar.MILLISECOND, 0);
        this.realTime.set(Calendar.SECOND, i);
    }

    public void nextSection(){
        if(++this.currSection == 6) // Over value
            this.currSection = 0; // 0: Second
    }

    public void increaseTime(){
        int[] section = new int[]{-1, Calendar.MINUTE,
                Calendar.HOUR_OF_DAY, Calendar.DATE, Calendar.YEAR,};
        switch(this.currSection){
            case 1 : // Minute
                this.realTime.add(Calendar.MINUTE, 1);
                if(this.realTime.get(Calendar.MINUTE) == 0)
                    this.realTime.add(Calendar.HOUR_OF_DAY, -1);
                break;

            case 2: // Hour
                this.realTime.add(Calendar.HOUR_OF_DAY, 1);
                if(this.realTime.get(Calendar.HOUR_OF_DAY) == 0)
                    this.realTime.add(Calendar.DATE, -1);
                break;

            case 3 : // Day
                this.realTime.add(Calendar.DATE, 1);
                if(this.realTime.get(Calendar.DATE) == 1)
                    this.realTime.add(Calendar.MONTH, -1);
                break;

            case 4 : // Month
                this.realTime.add(Calendar.MONTH, 1);
                if(this.realTime.get(Calendar.MONTH) == Calendar.JANUARY)
                    this.realTime.add(Calendar.YEAR, -1);
                break;

            case 5 : // Year
                this.realTime.add(Calendar.YEAR, 1);
                if(this.realTime.get(Calendar.YEAR) == 10000)
                    this.realTime.set(Calendar.YEAR, 1970);
                break;

            default:
                break;
        }

    }

    public void decreaseTime(){
        switch(this.currSection) {
            case 1: // Minute
                this.realTime.add(Calendar.MINUTE, -1);
                if(this.realTime.get(Calendar.MINUTE) == 59)
                    this.realTime.add(Calendar.HOUR_OF_DAY, 1);
                break;

            case 2: // Hour
                this.realTime.add(Calendar.HOUR_OF_DAY, -1);
                if(this.realTime.get(Calendar.HOUR_OF_DAY) == 23)
                    this.realTime.add(Calendar.DATE, 1);
                break;

            case 3: // Day
                int tempMonth = this.realTime.get(Calendar.MONTH);
                this.realTime.add(Calendar.DATE, -1);
                if(this.realTime.get(Calendar.MONTH) != tempMonth)
                    this.realTime.add(Calendar.MONTH, 1);
                break;

            case 4: // Month
                this.realTime.add(Calendar.MONTH, -1);
                if(this.realTime.get(Calendar.MONTH) == Calendar.DECEMBER)
                    this.realTime.add(Calendar.YEAR, 1);
                break;

            case 5: // Year
                this.realTime.add(Calendar.YEAR, -1);
                if(this.realTime.get(Calendar.YEAR) < 1970)
                   this.realTime.set(Calendar.YEAR, 1970);
                break;

            default:
                break;
        }
    }

    public void calculateTime(){ this.realTime.add(Calendar.MILLISECOND, 10); }
    public void requestChangeType(){ this.is24H = !this.is24H;}
    public void setCurrSection(int currSection){ this.currSection = currSection; }

    // [WatchGUI]
    // String -> String []
    public String[] showRealTime(){
        displayRealTimeData[0] = realTime.get(Calendar.YEAR) + "";
        displayRealTimeData[1] = realTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH).substring(0, 2);
        displayRealTimeData[2] = (realTime.get(Calendar.MONTH) < 9 ? "0" : "") + (realTime.get(Calendar.MONTH)+1);
        displayRealTimeData[3] = (realTime.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + realTime.get(Calendar.DAY_OF_MONTH);
        displayRealTimeData[4] = (realTime.get(Calendar.MINUTE) < 10 ? "0" : "")+realTime.get(Calendar.MINUTE);
        displayRealTimeData[5] = (realTime.get(Calendar.SECOND) < 10 ? "0" : "")+realTime.get(Calendar.SECOND);
        if(this.is24H) {
            displayRealTimeData[6] = (realTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + realTime.get(Calendar.HOUR_OF_DAY);
            displayRealTimeData[7] = "  ";
        }
        else {
            displayRealTimeData[6] = (realTime.get(Calendar.HOUR) < 10 ? "0" : "")+realTime.get(Calendar.HOUR);
            displayRealTimeData[7] = (realTime.get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
        }

        /* [sonarqube][Vuln #2] */
        return displayRealTimeData.clone();
    }
    public boolean isIs24H(){return this.is24H;}


    // Getters and Setters for Unit Test
    public int getCurrSection() { return this.currSection; }
    public void setRealTime(int section, int value){ this.realTime.set(section, value); }

}
