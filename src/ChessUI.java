import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessUI extends JPanel implements MouseListener, MouseMotionListener {
    public static int x,y;
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.ORANGE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        g.setColor(new Color(8*16, 0, 8*16));
        g.fillRect(x, y, 100, 200);
        g.setColor(Color.red);
        g.fillRect(50, 10, 10, 20);
        Image chessPieceImage = new ImageIcon("ChessPiecesArray.png").getImage();
        g.drawImage(chessPieceImage, x, 0, x+100, 100, x, 0, x+100, 100, this);
    }

    public void mouseClicked(MouseEvent e){
        x=e.getX();
        y=e.getY();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e){
        
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
