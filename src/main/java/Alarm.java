import java.util.Calendar;

public class Alarm implements Mode {

    private RealTime realTime;

    private Calendar[] reservated;
    private Calendar[] alarm;
    private Calendar[] frequency;
    private int[] repeat;
    private boolean[] alarmState;
    private int[] bell;

    private int status; // 0: List, 1: Alarm Time Setting, 2: Alarm Frequency, 3: Alarm Bell Setting
    private int currSection; // 0: Minute, 1: Hour, 2: Frequency_Second, 3: Frequency_Minute, 4: Count, 5: Bell
    private int currAlarm;

    public Alarm(RealTime realTime){
        this.reservated = new Calendar[4];
        for(int i = 0; i < 4; i++){
            this.reservated[i] = Calendar.getInstance();
            this.reservated[i].clear();
        }

        this.alarm = new Calendar[4];
        for(int i = 0; i < 4; i++){
            this.alarm[i] = Calendar.getInstance();
            this.alarm[i].clear();
        }

        this.frequency = new Calendar[4];
        for(int i = 0; i < 4; i++){
            this.frequency[i] = Calendar.getInstance();
            this.frequency[i].clear();
        }

        this.repeat = new int[]{0,0,0,0};
        this.bell = new int[]{0,0,0,0};
        this.alarmState = new boolean[]{false, false, false, false};

        this.status = 0;
        this.currAlarm = 0;
        this.realTime = realTime;
    }

    // Getters and Setters
    public Calendar[] getReservated() { return reservated; }
    public Calendar getReservated(int i){ return this.reservated[i]; }
    public void setReservated(Calendar[] reservated) { this.reservated = reservated; }
    public void setReservated(Calendar reservated){this.reservated[this.currAlarm] = reservated;}
    public Calendar[] getFrequency() { return frequency; }
    public Calendar getFrequency(int i){ return frequency[i]; }
    public void setFrequency(Calendar[] frequency) { this.frequency = frequency; }
    public void setFrequency(int i, Calendar frequency) { this.frequency[i] = frequency; }
    public int[] getRepeat() { return repeat; }
    public int getRepeat(int i){ return repeat[i]; }
    public void setRepeat(int[] repeat) { this.repeat = repeat; }
    public void setRepeat(int i, int repeat) { this.repeat[i] = repeat; }
    public int getStatus() { return status; }
    public void setStatus(int status ) { this.status = status; }
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public boolean getAlarmCurrAlarmStatus(){return this.alarmState[this.currAlarm];}
    public int getCurrAlarm(){ return this.currAlarm; }
    public void setCurrAlarm(int i) { this.currAlarm = i; }
    public int getCurrSection() { return this.currSection; }


    // Operations
    public void realTimeTaskAlarm(){

        System.out.println("[Alarm]");
        for(int i = 0; i < 4; i++){
            if(this.alarmState[i] == true){
                if((this.alarm[i].getTimeInMillis() - this.realTime.requestRealTime().getTimeInMillis()) == 0){
                    // Beep
                }
            }

        }



    }
    public void requestSettingAlarm(){
        if(this.status == 0)
            this.status = 1;
    }
    public void requestAlarmNextSection(){
        switch(++this.currSection){
            case 2 :
                this.status = 2; // 2: Alarm Frequency
                break;

            case 5 :
                this.status = 3; // 3: Alarm Bell Setting
                break;

            case 6:
                this.currSection = 0;
                this.status = 1; // 1: Alarm Setting
                break;

            default:
                break;
        }
    }

    public void increaseSection(){
        switch(this.status){
            case 1: // 1. Alarm Setting
                switch(this.currSection){
                    case 0: // 0: Minute
                        this.reservated[this.currAlarm].add(Calendar.MINUTE, 1);
                        if(this.reservated[this.currAlarm].get(Calendar.MINUTE) == 0)
                            this.reservated[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
                        break;
                    case 1: // 1: Hour
                        this.reservated[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
                        if(this.reservated[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 0)
                            this.reservated[this.currAlarm].add(Calendar.DATE, -1);
                        break;
                    default :
                        break;
                }
                break;
            case 2: // 2. Alarm Frequency
                switch(this.currSection){
                    case 2: // 2: Frequency_Second
                        this.frequency[this.currAlarm].add(Calendar.SECOND, 1);
                        if(this.frequency[this.currAlarm].get(Calendar.SECOND) == 0)
                            this.frequency[this.currAlarm].add(Calendar.MINUTE, -1);
                        break;
                    case 3: // 3: Frequency_Minute
                        this.frequency[this.currAlarm].add(Calendar.MINUTE, 1);
                        if(this.frequency[this.currAlarm].get(Calendar.MINUTE) == 11)
                            this.frequency[this.currAlarm].add(Calendar.MINUTE, 0);
                        break;

                    case 4: // 4: Count
                        if(++this.repeat[this.currAlarm] == 6)
                            this.repeat[this.currAlarm] = 0;
                        break;
                    default :
                        break;
                }
                break;
            case 3: // 3. Alarm Bell Setting
                // 4: Bell

                break;
            default:
                break;
        }


    }

    public void decreaseSection() {
        switch (this.status) {
            case 1: // 1. Alarm Setting
                switch (this.currSection) {
                    case 0: // 0: Minute
                        this.reservated[this.currAlarm].add(Calendar.MINUTE, -1);
                        if (this.reservated[this.currAlarm].get(Calendar.MINUTE) == 59)
                            this.reservated[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
                        break;
                    case 1: // 1: Hour
                        this.reservated[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
                        if (this.reservated[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 23)
                            this.reservated[this.currAlarm].add(Calendar.DATE, 1);
                        break;
                    default:
                        break;
                }
                break;
            case 2: // 2. Alarm Frequency
                switch (this.currSection) {
                    case 2: // 2: Frequency_Second
                        this.frequency[this.currAlarm].add(Calendar.SECOND, -1);
                        if (this.reservated[this.currAlarm].get(Calendar.SECOND) == 59)
                            this.reservated[this.currAlarm].add(Calendar.MINUTE, 1);
                        break;
                    case 3: // 3: Frequency_Minute
                        this.frequency[this.currAlarm].add(Calendar.MINUTE, -1);
                        if (this.reservated[this.currAlarm].get(Calendar.MINUTE) == 59)
                            this.reservated[this.currAlarm].add(Calendar.MINUTE, 10);
                        break;

                    case 4: // 4: Count
                        if (--this.repeat[this.currAlarm] == -1)
                            this.repeat[this.currAlarm] = 5;
                        break;
                    default:
                        break;
                }
                break;
            case 3: // 3. Alarm Bell Setting
                // 4: Bell

                break;
            default:
                break;
        }
    }


    //public void requestAlarmNextSection(){}
    public void requestSettingBellAlarm(){
        if(this.status == 0) // 0: List
            this.status = 1; // 1: Alarm Setting
    }
    public void requestAlarmPrevSection(){

    }
    public void requestNextAlarm(){
        if(++this.currAlarm == 4)
            this.currAlarm = 0;
    }
    public void requestStopRinging(){}
    public void requestAlarmOnOff(){ this.alarmState[this.currAlarm] = !this.alarmState[this.currAlarm]; }
    public void showAlarm(){}
}
