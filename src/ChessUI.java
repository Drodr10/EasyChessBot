import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessUI extends JPanel implements MouseListener, MouseMotionListener {
    public static int mouseX, mouseY, newMouseX, newMouseY, squareSize=60;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.ORANGE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
 
        for (int i = 0; i < 64; i+=2) {
            g.setColor(new Color(255, 200, 100));
            g.fillRect((i%8+i/8%2)*squareSize, i/8*squareSize, squareSize, squareSize);
            g.setColor(new Color(150, 50, 30));
            g.fillRect(((i+1)%8-(i+1)/8%2)*squareSize, (i+1)/8*squareSize, squareSize, squareSize);
        }

        Image chessPieceImage = new ImageIcon("lib/ChessPiecesArray.png").getImage();
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            switch(Chess.board[i/8][i%8]){
                case "P":  
                    j=5;
                    k=Chess.humanColor;
                    break;
                case "p":  
                    j=5;
                    k=1-Chess.humanColor;
                    break;
                case "R":
                    j=2;
                    k=Chess.humanColor;
                    break;
                case "r":
                    j=2;
                    k=1-Chess.humanColor;
                    break;
                case "K":
                    j=3;
                    k=Chess.humanColor;
                    break;
                case "k":
                    j=3;
                    k=1-Chess.humanColor;
                    break;
                case "B":
                    j=4;
                    k=Chess.humanColor;
                    break;
                case "b":
                    j=4;
                    k=1-Chess.humanColor;
                    break;
                case "Q":
                    j=0;
                    k=Chess.humanColor;
                    break;
                case "q":
                    j=0;
                    k=1-Chess.humanColor;
                    break;
                case "A":
                    j=1;
                    k=Chess.humanColor;
                    break;
                case "a":
                    j=1;
                    k=1-Chess.humanColor;
                    break;
                default:
                    break;
            }
            if (j!=-1&&k!=-1) {
                g.drawImage(chessPieceImage, i%8*squareSize, i/8*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*60, k*60, (j+1)*60,(k+1)*60, this);   
            }
            
        }
    }

    public void mouseClicked(MouseEvent e){
        
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX()<8*squareSize && e.getY()<8*squareSize) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }
    public void mouseReleased(MouseEvent e){
        if (e.getX()<8*squareSize && e.getY()<8*squareSize) {
            newMouseX = e.getX();
            newMouseY = e.getY();
            if (e.getButton()==MouseEvent.BUTTON1) {
                String dragMove;
                if (newMouseY/squareSize==0 && mouseY/squareSize==1 && 'P'==Chess.board[mouseY/squareSize][mouseX/squareSize].charAt(0)) {
                    dragMove = ""+mouseX/squareSize+newMouseX/squareSize+Chess.board[newMouseY/squareSize][newMouseX/squareSize]+"QP";
                }else{
                    dragMove = ""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+Chess.board[newMouseY/squareSize][newMouseX/squareSize];
                }
                String possibleMoves = Chess.possibleMoves();
                if (possibleMoves.contains(dragMove)) {
                    Chess.makeMove(dragMove);
                    Chess.flipBoard();
                    Chess.printBoard();
                    System.out.println(Chess.possibleMoves());
                    Chess.makeMove(Chess.alphaBeta(Chess.GLOBALDEPTH, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
                    Chess.flipBoard();
                    repaint();
                }
            }
            repaint();
        }
    }
    public void mouseMoved(MouseEvent e){
        
    }
    public void mouseDragged(MouseEvent e){
        
    }
    public void mouseEntered(MouseEvent e){
        
    }
    public void mouseExited(MouseEvent e){
      
    }

    
    
}
