
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
//import com.sun.media.util.SettableTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ModelCharacter
{
    protected AnimControl control;
    protected AnimChannel channel;
    protected CharacterControl player;
    protected Spatial model;
    
    protected boolean isMove, moving, attacking, healing, isPicked, endTurn;
    protected boolean targetIsDead = false;
    
    protected AssetManager assetManager;
    protected ParticleEmitter fire,spark;
    
    public ModelCharacter(AssetManager assetManager, String modelUrl, Main app, int hp)
    {
        this.hp = hp;
        this.app = app;
        this.assetManager = assetManager;
        model = assetManager.loadModel(modelUrl);
        
        
        BoxCollisionShape capsule = new BoxCollisionShape(new Vector3f(3, 20, 3)); 
        model.setLocalScale(3, 3, 3);
        control = model.getControl(AnimControl.class);
        channel = control.createChannel();
        player = new CharacterControl(capsule, 0.3f);
        model.addControl(player);
        
        isMove = moving = attacking = healing = isPicked = endTurn =  false;
    }
    
    Main app;
    
    public ModelCharacter(AssetManager assetManager, String modelUrl, String charType, Main app, int hp)
    {
        this.hp = hp;
        this.app = app;
        this.assetManager = assetManager;
        model = assetManager.loadModel(modelUrl);
        
        
        BoxCollisionShape capsule = new BoxCollisionShape(new Vector3f(3, 20, 3)); 
        model.setLocalScale(3, 3, 3);
        control = model.getControl(AnimControl.class);
        channel = control.createChannel();
        player = new CharacterControl(capsule, 0.3f);
        model.addControl(player);
        Material mat_lit = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat_lit.setTexture("DiffuseMap", assetManager.loadTexture("Textures/"+charType));
        model.setMaterial(mat_lit);
        isMove = moving = attacking = healing = isPicked = endTurn =  false;
    }
    
    Vector3f currPos, destination;
    
    public void viewTo(Vector3f koordinat)
    {
        currPos = player.getPhysicsLocation().clone();
        destination = koordinat.clone();

        //channel.setAnim("RunBase");
        
        if( Math.abs((float)(destination.x - currPos.x)) >= 8)
        {
            if( destination.x > currPos.x )
            {
                player.setViewDirection(new Vector3f(0.1f, 0, 0));
            }
            else if(destination.x < currPos.x)
            {
                player.setViewDirection(new Vector3f(-0.1f, 0, 0));
            }
        }
        else
        {
            if( destination.z > currPos.z )
            {
                player.setViewDirection(new Vector3f(0, 0, 0.1f));
            }
            else if( destination.z < currPos.z )
            {
                player.setViewDirection(new Vector3f(0, 0, -0.1f));
            }
        }
    }
    
    public void moveTo(Vector3f koordinat)
    {
        currPos = player.getPhysicsLocation().clone();
        destination = koordinat.clone();

        //channel.setAnim("RunBase");
        
        if( Math.abs((float)(destination.x - currPos.x)) >= 8)
        {
            if( destination.x > currPos.x )
            {
                //player.setWalkDirection(new Vector3f(0.1f, 0, 0));
                player.setViewDirection(new Vector3f(0.1f, 0, 0));
            }
            else if(destination.x < currPos.x)
            {
                //player.setWalkDirection(new Vector3f(-0.1f, 0, 0));
                player.setViewDirection(new Vector3f(-0.1f, 0, 0));
            }
        }
        else
        {
            if( destination.z > currPos.z )
            {
                //player.setWalkDirection(new Vector3f(0, 0, 0.1f));
                player.setViewDirection(new Vector3f(0, 0, 0.1f));
            }
            else if( destination.z < currPos.z )
            {
                //player.setWalkDirection(new Vector3f(0, 0, -0.1f));
                player.setViewDirection(new Vector3f(0, 0, -0.1f));
            }
        }
        
        viewTo(koordinat);
    }
    
    public void clearNode(Node node)
    {
        try{
            node.detachChild(fire);
            node.detachChild(spark);
        }catch(Exception e){}
    }
    
    public void healTo(Vector3f target, Node node)
    {   
        spark = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        spark.setMaterial(mat_red);
        spark.setImagesX(2); 
        spark.setImagesY(2); // 2x2 texture animation
        spark.setEndColor(  new ColorRGBA(0f, 0f, 1f, 1f));   // red
        spark.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        spark.setStartSize(20f);
        spark.setEndSize(10f);
        spark.setGravity(0, 0, 0);
        spark.setLowLife(1f);
        spark.setHighLife(3f);
        spark.getParticleInfluencer().setVelocityVariation(0.3f);
        
        spark.setLocalTranslation(target);
        if(app.soundEffectIsOn)
            app.heal_sound_effect.play();
        node.attachChild(spark);
        player.setViewDirection(target);
        viewTo(target);
    }
    
    public void refresh()
    {
        isMove = moving = attacking = healing = isPicked = endTurn = false;
    }
    
    int hp;
    
    public boolean isDead()
    {
        if(hp<=0)
        {
            return true;
        }
        return false;
    }
    
    public void attacked()
    {
        hp-=1000;
    }
    public void healed()
    {
        hp+=10;
    }
    
    public void attackTo(Vector3f target, Node node)
    {
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2); 
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        fire.setStartSize(20f);
        fire.setEndSize(10f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        
        fire.setLocalTranslation(target);
        if(app.soundEffectIsOn)
            app.attack_sound_effect.play();
        node.attachChild(fire);
        viewTo(target);
    }
}
