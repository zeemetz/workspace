
package magicalride;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Slider extends JPanel
{
    JFrame sliderFrame;
    
    // deklarasi
    int ImageIndex = 1; // deklarasi ImageIndex untuk index image mana yang ditampilkan
    Point slider = new Point(0,0); // posisi slider pada saat bergerak
    
    long lastSliderTime, currSliderTime;
    // thread utama
    Thread th = new Thread(){

        @Override
        public void run() 
        {
            while( ( currSliderTime - lastSliderTime ) < 20 )
            {
                currSliderTime = (System.currentTimeMillis())/1000;
                repaint();
                try 
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException ex) 
                {

                }
            }
            sliderFrame.dispose();
        }
    
    };
    
    Slider(JFrame frame)
    {
        // dapatkan frame slider supaya bisa ditutup
        sliderFrame = frame;
        // inisialisasi interval awal slider
        lastSliderTime = currSliderTime = (System.currentTimeMillis())/1000;
        
        // menjalankan thread utuk slider
        th.start();
    }
    
    public void paint(Graphics g)
    {
        // graphic untuk load gambar
        Graphics2D g2d = (Graphics2D)g;
        
        
        g2d.drawImage(new ImageIcon("assets/slider/image"+ImageIndex+".jpg").getImage(), 0,0,800,800, null);
        
        g2d.drawImage(new ImageIcon("assets/slider/image"+(ImageIndex+1)+".jpg").getImage(), slider.x, slider.y, 
                800, 600, null);
        
        slider.x+=3;
        slider.y+=3;
        
        if( slider.y > 600)
        {
            ImageIndex ++;
            slider.x = 0;
            slider.y = 0;

            if( ImageIndex > 4)
            {
                ImageIndex = 1;
            }
        }
        
        
    }
}
