import java.util.Calendar;

// Calendar Test Class
public class SeparateSection {
    private Calendar realTime;
    private int currSection;

    public SeparateSection(){
        this.realTime = Calendar.getInstance();
        this.realTime.set(1970, 0, 1, 0, 0, 0);
        this.realTime.set(Calendar.MILLISECOND, 0);

        this.currSection = 1;
    }

    public void init(){
        this.realTime.set(1970, 0, 1, 0, 0, 0);
    }

    public void test(){
        System.out.print("[INITIAL DATE]: ");
        this.printTime();

        System.out.print("[TEST 1] decrease MINUTE: ");
        this.realTime.add(Calendar.MINUTE, -1);
        this.printTime();
        this.init();

        System.out.print("[TEST 2] decrease MINUTE + options: ");
        this.realTime.add(Calendar.MINUTE, -1);
        if(this.realTime.get(Calendar.MINUTE) == 59)
            this.realTime.add(Calendar.HOUR_OF_DAY, 1);
        this.printTime();
        this.init();

        System.out.print("[TEST 3] decrease HOUR_OF_DAY: ");
        this.realTime.add(Calendar.HOUR_OF_DAY, -1);
        this.printTime();
        this.init();

        System.out.print("[TEST 4] decrease HOUR_OF_DAY + options: ");
        this.realTime.add(Calendar.HOUR_OF_DAY, -1);
        if(this.realTime.get(Calendar.HOUR_OF_DAY) == 23)
            this.realTime.add(Calendar.DATE, 1);
        this.printTime();
        this.init();

        System.out.print("[TEST 5] decrease DATE: ");
        this.realTime.set(Calendar.MONTH, 1);
        this.realTime.add(Calendar.DATE, -1);
        this.printTime();
        this.init();

        System.out.print("[TEST 6] decrease DATE + options: ");
        this.realTime.set(Calendar.MONTH, 1);
        int tempMonth = this.realTime.get(Calendar.MONTH);
        this.realTime.add(Calendar.DATE, -1);
        if(this.realTime.get(Calendar.MONTH) != tempMonth)
            this.realTime.add(Calendar.MONTH, 1);
        this.printTime();
        this.init();

        System.out.print("[TEST 7] decrease MILLISECONDS: ");
        this.realTime.add(Calendar.MILLISECOND, -1000);
        this.printTime();
        this.init();

        System.out.print("[TEST 8] clear: ");
        this.realTime.set(2018, 8,7,3,2,5);
        this.printTime();

        this.realTime.clear();
        System.out.print("[TEST 8] result: ");
        this.printTime();
        this.init();
    }
    public void nextSection(){
        if(++this.currSection == 7)
            this.currSection = 0;
    }

    public void increaseTime(){
        switch(this.currSection){
            case 2 : // Minute
                this.realTime.add(Calendar.MINUTE, 1);
                if(this.realTime.get(Calendar.MINUTE) == 0)
                    this.realTime.add(Calendar.HOUR_OF_DAY, -1);
                break;

            case 3: // Hour
                this.realTime.add(Calendar.HOUR_OF_DAY, 1);
                if(this.realTime.get(Calendar.HOUR_OF_DAY) == 0)
                    this.realTime.add(Calendar.DATE, -1);
                break;

            case 4 : // Day
                this.realTime.add(Calendar.DATE, 1);
                if(this.realTime.get(Calendar.DATE) == 1)
                    this.realTime.add(Calendar.MONTH, -1);
                break;

            case 5 : // Month
                this.realTime.add(Calendar.MONTH, 1);
                if(this.realTime.get(Calendar.MONTH) == 0)
                    this.realTime.add(Calendar.YEAR, -1);
                break;

            case 6 : // Year
                this.realTime.add(Calendar.YEAR, 1);
                break;

            default:
                break;
        }

    }

    public void decreaseTime(){
        switch(this.currSection) {
            case 2: // Minute
                this.realTime.add(Calendar.MINUTE, -1);
                if(this.realTime.get(Calendar.MINUTE) == 59)
                    this.realTime.add(Calendar.HOUR_OF_DAY, 1);
                break;

            case 3: // Hour
                this.realTime.add(Calendar.HOUR_OF_DAY, -1);
                if(this.realTime.get(Calendar.HOUR_OF_DAY) == 23)
                    this.realTime.add(Calendar.DATE, 1);
                break;

            case 4: // Day
                int tempMonth = this.realTime.get(Calendar.MONTH);
                this.realTime.add(Calendar.DATE, -1);
                if(this.realTime.get(Calendar.MONTH) != tempMonth)
                    this.realTime.add(Calendar.MONTH, 1);
                break;

            case 5: // Month
                this.realTime.add(Calendar.MONTH, -1);
                if(this.realTime.get(Calendar.MONTH) == Calendar.DECEMBER)
                    this.realTime.add(Calendar.YEAR, 1);
                break;

            case 6: // Year
                this.realTime.add(Calendar.YEAR, -1);
                //if(this.realTime.get(Calendar.YEAR) < 1970)
                //  this.realTime.set(Calendar.YEAR, 1970);
                break;

            default:
                break;
        }
    }

    public void printTime(){
        System.out.printf("%dY %dM %dD %dH %dM %dS %d\n",
                this.realTime.get(Calendar.YEAR),
                this.realTime.get(Calendar.MONTH),
                this.realTime.get(Calendar.DATE),
                this.realTime.get(Calendar.HOUR_OF_DAY),
                this.realTime.get(Calendar.MINUTE),
                this.realTime.get(Calendar.SECOND),
                this.realTime.get(Calendar.MILLISECOND)
        );
    }
}
