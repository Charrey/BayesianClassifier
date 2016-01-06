package gui;

import main.DataManager2;
import main.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by poesd_000 on 06/01/2016.
 */
public class MiddleRightScreen extends JPanel {


    private final logScreen logscreen;
    private final consoleScreen consolescreen;

    public MiddleRightScreen() {
        this.setLayout(new BorderLayout());
        logscreen = new logScreen();
        logscreen.setBounds(0,0,810,500);
        consolescreen = new consoleScreen();
        System.out.println(logscreen.getPreferredSize());
        add(logscreen, BorderLayout.CENTER);
        add(consolescreen, BorderLayout.SOUTH);
    }

    public void log(String s) {
        logscreen.addText(s);
    }


    public class logScreen extends JScrollPane {
        JPanel content;
        JTextArea jta = new JTextArea();

        public logScreen() {
            super(new JPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            content = (JPanel) this.getViewport().getComponents()[0];
            content.setLayout(new GridLayout(1,1));
            jta.setMargin(new Insets(30,30,30,30));
            content.add(jta);
            this.setPreferredSize(new Dimension(800,500));
            addText("Initialized.");
        }
        public void addText(String text) {


            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");


            jta.setText(jta.getText()+"\n"+"["+ft.format(dNow)+"]      "+text);
            content.setPreferredSize(jta.getPreferredSize());
        }
    }

    public class consoleScreen extends JPanel {
        private final JButton button;
        private final JTextField field;

        public consoleScreen() {
            this.setLayout(new BorderLayout());
            button = new JButton("Submit");
            field = new JTextField();
            add(field, BorderLayout.CENTER);
            add(button, BorderLayout.EAST);
            this.setPreferredSize(new Dimension(1920, 50));
            this.setMaximumSize(new Dimension(1920, 50));
            this.setMinimumSize(new Dimension(1920, 50));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DataManager2.INSTANCE.addDocumentToTrainingsset(Word.sanitize(MainFrame.lastchecked), field.getText(), true);
                        MainFrame.get().log("Succesfully learned.");
                    } catch (NullPointerException e1) {
                        MainFrame.get().log("No classification yet.");
                    }
                }
            });
        }
    }


}
