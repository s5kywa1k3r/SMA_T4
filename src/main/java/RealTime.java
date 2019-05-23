import java.util.Calendar;

public class RealTime implements Mode {
    private Calendar realTime; // day, month, year
    private int currSection;

    // Constructor
    public RealTime(){
        this.realTime = Calendar.getInstance();
        // Calendar Month start 0 => 0: January, 1: February, 2: March, ... , 11: December
        this.realTime.clear(); // Initialized to 1970. 1. 1 00:00:00.000
        this.currSection = 0; // 0: Second, 1: Minute, 2: Hour, 3: Day, 4: Month, 5: Year
    }

    // Operations
    public Calendar requestRealTime(){ return this.realTime; }

    public void nextSection(){
        if(++this.currSection == 6) // Over value
            this.currSection = 0; // 0: Second
    }

    public void increaseTime(){
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

    public void setSecond(int i){ this.realTime.set(Calendar.MILLISECOND, i * 1000); }
    public void calculateTime(){ this.realTime.add(Calendar.MILLISECOND, 10); }
    public void requestChangeType(){}
    public void showRealTime(){
       /*
        System.out.printf("[RealTime] %d YEAR %d MONTH %d DAY %d H %d M %d S %d\n",
                this.realTime.get(Calendar.YEAR),
                this.realTime.get(Calendar.MONTH),
                this.realTime.get(Calendar.DATE),
                this.realTime.get(Calendar.HOUR_OF_DAY),
                this.realTime.get(Calendar.MINUTE),
                this.realTime.get(Calendar.SECOND),
                this.realTime.get(Calendar.MILLISECOND) / 10
        );

        */
    }

    // Getters and Setters
    public int getCurrSection() { return this.currSection; }
    public void setCurrSection(int currSection){ this.currSection = currSection; }
    public void setRealTime(int section, int value){ this.realTime.set(section, value); }
}
