/*
package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

*/
/**
 * Created by poesd_000 on 05/01/2016.
 *//*

public class UnderBar extends JScrollPane {


    private JPanel content;
    private List<ClassBlock> classblock = new LinkedList<ClassBlock>();

    public UnderBar() {
        super(new JPanel(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        content = (JPanel) this.getViewport().getComponents()[0];
        content.setBackground(Color.yellow);
    }

    public void setClassBlocks(ClassBlock[] in) {
        for (ClassBlock i : classblock) {
            content.remove(i);
        }
        classblock.removeAll(classblock);
        for (int i = 0; i<in.length; i++) {
            classblock.add(in[i]);
            content.add(in[i]);
        }
    }



}
*/
