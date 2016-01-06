package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * Created by poesd_000 on 05/01/2016.
 */
public class TopBar extends JPanel {

    private JButton FILE;

    public TopBar() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        final JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(new AbstractAction("Classify File") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showOpenDialog(MainFrame.get());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    MainFrame.get().getMiddleScreen().setClassifyFile(file);
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction("Classify Folder") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showOpenDialog(MainFrame.get());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file.isDirectory()) {
                        MainFrame.get().getMiddleScreen().setClassifyFolder(file);
                    }
                }
            }
        }));
        popup.add(new JMenuItem(new AbstractAction("Set build source") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showOpenDialog(MainFrame.get());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file.isDirectory()) {
                        MainFrame.get().getMiddleScreen().setBuildFolder(file);
                    }
                }
            }
        }));

        Component[] contents = popup.getComponents();
        for (int i = 0; i<contents.length; i++) {
            if (contents[i] instanceof JMenuItem) {
                contents[i].setPreferredSize(new Dimension(100,30));
                ((JMenuItem)contents[i]).setBorder(BorderFactory.createLineBorder(Color.gray,1));
                ((JMenuItem)contents[i]).setFont(new Font("Arial", Font.PLAIN, 13));
                ((JMenuItem)contents[i]).setBackground(Color.white);
                ((JMenuItem)contents[i]).setPreferredSize(new Dimension(140,30));
            }
        }

        FILE = new JButton("File");
        FILE.setPreferredSize(new Dimension(100,30));
        FILE.setMinimumSize(new Dimension(100, 30));
        FILE.setMaximumSize(new Dimension(100, 30));
        FILE.setBackground(Color.gray);
        FILE.setForeground(Color.white);
        FILE.setFont(new Font("Arial", Font.PLAIN, 18));
        FILE.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getComponent().getX(), (int) (e.getComponent().getY()+e.getComponent().getSize().getHeight()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                FILE.setBackground(Color.lightGray);
                FILE.setForeground(Color.black);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                FILE.setBackground(Color.gray);
                FILE.setForeground(Color.white);

            }
        });
        FILE.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

            }
        });



        add(FILE);
        this.setPreferredSize(new Dimension(1920,30));
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
    }
}
