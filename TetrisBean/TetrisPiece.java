package TetrisBean;

import java.awt.Point;

public class TetrisPiece{

    public static final int L_PIECE   = 0;

    public static final int J_PIECE   = 1;

    public static final int I_PIECE   = 2;

    public static final int Z_PIECE   = 3;

    public static final int S_PIECE   = 4;

    public static final int O_PIECE   = 5;

    public static final int T_PIECE   = 6;

    public static final int LEFT      = 10;

    public static final int RIGHT     = 11;

    public static final int ROTATE    = 12;

    public static final int DOWN      = 13;

    public static final int FALL      = 14;

    private int     fType;

    private int     fRotation;

    private int     fMaxRotate;

    private Point   fCentrePoint = new Point();

    private Point[] fBlocks      = new Point[4];

    private final TetrisBoard fBoard;


    public TetrisPiece(int type, TetrisBoard board){
	    fType  = type;
        fBoard = board;
    	initializeBlocks();
    }

    public boolean move(int direction) {
        boolean result = true;

        if (direction == FALL) {
            boolean loop = true;
            
            while (loop) {
                fBoard.removePiece(this);
                fCentrePoint.y++; // Drop
                
                if (fBoard.willFit(this)){
                    fBoard.addPiece(this, false);
                } else {
                    fCentrePoint.y--;
                    fBoard.addPiece(this, true);
                    loop   = false;
                    result = false;
                }
            }
        } else {
            fBoard.removePiece(this);

            switch (direction) {
                case LEFT -> fCentrePoint.x--;

                case RIGHT -> fCentrePoint.x++;

                case DOWN -> fCentrePoint.y++;

                case ROTATE -> rotateClockwise();
            }

            if (fBoard.willFit(this))
                fBoard.addPiece(this, true);
            else {
                switch (direction) {
                    case LEFT -> fCentrePoint.x++;

                    case RIGHT -> fCentrePoint.x--;

                    case DOWN -> fCentrePoint.y--;

                    case ROTATE -> rotateAntiClockwise();
                }
                fBoard.addPiece(this, true);
                result = false;
            }
        }

        return result;
    }

    public Point getCentrePoint() {
        return fCentrePoint;
    }

    public void setCentrePoint(Point point) {
        fCentrePoint = point;
    }

    public Point[] getRelativePoints() {
        return fBlocks;
    }

    public void setRelativePoints(Point[] blocks) {
        if (blocks != null)
            fBlocks = blocks;
    }

    public int getType() {
        return fType;
    }

    public void setType(int type) {
        fType = type;
        initializeBlocks();
    }

    private void initializeBlocks() {
        switch (fType) {
            case I_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(-1, 0);
                fBlocks[2] = new Point(1, 0);
                fBlocks[3] = new Point(2, 0);
                fMaxRotate = 2;
            }
            case L_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(-1, 0);
                fBlocks[2] = new Point(-1, 1);
                fBlocks[3] = new Point(1, 0);
                fMaxRotate = 4;
            }
            case J_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(-1, 0);
                fBlocks[2] = new Point(1, 0);
                fBlocks[3] = new Point(1, 1);
                fMaxRotate = 4;
            }
            case Z_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(-1, 0);
                fBlocks[2] = new Point(0, 1);
                fBlocks[3] = new Point(1, 1);
                fMaxRotate = 2;
            }
            case S_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(1, 0);
                fBlocks[2] = new Point(0, 1);
                fBlocks[3] = new Point(-1, 1);
                fMaxRotate = 2;
            }
            case O_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(0, 1);
                fBlocks[2] = new Point(-1, 0);
                fBlocks[3] = new Point(-1, 1);
                fMaxRotate = 1;
            }
            case T_PIECE -> {
                fBlocks[0] = new Point(0, 0);
                fBlocks[1] = new Point(-1, 0);
                fBlocks[2] = new Point(1, 0);
                fBlocks[3] = new Point(0, 1);
                fMaxRotate = 4;
            }
        }
    }

    private void rotateClockwise() {
    	if (fMaxRotate > 1) {
    	    fRotation++;
        
    	    if (fMaxRotate == 2 && fRotation == 2) {
                rotateClockwiseNow();
                rotateClockwiseNow();
                rotateClockwiseNow();
            }
    	    else rotateClockwiseNow();
    	}

    	fRotation = fRotation % fMaxRotate;
    }

    private void rotateAntiClockwise() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwiseNow() {
    	for (int count = 0; count < 4; count++) {
    	    final int temp = fBlocks[count].x;
    
    	    fBlocks[count].x = -fBlocks[count].y;
    	    fBlocks[count].y = temp;
    	}
    }

    public static TetrisPiece getRandomPiece(TetrisBoard board) {
        return new TetrisPiece((int)(Math.random() * 7), board);
    }
}
