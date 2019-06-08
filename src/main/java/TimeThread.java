public class TimeThread extends Thread {

    /* [sonarqube][Variable 'system' must be private and have accessor methods] */
    public TimeThread() {
        WatchSystem sys = WatchSystem.getInstance();
    }

    @Override
    public void run() {
        try {
            while(true) {
                sleep(10);
                WatchSystem.realTimeTask();
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
            /* [sonarqube][Bug #3] */
            Thread.currentThread().interrupt();
            return;
        }
    }
}
