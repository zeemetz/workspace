
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HeightMap;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Guard;
import java.util.Random;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.java.games.input.Keyboard;
import sun.awt.EmbeddedFrame;
import sun.security.x509.IssuerAlternativeNameExtension;

public class PlayingController extends AbstractAppState implements ScreenController
{
    public Nifty nifty;
    Main app;
    Node rootNode;
    AssetManager assetManager;
    BulletAppState bulletAppState;
    
    TerrainQuad terrain;
    Geometry geom;
    
    FlyByCamera flyCam;
    
    Vector<ModelCharacter> hero= new Vector<ModelCharacter>();
    Vector<Enemy> enemy= new Vector<Enemy>();
    
    Node actionNode = new Node();
    Node guiNode;
    
    boolean playerPhase = true;
    
    Random rand = new Random();
    
    Geometry pickedSign;
    
    Vector<Vector2f> usedPos = new Vector<Vector2f>();
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        // main component
        super.initialize(stateManager, app);
        this.app = (Main)app;
        assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();
        this.cam = this.app.getCamera();
        this.inputManager = this.app.getInputManager();
        this.terrain = this.app.terrain;
        this.bulletAppState = this.app.bulletAppState;
        this.flyCam = this.app.getFlyByCamera();
        this.nifty = app.getStateManager().getState(MainMenu.class).getNifty();
        currTime = lastTime = 0;
        guiNode = this.app.getGuiNode();
        // cursor
        Box b = new Box(new Vector3f().zero(), 8, 0, 8);
        geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        
        // hero is picked
        pickedSign = createBox(Vector3f.ZERO);
        
        // listener
        inputManager.addMapping("leftMouseCLick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("action", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
        inputManager.addMapping("RotateUp", new KeyTrigger(KeyboardInputEvent.KEY_UP));
        inputManager.addMapping("RotateDown", new KeyTrigger(KeyboardInputEvent.KEY_DOWN));
        inputManager.addMapping("RotateLeft", new KeyTrigger(KeyboardInputEvent.KEY_LEFT));
        inputManager.addMapping("RotateRight", new KeyTrigger(KeyboardInputEvent.KEY_RIGHT));
        
        inputManager.addListener(action, "leftMouseCLick", "action", "RotateUp", "RotateDown", "RotateLeft" , "RotateRight");
        
        enemy.add(new Enemy(assetManager, "Models/Sinbad/Sinbad.mesh.xml",this.app,this.app.enemyHp));
        rootNode.attachChild(enemy.lastElement().model);
        bulletAppState.getPhysicsSpace().add(enemy.lastElement().player);
        Vector3f enemyPos =  new Vector3f((rand.nextInt()%5)*16, -400, 0);
        generateGridTales(enemyPos);
        enemy.lastElement().player.setPhysicsLocation(enemyPos);
        
        usedPos.add(convert3fTo2f(enemyPos));
        
        // enemy 
        for(int i = 0 ; i < this.app.enemyCount ; i++)
        {
            enemy.add(new Enemy(assetManager, "Models/Sinbad/Sinbad.mesh.xml",this.app,this.app.enemyHp));
            rootNode.attachChild(enemy.lastElement().model);
            bulletAppState.getPhysicsSpace().add(enemy.lastElement().player);
            enemyPos =  new Vector3f((rand.nextInt()%5)*16, -400, 0);
            for(int j = 0 ; j < usedPos.size() ; j++)
            {
                while(usedPos.get(j).equals(convert3fTo2f(enemyPos)))
                    enemyPos =  new Vector3f((rand.nextInt()%5)*16, -400, 0);
            }
            generateGridTales(enemyPos);
            enemy.lastElement().player.setPhysicsLocation(enemyPos);
            
            usedPos.add(convert3fTo2f(enemyPos));
        }
//        enemy.add(new Enemy(assetManager, "Models/Sinbad/Sinbad.mesh.xml"));
//        rootNode.attachChild(enemy.lastElement().model);
//        bulletAppState.getPhysicsSpace().add(enemy.lastElement().player);
//        enemy.lastElement().player.setPhysicsLocation(new Vector3f(-104, -400, 0));
//        
//        enemy.add(new Enemy(assetManager, "Models/Sinbad/Sinbad.mesh.xml"));
//        rootNode.attachChild(enemy.lastElement().model);
//        bulletAppState.getPhysicsSpace().add(enemy.lastElement().player);
//        enemy.lastElement().player.setPhysicsLocation(new Vector3f(104, -400, 0));
        
        //information
        initText();
    }
    
