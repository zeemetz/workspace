
package magicalride;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.java2d.Disposer;

public class MagicalRide implements KeyListener{

    JFrame frame = new JFrame();
    Game g;
    
    MagicalRide()
    {
        // setting panel
        g = new Game(frame);
        
        // setting  frame
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.add(g);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        
        //frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) 
    {
       // langsung panggil game
       //new MagicalRide();
        
       // buat menu loading bar
       new LoadingBar();
       //frame.dispose();
       
       // buat slider pas exit
       //new SliderFrame(); 
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
        if( e.getKeyCode() == e.VK_SPACE )
        {
            g.isfly = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        if( e.getKeyCode() == e.VK_SPACE )
        {
            g.isfly = false;
        }
    }
}
