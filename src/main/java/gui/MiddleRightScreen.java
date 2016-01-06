package gui;

import main.DataManager2;
import main.MathManager;
import main.Word;
import main.featureObject;

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
            addText("Type \"classify <text>\" in the console to classify without submitting a file.");
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
                        MainFrame.lastchecked=null;

                        MainFrame.get().log("Succesfully learned.");
                    } catch (NullPointerException e1) {
                        MainFrame.get().log("No classification done to use that feedback with");
                        if (field.getText().toLowerCase().startsWith("classify ")) {


                            MainFrame.get().log("Classifying Text:\n"+field.getText());

                            boolean chi = MainFrame.get().getMiddleLeftScreen().use_chi.isSelected();
                            String min_occur = MainFrame.get().getMiddleLeftScreen().min_occur.getText();
                            String min_doc_occur = MainFrame.get().getMiddleLeftScreen().min_doc_occur.getText();
                            String k = MainFrame.get().getMiddleLeftScreen().k.getText();
                            String chivalue = MainFrame.get().getMiddleLeftScreen().chivalue.getText();

                            if (min_occur == null || !MiddleLeftScreen.representsInteger(min_occur)) {
                                MainFrame.get().log("Min Occurance input invalid. Defaulting to 0.");
                                min_occur = "0";
                            }

                            if (min_doc_occur == null || !MiddleLeftScreen.representsInteger(min_doc_occur)) {
                                MainFrame.get().log("Min Document Occurance input invalid. Defaulting to 0.");
                                min_doc_occur = "0";
                            }

                            if (k == null || !MiddleLeftScreen.representsInteger(k)) {
                                MainFrame.get().log("K input invalid. Defaulting to 1.");
                                k = "1";
                            }

                            if (chivalue == null || !MiddleLeftScreen.representsInteger(chivalue)) {
                                MainFrame.get().log("Chi value input invalid. Defaulting to -1.");
                                chivalue = "-1";
                            }

                            featureObject feature = new featureObject(MainFrame.get().getMiddleLeftScreen().use_chi.isSelected(),
                                    Integer.parseInt(min_occur),
                                    Integer.parseInt(min_doc_occur),
                                    Integer.parseInt(k),
                                    Integer.parseInt(chivalue),
                                    MainFrame.get().getMiddleLeftScreen().remove_stop.isSelected() );

                            System.out.println(feature);


                            String classification = MathManager.getClassificationOfDocument(field.getText().substring("classify ".length()), feature, null);
                            MainFrame.lastchecked = field.getText().substring("classify ".length());
                            MainFrame.get().log("Classified as: \""+classification+"\". For correction, please input the correct classification in the console.");



                        }
                    }
                    field.setText("");
                }
            });
        }
    }


}
