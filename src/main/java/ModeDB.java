import java.util.ArrayList;
public class ModeDB {

    private ArrayList<ArrayList> db;
    public ModeDB(){
        this.db = new ArrayList<ArrayList>();
        for(int i = 0; i < 5; i++)
            db.add(null); // 0: Stopwatch, 1: Timer, 2: Alarm, 3: Worldtime, 4: Sun
    }

    public ArrayList loadData(int index){ return this.db.get(index); }
    public void saveData(int index, ArrayList data){ this.db.set(index, data); }
}
