import java.util.ArrayList;
public class ModeDB {

    private ArrayList<ArrayList> db;
    public ModeDB(){
        for(int i = 0; i < 5; i++)
            db.add(null); //
    }

    public ArrayList loadData(int index){ return this.db.get(index); }
    public void saveData(int index, ArrayList data){ this.db.set(index, data); }

}
