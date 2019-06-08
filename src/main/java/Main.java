/* Main Class*/

public class Main {

    /* [sonarqube][Class should define a constructor] */
    public Main(){
        /* Not Used */
    }

    public static void main(String[] args)  {

        WatchSystem watchSystem = new WatchSystem();
        /* [sonarqube][Bug #6] */
        Thread timeThread = new TimeThread(watchSystem);
        timeThread.start();
    }
}
