
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Random;
import javax.swing.JOptionPane;

public class MainMenu extends AbstractAppState implements ScreenController
{
    Nifty nifty;
    Main app;

    public Nifty getNifty() {
        return this.nifty;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (Main)app;
    }
    
    public void newGame()
    {
        nifty.gotoScreen("game");
    }

    public void option()
    {
        nifty.gotoScreen("option");
        app.om.attachMenu();
        app.detachMenuNode();
    }
    
    public void summonRed()
    {
        if(app.money>=2000)
        {
            app.summonControl = true;
            app.isSummon=true;
            app.charType = "Red.png";
            app.money-=2000;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Insuficient money");
            return;
        }
    }
    
    public void summonGreen()
    {
        if(app.money>=1000)
        {
            app.summonControl = true;
            app.isSummon=true;
            app.charType = "Green.png";
            app.money-=1000;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Insuficient money");
            return;
        }
    }
    
    public void playing()
    {
        app.isPlaying = true;
        nifty.gotoScreen("none");
    }
    
    public void start()
    {
        nifty.gotoScreen("start");
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
    }

    public void backAfterGameOver()
    {
        app.getRootNode().detachAllChildren();
        app.pc = new PlayingController();
        app.getStateManager().detach(app.pc);
        app.getStateManager().attach(app.pc);
        app.initTerrain();
        app.n.fromXml("Interface/Screen.xml", "splashScreen", app.mm,app.om, app.pc);
        nifty.gotoScreen("start");
    }

    long lastTime, currTime;
    boolean initedtime = false;
    public boolean switchSlide()
    {
        if((x+width)==width && (y+height)==height)
        {
            return true;
        }
        return false;
    }
    
    @Override
    public void update(float tpf) 
    {
        super.update(tpf);
        if(sliderOn)
        {
            if(switchSlide())
            {
                if(!initedtime)
                {
                    lastTime = System.currentTimeMillis();
                    initedtime=true;
                }
                currTime=System.currentTimeMillis();
                if(currTime-lastTime>5000)
                {
                    loadPicture();
                    lastTime=currTime;
                    initedtime=false;
                }
            }
            else
            {
                moveSlider();
            }
        }
    }
    
    Random rand = new Random();
    int x,y,width,height;
    int direction;
    Picture pic;
    
    public void moveSlider()
    {
        if(direction==1)
        {
           y++;
        }
        else if(direction==2)
        {
            y--;
        }
        else if(direction==3)
        {
            x++;
        }
        else if(direction==4)
        {
            x--;
        }
        //kiri atas
        else if(direction==5)
        {
            if(x+width<width)x++;
            if(y+height<height)y++;
        }
        //kiri bawah
        else if(direction==6)
        {
            if(x+width<width)x++;
            if(y+height>height)y--;
        }
        //kanan atas
        else if(direction==7)
        {
            if(x+width>width)x--;
            if(y+height<height)y++;
        }
        //kanan bawah
        else if(direction==8)
        {
            if(x+width>width)x--;
            if(y+height>height)y--;
        }
        pic.setPosition(x, y);
    }
    
    public void initPosition()
    {
        //atas
        if(direction==1)
        {
            y-=height;
        }
        //bawah
        else if(direction==2)
        {
            y+=height;
        }
        //kiri
        else if(direction==3)
        {
            x-=width;
        }
        //kanan
        else if(direction==4)
        {
            x+=width;
        }
        
        //kiri atas
        else if(direction==5)
        {
            x-=width;
            y-=height;
        }
        //kiri bawah
        else if(direction==6)
        {
            x-=width;
            y+=height;
        }
        //kanan atas
        else if(direction==7)
        {
            x+=width;
            y-=height;
        }
        //kanan bawah
        else if(direction==8)
        {
            x+=width;
            y+=height;
        }
        pic.setPosition(x, y);
    }
    int picRandomControl=0;
    int directionRandomControl=0;
    int picIndex;
    public void loadPicture()
    {
        do{
            picIndex = rand.nextInt(3)+1;
        }while(picIndex == picRandomControl);
        picRandomControl = picIndex;
        
        pic = new Picture("HUD Picture");
        pic.setImage(app.getAssetManager(), "Interface/"+picIndex+".jpg", true);
        
        width = app.setting.getWidth();
        height = app.setting.getHeight();
        x = (int)pic.getLocalTranslation().x;
        y = (int)pic.getLocalTranslation().y;
        pic.setWidth(width);
        pic.setHeight(width);
        do{
            direction = rand.nextInt(8)+1;
        }while(direction==directionRandomControl);
        directionRandomControl=direction;
        initPosition();
        app.getGuiNode().attachChild(pic);
    }
    
    boolean sliderOn = false;
    
    public void slider()
    {
        sliderOn = true;
        loadPicture();
    }
    
    public void exit()
    {
        app.saveSetting();
        System.exit(1);
    }
    
    public void onStartScreen() 
    {
    }

    public void onEndScreen() 
    {
    }
}
