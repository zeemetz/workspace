
package mygame;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class Choose 
{
    BitmapText yes;
    BitmapText no;
    boolean YES;
    Choose(BitmapText yes, BitmapText no)
    {
        this.yes = yes;
        this.no = no;
        
        yes.setText("Yes");
        yes.setColor(ColorRGBA.Blue);
        
        no.setText("No");
        no.setColor(ColorRGBA.Red);
    }
    
    public void reverse()
    {
        YES = true;
        yes.setColor(ColorRGBA.Red);
        no.setColor(ColorRGBA.Blue);
    }
}
