package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by poesd_000 on 05/01/2016.
 */
public class MainFrame extends JFrame {


   public static String lastchecked = null;



    private static MainFrame mf;

    private final TopBar tp;
    private final JPanel middlescreen = new JPanel();
    private final MiddleLeftScreen mls;
    private final MiddleRightScreen mrs;

    public static void main(String[] args) {
        MainFrame.get();
    }

    private MainFrame() {
        this.setLayout(new BorderLayout());

        mls = new MiddleLeftScreen();
        mrs = new MiddleRightScreen();

        middlescreen.setLayout(new GridLayout(1,2));
        middlescreen.add(mls);
        middlescreen.add(mrs);
        this.add(middlescreen, BorderLayout.CENTER);
        // = new UnderBar();
        //this.add(ub, BorderLayout.SOUTH);
        tp = new TopBar();
        this.add(tp, BorderLayout.NORTH);
        this.setVisible(true);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    public static MainFrame get() {
        if (mf==null) {
            mf = new MainFrame();
        }
        return mf;
    }

    public MiddleLeftScreen getMiddleLeftScreen() {
        return mls;
    }

    public MiddleLeftScreen getMiddleScreen() {
        return mls;
    }

    public void log(String s) {
        mrs.log(s);
    }
}
