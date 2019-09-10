    package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import jme3tools.converters.ImageToAwt;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.StyleBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.java.games.input.Keyboard;



public class Main extends SimpleApplication 
{
    MainMenu mm = new MainMenu();
    OptionMenu om = new OptionMenu();
    PlayingController pc = new PlayingController();
    BulletAppState bulletAppState;
    Nifty n;
    AppSettings setting;
    // control
    boolean passSplashScreen=false;
    boolean summonControl=false;
    boolean isSummon=false;
    boolean isPlaying = false;
    boolean isWin=false;
    boolean gameIsOver = false;
    boolean inFullScreen;
    boolean musicIsPlaying;
    boolean soundEffectIsOn;
    String charType;
    int money=3000;
    int enemyCount=1;
    long lastTime,currTime;
    
    Vector<BitmapText> menu = new Vector<BitmapText>();
    Picture pic;
    int enemyHp=100;
    public void initMenu()
    {
        pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Interface/pointer.png", true);
        pic.setLocalTranslation(250, 130, 0);
        pic.setHeight(25);
        pic.setWidth(25);
        guiNode.attachChild(pic);
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        menu.add(new BitmapText(guiFont, false));
        menu.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().setText("New Game");
        menu.lastElement().setLocalTranslation(270, 144, 0);
        guiNode.attachChild(menu.lastElement());
        
        menu.add(new BitmapText(guiFont, false));
        menu.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().setText("About us");
        menu.lastElement().setLocalTranslation(270, 118, 0);
        guiNode.attachChild(menu.lastElement());
        
        menu.add(new BitmapText(guiFont, false));
        menu.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().setText("Option");
        menu.lastElement().setLocalTranslation(270, 95, 0);
        guiNode.attachChild(menu.lastElement());
        
        menu.add(new BitmapText(guiFont, false));
        menu.lastElement().setSize(guiFont.getCharSet().getRenderedSize());
        menu.lastElement().setText("Exit");
        menu.lastElement().setLocalTranslation(270, 72, 0);
        guiNode.attachChild(menu.lastElement());
 
        for(int i = 0 ; i < menu.size() ; i++)
        {
            menu.get(i).setColor(ColorRGBA.Blue);
        }
        menu.firstElement().setColor(ColorRGBA.Red);
        initKey();
    }
    
