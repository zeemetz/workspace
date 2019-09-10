
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Enemy extends ModelCharacter
{
    protected boolean enemyAnimControl = true;
    
    public Enemy(AssetManager assetManager, String modelUrl, Main app,int hp) 
    {
        super(assetManager, modelUrl,app,hp);
        //model.setLocalScale(0.5f, 0.5f, 0.5f);
        model.setLocalTranslation(0, -500, 0);
    }
    
    protected Vector3f lastPos,enemyPos, heroPos;
    
    public void bot(Vector<ModelCharacter> hero)
    {
        enemyPos = player.getPhysicsLocation();
        heroPos = player.getPhysicsLocation();
        
        for(int i = 0 ; i < hero.size() ; i++)
        {
            if(enemyPos.y > heroPos.y)
            {
                model.move(0, 0, enemyPos.z+8);
                break;
            }
            if( enemyPos.x > heroPos.x )
            {
                model.move(enemyPos.x+8, 0, 0);
                break;
            }
        }
    }
}
