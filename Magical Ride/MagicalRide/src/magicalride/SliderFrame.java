
package magicalride;

import javax.swing.JFrame;


public class SliderFrame 
{
    // deklarasi frame dan panel slider
    JFrame frame = new JFrame();
    Slider slider = new Slider(frame);
    
    public SliderFrame() 
    {
        // setting frame
        frame.setSize(800,500);
        frame.setLocationRelativeTo(null);
        frame.add(slider); // add slider kedalam frame
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
