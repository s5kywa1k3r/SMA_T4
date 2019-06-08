/* TimeThread Class */

public class TimeThread extends Thread {

    /* [sonarqube][Variable 'system' must be private and have accessor methods] */
    private WatchSystem system;

    public TimeThread(WatchSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        try {
            while(true) {
                sleep(10);
                system.realTimeTask();
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
            /* [sonarqube][Bug #3] */
            Thread.currentThread().interrupt();
            return;
        }
    }
}
