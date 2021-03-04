package TetrisBean;

import java.awt.*;

public class BoardEvent extends Event{

    private final Object fSource;


    public BoardEvent(Object source) {
	    super(null, 0, source);
	    fSource = source;
    }

    public Object getSource()
    {
	    return fSource;
    }
}
