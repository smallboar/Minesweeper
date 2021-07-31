package games.minesweeper;/*
I was too lazy to add a restart button for the game and an input spot for row/column/mine count,
you can add it if you want

You'll also have to change the assetPath variable to your own files named mine.png and flag.png to play

It wasn't really written that efficiently, since this was essentially my first project, so there can
be a lot of improvements on this, but I'll be working on my next project now, I just used this to
practice some basic Java skills as well as learn basic Java graphics.
*/


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.Toolkit;

public class MinesweeperGraphics extends JPanel implements MouseListener{

    //Google Minesweeper uses these values:
    //Beginner: 8x10 with 10 mines  Medium: 14x18 with 40 mines  Hard: 20x24 with 99 mines

    int rowCount = 20;
    int columnCount = 24;
    int numMines = 99;
    final char flag = 9872;
    final char unexplored = ' ';
    final char mineSymbol = 'M';
    final char explodedMine = 164;
    char[][] realBoard;
    char[][] userBoard;
    public int cellLength = 35;
    public int headerHeight = 50;
    public int frameWidth = columnCount*cellLength;
    public int frameHeight = rowCount*cellLength + headerHeight;
    boolean isGameOver = false;
    final Color openedBackgroundColor = new Color(255,229,204);
    final Color unopenedBackgroundColor = new Color(229,255,204);
    final Color headerBackgroundColor = new Color(51,102,0);
    final String assetPath = "C:\\Users\\Willi\\IdeaProjects\\Summer\\assets\\";
    boolean isGameStarted = false;
    long startTime;
    double currentGameTimeSec = 0.0;


    public boolean tick(){
        if(isGameOver){
            return true;
        }
        if(isGameStarted) {
            currentGameTimeSec = (System.currentTimeMillis() - startTime)/1000.0;
            repaint();
        }
        return false;
    }

