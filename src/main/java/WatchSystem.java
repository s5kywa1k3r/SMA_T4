import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class WatchSystem {

    private ArrayList<Mode> menu;
    private int currMode;
    private int maxCnt;

    public static void main(String[] args) {
        SeparateSection test = new SeparateSection();
        test.test();
        WatchSystem run = new WatchSystem();
    }

    public WatchSystem() {
        TimeThread thread = new TimeThread(this);

        System_GUI system_gui = new System_GUI(this);
        this.menu = new ArrayList<Mode>();
        this.menu.add(new RealTime());
        this.menu.add(new TimeSetting((RealTime)this.menu.get(0)));
        this.menu.add(new Stopwatch());
        this.menu.add(new Timer());
        this.menu.add(new Alarm());

        this.currMode = 0; // [currMode] 0: Always RealTime
        this.maxCnt = 4;
        thread.run();
    }

    public void pressShowType() {
        if(this.menu.get(this.currMode) instanceof RealTime)
            ((RealTime)this.menu.get(this.currMode)).requestChangeType();
    }

    // Worked by thread
    public void realTimeTask() {
        for(int i = 0; i <= this.maxCnt; i++){
            Mode menu = this.menu.get(i);
            if(menu instanceof RealTime){
                ((RealTime)menu).calculateTime();
                if(this.currMode == i)
                    ((RealTime)menu).showRealTime();
                continue;
            }

            else if(menu instanceof TimeSetting){
                ((TimeSetting)menu).realTimeTaskTimeSetting();
                if(this.currMode == i)
                    ((TimeSetting)menu).getRealTime().showRealTime();
                continue;
            }

            else if(menu instanceof Stopwatch){
                ((Stopwatch) menu).realTimeTaskStopwatch();
                if(this.currMode == i)
                    ((Stopwatch)menu).showStopwatch();
                continue;
            }

            else if(menu instanceof Timer){
                ((Timer) menu).realTimeTimerTask();
                if(this.currMode == i)
                    ((Timer)menu).showTimer();
                continue;
            }

            else if(menu instanceof Alarm){
                ((Alarm) menu).realTimeTaskAlarm();
                if(this.currMode == i)
                continue;
            }

            else if(menu instanceof Worldtime){
                ((Worldtime) menu).realTimeTaskWorldtime();
                if(this.currMode == i)
                    ((Worldtime)menu).showWorldTime();
                continue;
            }

            else if(menu instanceof Sun){
                ((Sun) menu).realTimeTaskSun();
                if(this.currMode == i)
                    ((Sun)menu).showSun();
                continue;
            }
            else{
                System.out.println("{Exception}[WatchSystem][realTimeTask] NotValidModeException");
                return ;
            }
        }
    }

    public void pressChangeMode() {
        if(++this.currMode == this.maxCnt)
            this.currMode = 0;
    }

    public void callNextMode(){
        if(++this.currMode == this.maxCnt)
            this.currMode = 0; // 0: RealTime
    }

    // Mode Setting
    public void enterModeSetting(){}
    public void pressNextMode(){}
    public void pressSelectMode(){}
    public void confirmSelectMode(){}
    public void exitSelectMode(){}

    // Time Setting
    public void nextTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestPointNextTimeSection(); }
    public void increaseTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestIncreaseTimeSection(); }
    public void decreaseTimeSection(){ ((TimeSetting)this.menu.get(this.currMode)).requestDecreaseTimeSection(); }
    public void pressResetSecond(){ ((TimeSetting)this.menu.get(this.currMode)).requestResetSecond(); }

    // Stopwatch
    public void pressStartStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestStartStopwatch(); }
    public void pressStopStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestStopStopwatch(); }
    public void pressSplitStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestSplitStopwatch(); }
    public void pressResetStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestResetStopwatch(); }

    // Timer
    public void enterSetTimerTime(){ ((Timer)this.menu.get(this.currMode)).requestTimerTime(); }
    public void nextTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestNextTimerTimeSection(); }
    public void increaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestIncreaseTimerTimeSection(); }
    public void decreaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestDecreaseTimerTimeSection(); }
    public void exitSetTimerTime(){ ((Timer)this.menu.get(this.currMode)).requestExitSetTimerTime(); }
    public void pressStartTimer(){ ((Timer)this.menu.get(this.currMode)).changeStatus(1); }
    public void pressStopTimer(){ ((Timer)this.menu.get(this.currMode)).changeStatus(0); }
    public void pressStopRingingTimer(){ ((Timer)this.menu.get(this.currMode)).ringOff(); }

    // Alarm
    public void enterSetAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).requestSettingAlarm(); }
    public void nextAlarmTimeSection(){}
    public void increaseAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).increaseSection(); }
    public void decreaseAlarmTime(){ ((Alarm)this.menu.get(this.currMode)).decreaseSection(); }
    public void enterSetAlarmFrequency(){}
    public void nextFrequencySection(){}
    public void increaseCount(){}
    public void increaseFrequency(){}
    public void decreaseCount(){}
    public void decreaseFrequency(){}
    public void enterSetAlarmBell(){}
    public void nextBell(){}
    public void prevBell(){}
    public void pressNextAlarm(){ ((Alarm)this.menu.get(this.currMode)).requestNextAlarm(); }
    public void pressStopRingingAlarm(){ ((Alarm)this.menu.get(this.currMode)).requestStopRinging(); }
    public void pressAlarmOnOff(){ ((Alarm)this.menu.get(this.currMode)).requestAlarmOnOff(); }

    // Worldtime
    public void nextWorldtimeCity(){ ((Worldtime)this.menu.get(this.currMode)).nextCity(); }
    public void prevWorldtimeCity(){ ((Worldtime)this.menu.get(this.currMode)).prevCity(); }
    public void pressSummerTime(){ ((Worldtime)this.menu.get(this.currMode)).changeSummerTime(); }

    // Sun
    public void pressSetRise(){ ((Sun)this.menu.get(this.currMode)).requestSetRise(); }
    public void nextSunCity(){ ((Sun)this.menu.get(this.currMode)).requestNextCity(); }
    public void prevSunCity(){ ((Sun)this.menu.get(this.currMode)).requestPrevCity(); }


    // GUI
    public class System_GUI implements ActionListener {
        private JFrame jFrame;
        private JButton[] button = new JButton[4];
        private WatchSystem system;

        // Year, Name of Menu
        public JTextField year = new JTextField();
        // day of the week, month, day
        public JTextField[] showDate = new JTextField[]{new JTextField(), new JTextField(), new JTextField()};
        // Colon between week, month, and month, day
        // It will always set the text, but disable to see
        public JTextField[] colonForDate = new JTextField[]{new JTextField(":"), new JTextField(":")};
        // Hour, Minute, Second
        public JTextField[] showTime = new JTextField[]{new JTextField(), new JTextField(), new JTextField()};
        // Colon between Hour, Minute, and Minute, Second
        // It will always set the text, but disable to see
        public JTextField[] colonForTime = new JTextField[]{new JTextField(":"), new JTextField(":")};
        // Split time, ring number, or AM/PM
        public JTextField[] subTime = new JTextField[]{new JTextField(), new JTextField(), new JTextField()};
        // Colon between Hour, Minute, and Minute, Second
        // It will always set the text, but disable to see
        public JTextField[] colonForSubTime = new JTextField[]{new JTextField(":"), new JTextField(":")};
        // City information, on off
        public JTextField cities = new JTextField();
        // Biggest Size
        private Font mainFont;
        // Middle Size
        private Font subFont;
        // Smallest Size
        private Font subsubFont;


        public System_GUI(WatchSystem system) {
            this.system = system;
            jFrame = new JFrame();
            jFrame.setResizable(false);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setBounds(300, 100, 730, 730);

            // Setting Font
            try {
                // /build/classes/java/main/DS-DIGIB.TTF
                this.mainFont = Font.createFont(Font.TRUETYPE_FONT, new File(WatchSystem.class.getResource("").getPath() + "DS-DIGI.TTF"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainFont = mainFont.deriveFont(Font.BOLD, 60f);
            subFont = mainFont.deriveFont(Font.BOLD, 45f);
            subsubFont = mainFont.deriveFont(Font.BOLD, 40f);

            for (int i = 0; i < 4; i++) button[i] = new JButton(String.valueOf((char) ('A' + i)));


            JPanel background = new JPanel() {
                public void paintComponent(Graphics g) {
                    // Display image at full size
                    g.drawImage(new ImageIcon("background.jpg").getImage(), 0, 0, null);
                    setOpaque(false);
                    super.paintComponent(g);
                }
            };
            background.setLayout(null);

            button[0].setBounds(115, 180, 70, 70);
            button[1].setBounds(500, 175, 70, 70);
            button[2].setBounds(115, 410, 70, 70);
            button[3].setBounds(500, 400, 70, 70);

            for (int i = 0; i < 4; i++) {
                button[i].setBackground(null);
                button[i].setBorderPainted(false);
                button[i].setOpaque(false);
                button[i].setContentAreaFilled(false);
                button[i].addActionListener(this);
                button[i].setForeground(Color.RED);
                background.add(button[i]);
            }
            // Year
            year.setBounds(275, 195, 140, 35);
            year.setHorizontalAlignment(JTextField.CENTER);
            // Day of the week
            showDate[0].setBounds(245, 238, 55, 40);
            // Colon between Day of week, and Month
            colonForDate[0].setBounds(300, 238, 18, 40);
            // Month
            showDate[1].setBounds(318, 238, 55, 40);
            // Colon between Month, and Date
            colonForDate[1].setBounds(373, 238, 17, 40);
            // Date
            showDate[2].setBounds(390, 238, 55, 40);

            // Hour
            showTime[0].setBounds(225, 295, 65, 55);
            // Colon between Hour, and Min
            colonForTime[0].setBounds(290, 295, 22, 55);
            // Min
            showTime[1].setBounds(312, 295, 66, 55);
            // Colon between Min, Sec
            colonForTime[1].setBounds(378, 295, 20, 55);
            // Sec
            showTime[2].setBounds(398, 295, 65, 55);

            // Hour
            subTime[0].setBounds(245, 358, 55, 40);
            // Colon between Hour, and Min
            colonForSubTime[0].setBounds(300, 358, 18, 40);
            // Min
            subTime[1].setBounds(318, 358, 55, 40);
            // Colon between Min, Sec
            colonForSubTime[1].setBounds(373, 358, 17, 40);
            // Sec
            subTime[2].setBounds(390, 358, 55, 40);

            // cities
            cities.setBounds(270, 407, 150, 35);

            for (int i = 0; i < 3; i++) {
                showDate[i].setFont(subFont);
                background.add(generalization(showDate[i]));
                showTime[i].setFont(mainFont);
                background.add(generalization(showTime[i]));
                subTime[i].setFont(subFont);
                background.add(generalization(subTime[i]));
                if (i < 2) {
                    colonForDate[i].setFont(subFont);
                    background.add(generalization(colonForDate[i]));
                    colonForTime[i].setFont(mainFont);
                    background.add(generalization(colonForTime[i]));
                    colonForSubTime[i].setFont(subFont);
                    background.add(generalization(colonForSubTime[i]));
                }
                if (i == 0) {
                    year.setFont(subsubFont);
                    background.add(generalization(year));
                    cities.setFont(subsubFont);
                    background.add(generalization(cities));
                }
            }

            jFrame.add(background);
            jFrame.setVisible(true);
        }

        public JTextField generalization(JTextField textField) {
            textField.setEditable(false);
            textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            textField.setBackground(new Color(206, 208, 205));
            textField.setForeground(Color.BLACK);
            textField.setEnabled(false);
            return textField;
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(button[0])) {           // A

            }
            if(e.getSource().equals(button[1])) {            // B

            }
            if(e.getSource().equals(button[2])) {           // C

            }
            if(e.getSource().equals(button[3])) {

            }
        }
    }
}