    public void initKey()
    {
        inputManager.addMapping("up", new KeyTrigger(KeyboardInputEvent.KEY_UP));
        inputManager.addMapping("down", new KeyTrigger(KeyboardInputEvent.KEY_DOWN));
        inputManager.addMapping("enter", new KeyTrigger(KeyboardInputEvent.KEY_SPACE));
        
        inputManager.addListener(action, "up","down","enter");
    }
    public void attachMenu()
    {
        for(int i = 0 ; i < menu.size() ; i++)
        {
            guiNode.attachChild(menu.get(i));
        }
        guiNode.attachChild(pic);
    }
    public void detachMenuNode()
    {
        for(int i = 0 ; i < menu.size() ; i++)
        {
            guiNode.detachChild(menu.get(i));
        }
        guiNode.detachChild(pic);
    }
    int menuIndex=0;
    public ActionListener action = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) 
        {
            if(isPressed)
            {
                if(name.equals("down"))
                {
                    if(menuIndex<menu.size()-1)
                    {
                        menu.get(menuIndex).setColor(ColorRGBA.Blue);
                        menuIndex++;
                        menu.get(menuIndex).setColor(ColorRGBA.Red);
                        pic.setLocalTranslation(250, pic.getLocalTranslation().y-26, 0);
                    }
                    
                }
                if(name.equals("up"))
                {
                    if(menuIndex>0)
                    {
                        menu.get(menuIndex).setColor(ColorRGBA.Blue);
                        menuIndex--;
                        menu.get(menuIndex).setColor(ColorRGBA.Red);
                        pic.setLocalTranslation(250, pic.getLocalTranslation().y+26, 0);
                    }
                }
                if(name.equals("enter"))
                {
                    if(menuIndex==0)
                    {
                        mm.newGame();
                    }
                    else if(menuIndex==1)
                    {
                        mm.slider();
                    }
                    else if(menuIndex==2)
                    {
                        mm.option();
                    }
                    else if(menuIndex==3)
                    {
                        mm.exit();
                    }
                    detachMenuNode();
                }
            }
                
        }
    };
    
    public void initControl()
    {
        summonControl = isSummon = isPlaying = isWin = gameIsOver =passSplashScreen= false;
    }
    
    public BitmapFont getGuiFont()
    {
        return guiFont;
    }
    
    public static void main(String[] args) 
    {
        appset = new AppSettings(true);
        appset.setFullscreen(false);
        appset.setTitle("RPG");
        
        Main app = new Main();
        //implement setting kedalam app
        app.setSettings(appset);
        app.setShowSettings(false);
        
        app.start();
    }
    static AppSettings appset;
    @Override
    public void simpleInitApp() 
    {
        inputManager.clearMappings();
        
        loadSetting();
        // splash screen
        this.setting = settings;
        lastTime = System.currentTimeMillis();
        // option screen
        stateManager.attach(mm);
        stateManager.attach(om);
        stateManager.attach(pc);
        
//        Logger root = Logger.getLogger("");
//        Handler[] handlers = root.getHandlers();
//        for (int i = 0; i < handlers.length; i++) {
//        if (handlers[i] instanceof ConsoleHandler) {
//        ConsoleHandler consoleHandler = (ConsoleHandler) handlers[i] ;
//        ((ConsoleHandler) handlers[i]).setLevel(Level.OFF);
//        }
//        }
        
        // nifty
        NiftyJmeDisplay display = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        n = display.getNifty();
        guiViewPort.addProcessor(display);
        n.fromXml("Interface/Screen.xml", "splashScreen", mm,om, pc); // dengan manipulasi state
        
        // variable declaration
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        // setting root
        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0, 200, 0));
        cam.lookAt(new Vector3f(0, -1, 0), Vector3f.UNIT_Y);
        
        flyCam.setMoveSpeed(1000);
        flyCam.setDragToRotate(true);
        setDisplayStatView(false);
        setDisplayFps(false);
        
        initLight();
        initTerrain();
        initAudio();
        initSoundEffect();
        initMenu();
    }
    
    AudioNode attack_sound_effect,heal_sound_effect;
    AudioNode audio_setting;
    
    public void initSoundEffect()
    {
        attack_sound_effect = new AudioNode(assetManager, "Sounds/attack.wav", false);
        attack_sound_effect.setLooping(false);  
        
        heal_sound_effect = new AudioNode(assetManager, "Sounds/attack.wav", false);
        heal_sound_effect.setLooping(false);  
        
        rootNode.attachChild(attack_sound_effect);
        rootNode.attachChild(heal_sound_effect);
    }
    
    private void initAudio() 
    {
        audio_setting = new AudioNode(assetManager, "Sounds/lagu.ogg", false);
        audio_setting.setLooping(true);  
        rootNode.attachChild(audio_setting);
        if(musicIsPlaying)
        {
            System.out.println("music is on");
            audio_setting.play();
        }
    }

    public void initLight()
    {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -1, -1));
        rootNode.addLight(sun);
    }
    
    TerrainQuad terrain;
    public void initTerrain()
    {
        // Alpha Map
        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/alphaMap.png"));
        
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64);
        
        Texture road = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", road);
        mat_terrain.setFloat("Tex2Scale", 64);
        
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", dirt);
        mat_terrain.setFloat("Tex3Scale", 64);
        
        // Height Map
         Texture  heightMapImage = assetManager.loadTexture("Textures/heightMap.png");
        //AbstractHeightMap heighMap = new ImageBasedHeightMap( heightMapImage.getImage() );
        AbstractHeightMap heighMap = new ImageBasedHeightMap(ImageToAwt.convert(heightMapImage.getImage(), false, true, 0) );
        heighMap.load();
        
        // Attach to root
        terrain = new TerrainQuad("My Terrain", 65, 513, heighMap.getHeightMap());
        
        terrain.setMaterial(mat_terrain);
        
        terrain.setLocalScale(1, 0.3f, 1);
        terrain.setLocalTranslation(0, -500, 0);
        rootNode.attachChild(terrain);
        
        //Physics
        CollisionShape colShape = CollisionShapeFactory.createMeshShape(terrain); 
        RigidBodyControl landscape = new RigidBodyControl(colShape, 0); 
        
        terrain.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
        
        // sky
        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
    }
    
    public void loadSetting()
    {
        BufferedReader br = null;
        try 
        {
            String sCurrentLine;

            br = new BufferedReader(new FileReader("d://setting.txt"));

            inFullScreen = br.readLine().equals("true");
            musicIsPlaying = br.readLine().equals("true");
            soundEffectIsOn = br.readLine().equals("true");
            System.out.printf(inFullScreen+"\n"+musicIsPlaying+"\n"+soundEffectIsOn);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try {
                    if (br != null)br.close();
            } catch (IOException ex) {
                    ex.printStackTrace();
            }
        }
        
    }
    
    public void saveSetting()
    {
        try 
        {
            String content = inFullScreen+"\n"+musicIsPlaying+"\n"+soundEffectIsOn;

            File file = new File("d://setting.txt");

            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");
 
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        if(isWin)
        {
            rootNode.detachAllChildren();
            initLight();
            initTerrain();
            initControl();
            enemyCount = 2;
            money = 5000;
            pc.initialize(stateManager, this);
        }
        if(gameIsOver)
        {
            initControl();
        }
        
        currTime = System.currentTimeMillis();
        
        if((currTime-lastTime) > 6000 && !passSplashScreen)
        {
            n.gotoScreen("start");
            passSplashScreen=true;
        }
        
        //camera movement
        //System.out.println("camera movement"+inputManager.getCursorPosition().x+"  "+inputManager.getCursorPosition().y);
        if(inputManager.getCursorPosition().x <=10)
        {
            cam.setLocation( new Vector3f(cam.getLocation().x+10, cam.getLocation().y, cam.getLocation().z) );
        }
        else if(inputManager.getCursorPosition().x >=630)
        {
            cam.setLocation( new Vector3f(cam.getLocation().x-10, cam.getLocation().y, cam.getLocation().z) );
        }
        
        if(inputManager.getCursorPosition().y <=10)
        {
            cam.setLocation( new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z-10) );
        }
        else if(inputManager.getCursorPosition().y >=470)
        {
            cam.setLocation( new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z+10) );
        }
    }

    @Override
    public void simpleRender(RenderManager rm) 
    {
        //TODO: add render code
    }
}