    public Vector2f convert3fTo2f(Vector3f source)
    {
        return new Vector2f(source.clone().x, source.clone().z);
    }
    
    public ActionListener action = new ActionListener() 
    {
        public void onAction(String name, boolean isPressed, float tpf) 
        {
            if(isPressed)
            {
                if(name.equals("leftMouseCLick"))
                {
                    if(app.summonControl)
                    {
                        int temp=0;
                        for(int i = 0 ; i < usedPos.size() ; i++)
                        {
                            if(usedPos.get(i).equals(convert3fTo2f(koordinat)))
                                temp++;
                        }
                        if(temp==0)
                        {
                            flyCam.setEnabled(false);
                            app.summonControl=false;
                            size++;
                            usedPos.add(convert3fTo2f(hero.lastElement().model.getLocalTranslation().clone()));
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Insuficient Place");
                        }
                    }
                    
                    //====================================================================================
                    
                    // pick hero
                    if(app.isPlaying)
                    {
                        CollisionResults crs = new CollisionResults();
                        Vector2f cursor = inputManager.getCursorPosition();
                        Vector3f start = cam.getWorldCoordinates(cursor, 0);
                        Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();

                        // update box position
                        Ray ray = new Ray(start, des);
                        for(int i = 0 ; i < hero.size() ; i++)
                        {
                            hero.get(i).model.collideWith(ray, crs);
                            if(crs.size()>0)
                            {
                               nifty.gotoScreen("action");
                               hero.get(i).isPicked = true;
                               hero.get(i).channel.setAnim("RunBase");
                               crs.clear();
                               pickedSign.setLocalTranslation(hero.get(i).player.getPhysicsLocation().clone());
                               rootNode.attachChild(pickedSign);
                               break;
                            }
                            else
                            {
                                nifty.gotoScreen("none");
                                hero.get(i).isPicked = false;
                                hero.get(i).attacking = false;
                                hero.get(i).isMove = false;
                                hero.get(i).healing = false;
                                hero.get(i).channel.setAnim("IdleBase");
                                //hero.get(i).clearNode(actionNode);
                                //rootNode.detachChild(actionNode);
                                floor.detachAllChildren();
                                rootNode.detachChild(pickedSign);
                            }
                        }
                    }
                }
                
                //==========================================================================================
                
                if (name.equals("action") && playerPhase)
                {
                    CollisionResults crs = new CollisionResults();
                    Vector2f cursor = inputManager.getCursorPosition();
                    Vector3f start = cam.getWorldCoordinates(cursor, 0);
                    Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();

                    // update box position
                    Ray ray = new Ray(start, des);
                    
                    for(int i = 0 ; i < hero.size() ; i++)
                    {
                        floor.collideWith(ray, crs);
                        
                        if(crs.size()>0 && !hero.get(i).endTurn)
                        {
                            if(hero.get(i).isMove && hero.get(i).isPicked)
                            {
                               hero.get(i).moveTo(koordinat);

                               hero.get(i).isMove = false;
                               hero.get(i).moving = true;
                            }
                            if(hero.get(i).attacking && hero.get(i).isPicked)
                            {
                                if(cekColideWithEnemy())
                                {
                                    hero.get(i).attackTo(koordinat,actionNode);
                                    rootNode.attachChild(actionNode);
                                    hero.get(i).endTurn = true;
                                    hero.get(i).channel.setAnim("JumpLoop");
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Pick Enemy To Attack");
                                }
                            }
                            if(hero.get(i).healing && hero.get(i).isPicked)
                            {
                                if(cekColideWithHero())
                                {
                                    hero.get(i).healTo(koordinat,actionNode);
                                    rootNode.attachChild(actionNode);
                                    hero.get(i).endTurn = true;
                                    hero.get(i).channel.setAnim("JumpLoop");
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Pick Hero To Heal");
                                }
                            }
                        }
                    }
                }
                
                //==============================================================
                //==============================================================
                //==============================================================
                //==============================================================
                //==============================================================
                //==============================================================
                if(name.equals("RotateUp"))
                {
                    cam.setRotation(cam.getRotation().add(new Quaternion((float)-(10*tpf*Math.PI/180), 0, 0, 0)));
                }
                if(name.equals("RotateDown"))
                {
                    cam.setRotation(cam.getRotation().add(new Quaternion((float)(10*tpf*Math.PI/180), 0, 0, 0)));
                }
                if(name.equals("RotateLeft"))
                {
                    cam.setRotation(cam.getRotation().add(new Quaternion(0,(float)-(10*tpf*Math.PI/180),(float)-(10*tpf*Math.PI/180),0)));
                }
                if(name.equals("RotateRight"))
                {
                    cam.setRotation(cam.getRotation().add(new Quaternion(0,(float)(10*tpf*Math.PI/180),(float)(10*tpf*Math.PI/180),0)));
                }
            }
        }
    };
    
