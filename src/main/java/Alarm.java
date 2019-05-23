import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Calendar;

public class Alarm implements Mode {

    private RealTime realTime;

    private Calendar[] reservedAlarm;
    private Calendar[] alarm;
    private Calendar[] frequency;
    private Bell[] bell;
    private int [] repeat;
    private int [] tmpRepeat;
    private int isRinging;          // -1: not Activated, 0~3 : index of Ringing Bell
    private int [] bellIndex;
    private boolean[] alarmState;

    private int status; // 0: List, 1: Alarm Time Setting, 2: Alarm Frequency, 3: Alarm Bell Setting
    private int currSection; // 0: Minute, 1: Hour, 2: Frequency_Second, 3: Frequency_Minute, 4: Count, 5: Bell
    private int currAlarm;

    public Alarm(RealTime realTime) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.reservedAlarm = new Calendar[4];
        for(int i = 0; i < 4; i++){
            this.reservedAlarm[i] = Calendar.getInstance();
            this.reservedAlarm[i].clear();
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

        this.bell = new Bell[4];
        for(int i =0; i<4; i++) {
            this.bell[i] = new Bell(i+1);
        }

        this.repeat = new int[]{1,1,1,1};
        this.tmpRepeat = new int[]{1,1,1,1};                // To protect repeat from ringing Again, -1: stop Call again
        this.bellIndex = new int[]{0,0,0,0};
        this.alarmState = new boolean[]{false, false, false, false};
        this.isRinging = -1;

        this.status = 0;
        this.currAlarm = 0;
        this.realTime = realTime;
    }

    // Getters and Setters
    public Calendar[] getReservated() { return reservedAlarm; }
    public Calendar getReservated(int i){ return this.reservedAlarm[i]; }
    public void setReservated(Calendar[] reservated) { this.reservedAlarm = reservated; }
    public void setReservated(Calendar reservated){this.reservedAlarm[this.currAlarm] = reservated;}
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
        //System.out.println("[Alarm]");
        for(int i = 0; i < 4; i++){
            if(this.alarmState[i]){
                if((this.alarm[i].getTimeInMillis() - this.realTime.requestRealTime().getTimeInMillis()) == 0){
                    if(isRinging != -1) {
                        // If Alarm is already Ringing, should be change to other one
                        bell[bellIndex[isRinging]].pause();
                    }
                    isRinging = i;
                    bell[bellIndex[i]].play(30);
                    if(tmpRepeat[i]-- > 0) {
                        alarm[i].add(Calendar.MINUTE, frequency[i].get(Calendar.MINUTE));
                        alarm[i].add(Calendar.SECOND, frequency[i].get(Calendar.SECOND));
                    }
                    else {
                        tmpRepeat[i] = repeat[i];
                        alarm[i] = reservedAlarm[i];
                    }
                }
            }
        }
    }

    public void requestSettingAlarm(){
        if(this.status == 0) {
            // Because while Setting specific Alarm, should NOT ring
            alarmState[this.currAlarm] = false;
            this.status = 1;
        }
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
                        this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, 1);
                        if(this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) == 0)
                            this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
                        break;
                    case 1: // 1: Hour
                        this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
                        if(this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 0)
                            this.reservedAlarm[this.currAlarm].add(Calendar.DATE, -1);
                        break;
                    default :
                        break;
                }
                alarm[this.currAlarm] = reservedAlarm[this.currAlarm];
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
            case 3: // 3. Alarm Bell
                if(++bellIndex[this.currAlarm] == 4) {
                    bellIndex[this.currAlarm] = 0;
                }
                bell[bellIndex[this.currAlarm]].play(1);
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
                        this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, -1);
                        if (this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) == 59)
                            this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
                        break;
                    case 1: // 1: Hour
                        this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
                        if (this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 23)
                            this.reservedAlarm[this.currAlarm].add(Calendar.DATE, 1);
                        break;
                    default:
                        break;
                }
                break;
            case 2: // 2. Alarm Frequency
                switch (this.currSection) {
                    case 2: // 2: Frequency_Second
                        this.frequency[this.currAlarm].add(Calendar.SECOND, -1);
                        if (this.reservedAlarm[this.currAlarm].get(Calendar.SECOND) == 59)
                            this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, 1);
                        break;
                    case 3: // 3: Frequency_Minute
                        this.frequency[this.currAlarm].add(Calendar.MINUTE, -1);
                        if (this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) == 59)
                            this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, 10);
                        break;

                    case 4: // 4: Count
                        if (--this.repeat[this.currAlarm] == -1)
                            this.repeat[this.currAlarm] = 5;
                        break;
                    default:
                        break;
                }
                break;
            case 3: // 3. Alarm Bell
                if(bellIndex[this.currAlarm]-- == 0) {
                    bellIndex[this.currAlarm] = 3;
                }
                bell[bellIndex[this.currAlarm]].play(1);
                break;
            default:
                break;
        }
    }


    public void requestSettingBellAlarm(){
        if(this.status == 0) // 0: List
            this.status = 1; // 1: Alarm Setting
    }
    public void requestAlarmPrevSection(){
        // This function is only for 'Setting Bell', I don't think it needs....
    }
    public void requestNextAlarm(){
        if(++this.currAlarm == 4)
            this.currAlarm = 0;
    }
    public void requestStopRinging(){
        if(isRinging != -1)
            bell[isRinging].pause();
    }
    public void requestAlarmOnOff(){ this.alarmState[this.currAlarm] = !this.alarmState[this.currAlarm]; }
    public void showAlarm(){}
}