    public MinesweeperGraphics(){
        setSize(new Dimension(frameWidth, frameHeight));
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        realBoard = createBoard(numMines);
        userBoard = new char[rowCount][columnCount];
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                userBoard[r][c] = unexplored;
            }
        }
        printBoard(userBoard);
        printBoard(realBoard);
    }

    private void drawUserBoard(Graphics g){
        System.out.println();
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                char v = userBoard[r][c];
                int cellX = cellLength*c + cellLength/2;
                int cellY = cellLength*r + headerHeight + cellLength/2;
                if(v == flag){
                    fillCell(g,r,c,unopenedBackgroundColor);
                    g.setColor(Color.red);
                    Image image = Toolkit.getDefaultToolkit().getImage(assetPath+"flag.png");
                    g.drawImage(image,cellX - cellLength/2 +8,cellY - cellLength/2 + 6,
                            cellLength - 10, cellLength - 10,this);
                    g.setColor(Color.black);
                }
                else if(v == '0'){
                    fillCell(g,r,c,openedBackgroundColor);                    
                    g.setColor(Color.black);
                }
                else if(v == explodedMine){
                    fillCell(g,r,c,Color.red);
                    g.setColor(Color.black);
                    Image image = Toolkit.getDefaultToolkit().getImage(assetPath+"mine2.png");
                    g.drawImage(image,cellX - cellLength/2,cellY - cellLength/2,
                            cellLength, cellLength,this);
                }
                else {
                    if (v == unexplored) {
                        fillCell(g,r,c,unopenedBackgroundColor);
                    }
                    else{
                        fillCell(g,r,c,openedBackgroundColor);
                    }
                    g.setColor(Color.black);
                    g.drawString("" + v,cellX,cellY);
                }
            }
        }
    }

    private void drawTimer(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("" + currentGameTimeSec, frameWidth/5, 33);
        Image image = Toolkit.getDefaultToolkit().getImage(assetPath+"stopwatch.png");
        g.drawImage(image, frameWidth/5 - 33, 9,cellLength/2 + 10, cellLength/2 + 13,this);

    }

    public void drawHeader(Graphics g) {
        if(!isGameOver) {
            g.setColor(headerBackgroundColor);
            g.fillRect(0, 0, frameWidth, headerHeight);
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Minesweeper", frameWidth / 2 - 62, 33);
            int flagsLeft = numMines - flagCount();
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            Image image = Toolkit.getDefaultToolkit().getImage(assetPath+"flag.png");
            g.drawImage(image, frameWidth*3/4 ,12,cellLength/2 + 10, cellLength/2 + 10,this);
            g.drawString(" "+flagsLeft, frameWidth * 3 / 4 + 20, 33);
        }
        else{
            int x = hasWon();
            if(x == WON){
                g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                g.setColor(Color.white);
                g.drawString("Congratulations, you win!",frameWidth/2 - 80,30 );
            }
            for(int r = 0; r < rowCount; r++){
                for(int c = 0; c < columnCount; c++){
                    if(userBoard[r][c] == explodedMine){
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                        g.setColor(Color.white);
                        g.drawString("You hit a mine. Better luck next time.",frameWidth/2 - 80,30 );
                        repaint();
                    }
                }
            }

        }
        drawTimer(g);
    }


    @Override
    public void paint(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0,0,frameWidth,frameHeight);
        g.setColor(Color.black);
        drawUserBoard(g);
        drawGrid(g);
        drawHeader(g);
    }

    private void drawGrid(Graphics g){
        g.fillRect(0,0,frameWidth,headerHeight);
        for(int i = 1; i < columnCount; i++){
            g.drawLine(i*cellLength,headerHeight,i*cellLength,frameHeight);
        }
        for(int i = 1; i < rowCount; i++){
            g.drawLine(0,i*cellLength+headerHeight,frameWidth,i*cellLength+headerHeight);
        }
    }

    private char[][] createBoard(int mineCount){
        char[][] res = new char[rowCount][columnCount];
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                res[r][c] = '0';
            }
        }
        while(mineCount > 0){
            int x = (int) Math.floor(Math.random()*(rowCount));
            int y = (int) Math.floor(Math.random()*(columnCount));
            if(res[x][y] != mineSymbol){
                res[x][y] = mineSymbol;
                //row above
                incCell(res,x-1,y-1);
                incCell(res,x-1,y);
                incCell(res,x-1,y+1);
                //same row
                incCell(res,x,y-1);
                incCell(res,x,y+1);
                //row below
                incCell(res,x+1,y-1);
                incCell(res,x+1,y);
                incCell(res,x+1,y+1);
                mineCount--;
            }
        }
        return res;
    }

    private void incCell(char[][] board, int r, int c){
        if(r >=0 && c >=0 && r < rowCount && c < columnCount && board[r][c] != mineSymbol){
            board[r][c]++;
        }
    }

    public void printBoard(char[][] board){
        int count = 0;
        System.out.print("   ");
        for(int i = 0; i < columnCount; i++){
            if(i < 10) {
                System.out.print(ANSI_GREEN + i + "  " + ANSI_RESET);
            }
            else{
                System.out.print(ANSI_GREEN + i + " " + ANSI_RESET);
            }
        }
        System.out.println();
        for(int r = 0; r < rowCount; r++){
            if(count < 10) {
                System.out.print(ANSI_GREEN + count + "  " + ANSI_RESET);
            }
            else{
                System.out.print(ANSI_GREEN + count + " " + ANSI_RESET);
            }
            for(int c = 0; c < columnCount; c++){
                char v = board[r][c];
                if(v == flag){
                    System.out.print(ANSI_RED + flag + ANSI_RESET + " ");
                }
                else if(v == '0'){
                    System.out.print(ANSI_BLACK + "-  " + ANSI_RESET);
                }
                else if(v == explodedMine){
                    System.out.print(ANSI_RED_BACKGROUND + ANSI_BLACK + v + " " + ANSI_RESET);
                }
                else {
                    System.out.print(v + "  ");
                }
            }
            System.out.println();
            count++;
        }
    }

    private int mouseXToColumn(MouseEvent e){
        return e.getX()/cellLength;
    }

    private int mouseYToRow(MouseEvent e){
        if (e.getY() - headerHeight < 0) {
           return -1;
        }
        return (e.getY()-headerHeight)/cellLength;
    }

    private void openCell(int r, int c){
        if(r >= 0 && c >= 0 && r < rowCount && c < columnCount && userBoard[r][c] == unexplored) {
            userBoard[r][c] = realBoard[r][c];
            if(userBoard[r][c] == '0'){
                openAdjacentCells(userBoard,realBoard,r,c);
            }
        }
    }
    private void openAdjacentCells(char[][] userBoard, char[][] realBoard, int x, int y){
        //row above
        openCell(x-1,y-1);
        openCell(x-1,y);
        openCell(x-1,y+1);
        //same row
        openCell(x,y-1);
        openCell(x,y+1);
        //row below
        openCell(x+1,y-1);
        openCell(x+1,y);
        openCell(x+1,y+1);
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    int count = 0;
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int row = mouseYToRow(e);
        int column = mouseXToColumn(e);
        if (row < 0 || column < 0){
            return;
        }
        if (!isGameStarted) {
            startTime = System.currentTimeMillis();
        }
        isGameStarted = true;
        if(e.getButton() == MouseEvent.BUTTON3){
            if(userBoard[row][column] == flag){
                userBoard[row][column] = unexplored;
            }
            else{
                userBoard[row][column] = flag;
            }
        }
        else if(!isGameOver) {
            openCell(row,column);
        }
        int x = hasWon();
        if (x == WON) {
            System.out.println("You win!");
            isGameOver = true;
        }
        if (x == LOST){
            System.out.println("You hit a mine. Better luck next time.");
            userBoard[row][column] = explodedMine;
            isGameOver = true;
        }
        repaint();
        printBoard(userBoard);
        printBoard(realBoard);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    final int WON = 1;
    final int LOST = -1;
    final int ONGOING = 0;

    private int hasWon(){
        int openCells = 0;
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                if(userBoard[r][c] != unexplored && userBoard[r][c] != flag){
                    openCells++;
                }
                if(userBoard[r][c] == 'M' ){
                    userBoard[r][c] = explodedMine;
                    return LOST;
                }
            }
        }
        if (rowCount*columnCount-openCells-numMines == 0){
            return WON;
        }
        else {
            return ONGOING;
        }
    }
    private int flagCount(){
        int res = 0;
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                if(userBoard[r][c] == flag){
                    res++;
                }
            }
        }
        return res;
    }

    private void fillCell(Graphics g, int r, int c, Color color) {
        g.setColor(color);
        g.fillRect(c*cellLength,r*cellLength + headerHeight, cellLength,cellLength);
        g.setColor(Color.black);

    }
}
