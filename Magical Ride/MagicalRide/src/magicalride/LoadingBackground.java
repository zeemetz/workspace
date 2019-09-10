
package magicalride;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class LoadingBackground extends JPanel
{
    class Anim
    {
        int x1,y1;
        int x2,y2;
    }
    
    Vector<Anim> bg = new Vector<Anim>();
    int bgAnimIndex=89;
    
    JFrame frame;
    int loadingBar = 0;
    Thread th = new Thread(){

        @Override
        public void run() {
            
            while(true)
            {
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoadingBackground.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    
    
    };
    
    public LoadingBackground(JFrame frame) 
    {
        
        bg.add(new Anim());
        bg.get(0).x1 = 3;
        bg.get(0).y1 = 28;
        bg.get(0).x2 = 251;
        bg.get(0).y2 = 177;
        
        bg.add(new Anim());
        bg.get(1).x1 = 255;
        bg.get(1).y1 = 28;
        bg.get(1).x2 = 507;
        bg.get(1).y2 = 177;
        
        bg.add(new Anim());
        bg.get(2).x1 = 3;
        bg.get(2).y1 = 180;
        bg.get(2).x2 = 251;
        bg.get(2).y2 = 307;
        
        this.frame = frame;
        th.start();
    }   
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        // background
        g2d.drawImage(new ImageIcon("assets/bg/image3.jpg").getImage(), 0, 0, 800, 450, this);
        g2d.drawImage(new ImageIcon("assets/bg/bg_image.png").getImage(), 0, 0, 800, 450, this);
        
        // animation background
        g2d.drawImage(new ImageIcon("assets/bg/imageSplashScreen.png").getImage(), 
                260, 175, 500, 359 , 
                bg.get(bgAnimIndex/30).x1, bg.get(bgAnimIndex/30).y1, 
                bg.get(bgAnimIndex/30).x2, bg.get(bgAnimIndex/30).y2 , frame);
        
        // animation control
        if( bgAnimIndex >=0 )
            bgAnimIndex -=2;
        else
            bgAnimIndex = 89;
        
        
        // loading bar
        g2d.fillRect(0, 450, loadingBar, 20);
        if(loadingBar <= 800)
            loadingBar+=5;
        else
        {
            frame.dispose();
            new Menu();
        }
        
    }
}
