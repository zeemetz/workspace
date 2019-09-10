
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;

public class Food 
{
    Random rand = new Random();
    Geometry food;
    Vector3f pos;
    
    Food(Node rootNode, AssetManager assetManager)
    {
        
        
        int x = (rand.nextInt()%10);
        int y = (rand.nextInt()%10);
        int z = (rand.nextInt()%10);
        
        pos = new Vector3f(x*10, 0, 0);
        
        Box b = new Box(pos, 5f, 5f, 5f);
        food = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        mat.getAdditionalRenderState().setWireframe(true);
        food.setMaterial(mat);
        rootNode.attachChild(food);
    }
}
