import java.util.ArrayList;
import java.util.Calendar;

public class Stopwatch {

    private Calendar stpTime;
    private Calendar splitTime;

    // boolean -> int
    private int status; // [status] 0: Stopped, 1: Continued

    // Constructor
    public Stopwatch(){
        this.stpTime = Calendar.getInstance();
        this.stpTime.clear();

        this.splitTime = Calendar.getInstance();
        this.splitTime.clear();

        this.status = 0; // [status] 0: Stopped
    }

    // [ModeDB] Methods
    // Load Data from ModeDB
    public Stopwatch(ArrayList db){
        this();
        if(db != null){
            this.stpTime = (Calendar)db.get(0);
            this.splitTime = (Calendar)db.get(1);
        }
    }

    // Save Data to ModeDB
    public ArrayList getStopwatchData(){
        ArrayList save = new ArrayList();

        save.add(this.stpTime);
        save.add(this.splitTime);

        return save;
    }

    // [Stopwatch] System Methods
    public void realTimeTaskStopwatch(){
        if(this.status == 1) // 1: Continued
            this.stpTime.add(Calendar.MILLISECOND, 10);
    }

    public void requestStartStopwatch(){ this.status = 1; } // [status] 0: Stopped -> 1: Continued
    public void requestStopStopwatch(){ this.status = 0; } // [status] 1: Continued -> 0: Stopped
    public void requestSplitStopwatch(){
        if(this.status == 1){ // [status] 1: Continued
            this.splitTime.set(Calendar.MILLISECOND, this.stpTime.get(Calendar.MILLISECOND));
            this.splitTime.set(Calendar.SECOND, this.stpTime.get(Calendar.SECOND));
            this.splitTime.set(Calendar.MINUTE, this.stpTime.get(Calendar.MINUTE));
            this.splitTime.set(Calendar.HOUR_OF_DAY, this.stpTime.get(Calendar.HOUR_OF_DAY));
        }
    }

    public void requestResetStopwatch(){
        if(this.status == 0){ // [status] 0: Stopped
            this.stpTime.clear();
            this.splitTime.clear();
        }
    }


    // [WatchGUI]
    // void -> String
    public String showStopwatch() {
        String data = "";
        if (this.stpTime.get(Calendar.HOUR_OF_DAY) > 0)
            data += (this.stpTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.stpTime.get(Calendar.HOUR_OF_DAY);
        data += (this.stpTime.get(Calendar.MINUTE) < 10 ? "0" : "") + this.stpTime.get(Calendar.MINUTE);
        data += (this.stpTime.get(Calendar.SECOND) < 10 ? "0" : "") + this.stpTime.get(Calendar.SECOND);
        if (this.stpTime.get(Calendar.HOUR_OF_DAY) == 0)
            data += (this.stpTime.get(Calendar.MILLISECOND) < 100 ? "0" : "") + (this.stpTime.get(Calendar.MILLISECOND) / 10);
        if (this.splitTime.get(Calendar.HOUR_OF_DAY) > 0)
            data += (this.splitTime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.splitTime.get(Calendar.HOUR_OF_DAY);
        data += (this.splitTime.get(Calendar.MINUTE) < 10 ? "0" : "") + this.splitTime.get(Calendar.MINUTE);
        data += (this.splitTime.get(Calendar.SECOND) < 10 ? "0" : "") + this.splitTime.get(Calendar.SECOND);
        if (this.splitTime.get(Calendar.HOUR_OF_DAY) == 0)
            data += (this.splitTime.get(Calendar.MILLISECOND) < 100 ? "0" : "") + (this.splitTime.get(Calendar.MILLISECOND) / 10);
        return data;
    }

    public int requestStopwatchFlag(){ return this.status; }

    // Getters and Setters for Unit Test
    public Calendar getStpTime() { return stpTime; }
    public void setStpTime(Calendar stpTime) { this.stpTime = stpTime; }
    public Calendar getSplitTime() { return splitTime; }
    public void setSplitTime(Calendar splitTime) { this.splitTime = splitTime; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
   



}
