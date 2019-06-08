public class Main {

    /* [sonarqube][Class should define a constructor] */
    public Main(){}

    public static void main(String[] args)  {

        WatchSystem watchSystem = new WatchSystem();
        /* [sonarqube][Bug #6] */
        Thread timeThread = new TimeThread(watchSystem);
        timeThread.start();
    }
}
