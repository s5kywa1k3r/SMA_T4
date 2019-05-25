import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class WatchGUI implements ActionListener {
    // 0 : Mode Setting, 1 : RealTime, 2 : SettingTime, 3 : StopWatch, 4 : Timer, 5 : Alarm, 6 : WorldTime, 7 : Sun
    private int presentModeIndex;
    private int flag;
    private int section;
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

    private int blinkCount;

    public WatchGUI(WatchSystem system) {
        this.presentModeIndex = 0;
        this.flag = 0;
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
                cities.setHorizontalAlignment(JTextField.CENTER);
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

    public void setMode(Object mode) {
        // 0 : Mode Setting, 1 : RealTime, 2 : SettingTime, 3 : StopWatch, 4 : Timer, 5 : Alarm, 6 : WorldTime, 7 : Sun\
        switch (mode.getClass().getTypeName()) {
            case "ModeSetting" :
                presentModeIndex = 0;
                break;
            case "RealTime" :
                presentModeIndex = 1;
                break;
            case "SettingTime" :
                presentModeIndex = 2;
                break;
            case "Stopwatch" :
                presentModeIndex = 3;
                break;
            case "Timer" :
                presentModeIndex = 4;
                break;
            case "Alarm" :
                presentModeIndex = 5;
                break;
            case "Worldtime":
                presentModeIndex = 6;
                break;
            case "Sun" :
                presentModeIndex = 7;
                break;
            default: break;
        }
    }

    public void designMode(boolean isActive) {
        switch (presentModeIndex) {
            case 0:             // Mode Setting
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                subTime[0].setEnabled(isActive);
                subTime[2].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                break;
            case 1:             // RealTime
            case 2:             // SettingTime
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[1].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[0].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                // If case 1 should Blink
                break;
            case 3:             // Stopwatch
                year.setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[0].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                subTime[0].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                subTime[2].setEnabled(isActive);
                colonForSubTime[0].setEnabled(isActive);
                colonForSubTime[1].setEnabled(isActive);
                break;
            case 4:             // Timer
                year.setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[0].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                break;
            case 5:             // Alarm
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[1].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                colonForDate[1].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                subTime[0].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                subTime[2].setEnabled(isActive);
                cities.setEnabled(isActive);
                break;
            case 6:              // WorldTime
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[1].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[0].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                cities.setEnabled(isActive);
                break;
            case 7:              // Sun
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[1].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
                colonForTime[1].setEnabled(isActive);
                subTime[0].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                subTime[2].setEnabled(isActive);
                subTime[1].setEnabled(isActive);
                cities.setEnabled(isActive);
                break;
            default: break;
        }
    }

    public void realtimeGUI(String data) {
        if(++blinkCount > 100) blinkCount = 0;
        switch(presentModeIndex) {
            // 0 : Mode Setting, 1 : RealTime, 2 : SettingTime, 3 : StopWatch, 4 : Timer, 5 : Alarm, 6 : WorldTime, 7 : Sun
            case 0 :
                year.setText(" MODE ");
                showDate[0].setText(data.substring(0, 2));
                showDate[2].setText(data.substring(2, 4));
                subTime[0].setText(data.substring(4, 6));
                subTime[2].setText(data.substring(6,8));
                showTime[0].setText(data.substring(8, 10));
                showTime[1].setText(data.substring(10, 12));
                showTime[2].setText(data.substring(12));
                break;
            case 1 :
            case 2 :
                year.setText(data.substring(0, 4));
                showDate[0].setText(data.substring(4, 6));
                showDate[1].setText(data.substring(6, 8));
                showDate[2].setText(data.substring(8, 10));
                showTime[1].setText(data.substring(10, 12));
                showTime[2].setText(data.substring(12, 14));
                showTime[0].setText(data.substring(14, 16));
                subTime[1].setText(data.substring(16,18));
                if(presentModeIndex == 2 && !data.substring(18).equals(" ")) {
                    switch (Integer.parseInt(data.substring(18))) {
                        case 0: showTime[2].setText("  ");break;
                        case 1: showTime[1].setText("  ");break;
                        case 2: showTime[0].setText("  ");break;
                        case 3: showDate[2].setText("  ");break;
                        case 4: showDate[1].setText("  ");break;
                        case 5: year.setText("    ");break;
                    }
                }
                break;
            case 3 :
                year.setText("STOPWC");
                showTime[0].setText(data.substring(0, 2));
                showTime[1].setText(data.substring(2, 4));
                showTime[2].setText(data.substring(4, 6));
                subTime[0].setText(data.substring(6, 8));
                subTime[1].setText(data.substring(8, 10));
                subTime[2].setText(data.substring(10, 12));
                break;
            case 4 :
                year.setText("TIMER");
                showTime[0].setText(data.substring(0, 2));
                showTime[1].setText(data.substring(2, 4));
                showTime[2].setText(data.substring(4, 6));
                // 0: Stopped, 1: Continued, 2: Setting, 3: ringing
                break;
            case 5 :
                year.setText("REP "+data.substring(0, 1));
                showDate[0].setText("FQ");
                showDate[1].setText(data.substring(1, 3));
                showDate[2].setText(data.substring(3, 5));
                showTime[2].setText(data.substring(5, 7));
                subTime[0].setText("  R");
                subTime[1].setText("IN");
                subTime[2].setText("G"+data.substring(7, 8));
                if(data.substring(8, 9).equals("1")) cities.setText("ON");
                else cities.setText("OFF");
                showTime[1].setText(data.substring(9, 11));
                showTime[0].setText(data.substring(11, 13));
                this.flag = system.getAlarmFlag();
                this.section = system.getAlarmSection();
                if(flag != 0 && !data.substring(13).equals(" ")) {
                    switch (Integer.parseInt(data.substring(13))) {
                        case 0: showTime[2].setText("  "); break;
                        case 1: showTime[1].setText("  "); break;
                        case 2: showDate[2].setText("  "); break;
                        case 3: showDate[1].setText("  "); break;
                        case 4: year.setText("  "); break;
                        case 5:
                            subTime[0].setText("");
                            subTime[1].setText("");
                            subTime[2].setText("");
                        break;
                    }
                }
                break;
            case 6 :
                year.setText(data.substring(0, 4));
                showDate[0].setText(data.substring(4, 6));
                showDate[1].setText(data.substring(6, 8));
                showDate[2].setText(data.substring(8, 10));
                showTime[1].setText(data.substring(10, 12));
                showTime[2].setText(data.substring(12, 14));
                if(data.substring(16,18).equals("  ")) {
                    showTime[0].setText(data.substring(14, 16));
                    subTime[1].setText("");
                }
                else {
                    showTime[0].setText(data.substring(14, 16));
                    subTime[1].setText(data.substring(16, 18));
                }
                cities.setText(data.substring(18));
                break;
            case 7:
                year.setText(data.substring(0, 4));
                showDate[0].setText(data.substring(4, 6));
                showDate[1].setText(data.substring(6, 8));
                showDate[2].setText(data.substring(8, 10));
                if(data.substring(10, 11).equals("1")) {
                    subTime[0].setText("");
                    subTime[1].setText("S E");
                    subTime[2].setText("T");
                }
                else  {
                    subTime[0].setText("  R");
                    subTime[1].setText("I S");
                    subTime[2].setText("E");
                }
                if(data.substring(11, 13).equals("  ")) showTime[0].setText("");
                else showTime[0].setText(data.substring(11, 13));
                showTime[1].setText(data.substring(13,15));
                showTime[2].setText(data.substring(15,17));
                cities.setText(data.substring(17));
                break;
            default: break;
        }
    }

    public void actionPerformed(ActionEvent e) {

        int index = -1;
        if(e.getSource() == button[0]) { index = 0; }
        if(e.getSource() == button[1]) { index = 1; }
        if(e.getSource() == button[2]) { index = 2; }
        if(e.getSource() == button[3]) { index = 3; }

        try {
            reaction(index);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public void reaction(int buttonIndex) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        switch(presentModeIndex) {
            case 0 : // Mode Setting
                switch(buttonIndex){
                    case 0: system.pressConfirmSelectMode();break;
                    case 1: system.pressSelectMode();break;
                    case 2: system.pressChangeMode();break;
                    case 3: system.pressNextMode();break;
                    default:break;
                }
                break;
            case 1 : // RealTime
                switch(buttonIndex){
                    case 0 : system.enterModeSetting(); break;
                    //case 1 : break;
                    case 2 : system.pressChangeMode();break;
                    case 3 : system.pressShowType();break;
                    default: break;
                }
                break;

            case 2 : // SettingTime
                this.flag = system.getSettingTimeFlag();
                switch(buttonIndex){
                    case 0 : system.nextTimeSection();break;
                    case 1 :
                        if(this.flag == 0) system.pressResetSecond();
                        else system.increaseTimeSection();
                        break;
                    case 2 :
                        system.exitSettingTime();
                        system.pressChangeMode();
                        break;
                    case 3 :
                        if(this.flag == 0) system.pressResetSecond();
                        else system.decreaseTimeSection();break;
                    default: break;
                }
                break;

            case 3 : // Stopwatch  0 : Stopped 1: Continued
                this.flag = system.getStopwatchFlag();
                switch(buttonIndex){
                    case 0 : system.pressSplitStopwatch();break;
                    case 1 :
                        if(this.flag == 0) system.pressStartStopwatch();
                        else system.pressStopStopwatch();
                        break;
                    case 2 : system.pressChangeMode();break;
                    case 3 : system.pressResetStopwatch();break;
                    default: break;
                }
                break;
            case 4 : // Timer
                this.flag = system.getTimerFlag();
                switch(buttonIndex){
                    case 0 :
                        if(this.flag == 0) system.enterSetTimerTime();
                        else if(this.flag == 2)system.nextTimerTimeSection();
                        break;
                    case 1 :
                        if(this.flag == 0) system.pressStartTimer();
                        else if(this.flag == 1) system.pressStopTimer();
                        else if(this.flag == 3) system.pressStopRingingTimer();
                        else system.increaseTimerTimeSection();
                        break;
                    case 2 :
                        if(flag == 2) system.exitSetTimerTime();
                        else system.pressChangeMode();
                        break;
                    case 3:
                        if(flag == 2) system.decreaseTimerTimeSection();
                        else system.pressResetTimer();
                        break;
                    default: break;
                }
                break;
            case 5 : // Alarm
                this.flag = system.getAlarmFlag();
                switch(buttonIndex){
                    case 0 :
                        if(flag == 0) system.enterSetAlarmTime();
                        else system.nextAlarmTimeSection();
                        break;
                    case 1 :
                        if(flag == 0) system.pressAlarmOnOff();
                        else if(flag == 4) system.pressStopRingingAlarm();
                        else system.increaseAlarmTime();
                        break;
                    case 2 :
                        if(flag != 0) system.exitSetAlarmSetting();
                        else system.pressChangeMode();
                        break;
                    case 3 :
                        if(flag == 0) system.pressNextAlarm();
                        else system.decreaseAlarmTime();
                        break;
                    default: break;
                }
                break;
            case 6 : // Worldtime
                switch(buttonIndex){
                    //case 0 : break;
                    case 1 : system.prevWorldtimeNation();break;
                    case 2 : system.pressChangeMode();break;
                    case 3 : system.nextWorldtimeNation();break;
                    default: break;
                }
                break;
            case 7 : // Sun
                switch(buttonIndex){
                    case 0 : system.pressSetRise();break;
                    case 1 : system.prevSunNation();break;
                    case 2 : system.pressChangeMode();break;
                    case 3 : system.nextSunNation();break;
                    default: break;
                }
                break;
            default: break;
        }
    }
}
