
package magicalride;

import javax.swing.JFrame;



public class LoadingBar extends JFrame
{
    public LoadingBar() 
    {
        this.setSize(800,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.add(new LoadingBackground(this));
        this.setVisible(true);
    }
}
