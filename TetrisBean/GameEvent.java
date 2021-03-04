package TetrisBean;

import java.awt.*;

public class GameEvent extends Event{

    public static final int START = 0;

    public static final int END   = 1;

    private final int fType;

    public GameEvent(int type) {
	    super(null, 0, null);
        fType   = type;
    }

    public int getType()
    {
        return fType;
    }
}
