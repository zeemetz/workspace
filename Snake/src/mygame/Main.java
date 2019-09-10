package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Main extends SimpleApplication 
{
    Vector<Snake> snake = new Vector<Snake>();
    Vector<Food> food = new Vector<Food>();
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() 
    {
        initMap();
        
        cam.setLocation(new Vector3f(0, -200, 0));
        cam.lookAt(new Vector3f(0, -1, 0), Vector3f.UNIT_Y);
        
        inputManager.clearMappings();
        
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        /*
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
         *
         */
        lastTime = System.currentTimeMillis();
        
        initKeys();
        food.add(new Food(rootNode, assetManager));
    }

    public void createBox(Vector3f pos, float x, float y , float z)
    {
        Box b = new Box(pos, x,y,z);
        Geometry box = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setWireframe(true);
        box.setMaterial(mat);
        rootNode.attachChild(box);
    }
    
    public void initMap()
    {
        createBox(new Vector3f(100, 0, 0), 0, 100, 100);
        createBox(new Vector3f(-100, 0, 0), 0, 100, 100);
        
        createBox(new Vector3f(0, 100, 0), 100, 0, 100);
        createBox(new Vector3f(0, -100, 0), 100, 0, 100);
        
        createBox(new Vector3f(0, 0, 100), 100, 100, 0);
        createBox(new Vector3f(0, 0, -100), 100, 100, 0);
    }
    
    public void initKeys()
    {
        inputManager.addMapping("left", new KeyTrigger(keyInput.KEY_LEFT));
        inputManager.addMapping("up", new KeyTrigger(keyInput.KEY_UP));
        inputManager.addMapping("down", new KeyTrigger(keyInput.KEY_DOWN));
        inputManager.addMapping("right", new KeyTrigger(keyInput.KEY_RIGHT));
        inputManager.addMapping("top", new KeyTrigger(keyInput.KEY_W));
        inputManager.addMapping("bottom", new KeyTrigger(keyInput.KEY_S));
        
        inputManager.addListener(action, "left","right","up","down","top","bottom");
    }
    public boolean goLeft, goRight, goUp, goDown, goTop, goBottom;
    
    public void initControl()
    {
        goLeft = goRight = goUp = goDown = goTop = goBottom = false;
    }
    
    public ActionListener action = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) 
        {
            if(isPressed)
            {
                initControl();
                if(name.equals("left"))
                {
                    goLeft = true;
                }
                else if(name.equals("right"))
                {
                    goRight = true;
                }
                else if(name.equals("up"))
                {
                    goUp = true;
                }
                else if(name.equals("down"))
                {
                    goDown=true;
                }
                else if(name.equals("top"))
                {
                    goTop=true;
                }
                else if(name.equals("bottom"))
                {
                    goBottom = true;
                }
            }
        }
    };
    
    long lastTime,currTime;
    boolean ismoving = false;
    @Override
    public void simpleUpdate(float tpf) 
    {
        currTime = System.currentTimeMillis();
        
        if((currTime-lastTime) > 300)
        {
            for(int i = snake.size()-1 ; i>0 ; i--)
            {
                snake.get(i).body.setLocalTranslation(snake.get(i-1).body.getLocalTranslation().clone());   
            }
            
            lastTime = currTime;
            snakeHeadMove();
        }
        mapControl();
        eatFood();
    }
    
    public void eatFood()
    {
        if(snake.firstElement().body.getLocalTranslation().clone().distance(food.firstElement().pos)==0)
        {
            snake.add(new Snake(rootNode, assetManager));
            snake.lastElement().body.setLocalTranslation(snake.firstElement().body.getLocalTranslation());
            
            rootNode.detachChild(food.firstElement().food);
            food.clear();
            food.add(new Food(rootNode, assetManager));
        }
    }
    
    public void snakeHeadMove()
    {
        if(goLeft)
            snake.firstElement().snakeMove(new Vector3f(0, 0, -10));
        else if(goRight)
            snake.firstElement().snakeMove(new Vector3f(0, 0, 10));
        else if(goUp)
            snake.firstElement().snakeMove(new Vector3f(-10, 0, 0));
        else if(goDown)
            snake.firstElement().snakeMove(new Vector3f(10, 0, 0));
        else if(goTop)
            snake.firstElement().snakeMove(new Vector3f(0, -10, 0));
        else if(goBottom)
            snake.firstElement().snakeMove(new Vector3f(0, 10, 0));
    }
    
    public void mapControl()
    {
        Vector3f pos = snake.firstElement().body.getLocalTranslation();
        if(snake.firstElement().body.getLocalTranslation().clone().z > 100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, pos.y, -100);
        }
        if(snake.firstElement().body.getLocalTranslation().clone().z < -100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, pos.y, 100);
        }
        
        if(snake.firstElement().body.getLocalTranslation().clone().y > 100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, -100, pos.z);
        }
        if(snake.firstElement().body.getLocalTranslation().clone().y < -100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, 100, pos.z);
        }
        
        if(snake.firstElement().body.getLocalTranslation().clone().x > 100)
        {
            snake.firstElement().body.setLocalTranslation(-100, pos.y, pos.z);
        }
        if(snake.firstElement().body.getLocalTranslation().clone().x < -100)
        {
            snake.firstElement().body.setLocalTranslation(100, pos.y, pos.z);
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) 
    {
    }
}
