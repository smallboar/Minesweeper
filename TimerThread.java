package games.minesweeper;

public class TimerThread extends Thread {
    MinesweeperGraphics minesweeper;

    public TimerThread(MinesweeperGraphics mg){
        minesweeper = mg;
    }
    @Override
    public void run(){
        while(true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(minesweeper.tick()){
                break;
            }


        }
    }
}
