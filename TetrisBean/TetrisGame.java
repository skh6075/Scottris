package TetrisBean;

import java.util.Vector;
import java.awt.Point;

public class TetrisGame{

    private final EventHandler fEventHandler;

    private final TetrisBoard  fBoard;

    private TetrisPiece  fCurrPiece;

    private Thread       fGameThread;

    private int          fScore;

    private int          fTotalLines;

    private int          fDelay;

    private boolean      fPlaying;

	private boolean      fPaused;


    public TetrisGame() {
        fEventHandler = new EventHandler();
    	fBoard        = new TetrisBoard(10, 20);
        fPlaying      = false;
    }

    public void startGame() {
        if (!fPlaying) {
            fBoard.resetBoard();
        	fTotalLines = 0;
            fScore      = 0;
            fDelay      = 500;
            fPlaying    = true;
            fPaused     = false;
            fCurrPiece  = null;
            fEventHandler.fireScoreEvent();            
            fEventHandler.fireGameEvent(new GameEvent(GameEvent.START));

        	fGameThread = new GameThread();
        	fGameThread.start();
        }
    }

    public void stopGame() {
        fPlaying = false;
        fEventHandler.fireGameEvent(new GameEvent(GameEvent.END));
    }

    public TetrisPiece getCurrentPiece() {
        return fCurrPiece;
    }

    public void setCurrentPiece(TetrisPiece currPiece) {
        fCurrPiece = currPiece;
    }

    public boolean isPaused() {
        return fPaused;
    }

    public void setPaused(boolean pauseIt) {
        if (fPlaying)
            fPaused = pauseIt;
    }

    public boolean isPlaying() {
        return fPlaying;
    }

    public void setPlaying(boolean playing) {
        if (playing)
            fPlaying = false;
        else
            startGame();
    }

    public boolean move(int direction) {
        boolean result = false;

    	if (fCurrPiece != null && fPlaying && !fPaused) {
    	    if (direction == TetrisPiece.DOWN || direction == TetrisPiece.FALL) {
                if (!fCurrPiece.move(direction)) fCurrPiece = null;
                else result = true;
            }
            else result = fCurrPiece.move(direction);
        }

        return result;
    }

    public int getScore() {
        return fScore;
    }

    public void setScore(int score) {
        fScore = score;
    }

    public int getTotalLines() {
        return fTotalLines;
    }

    public void setTotalLines(int totalLines) {
        fTotalLines = totalLines;
    }

    public void addBoardListener(BoardListener listener) {
        fEventHandler.addBoardListener(listener);
    }

    public void removeBoardListener(BoardListener listener) {
        fEventHandler.removeBoardListener(listener);
    }

    public void addScoreListener(ScoreListener listener) {
        fEventHandler.addScoreListener(listener);
    }

    public void removeScoreListener(ScoreListener listener) {
        fEventHandler.removeScoreListener(listener);
    }   
    
    private class GameThread extends Thread {
        public void run() {
            while(fPlaying) {
                if (!fPaused) {
                    if (fCurrPiece == null) {
                        int completeLines = 0;

                        for (int rows = fBoard.getRows() - 1; rows >= 0; rows--) {
                            boolean same = true;

                            for (int cols = 0; cols < fBoard.getColumns(); cols++) {
                                if (fBoard.getPieceAt(cols, rows) == TetrisBoard.EMPTY_BLOCK) 
                                    same = false;
                            }

                            if (same) {
                                fBoard.removeRow(rows);
                                rows++;
                                completeLines++;
                                fTotalLines++;

                                if (fTotalLines == 5) fDelay = 400;
                                if (fTotalLines == 10) fDelay = 300;
                                if (fTotalLines == 30) fDelay = 200;
                                if (fTotalLines == 40) fDelay = 150;
                                if (fTotalLines == 50) fDelay = 120;
                            }
                        }

                        if (completeLines > 0) {
                            fScore += completeLines * completeLines * 100;
                            fEventHandler.fireScoreEvent();
                        }

                        fCurrPiece = TetrisPiece.getRandomPiece(fBoard);
                        fCurrPiece.setCentrePoint(new Point(fBoard.getColumns() / 2, 1));
                        if (fBoard.willFit(fCurrPiece)) {
                            fBoard.addPiece(fCurrPiece, true);
                        } else {
                            fBoard.addPiece(fCurrPiece, true);
                            stopGame();
                            break;
                        }
                    } else {
                        move(TetrisPiece.DOWN);
                    }
                }

                if (fCurrPiece != null)
                    try {
                        sleep(fDelay);
                    } catch (InterruptedException e) {
                        System.err.println("Exception e: " + e.getMessage());
                    }
            }
        }
    }

    private class EventHandler {

        private final Vector fGameListeners     = new Vector();

        private final Vector fBoardListeners    = new Vector();

        private final Vector fScoreListeners    = new Vector();


        public void addGameListener(GameListener listener) {
            fGameListeners.addElement(listener);
        }
    
        public void removeGameListener(GameListener listener) {
            fGameListeners.removeElement(listener);
        }
    
        public void fireGameEvent(GameEvent event) {
            for (int count = 0; count < fGameListeners.size(); count++) {
                ((GameListener)fGameListeners.elementAt(count)).gameChange(event);
            }
        }
    
        public void addBoardListener(BoardListener listener) {
            fBoard.addBoardListener(listener);
        }
    
        public void removeBoardListener(BoardListener listener) {
            fBoard.removeBoardListener(listener);
        }
    
        public void addScoreListener(ScoreListener listener) {
            fScoreListeners.addElement(listener);
        }
    
        public void removeScoreListener(ScoreListener listener) {
            fScoreListeners.removeElement(listener);
        }
    
        public void fireScoreEvent() {
            for (int count = 0; count < fScoreListeners.size(); count++) {
                final ScoreEvent event = new ScoreEvent(fScore);

                ((ScoreListener)fScoreListeners.elementAt(count)).scoreChange(event);
            }
        }
    }
}
