public class TimeThread extends Thread {

    /* [sonarqube][Variable 'system' must be private and have accessor methods] */
    private WatchSystem system;

    public TimeThread(WatchSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        while(true) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            system.realTimeTask();
        }
    }
}