    BitmapText hudText;
    BitmapText moneyText;
    
    BitmapText red, green;
    public void initText()
    {
        hudText = new BitmapText(app.getGuiFont(), false);          
        hudText.setSize(app.getGuiFont().getCharSet().getRenderedSize());      
        hudText.setColor(ColorRGBA.White);                             
        hudText.setText("You can write any string here");
        guiNode.attachChild(hudText);
        
        moneyText = new BitmapText(app.getGuiFont(), false);          
        moneyText.setSize(app.getGuiFont().getCharSet().getRenderedSize());      
        moneyText.setColor(ColorRGBA.White);                             
        moneyText.setText("You can write any string here");
        moneyText.setLocalTranslation(50, 450, 0);
        guiNode.attachChild(moneyText);
        
        red = new BitmapText(app.getGuiFont(), false);          
        red.setSize(app.getGuiFont().getCharSet().getRenderedSize());      
        red.setColor(ColorRGBA.Red);                             
        red.setText("2000");
        red.setLocalTranslation(20, 65, 0);
        guiNode.attachChild(red);
        
        green = new BitmapText(app.getGuiFont(), false);          
        green.setSize(app.getGuiFont().getCharSet().getRenderedSize());      
        green.setColor(ColorRGBA.Green);                             
        green.setText("1000");
        green.setLocalTranslation(85, 65, 0);
        guiNode.attachChild(green);
    }
    
    public void updateTagGuiNode()
    {
       hudText.setLocalTranslation(inputManager.getCursorPosition().x, inputManager.getCursorPosition().y, 0);
       moneyText.setText(app.money+"");
    }
    
    public boolean cekColideWithHero()
    {
        CollisionResults crs = new CollisionResults();
        Vector2f cursor = inputManager.getCursorPosition();
        Vector3f start = cam.getWorldCoordinates(cursor, 0);
        Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();

        // update box position
        Ray ray = new Ray(start, des);
        
        for(int i = 0 ; i < hero.size();i++)
        {
            hero.get(i).model.collideWith(ray, crs);
            if(crs.size()>0)
            {
                // heal target
                hero.get(i).healed();
                return true;
            }
        }
        return false;
    }
    
    public boolean cekColideWithEnemy()
    {
        CollisionResults crs = new CollisionResults();
        Vector2f cursor = inputManager.getCursorPosition();
        Vector3f start = cam.getWorldCoordinates(cursor, 0);
        Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();

        // update box position
        Ray ray = new Ray(start, des);
        
        for(int i = 0 ; i < enemy.size();i++)
        {
            enemy.get(i).model.collideWith(ray, crs);
            if(crs.size()>0)
            {
                crs.clear();
                //enemy attacked
                enemy.get(i).attacked();
                return true;
            }
        }
        return false;
    }
    
