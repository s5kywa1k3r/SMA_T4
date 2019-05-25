<<<<<<< HEAD
public class TimeThread extends Thread {
    WatchSystem system;

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
=======
public class TimeThread extends Thread {
    WatchSystem system;

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
>>>>>>> 4438be0b9f34d8ba11db7f514eef8ef45ce44b52
