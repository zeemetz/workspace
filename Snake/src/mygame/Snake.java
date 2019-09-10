
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Snake 
{
    Geometry body;
    
    Snake(Node rootNode, AssetManager assetManager)
    {
        Box b = new Box(Vector3f.ZERO, 5f, 5f, 5f);
        body = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        mat.getAdditionalRenderState().setWireframe(true);
        body.setMaterial(mat);
        rootNode.attachChild(body);
    }
    
    public void snakeMove(Vector3f position)
    {
        body.move(position);
    }
}
