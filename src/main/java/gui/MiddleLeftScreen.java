package gui;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
import main.Builder2;
import main.DataManager2;
import main.MathManager;
import main.featureObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by poesd_000 on 05/01/2016.
 */
public class MiddleLeftScreen extends JPanel{

    private File classify_file = null;
    private File build_file = null;


    JLabel classifying_name = new JLabel("No classification file/folder selected.");
    JLabel build_folder = new JLabel("No build folder selected.");

    JCheckBox use_chi = new JCheckBox("Use Chi Squared");

    JLabel min_occur_label = new JLabel("Minimal occurance:");
    JTextField min_occur = new JTextField();

    JLabel min_doc_occur_label = new JLabel("Minimal document occurance:");
    JTextField min_doc_occur = new JTextField();

    JLabel k_label = new JLabel("K:");
    JTextField k = new JTextField();

    JLabel chivalue_label = new JLabel("Chi Value");
    private JTextField chivalue = new JTextField();

    private JButton build = new JButton("Build");
    private JButton classify = new JButton("Classify");

    private final Font font = new Font("Arial", Font.PLAIN, 15);


    //private final int vgap = 20;
    private final int componentsize = 40;


    public MiddleLeftScreen() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(100,100,0,0));

        classifying_name.setPreferredSize(new Dimension(800,componentsize));
        classifying_name.setMaximumSize(new Dimension(800, componentsize));
        classifying_name.setFont(font);
        classifying_name.setForeground(Color.gray);
        add(classifying_name);

        //add(Box.createVerticalStrut(vgap));

        build_folder.setPreferredSize(new Dimension(800, componentsize));
        build_folder.setMaximumSize(new Dimension(800, componentsize));
        build_folder.setFont(font);
        build_folder.setForeground(Color.gray);
        add(build_folder);

        //add(Box.createVerticalStrut(vgap));

        use_chi.setPreferredSize(new Dimension(800,componentsize));
        use_chi.setMaximumSize(new Dimension(800, componentsize));
        use_chi.setFont(font);
        add(use_chi);

       // add(Box.createVerticalStrut(vgap));

        min_occur_label.setPreferredSize(new Dimension(800,componentsize));
        min_occur_label.setFont(font);
        add(min_occur_label);


        min_occur.setPreferredSize(new Dimension(800, componentsize));
        min_occur.setMaximumSize(new Dimension(800, componentsize));
        add(min_occur);

       // add(Box.createVerticalStrut(vgap));

        min_doc_occur_label.setPreferredSize(new Dimension(800,componentsize));
        min_doc_occur_label.setFont(font);
        add(min_doc_occur_label);


        min_doc_occur.setPreferredSize(new Dimension(800, componentsize));
        min_doc_occur.setMaximumSize(new Dimension(800, componentsize));
        add(min_doc_occur);

        //add(Box.createVerticalStrut(vgap));


        k_label.setPreferredSize(new Dimension(800,componentsize));
        k_label.setFont(font);
        add(k_label);

        //k.setMinimumSize(new Dimension(800,componentsize));
        k.setPreferredSize(new Dimension(800,componentsize));
        k.setMaximumSize(new Dimension(800, componentsize));
        add(k);

        chivalue_label.setPreferredSize(new Dimension(800,componentsize));
        chivalue_label.setFont(font);
        add(chivalue_label);

        //k.setMinimumSize(new Dimension(800,componentsize));
        chivalue.setPreferredSize(new Dimension(800, componentsize));
        chivalue.setMaximumSize(new Dimension(800, componentsize));
        add(chivalue);

        add(Box.createVerticalStrut(30));

        build.setPreferredSize(new Dimension(200,componentsize));
        build.setMaximumSize(new Dimension(200, componentsize));
        build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new Builder2(build_file.getAbsolutePath(), null);
                    MainFrame.get().log("Successfully built dictionary!");
                } catch (IllegalStateException e1) {
                    MainFrame.get().log("Error while building dictionary; Please ensure file integrity!");
                }

            }
        });
        add(build);

        classify.setPreferredSize(new Dimension(200,componentsize));
        classify.setMaximumSize(new Dimension(200, componentsize));
        classify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hasValidTextInput()) {

                    if (classify_file.isFile()) {
                    String content = DataManager2.readFile(classify_file);
                        MainFrame.get().log("Classifying File:\n"+content);
                        featureObject feature = new featureObject(use_chi.isSelected(), Integer.parseInt(min_occur.getText()),Integer.parseInt(min_doc_occur.getText()), Integer.parseInt(k.getText()), Integer.parseInt(chivalue.getText()) );
                        String classification = MathManager.getClassificationOfDocument(content, feature, null);
                        MainFrame.get().log("Classified as: \""+classification+"\". For correction, please input the correct classification in the console.");

                    }
                } else {
                    MainFrame.get().log("Please ensure Integer input.");
                }
            }
        });
        add(classify);

        use_chi.setEnabled(false);
        min_occur.setEnabled(false);
        min_doc_occur.setEnabled(false);
        k.setEnabled(false);
        build.setEnabled(false);
        classify.setEnabled(false);


    }


    public void setClassifyFile(File file) {
        classify.setEnabled(true);
        use_chi.setEnabled(true);
        min_occur.setEnabled(true);
        min_doc_occur.setEnabled(true);
        k.setEnabled(true);

        if (file==null) {
            classifying_name.setText("No classification file/folder selected.");
            classifying_name.setForeground(Color.gray);
            classify_file = null;
            use_chi.setEnabled(false);
            min_occur.setEnabled(false);
            min_doc_occur.setEnabled(false);
            k.setEnabled(false);
            classify.setEnabled(false);
            return;
        }

        classify_file = file;
        classifying_name.setText("Classifying File: "+file.getAbsolutePath());
        classifying_name.setForeground(Color.black);

    }

    public void setClassifyFolder(File folder) {
        classify.setEnabled(true);
        use_chi.setEnabled(true);
        min_occur.setEnabled(true);
        min_doc_occur.setEnabled(true);
        k.setEnabled(true);

        if (folder==null) {
            classifying_name.setText("No classification file/folder selected.");
            classifying_name.setForeground(Color.gray);
            classify_file = null;
            use_chi.setEnabled(false);
            min_occur.setEnabled(false);
            min_doc_occur.setEnabled(false);
            k.setEnabled(false);
            classify.setEnabled(false);
            return;
        }

        classify_file = folder;
        classifying_name.setText("Classifying Folder: "+folder.getAbsolutePath());
        classifying_name.setForeground(Color.black);
    }

    public void setBuildFolder(File folder) {


        if (folder==null) {
            build_folder.setText("No build folder selected.");
            build_folder.setForeground(Color.gray);
            build_file = null;
            use_chi.setEnabled(false);
            min_occur.setEnabled(false);
            min_doc_occur.setEnabled(false);
            k.setEnabled(false);
            build.setEnabled(false);
            classify.setEnabled(false);
            return;
        }
        build_file = folder;
        if (classify_file!=null) {
            use_chi.setEnabled(true);
            min_occur.setEnabled(true);
            min_doc_occur.setEnabled(true);
            k.setEnabled(true);
            build.setEnabled(true);
        }
        build_folder.setText("Build folder: "+folder.getAbsolutePath());
        build_folder.setForeground(Color.black);

    }

    public boolean hasValidTextInput() {
        return representsInteger(min_occur.getText()) && representsInteger(min_doc_occur.getText()) && representsInteger(k.getText());
    }

    public static boolean representsInteger(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


}
