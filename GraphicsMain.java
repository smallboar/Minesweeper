package games.minesweeper;

import javax.swing.JFrame;
public class GraphicsMain {
    public static void main(String[] args){
        MinesweeperGraphics g1 = new MinesweeperGraphics();
        TimerThread timer = new TimerThread(g1);
        JFrame frame = new JFrame();
        frame.add(g1);
        frame.pack();
        frame.getContentPane().addMouseListener(g1);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer.start();
    }
}