    public Geometry createBox(Vector3f pos)
    {
        Box b = new Box(pos, 8, 0, 8);
        Geometry temp = new Geometry("box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        temp.setMaterial(mat);
        mat.getAdditionalRenderState().setWireframe(true);
        return temp;
    }
    
    Node floor = new Node();
    private void generateMovement(Vector3f pos, int times) 
    {
        for(int row = -times ; row <= times; row++ ) {
            for(int col = -times; col <= times; col++) {
                if( Math.abs(row) + Math.abs(col) > times ) continue;
                Vector3f temporary = pos.clone();
                temporary.x += row*16;
                temporary.z += col*16;
                temporary.y -= 8;
                        
                Box b = new Box(temporary, 8, 0, 8);
                Geometry geom = new Geometry("box", b);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Red);
                geom.setMaterial(mat);
                mat.getAdditionalRenderState().setWireframe(true);
                floor.attachChild(geom);
            }
        }
        rootNode.attachChild(floor);
    }
    
    public void cekColideCharaceter()
    {
        CollisionResults crs = new CollisionResults();
        Vector2f cursor = inputManager.getCursorPosition();
        Vector3f start = cam.getWorldCoordinates(cursor, 0);
        Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();
        
        Ray ray = new Ray(start, des);
        
        
        for(int i = 0 ; i < hero.size() ; i++)
        {
            hero.get(i).model.collideWith(ray, crs);
            if(crs.size()>0)
            {
                hudText.setText("  "+hero.get(i).hp+"");
                return;
            }
            else
            {
                hudText.setText("");
            }
        }
        
        for(int i = 0 ; i < enemy.size() ; i++)
        {
            enemy.get(i).model.collideWith(ray, crs);
            if(crs.size()>0)
            {
                hudText.setText("  "+enemy.get(i).hp+"");
                return;
            }
            else
            {
                hudText.setText("");
            }
        }
    }
    
