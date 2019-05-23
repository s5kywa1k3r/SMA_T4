import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class WatchGUI implements ActionListener {
    // 0 : Mode Setting, 1 : RealTime, 2 : TimeSetting, 3 : StopWatch, 4 : Timer, 5 : Alarm, 6 : WorldTime, 7 : Sun
    private int presentModeIndex;
    private Object presentMode;
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

    public WatchGUI(WatchSystem system) {
        this.presentModeIndex = 0;
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

    public void setMode(Object mode) {
        // 0 : Mode Setting, 1 : RealTime, 2 : TimeSetting, 3 : StopWatch, 4 : Timer, 5 : Alarm, 6 : WorldTime, 7 : Sun
        switch (mode.getClass().getTypeName()) {
            case "ModeSetting" :
                presentModeIndex = 0;
            case "RealTime" :
                presentModeIndex = 1;
                break;
            case "TimeSetting" :
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
        presentMode = mode;
    }

    public void designMode(boolean isActive) {
        switch (presentModeIndex) {
            case 0:             // Mode Setting
                break;
            case 1:             // RealTime
            case 2:             // TimeSetting
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
                break;
            case 5:             // Alarm
                year.setEnabled(isActive);
                showDate[0].setEnabled(isActive);
                showDate[1].setEnabled(isActive);
                showDate[2].setEnabled(isActive);
                colonForDate[1].setEnabled(isActive);
                showTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                subTime[0].setEnabled(isActive);
                showTime[1].setEnabled(isActive);
                showTime[2].setEnabled(isActive);
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
            default: break;
        }
    }

    public void realtimeGUI() {
        switch(presentModeIndex) {
            case 0 :                                // Mode Setting
                break;
            case 1 :                                // RealTime
                Calendar time = ((RealTime)presentMode).requestRealTime();
                year.setText(time.get(Calendar.YEAR)+"");
                // Maybe that part is going to be a problem
                showDate[0].setText(time.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH).substring(0, 2));
                showDate[1].setText((time.get(Calendar.MONTH) < 9 ? "0" : "")+(time.get(Calendar.MONTH)+1));
                showDate[2].setText((time.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + time.get(Calendar.DAY_OF_MONTH));
                if(((RealTime) presentMode).isIs24H()) {
                    showTime[0].setText((time.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + time.get(Calendar.HOUR_OF_DAY));
                    subTime[1].setText("");
                }
                else {
                    showTime[0].setText((time.get(Calendar.HOUR) < 10 ? "0" : "")+time.get(Calendar.HOUR));
                    subTime[1].setText(time.get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
                }
                showTime[1].setText((time.get(Calendar.MINUTE) < 10 ? "0" : "")+time.get(Calendar.MINUTE));
                showTime[2].setText((time.get(Calendar.SECOND) < 10 ? "0" : "")+time.get(Calendar.SECOND));
                break;

            case 2 :

                break;
            case 3 :

                break;
            case 4 :

                break;
            case 5 :

                break;
            case 6 :

                break;
            case 7:
                break;
            default: break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button[0]) {            // A
            reaction(0);
        }
        if(e.getSource() == button[1]) {            // B
            reaction(1);
        }
        if(e.getSource() == button[2]) {            // C
            reaction(2);
        }
        if(e.getSource() == button[3]) {            // D
            reaction(3);
        }
    }

    public void reaction(int buttonIndex) {
        switch(presentModeIndex) {
            case 0 :                                // Mode Setting
                break;
            case 1 :                                // RealTime
                if(buttonIndex == 3)
                    system.pressShowType();
                break;
            case 2 :

                break;
            case 3 :

                break;
            case 4 :

                break;
            case 5 :

                break;
            case 6 :

                break;
            case 7:
                break;
            default: break;
        }
    }
}
