import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import TetrisBean.*;

public class Scottris implements KeyListener{

    final TetrisGame     fGame        = new TetrisGame();

    final TetrisBoardGUI fBoardGUI    = new TetrisBoardGUI();

    final JFrame         fFrame       = new JFrame("테트리스");

    final JButton        fStartButton = new JButton("Start");

    final JLabel         fScoreLabel  = new JLabel("Score:");


    public Scottris() {
        final Container pane = fFrame.getContentPane();
        
        pane.setLayout(new BorderLayout());
        pane.add(fBoardGUI, BorderLayout.CENTER);
        pane.add(fStartButton, BorderLayout.SOUTH);
        pane.add(fScoreLabel, BorderLayout.NORTH);

        fStartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fGame.startGame();
                fFrame.requestFocus();
            }
        });

        fGame.addScoreListener(new ScoreListener() {
            public void scoreChange(ScoreEvent e) {
                fScoreLabel.setText("나의 점수: " + e.getScore());
            }
        });

        fFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        fGame.addBoardListener(fBoardGUI);
        fFrame.addKeyListener(this);
        fFrame.pack();
        fFrame.show();
    }

    public void keyPressed(KeyEvent e) {
    	switch (e.getKeyCode()) {
        	case KeyEvent.VK_LEFT:
        	    fGame.move(TetrisPiece.LEFT);
        	    break;
        	case KeyEvent.VK_RIGHT:
        	    fGame.move(TetrisPiece.RIGHT);
        	    break;
        	case KeyEvent.VK_UP:
        	    fGame.move(TetrisPiece.ROTATE);
        	    break;
        	case KeyEvent.VK_DOWN:
        	    fGame.move(TetrisPiece.DOWN);
        	    break;
            case KeyEvent.VK_SHIFT:
                fGame.move(TetrisPiece.FALL);
    	}
    }
    
    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new Scottris();
    }

    public class TetrisBoardGUI extends JPanel implements BoardListener{

        private TetrisBoard fBoard;
    
        public void boardChange(BoardEvent e) {
    	    fBoard = (TetrisBoard)e.getSource();   
            repaint();
        }
    
        public void paintComponent(Graphics graphics) {
        	final int width   = getBounds().width;
        	final int height  = getBounds().height;	

        	final Image    image = createImage(width, height);
            final Graphics g     = image.getGraphics();           

        	if (fBoard != null) {
                final int numCols = fBoard.getColumns();
                final int numRows = fBoard.getRows();

                for (int cols = 0; cols < numCols; cols++) {
                    for (int rows = 0; rows < numRows; rows++) {
                        final int piece = fBoard.getPieceAt(cols, rows);

                        if (piece != TetrisBoard.EMPTY_BLOCK) {
                            g.setColor(getPieceColor(piece));
                            drawBlock(g, (cols * width / numCols) + 1, (rows * height / numRows) + 1, (width / numCols) - 1, (height / numRows) - 1);
                        }
                    }
                }
            }
            
            g.setColor(Color.blue);
            g.drawRect(0, 0, width - 1, height - 1);
            graphics.drawImage(image, 0, 0, width, height, null);
        }
    
        private void drawBlock(Graphics g, int x, int y, int width, int height) {
            g.fillRect(x, y, width, height);
            g.setColor(Color.black);
            g.drawRect(x, y, width, height);
        }
    
        public Dimension getPreferredSize() {
            return new Dimension(300, 600);
        }
    
        private Color getPieceColor(int color) {
            return switch (color) {
                case TetrisPiece.L_PIECE -> new Color(24, 105, 198);

                case TetrisPiece.J_PIECE -> new Color(206, 56, 173);

                case TetrisPiece.I_PIECE -> new Color(41, 40, 206);

                case TetrisPiece.Z_PIECE -> new Color(212, 0, 0);

                case TetrisPiece.S_PIECE -> new Color(82, 154, 16);

                case TetrisPiece.O_PIECE -> new Color(123, 121, 123);

                case TetrisPiece.T_PIECE -> new Color(156, 142, 8);

                default -> null;
            };
        }
    }
}
