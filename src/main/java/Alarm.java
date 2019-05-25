import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Alarm {

    private RealTime realTime;
    private Calendar currTime;

    private Calendar[] reservedAlarm;
    private Calendar[] alarm;
    private Calendar[] frequency;
    private Bell[] bell;
    private String[] displayAlarmData;
    private int [] repeat;
    private int [] tmpRepeat;
    private int RingingIndex;          // -1: not Activated, 1~4 : index of Ringing Bell
    private int [] bellIndex;
    private boolean[] alarmState;

    private int status; // 0: List, 1: Alarm Time Setting, 2: Alarm Frequency, 3: Alarm Bell Setting, 4: Ringing
    private int currSection; // 0: Minute, 1: Hour, 2: Frequency_Second, 3: Frequency_Minute, 4: Count, 5: Bell
    private int currAlarm;
    private int blink;

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

        this.displayAlarmData = new String[8];
        this.repeat = new int[]{1,1,1,1};
        this.tmpRepeat = new int[]{1,1,1,1};                // To protect repeat from ringing Again, -1: stop Call again
        this.bellIndex = new int[]{1,1,1,1};
        this.alarmState = new boolean[]{false, false, false, false};
        this.RingingIndex = -1;

        this.status = 0;
        this.currAlarm = 0;
        this.realTime = realTime;
    }

    public Alarm(RealTime realTime, ArrayList db) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this(realTime);
        if(db != null){
            this.reservedAlarm = (Calendar[])db.get(0);
            this.alarm = (Calendar[])db.get(1);
            this.frequency = (Calendar[])db.get(2);
            this.repeat = (int[])db.get(3);
        }
    }

    public ArrayList getAlarmData(){
        ArrayList data = new ArrayList();

        data.add(this.reservedAlarm);
        data.add(this.alarm);
        data.add(this.frequency);
        data.add(this.repeat);

        return data;
    }

    public void realTimeTaskAlarm(){
        this.currTime = this.realTime.requestRealTime();
        for(int i = 0; i < 4; i++){
            if(this.alarmState[i]){
                if(
                        (this.currTime.get(Calendar.HOUR_OF_DAY) == this.alarm[i].get(Calendar.HOUR_OF_DAY)) &&
                                (this.currTime.get(Calendar.MINUTE) == this.alarm[i].get(Calendar.MINUTE)) &&
                                (this.currTime.get(Calendar.SECOND) == this.alarm[i].get(Calendar.SECOND))
                )
                {
                    this.status = 4;
                    if(RingingIndex != -1) {
                        // If Alarm is already Ringing, should be change to other one
                        requestStopRinging();
                    }
                    RingingIndex = bellIndex[i];
                    bell[RingingIndex-1].play();
                    if(tmpRepeat[i]-- > 0) {
                        alarm[i].add(Calendar.MINUTE, frequency[i].get(Calendar.MINUTE));
                        alarm[i].add(Calendar.SECOND, frequency[i].get(Calendar.SECOND));
                    }
                    else {
                        tmpRepeat[i] = repeat[i];
                        alarm[i] = (Calendar)reservedAlarm[i].clone();
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

    public void requestExitAlarmSetting(){
        this.status = 0;
        this.currSection = 0;
        this.alarm[currAlarm] = (Calendar) this.reservedAlarm[currAlarm].clone();
        this.tmpRepeat[currAlarm] = repeat[currAlarm];
    }

    public void increaseSection(){
        switch(this.currSection){
            case 0: // 1. Alarm Setting
            case 1:
                incraseAlarmTime();
                break;
            case 2: // 2. Alarm Frequency
            case 3:
            case 4:
                increaseFrequencyTime();
                break;
            case 5: // 3. Alarm Bell
            case 6:
                increaseBellIndex();
                break;
            default:
                break;
        }
    }

    public void decreaseSection() {
        switch (this.currSection) {
            case 0: // 1. Alarm Setting
            case 1:
                decreaseAlarmTime();
                break;
            case 2: // 2. Alarm Frequency
            case 3:
            case 4:
                decreaseFrequencyTime();
                break;
            case 5: // 3. Alarm Bell
            case 6:
                decreaseBellIndex();
                break;
            default:
                break;
        }
    }

    public void incraseAlarmTime() {
        if(this.currSection == 0) {
            this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, 1);
            if(this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) == 0)
                this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
        }
        else {
            this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
            if(this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 0)
                this.reservedAlarm[this.currAlarm].add(Calendar.DATE, -1);
        }
    }

    public void increaseFrequencyTime() {
        if(this.currSection == 2) {
            this.frequency[this.currAlarm].add(Calendar.SECOND, 1);
            if(this.frequency[this.currAlarm].get(Calendar.SECOND) == 0)
                this.frequency[this.currAlarm].add(Calendar.MINUTE, -1);
        }
        else if(this.currSection == 3) {
            this.frequency[this.currAlarm].add(Calendar.MINUTE, 1);
            if(this.frequency[this.currAlarm].get(Calendar.MINUTE) == 11)
                this.frequency[this.currAlarm].add(Calendar.MINUTE, 0);
        }
        else {
            if(++this.repeat[this.currAlarm] == 6)
                this.repeat[this.currAlarm] = 0;
        }
    }

    public void increaseBellIndex() {
        if(++bellIndex[this.currAlarm] == 5)
            bellIndex[this.currAlarm] = 1;
        bell[bellIndex[this.currAlarm]-1].play();
        try { Thread.sleep(3000);} catch(InterruptedException e) {e.printStackTrace();}
        bell[bellIndex[this.currAlarm]-1].pause();
    }

    public void decreaseAlarmTime() {
        if(this.currSection == 0) {
            this.reservedAlarm[this.currAlarm].add(Calendar.MINUTE, -1);
            if (this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) == 59)
                this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, 1);
        }
        else {
            this.reservedAlarm[this.currAlarm].add(Calendar.HOUR_OF_DAY, -1);
            if (this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) == 23)
                this.reservedAlarm[this.currAlarm].add(Calendar.DATE, 1);
        }
    }

    public void decreaseFrequencyTime() {
        if(this.currSection == 2) {
            this.frequency[this.currAlarm].add(Calendar.SECOND, -1);
            if (this.frequency[this.currAlarm].get(Calendar.SECOND) == 59)
                this.frequency[this.currAlarm].add(Calendar.MINUTE, 1);
        }
        else if(this.currSection == 3) {
            this.frequency[this.currAlarm].add(Calendar.MINUTE, -1);
            if (this.frequency[this.currAlarm].get(Calendar.MINUTE) == 59)
                this.frequency[this.currAlarm].add(Calendar.MINUTE, 10);
        }
        else {
            if (--this.repeat[this.currAlarm] == -1)
                this.repeat[this.currAlarm] = 5;
        }
    }

    public void decreaseBellIndex() {
        if(--bellIndex[this.currAlarm] == 0)
            bellIndex[this.currAlarm] = 4;
        bell[bellIndex[this.currAlarm]-1].play();
        try { Thread.sleep(3000);} catch(InterruptedException e) {e.printStackTrace();}
        bell[bellIndex[this.currAlarm]-1].pause();
    }

    public void requestNextAlarm(){
        if(++this.currAlarm == 4)
            this.currAlarm = 0;
    }
    public void requestStopRinging(){
        if(RingingIndex != -1) {
            bell[RingingIndex-1].pause();
            RingingIndex = -1;
            this.status = 0;
        }
    }
    public void requestAlarmOnOff(){ this.alarmState[this.currAlarm] = !this.alarmState[this.currAlarm]; }

    // String -> String[]
    public String[] showAlarm(){
        if(blink++ > 100) blink = 0;
        displayAlarmData[1] = "0"+this.frequency[this.currAlarm].get(Calendar.MINUTE);
        displayAlarmData[2] = (this.frequency[this.currAlarm].get(Calendar.SECOND) < 10 ? "0" : "") + this.frequency[this.currAlarm].get(Calendar.SECOND);
        displayAlarmData[4] = this.bellIndex[this.currAlarm] + "";
        if(alarmState[this.currAlarm]) displayAlarmData[5] = "ON";
        else displayAlarmData[5] = "OFF";
        if(this.status == 0 || this.status == 4) {
            displayAlarmData[0] = this.tmpRepeat[this.currAlarm] + "";
            displayAlarmData[3] = (this.alarm[this.currAlarm].get(Calendar.MINUTE) < 10 ? "0" : "") + this.alarm[this.currAlarm].get(Calendar.MINUTE);
            if (this.realTime.isIs24H()) {
                displayAlarmData[6] = (this.alarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.alarm[this.currAlarm].get(Calendar.HOUR_OF_DAY);
                displayAlarmData[7] = "";
            } else {
                displayAlarmData[6] = (this.alarm[this.currAlarm].get(Calendar.HOUR) < 10 ? "0" : "") + this.alarm[this.currAlarm].get(Calendar.HOUR);
                displayAlarmData[7] = (this.alarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
            }
        }
        else {
            displayAlarmData[0] = this.repeat[this.currAlarm] + "";
            displayAlarmData[3] = (this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE) < 10 ? "0" : "") + this.reservedAlarm[this.currAlarm].get(Calendar.MINUTE);
            if (this.realTime.isIs24H()) {
                displayAlarmData[6] = (this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY);
                displayAlarmData[7] = "";
            } else {
                displayAlarmData[6] = (this.reservedAlarm[this.currAlarm].get(Calendar.HOUR) < 10 ? "0" : "") + this.reservedAlarm[this.currAlarm].get(Calendar.HOUR);
                displayAlarmData[7] = (this.reservedAlarm[this.currAlarm].get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
            }
            if(blink > 50) {
                switch (currSection) {
                    case 0: displayAlarmData[3] = ""; break;
                    case 1: displayAlarmData[6] = ""; break;
                    case 2: displayAlarmData[2] = ""; break;
                    case 3: displayAlarmData[1] = ""; break;
                    case 4: displayAlarmData[0] = " "; break;
                    case 5: displayAlarmData[4] = ""; break;
                }
            }
        }
        return displayAlarmData;
    }

    public int requestAlarmFlag(){ return this.status; }

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
    public void setRepeat(int i, int repeat) { this.repeat[i] = repeat; this.tmpRepeat[i]=repeat; }
    public int getStatus() { return this.status; }
    public void setStatus(int status ) { this.status = status; }
    public RealTime getRealTime() { return realTime; }
    public void setRealTime(RealTime realTime) { this.realTime = realTime; }
    public boolean getAlarmCurrAlarmStatus(){return this.alarmState[this.currAlarm];}
    public int getCurrAlarm(){ return this.currAlarm; }
    public void setCurrAlarm(int i) { this.currAlarm = i; }
    public int getCurrSection() { return this.currSection; }
    public void setCurrSection(int i) { this.currSection = i; }
    public Bell getBell(int index) {
        this.RingingIndex = index;
        return bell[index];
    }
    public int getBellIndex() {return bellIndex[currAlarm];}
    public boolean isRinging() {
        if(RingingIndex == -1) return false;
        else return true;
    }
    public void setAlarm(Calendar tmp) {
        alarm[currAlarm] = tmp;
        reservedAlarm[currAlarm] = (Calendar)tmp.clone();
    }
    public Calendar getAlarm() {return alarm[currAlarm];}
}
