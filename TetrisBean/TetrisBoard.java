package TetrisBean;

import java.util.*;
import java.awt.*;

public class TetrisBoard{

    public static final int EMPTY_BLOCK = -1;

    private final Vector  fBoardListeners = new Vector();

    private int[][] fMatrix;

    private int     fColumns;

    private int     fRows;


    public TetrisBoard(int cols, int rows) {
        fColumns = cols;
        fRows    = rows;

        resetBoard();
    }

    public void resetBoard() {
        fMatrix  = new int[fColumns][fRows];
       	
        for (int cols = 0; cols < fColumns; cols++) 
    	    for (int rows = 0; rows < fRows; rows++) 
    	    	fMatrix[cols][rows] = EMPTY_BLOCK;
    }

    public int getColumns() {
        return fColumns;
    }

    public void setColumns(int columns) {
        fColumns = columns;
        resetBoard();
    }

    public int getRows() {
        return fRows;
    }

    public void setRows(int rows) {
        fRows = rows;
        resetBoard();
    }   

    public int getPieceAt(int x, int y) {
        return fMatrix[x][y];
    }

    public void setPieceAt(int x, int y, int value) {
        fMatrix[x][y] = value;
    }

    public void addPiece(TetrisPiece piece, boolean notify) {
        if (piece != null) {
            final Point   centre = piece.getCentrePoint();
            final Point[] blocks = piece.getRelativePoints();

        	for (int count = 0; count < 4; count++) {
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;
        
        	    fMatrix[x][y] = piece.getType();
        	}
                    
        	if (notify) fireBoardEvent();
        }
    }

    public void removePiece(TetrisPiece piece) {
        if (piece != null) {
            final Point   centre = piece.getCentrePoint();
            final Point[] blocks = piece.getRelativePoints();

            for (int count = 0; count < 4; count++) {
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;
            
        	    fMatrix[x][y] = EMPTY_BLOCK;
        	}
        }
    }

    public void removeRow(int row) {
    	for (int tempRow = row; tempRow > 0; tempRow--) {
    	    for (int tempCol = 0; tempCol < fColumns; tempCol++) {
    		    fMatrix[tempCol][tempRow] = fMatrix[tempCol][tempRow - 1];
    	    }
    	}
    
    	for (int tempCol = 0; tempCol < fColumns; tempCol++) {
    	    fMatrix[tempCol][0] = EMPTY_BLOCK;
    	}
    
    	fireBoardEvent();
    }

    public boolean willFit(TetrisPiece piece) {
        boolean result = true;

        if (piece != null) {
            final Point   centre = piece.getCentrePoint();
            final Point[] blocks = piece.getRelativePoints();
    
        	for (int count = 0; count < 4 && result; count++) {
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;

        	    if (x < 0 || x >= fColumns || y < 0 || y >= fRows)
        		    result = false;
        	     
                if (result && fMatrix[x][y] != EMPTY_BLOCK) result = false;
        	}
        }

    	return result;
    }

    public void addBoardListener(BoardListener listener) {
        fBoardListeners.addElement(listener);
    }

    public void removeBoardListener(BoardListener listener) {
        fBoardListeners.removeElement(listener);
    }

    private void fireBoardEvent() {
        final BoardEvent event = new BoardEvent(this);

        for (int count = 0; count < fBoardListeners.size(); count++)
            ((BoardListener)fBoardListeners.elementAt(count)).boardChange(event);
    }
}