    int size=0;
    InputManager inputManager;
    Camera cam;
    @Override
    public void update(float tpf) 
    {
        super.update(tpf);
        
        //=====================================================================================================================
        // get mouse position
        CollisionResults crs = new CollisionResults();
        Vector2f cursor = inputManager.getCursorPosition();
        Vector3f start = cam.getWorldCoordinates(cursor, 0);
        Vector3f des = cam.getWorldCoordinates(cursor, 1).subtract(start).normalize();
        
        //=====================================================================================================================
        // update box position
        Ray ray = new Ray(start, des);
        terrain.collideWith(ray, crs);
        if(crs.size()>0)
        {
           createCursor(crs.getClosestCollision().getContactPoint());
        }
        cekColideCharaceter();
        //=====================================================================================================================
        // summon control and placing character
        if(app.summonControl)
        {
            flyCam.setEnabled(true);
        }
        if(app.isSummon)
        {
            summon();
            app.isSummon = false;
        }
        if(hero.size()>size)
        {
            hero.lastElement().player.setPhysicsLocation(new Vector3f(koordinat.x,koordinat.y+50,koordinat.z));
        }
        
        //=====================================================================================================================
        // hero movement stoping
        for(int i = 0 ; i < hero.size() ; i++)
        {
            if(hero.get(i).moving)
            {
                hero.get(i).currPos = hero.get(i).player.getPhysicsLocation().clone();
                //System.out.println(hero.get(i).currPos.distance(hero.get(i).destination));
                if(hero.get(i).currPos.distance(hero.get(i).destination) <= 20.6)
                {
                    Vector3f newPos = hero.get(i).destination.clone();
                    hero.get(i).player.setWalkDirection(Vector3f.ZERO);
                    hero.get(i).player.setPhysicsLocation(new Vector3f(newPos.x, newPos.y+20, newPos.z));
                    hero.get(i).moving = false;
                    hero.get(i).channel.setAnim("IdleBase");
                    floor.detachAllChildren();
                }
            }
        }
        
        //=====================================================================================================================
        // enemy bot
        int heroIndex=hero.size()-1;
        if(hero.size() > 0 && app.isPlaying && !playerPhase)
        {
            for(int i = 0 ; i < enemy.size(); i ++)
            {
                int max = (int) Math.abs(enemy.get(i).player.getPhysicsLocation().clone().distance(hero.lastElement().player.getPhysicsLocation().clone()));
                ModelCharacter nearestHero = hero.lastElement();
                for(int j = 0 ; j < hero.size()-1 ; j++)
                {
                    if((int)Math.abs(enemy.get(i).player.getPhysicsLocation().clone().distance(hero.get(j).player.getPhysicsLocation().clone())) <= max)
                    {
                        max = (int)Math.abs(enemy.get(i).player.getPhysicsLocation().clone().distance(hero.get(j).player.getPhysicsLocation().clone()));
                        nearestHero = hero.get(j);
                        heroIndex = j;
                    }
                }
                System.out.println(heroIndex);
                if(enemy.get(i).enemyAnimControl)
                {
                    enemy.get(i).lastPos = enemy.get(i).player.getPhysicsLocation().clone();
                    enemy.get(i).channel.setAnim("RunBase");
                    enemy.get(i).enemyAnimControl = false;
                }
                enemy.get(i).moveTo(nearestHero.player.getPhysicsLocation().clone());
                enemyMoveControl(enemy.get(i), nearestHero);
                
                if(cekEnemyEndPhase(enemy))
                {
                    playerPhase = true;
                    enemyEndPhase();
                }
               
            }
        }
        
        if(playerPhase)
        {
           currTime = System.currentTimeMillis(); 
        }
        
        if((currTime - lastTime) > 3000)
        {
            enemyNode.detachAllChildren();
            rootNode.detachChild(enemyNode);
        }
        
        //======================================================================
        for(int i = 0 ; i < hero.size() ; i++)
        {
            if(hero.get(i).isDead())
            {
                rootNode.detachChild(hero.get(i).model);
                hero.removeElementAt(i);
            }
        }
        
        for(int i = 0 ; i < enemy.size() ; i++)
        {
            if(enemy.get(i).isDead())
            {
                rootNode.detachChild(enemy.get(i).model);
                enemy.removeElementAt(i);
            }
        }
        
        try{
            updateTagGuiNode();
        }catch(Exception e){}
        //======================================================================
        if(enemy.size()==0 && app.isPlaying)
        {
            app.isWin = true;
        }
        if(hero.size()==0 && app.isPlaying)
        {
            hudText.setText("Game Over");
            app.gameIsOver = true;
            nifty.gotoScreen("gameOver");
        }
        
        System.out.println(enemy.size());
        
        try{
            for(int i = 0 ; i < usedPos.size() ; i++)
            {
                System.out.println(usedPos.get(i).equals(convert3fTo2f(koordinat)));
            }
        }catch(Exception e){}
    }
    
    long lastTime, currTime;
    
    public void heroEndPhase()
    {
        for(int i = 0 ; i < hero.size() ; i++)
        {
            hero.get(i).refresh();
        }
        actionNode.detachAllChildren();
        rootNode.detachChild(actionNode);
    }
    
    public void enemyEndPhase()
    {
        for(int i = 0 ; i < enemy.size() ; i++)
        {
            enemy.get(i).refresh();
            enemy.get(i).enemyAnimControl = true;
        }
        //enemyNode.detachAllChildren();
        //rootNode.detachChild(enemyNode);
    }
    
    public boolean cekEnemyEndPhase( Vector<Enemy> enemy )
    {
        int index = 0;
        for(int i = 0 ; i < enemy.size() ; i ++)
        {
            if(enemy.get(i).endTurn)
            {
                index++;
            }
        }
        if(index == enemy.size())
            return true;
        
        else
            return false;
    }
    
    Node enemyNode = new Node();
    
