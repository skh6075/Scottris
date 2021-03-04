package TetrisBean;

import java.awt.*;

public class ScoreEvent extends Event{

    private int fScore;


    public ScoreEvent(int score) {
	    super(null, 0, null);
	    fScore = score;
    }

    public int getScore()
    {
	    return fScore;
    }
}
