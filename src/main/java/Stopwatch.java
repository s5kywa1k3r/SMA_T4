import java.util.ArrayList;
import java.util.Calendar;

public class Stopwatch {

    private Calendar stpTime;
    private Calendar splitTime;

    private int status; // [Report] Boolean to Int 0: Stopped, 1: Continued

    public Stopwatch(){
        this.stpTime = Calendar.getInstance();
        this.stpTime.clear();

        this.splitTime = Calendar.getInstance();
        this.splitTime.clear();

        this.status = 0; // 0: Stopped
    }

    // For load stored data
    public Stopwatch(ArrayList db){
        this();
        if(db != null){
            this.stpTime = (Calendar)db.get(0);
            this.splitTime = (Calendar)db.get(1);
            this.status = (int)db.get(2);
        }
    }

    // Getters and Setters
    public Calendar getStpTime() { return stpTime; }
    public void setStpTime(Calendar stpTime) { this.stpTime = stpTime; }
    public Calendar getSplitTime() { return splitTime; }
    public void setSplitTime(Calendar splitTime) { this.splitTime = splitTime; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    // Operations
    public void realTimeTaskStopwatch(){
        //System.out.println("[Stopwatch]");
        if(this.status == 1) // 1: Continued
            this.stpTime.add(Calendar.MILLISECOND, 10);
    }

    public void requestStartStopwatch(){ this.status = 1; } // 0: Stopped -> 1: Continued
    public void requestStopStopwatch(){ this.status = 0; } // 1: Continued -> 0: Stopped
    public void requestSplitStopwatch(){
        if(this.status == 1){ // 1: Continued
            this.splitTime.set(Calendar.MILLISECOND, this.stpTime.get(Calendar.MILLISECOND));
            this.splitTime.set(Calendar.SECOND, this.stpTime.get(Calendar.SECOND));
            this.splitTime.set(Calendar.MINUTE, this.stpTime.get(Calendar.MINUTE));
            this.splitTime.set(Calendar.HOUR_OF_DAY, this.stpTime.get(Calendar.HOUR_OF_DAY));
        }
    }

    public void requestResetStopwatch(){
        if(this.status == 0){ // 0: Stopped
            this.stpTime.clear();
            this.splitTime.clear();
        }
    }

    public void showStopwatch(){

    }

}