    public void enemyMoveControl(Enemy enemy, ModelCharacter hero)
    {
        Vector3f currHeroPos = hero.player.getPhysicsLocation().clone();
        Vector3f currEnemyPos = enemy.player.getPhysicsLocation().clone();
        generateGridTales(currEnemyPos);
        //System.out.println((int)Math.abs( currEnemyPos.distance(enemy.lastPos) ));
        if( (int)Math.abs( currEnemyPos.distance(enemy.lastPos) ) >8 || (int)Math.abs( currEnemyPos.distance(currHeroPos) ) < 48 || enemy.targetIsDead )
        {
            enemy.player.setWalkDirection(Vector3f.ZERO);
            generateGridTales(currEnemyPos);
            enemy.player.setPhysicsLocation(currEnemyPos);
            enemy.channel.setAnim("IdleBase");
            
             // enemy attack
            if(!enemy.attacking && (int)Math.abs( currEnemyPos.distance(currHeroPos) ) <= 64 )
            {
                enemy.attackTo(hero.player.getPhysicsLocation().clone(), enemyNode);
                rootNode.attachChild(enemyNode);
                enemy.channel.setAnim("JumpLoop");
                hero.attacked();
                if(hero.isDead())
                {
                    enemy.targetIsDead = true;
                }
                else
                {
                    enemy.targetIsDead = false;
                }
                enemy.attacking = true;
                lastTime = System.currentTimeMillis();
            }
            
            enemy.endTurn = true;
        }
    }
    
    public void generateGridTales(Vector3f koordinat)
    {
        koordinat.x = (koordinat.x > 0) ? (koordinat.x-(koordinat.x%16)+8) : (koordinat.x-(koordinat.x%16)-8);
        koordinat.z = (koordinat.z > 0) ? (koordinat.z-(koordinat.z%16)+8) : (koordinat.z-(koordinat.z%16)-8);
    }
    
    Vector3f koordinat;
    public void createCursor(Vector3f koordinat)
    {
        
        generateGridTales(koordinat);
        if(terrain.getHeight(new Vector2f(koordinat.x, koordinat.z))<=33)
        {       
            geom.setLocalTranslation(koordinat);
            this.koordinat = koordinat;
        }
    }
    
    public void attachCharacter()
    {
        for(int i = 0 ; i < hero.size() ; i++)
        {
            bulletAppState.getPhysicsSpace().add(hero.get(i).player);
            rootNode.attachChild(hero.get(i).model);
        }
    }
    
    public void summon() 
    {
        if(app.charType.compareToIgnoreCase("red.png")==0)
            hero.add(new ModelCharacter(assetManager, "Models/Sinbad/Sinbad.mesh.xml",app.charType,app,150));
        else
            hero.add(new ModelCharacter(assetManager, "Models/Sinbad/Sinbad.mesh.xml",app.charType,app,100));
       
        koordinat.y+=100;
        bulletAppState.getPhysicsSpace().add(hero.lastElement().player);
        
        rootNode.attachChild(hero.lastElement().model);
        //attachCharacter();
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
    
    public ModelCharacter findActiveHero()
    {
        for(int i = 0 ; i < hero.size() ; i++)
        {
            if(hero.get(i).isPicked)
            {
                return hero.get(i);
            }
        }
        return null;
    }
    
    ModelCharacter currHero;
    
    public void moveMenu()
    {
        currHero = findActiveHero();
        currHero.isMove = true;
        generateMovement(currHero.model.getLocalTranslation().clone(), 1);
    }
    
    public void attackMenu()
    {
        currHero = findActiveHero();
        currHero.attacking = true;
        currHero.healing = false;
        currHero.isMove = false;
        generateMovement(currHero.model.getLocalTranslation().clone(), 3);
    }
    
    public void healMenu()
    {
        currHero = findActiveHero();
        currHero.healing = true;
        currHero.attacking = false;
        currHero.isMove = false;
        generateMovement(currHero.model.getLocalTranslation().clone(), 3);
    }
    
    public void endOfMenu()
    {
        currHero = findActiveHero();
        nifty.gotoScreen("none");
        currHero.isPicked = false;
        currHero.isMove = false;
        currHero.attacking = false;
        currHero.healing = false;
        
        //currHero.clearNode(actionNode);
        //rootNode.detachChild(actionNode);
        currHero.channel.setAnim("IdleBase");
        floor.detachAllChildren();
    }
    
    public void endOfPhase()
    {
        playerPhase = false;
        heroEndPhase();
    }
}
