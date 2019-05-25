import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Timer {

    private Calendar timerTime;
    private Calendar rsvTime;
    private Bell bell;
    private String [] displayTimerData;
    private int status; // 0: Stopped, 1: Continued, 2: Setting, 3: Ringing
    private int currSection; // 0: Second, 1: Minute, 2: Hour
    private int blink;

    // Constructors
    public Timer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.timerTime = Calendar.getInstance();
        this.timerTime.clear();

        this.rsvTime = Calendar.getInstance();
        this.rsvTime.clear();
        this.bell = new Bell(0);
        this.displayTimerData = new String[3];
        this.status = 0;
        this.currSection = 0;
    }

    // [ModeDB] Methods
    // Load Data from ModeDB
    public Timer(ArrayList db) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this();
        if(db != null){
            this.timerTime = (Calendar)db.get(0);
            this.rsvTime = (Calendar)db.get(1);
        }
    }

    // Save Data to ModeDB
    public ArrayList getTimerData(){
        ArrayList data = new ArrayList();

        data.add(this.timerTime);
        data.add(this.rsvTime);

        return data;
    }

    // [Timer] System Methods
    public void requestTimerTime(){
        if(this.status == 0) // [status] 0: Stopped -> 2: Setting
            this.changeStatus(2);
    }

    /* [Removed] public void getTimerTime() */
    public void requestNextTimerTimeSection(){
        if(++this.currSection == 3)
            this.currSection = 0; // [currSection] 2: Hour -> 0: Second
    }

    public void requestIncreaseTimerTimeSection(){
        switch(this.currSection){
            case 0 : // [currSection] 0: Second
                this.rsvTime.add(Calendar.SECOND, 1);
                if(this.rsvTime.get(Calendar.SECOND) == 0)
                    this.rsvTime.add(Calendar.MINUTE, -1);
                break;

            case 1: // [currSection] 1: Minute
                this.rsvTime.add(Calendar.MINUTE, 1);
                if(this.rsvTime.get(Calendar.MINUTE) == 0)
                    this.rsvTime.add(Calendar.HOUR_OF_DAY, -1);
                break;

            case 2: // [currSection] 2: Hour
                this.rsvTime.add(Calendar.HOUR_OF_DAY, 1);
                if(this.rsvTime.get(Calendar.HOUR_OF_DAY) == 0)
                    this.rsvTime.add(Calendar.DATE, -1);
                break;

            default: break;
        }
    }

    public void requestDecreaseTimerTimeSection(){
        switch(this.currSection){
            case 0 : // [currSection] 0: Second
                this.rsvTime.add(Calendar.SECOND, -1);
                if(this.rsvTime.get(Calendar.SECOND) == 59)
                    this.rsvTime.add(Calendar.MINUTE, 1);
                break;

            case 1: // [currSection] 1: Minute
                this.rsvTime.add(Calendar.MINUTE, -1);
                if(this.rsvTime.get(Calendar.MINUTE) == 59)
                    this.rsvTime.add(Calendar.HOUR, 1);
                break;

            case 2: // [currSection] 2: Hour
                this.rsvTime.add(Calendar.HOUR_OF_DAY, -1);
                if(this.rsvTime.get(Calendar.HOUR_OF_DAY) == 23)
                    this.rsvTime.add(Calendar.DATE, 1);
                break;

            default: break;
        }
    }

    public void changeStatus(int i){
        if(-1 < i && i < 4) // [status] 0: Stopped, 1: Continued, 2: Setting, 3: Ringing
            this.status = i;
    }

    public void requestResetTimer(){
        if(this.status == 0) { // [status] 0: Stopped
            this.timerTime.set(Calendar.MILLISECOND, 0);
            this.timerTime.set(
                    this.rsvTime.get(Calendar.YEAR),
                    this.rsvTime.get(Calendar.MONTH),
                    this.rsvTime.get(Calendar.DATE),
                    this.rsvTime.get(Calendar.HOUR_OF_DAY),
                    this.rsvTime.get(Calendar.MINUTE),
                    this.rsvTime.get(Calendar.SECOND)
            );
        }
    }

    /* [Removed] public void setTimerReservatedTime() */
    /* [Removed] public void setTimerTime() */
    public void ringOff(){
        System.out.println("Ring off operation activated");
        bell.pause();
        this.status = 0;
    } // [status] 3: Ringing -> 0: Stopped
    // public -> private
    private void startRingingTimer(){
        System.out.println("String ringing");
        this.status = 3;
        bell.play();
    } //  [status] 1: Continued -> 3: Ringing

    public void realTimeTimerTask(){
        if(this.timerTime.getTimeInMillis() > -32400000) { // If timer is not expired
            if (this.status == 1) { // [status] 1: Continued
                this.timerTime.add(Calendar.MILLISECOND, -10);
                if (this.timerTime.getTimeInMillis() == -32400000)  // IF timer is expired
                    this.startRingingTimer(); // Ring
            }
        }
        // else if timer is already expired
        else if(this.status <= 1) this.status = 0; // [status] 1: Continued -> 0: Stopped

        if(status == 3 && !bell.isRunning()){
            status = 0;
        }
    }

    public void requestExitSetTimerTime(){
        if(this.status == 2){ // [Status] 2: Setting
            this.changeStatus(0); // [Status] 2: Setting -> 0: Stopped
            this.requestResetTimer();
            this.currSection = 0; // [CurrSection] 0: Second -> Setting Section initialization
        }
    }

    // [WatchGUI]
    // void -> String
    public String[] showTimer(){
        if(blink++ > 100) blink = 0;
        if(this.status == 2) {
            displayTimerData[0] = (rsvTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + rsvTime.get(Calendar.HOUR_OF_DAY);
            displayTimerData[1] = (rsvTime.get(Calendar.MINUTE) < 10 ? "0" : "") + rsvTime.get(Calendar.MINUTE);
            displayTimerData[2] = (rsvTime.get(Calendar.SECOND) < 10 ? "0" : "") + rsvTime.get(Calendar.SECOND);
            if(blink > 50) {
                switch (this.currSection) {
                    case 0: displayTimerData[2] = "";break;
                    case 1: displayTimerData[1] = "";break;
                    case 2: displayTimerData[0] = "";break;
                }
            }
        }
        else {
            displayTimerData[0] = (timerTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + timerTime.get(Calendar.HOUR_OF_DAY);
            displayTimerData[1] = (timerTime.get(Calendar.MINUTE) < 10 ? "0" : "") + timerTime.get(Calendar.MINUTE);
            displayTimerData[2] = (timerTime.get(Calendar.SECOND) < 10 ? "0" : "") + timerTime.get(Calendar.SECOND);
        }
        return displayTimerData;
    }

    public int requestTimerFlag(){ return this.status; }

    // Getters and Setters for Unit Test
    public Calendar getTimerTime(){ return this.timerTime; }
    //public void setTimerTime(Calendar timerTime){ this.timerTime = timerTime; }
    public Calendar getRsvTime(){ return this.rsvTime; }
    public void setRsvTime(Calendar rsvTime) { this.rsvTime = rsvTime; }
    public int getStatus(){ return this.status; }
    public void setStatus(int status) { this.status = status; }
    public int getCurrSection(){ return this.currSection; }
    public void setCurrSection(int currSection){ this.currSection = currSection; }
}
