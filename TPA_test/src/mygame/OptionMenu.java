
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class OptionMenu extends AbstractAppState implements ScreenController
{
    Main app;
    Nifty nifty;
    AppSettings appSet = new AppSettings(true);
    
    Vector<Choose> menu = new Vector<Choose>();
    Vector<BitmapText> option = new Vector<BitmapText>();
    //BitmapFont guiFont = app.getGuiFont();
    BitmapFont guiFont;
    Node guiNode;
    
    boolean inOptionMenu=false;
    Picture pic;
    public void initMenu()
    {
        pic = new Picture("HUD Picture");
        pic.setImage(app.getAssetManager(), "Interface/pointer.png", true);
        pic.setLocalTranslation(150, 125, 0);
        pic.setHeight(25);
        pic.setWidth(25);
        guiNode.attachChild(pic);
        
        guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        
        option.add(new BitmapText(guiFont));
        option.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        option.lastElement().setText("FullScreen");
        option.lastElement().setLocalTranslation(200, 144, 0);
        option.lastElement().setColor(ColorRGBA.Blue);
        
        menu.add(new Choose(new BitmapText(guiFont, false), new BitmapText(guiFont, false)));
        menu.lastElement().yes.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().yes.setLocalTranslation(350, 144, 0);
        menu.lastElement().no.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().no.setLocalTranslation(400, 144, 0);
        if(app.inFullScreen)
        {
            menu.lastElement().reverse();
        }
        
        option.add(new BitmapText(guiFont));
        option.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        option.lastElement().setText("Background Sound");
        option.lastElement().setLocalTranslation(200, 118, 0);
        option.lastElement().setColor(ColorRGBA.Blue);
        
        menu.add(new Choose(new BitmapText(guiFont, false), new BitmapText(guiFont, false)));
        menu.lastElement().yes.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().yes.setLocalTranslation(350, 118, 0);
        menu.lastElement().no.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().no.setLocalTranslation(400, 118, 0);
        if(app.musicIsPlaying)
        {
            menu.lastElement().reverse();
        }
        
        option.add(new BitmapText(guiFont));
        option.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        option.lastElement().setText("Sound Effect");
        option.lastElement().setLocalTranslation(200, 95, 0);
        option.lastElement().setColor(ColorRGBA.Blue);
        
        menu.add(new Choose(new BitmapText(guiFont, false), new BitmapText(guiFont, false)));
        menu.lastElement().yes.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().yes.setLocalTranslation(350, 95, 0);
        menu.lastElement().no.setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().no.setLocalTranslation(400, 95, 0);
        if(app.soundEffectIsOn)
        {
            menu.lastElement().reverse();
        }
        
       
        initKey();
        detachMenuNode();
    }
    
    public void initKey()
    {
        app.getInputManager().addMapping("up", new KeyTrigger(KeyboardInputEvent.KEY_UP));
        app.getInputManager().addMapping("down", new KeyTrigger(KeyboardInputEvent.KEY_DOWN));
        app.getInputManager().addMapping("left", new KeyTrigger(KeyboardInputEvent.KEY_LEFT));
        app.getInputManager().addMapping("right", new KeyTrigger(KeyboardInputEvent.KEY_RIGHT));
        app.getInputManager().addMapping("back", new KeyTrigger(KeyboardInputEvent.KEY_ESCAPE));
        
        app.getInputManager().addListener(action, "up","down","back","left","right");
    }
    public void attachMenu()
    {
        for(int i = 0 ; i < menu.size() ; i++)
        {
            guiNode.attachChild(menu.get(i).yes);
            guiNode.attachChild(menu.get(i).no);
            guiNode.attachChild(option.get(i));
        }
        guiNode.attachChild(pic);
    }
    public void detachMenuNode()
    {
        for(int i = 0 ; i < menu.size() ; i++)
        {
            guiNode.detachChild(menu.get(i).yes);
            guiNode.detachChild(menu.get(i).no);
            guiNode.detachChild(option.get(i));
        }
        guiNode.detachChild(pic);
    }
    
    public void loadBackground()
    {
        
    }
    int menuIndex=0;
    public ActionListener action = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) 
        {
            if(isPressed)
            {
                if(name.equals("back"))
                {
                    activateOption();
                    detachMenuNode();
                    app.attachMenu();
                    app.n.gotoScreen("start");
                }
                if(name.equals("down"))
                {
                    if(menuIndex<menu.size()-1)
                    {
                        option.get(menuIndex).setColor(ColorRGBA.Blue);
                        menuIndex++;
                        option.get(menuIndex).setColor(ColorRGBA.Red);
                        pic.setLocalTranslation(150, pic.getLocalTranslation().y-26, 0);
                    }
                    
                }
                if(name.equals("up"))
                {
                    if(menuIndex>0)
                    {
                        option.get(menuIndex).setColor(ColorRGBA.Blue);
                        menuIndex--;
                        option.get(menuIndex).setColor(ColorRGBA.Red);
                        pic.setLocalTranslation(150, pic.getLocalTranslation().y+26, 0);
                    }
                }
                if(name.equals("left"))
                {
                    menu.get(menuIndex).yes.setColor(ColorRGBA.Red);
                    menu.get(menuIndex).no.setColor(ColorRGBA.Blue);
                    menu.get(menuIndex).YES=true;
                    pic.setLocalTranslation(350, pic.getLocalTranslation().y, 0);
                }
                if(name.equals("right"))
                {
                    menu.get(menuIndex).yes.setColor(ColorRGBA.Blue);
                    menu.get(menuIndex).no.setColor(ColorRGBA.Red);
                    menu.get(menuIndex).YES=false;
                    pic.setLocalTranslation(400, pic.getLocalTranslation().y, 0);
                }
            }
                
        }
    };
    
    public void activateOption()
    {
        if(menu.get(0).YES)
        {
            fullScreen();
        }
        else
        {
            normalScreen();
        }
        
        if(menu.get(1).YES)
        {
            app.audio_setting.play();
            app.musicIsPlaying=true;
        }
        else
        {
            normalScreen();
            app.audio_setting.stop();
            app.musicIsPlaying=false;
        }
        
        if(menu.get(2).YES)
        {
            app.soundEffectIsOn=true;
        }
        else
        {
            app.soundEffectIsOn=false;
        }
        app.saveSetting();
    }
    
    public void fullScreen()
    {
        appSet.setFullscreen(true);
        app.setSettings(appSet);
        app.restart();
        app.inFullScreen=true;
    }
    
    
    public void normalScreen()
    {
        app.inFullScreen=false;
        appSet.setFullscreen(false);
        app.setSettings(appSet);
        app.restart();
    }
    
    boolean musicControl = true;
    public void music()
    {
        System.out.println("tes");
        if(musicControl)
        {
            app.audio_setting.stop();
            musicControl=false;
            app.musicIsPlaying=false;
        }
        else
        {
            app.audio_setting.play();
            musicControl=true;
            app.musicIsPlaying=true;
        }
    }
    
    public void back()
    {
        app.saveSetting();
        nifty.gotoScreen("start");
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (Main)app;
        guiFont = this.app.getGuiFont();
        guiNode = this.app.getGuiNode();
        initMenu();
    }

    @Override
    public void update(float tpf) 
    {
        super.update(tpf);
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
    }

    public void onStartScreen() 
    {
    }

    public void onEndScreen() 
    {
    }
}
