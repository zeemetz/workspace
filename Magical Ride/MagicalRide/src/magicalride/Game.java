
package magicalride;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Anim
{
    int x1,x2,y1,y2;
}

public class Game extends JPanel
{
    // DEKLARASI DAN INITIALISASI == VARIABLE UTAMA
    
    // frame size
    int FrameWidth = 800, FrameHeight = 500;
    
    // Speed untuk atur background, monster, sama coin
    int speed = 3;
    
    //background
    Point bg1 = new Point(0, 0);
    Point bg2 = new Point(0 , FrameHeight-130 ); // 130 height dari image7
    
    //power up
    Point PowerUp = new Point(200, 200);
    int PowerUpType=0;
    int coinSpeed = 10; // kalau power up hisap koin
    Vector<Anim> PowerUpAnim = new Vector<Anim>();
    
    //character animation
    Point character = new Point( 50 , 0);
    int CharAnimIndex = 40;
    int CharDividenIndex = 20;
    Vector<Anim> CharAnimation = new Vector<Anim>();
    
    // monster animation
    Point monster = new Point( FrameWidth, 300); // harus dibuat vector biar bisa banyak
    int MonsAnimIndex = 40;
    int MonsDividenIndex = 20;
    Vector<Anim> MonsAnimation = new Vector<Anim>();
    
    //Coin Animation
    Vector<Point> coin = new Vector<Point>();// harus dibuat vector biar bisa banyak
    int CoinAnimIndex = 40;
    int CoinDividenIndex = 20;
    Vector<Anim> CoinAnimation = new Vector<Anim>();
    
    // control
    // character control
    boolean isfly = false;
    boolean istank = false;
    boolean ismagnetis = false;
    
    // Collision
    Rectangle PlayerRect;
    
    // harus dibuat menjadi vector biar bisa banyak
    Rectangle MonsterRect, CoinRect, PowerUpRect; 
    // bollean intersect buat check collision
    boolean hitMonster = true;
    boolean hitPowerUp = true;
    
    // Timer Interval Summon Coin dan Monster
    long lastTime, currTime;
    
    // Timer Interval lamanya power up bertahan
    long lastPowerUpTime, currPowerUpTime;
    
    //scoring
    int score = 0;
    boolean isOver = false;
    
    // Thread untuk menjalankan process
    Thread thread = new Thread()
    {

        @Override
        public void run() 
        {
            while(true)
            {
                if(!isOver)
                {
                    repaint();
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch(Exception e)
                    {

                    }
                }
                else
                {
                    break;
                }
                
            }
            
        }
        
    };
    
    // menu
    JFrame GameFrame; // game frame, ditutup pas game over
    
    // contructor
    Game(JFrame frame)
    {
        //menu
        GameFrame = frame; // ambil variabel yang dipasing jadi global variable
        
        // char animasi
        CharAnimation.add(new Anim());
        CharAnimation.add(new Anim());
        
        // initialisasi vector animasi player
        // index 0
        CharAnimation.get(0).x1 = 1408;
        CharAnimation.get(0).y1 = 966;
        
        CharAnimation.get(0).x2 = 1518;
        CharAnimation.get(0).y2 = 1039;
        
        //index 1
        CharAnimation.get(1).x1 = 1083;
        CharAnimation.get(1).y1 = 965;
        
        CharAnimation.get(1).x2 = 1188;
        CharAnimation.get(1).y2 = 1040;
        
        // Monster
        // initialisasi vector animasi monster
        //index 0
        MonsAnimation.add(new Anim());
        MonsAnimation.get(0).x1 = 6;
        MonsAnimation.get(0).y1 = 268;
        MonsAnimation.get(0).x2 = 172;
        MonsAnimation.get(0).y2 = 418;
        // index 1
        MonsAnimation.add(new Anim());
        MonsAnimation.get(1).x1 = 175;
        MonsAnimation.get(1).y1 = 265;
        MonsAnimation.get(1).x2 = 352;
        MonsAnimation.get(1).y2 = 435;
        
        //coin
        // index 0
        CoinAnimation.add(new Anim());
        CoinAnimation.get(0).x1 = 0;
        CoinAnimation.get(0).y1 = 0;
        CoinAnimation.get(0).x2 = 68;
        CoinAnimation.get(0).y2 = 70;
        //index1
        CoinAnimation.add(new Anim());
        CoinAnimation.get(1).x1 = 0;
        CoinAnimation.get(1).y1 = 70;
        CoinAnimation.get(1).x2 = 68;
        CoinAnimation.get(1).y2 = 140;
        
        // power up
        // index 0 = speed bertambah
        PowerUpAnim.add(new Anim());
        PowerUpAnim.get(0).x1 = 400 ;
        PowerUpAnim.get(0).y1 = 200;
        PowerUpAnim.get(0).x2 = 462 ;
        PowerUpAnim.get(0).y2 = 263;
        // index 1 = kebal
        PowerUpAnim.add(new Anim());
        PowerUpAnim.get(1).x1 = 166;
        PowerUpAnim.get(1).y1 = 201;
        PowerUpAnim.get(1).x2 = 234;
        PowerUpAnim.get(1).y2 = 268;
        // index 2 = hisap coin
        PowerUpAnim.add(new Anim());
        PowerUpAnim.get(2).x1 = 126;
        PowerUpAnim.get(2).y1 = 123;
        PowerUpAnim.get(2).x2 = 185;
        PowerUpAnim.get(2).y2 = 185;
        
        lastTime = (System.currentTimeMillis())/1000;
        lastPowerUpTime = (System.currentTimeMillis())/1000;
        
        //init coin pertama karena dia adalah vector
        initCoin();
        
        // mulai animasi
        thread.start();
    }
    
    // FUNGSI BUAT COIN
    // fungsi buat reset posisi coin
    public void initCoin()
    {
        /*
        coin.x = FrameWidth;
        // posisi y harus dirandom
        Random rand = new Random();
        coin.y = rand.nextInt((FrameHeight-50)); // 50 karena ukuran coin
        */
        
        coin.add(new Point(FrameWidth,0));
        coin.add(new Point(FrameWidth,50));
        coin.add(new Point(FrameWidth, 100));
        coin.add(new Point(FrameWidth, 150));
        coin.add(new Point(FrameWidth, 200));
    }
    // fungsi gambar coin
    public void drawCoinImage(Graphics2D g2d)
    {
        if(CoinAnimIndex > 0)
        {
            CoinAnimIndex --;
        }
        else
        {
            CoinAnimIndex = 39;
        }
        // looping sesuai dengan ukuran vector dari coin
        for(int i = 0 ; i < coin.size() ; i++)
        {
            if(i != coin.size()-1)
        g2d.drawImage( new ImageIcon("assets/play/image 16.png").getImage(),
                coin.get(i).x, coin.get(i).y, coin.get(i).x +50, coin.get(i).y+50, 
                CoinAnimation.get((CoinAnimIndex/CoinDividenIndex)).x1, CoinAnimation.get((CoinAnimIndex/CoinDividenIndex)).y1, 
                CoinAnimation.get((CoinAnimIndex/CoinDividenIndex)).x2, CoinAnimation.get((CoinAnimIndex/CoinDividenIndex)).y2, this);
        
        // coin ikut bergerak dengan background
        if( coin.get(i).x > (0-50) ) // 50 karena ukuran coin adalah 50
        {
            coin.get(i).x -=speed;
            
            //kalau dapat power up
            if(ismagnetis)
            {
                coin.get(i).x -=coinSpeed;
            }
        }
        else
        {
            coin.removeAllElements();
            initCoin();
        }
        
        // jika dapat power up ismagnetis coin akan dihisap
        if( ismagnetis )
        {
            if( coin.get(i).y > character.y )
            {
                coin.get(i).y -= coinSpeed;
            }
            else if( coin.get(i).y < character.y )
            {
                coin.get(i).y += coinSpeed;
            }
        }
        
        }
    }
    
    // FUNGSI BUAT MONSTER
    // fungsi buat reset posisi monster
    public void initMonster()
    {
         monster.x = FrameWidth;
         // posisi y monster harus dirandom
         Random rand = new Random();
         monster.y = rand.nextInt((FrameHeight-100)); // 100 karena ukuran monster adalah 100
    }
    // fungsi gambar monster
    public void drawImageMonster(Graphics2D g2d)
    {
        if(MonsAnimIndex > 0)
        {
            MonsAnimIndex --;
        }
        else
        {
            MonsAnimIndex = 39;
        }
        
        g2d.drawImage( new ImageIcon("assets/play/image 13.png").getImage(),
                monster.x, monster.y, monster.x+100, monster.y+100, 
                MonsAnimation.get((MonsAnimIndex/MonsDividenIndex)).x1, MonsAnimation.get((MonsAnimIndex/MonsDividenIndex)).y1, 
                MonsAnimation.get((MonsAnimIndex/MonsDividenIndex)).x2, MonsAnimation.get((MonsAnimIndex/MonsDividenIndex)).y2, this);
        
        // monster ikut bergerak dengan background
        if( monster.x > (0-100) ) // 100 karena ukuran x dari monster
        {
            monster.x -=speed;
        }
        else
        {
           initMonster();
        }
    }
    
    // FUNGSI POWER UP
    // inisialisasi power up ( posisi dan jenis power up )
    public void initPowerUp()
    {
        PowerUp.x = FrameWidth;
        // posisi y power up harus dirandom
         Random rand = new Random();
         PowerUp.y = rand.nextInt((FrameHeight-50)); // 100 karena ukuran power up adalah 50
         
         // random jenis power up
         PowerUpType = rand.nextInt(3);
    }
    // draw image power up
    public void drawPowerUp(Graphics2D g2d)
    {
        // draw power up pada g2d
        g2d.drawImage(new ImageIcon("assets/play/image 8.png").getImage(), 
                    PowerUp.x, PowerUp.y, PowerUp.x +50, PowerUp.y+50, 
                    PowerUpAnim.get(PowerUpType).x1, PowerUpAnim.get(PowerUpType).y1, 
                    PowerUpAnim.get(PowerUpType).x2, PowerUpAnim.get(PowerUpType).y2, null);
        
        if( PowerUp.x > (0-50) ) // 50 karena ukuran koin
        {
            PowerUp.x -= speed  ;
        }
        else
        {
            initPowerUp();
        }
        
    }
    
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        // BACKGROUND
        g2d.drawImage( new ImageIcon("assets/bg/image3.jpg").getImage(), bg1.x, bg1.y,   this);
        g2d.drawImage( new ImageIcon("assets/bg/image3.jpg").getImage(), bg1.x + FrameWidth, bg1.y, this);
        
        g2d.drawImage( new ImageIcon("assets/bg/image7.png").getImage(), bg2.x, bg2.y, this);
        g2d.drawImage( new ImageIcon("assets/bg/image7.png").getImage(), bg2.x + FrameWidth, bg2.y, this);
       
        // 3 speed bg belakang, 10 speed bg depan
        bg1.x-=speed;
        if(bg1.x<=-FrameWidth)
        {
            bg1.x=0;
        }
        
        bg2.x-=speed+7;
        if(bg2.x<=-FrameWidth)
        {
            bg2.x=0;
        }
        
        
        
        // PLAYER
        // 75 = index besar ukuran character + 25 belum tau dari mana
        if(character.y < FrameHeight - 100  && !isfly)
        {
            character.y += 4;
        }
        if(isfly)
        {
            if(character.y > 0)
            {
                character.y-=4;
            }
        }
        
        // animasi player
        if(CharAnimIndex > 0 )
        {
            CharAnimIndex --;
        }
        else
        {
            CharAnimIndex = 39;
        }
        
        g2d.drawImage( new ImageIcon("assets/play/image10.png").getImage(),
                character.x, character.y, character.x+75, character.y+75, 
                CharAnimation.get((CharAnimIndex/CharDividenIndex)).x1, CharAnimation.get((CharAnimIndex/CharDividenIndex)).y1, 
                CharAnimation.get((CharAnimIndex/CharDividenIndex)).x2, CharAnimation.get((CharAnimIndex/CharDividenIndex)).y2, this);
        
        // animasi monster
        // dibuat menjadi fungsi
        // drawImageMonster(g2d);
        if( !hitMonster )
        {
            drawImageMonster(g2d);
        }
        
        // Animasi Coin
        // dibuat menjadi fungsi
        // drawCoinImage(g2d);
        
        drawCoinImage(g2d);
        
        // draw power up
        if( !hitPowerUp )
        {
            drawPowerUp(g2d);
        }
        
        //check collision
        // buat kotak mengelilingi character
       PlayerRect = new Rectangle(character.x, character.y, 75, 75); // 75 adalah ukuran Player
       MonsterRect = new Rectangle(monster.x, monster.y, 100, 100); // 100 adalah ukuran monster
       //CoinRect = new Rectangle(coin.x, coin.y, 50, 50); // 50 adalah ukuran dari coin
       PowerUpRect = new Rectangle(PowerUp.x, PowerUp.y, 50, 50); // 50 adalah ukuran power up
       
       // collision player dengan monster
       if ( PlayerRect.intersects(MonsterRect) )
       {
           hitMonster = true;
           initMonster();
           
           if( !istank )
           {
                // ulang ke loading bar
                new LoadingBar();
                
                isOver = true;
                // matikan frame game yang sedang berjalan, karena di loading bar sudah dipanggil game baru
                GameFrame.dispose();
                JOptionPane.showMessageDialog(null, score);
           }
       }
       
       for(int i = 0 ; i < coin.size()-1 ; i++)
       {
            CoinRect = new Rectangle(coin.get(i).x, coin.get(i).y, 50, 50);
            // collision player dengan coin
            if( PlayerRect.intersects(CoinRect) )
            {
                coin.remove(i);
                score+=10;
            }
       }
       
       // collision player dengan power up
       if( PlayerRect.intersects(PowerUpRect) )
       {
           // setting hit, jika true maka gambar tidak ditampilkan
           hitPowerUp = true;
           
           // menentukan kejadian yang terjadi terhadap power up yang didapat 
           if(PowerUpType == 0)
           {
               speed = 10;
               lastPowerUpTime = currPowerUpTime =(System.currentTimeMillis())/1000;
           }
           else if( PowerUpType == 1 )
           {
               istank = true;
               lastPowerUpTime = currPowerUpTime =(System.currentTimeMillis())/1000;
           }
           else if( PowerUpType == 2 )
           {
               ismagnetis = true;
               lastPowerUpTime = currPowerUpTime =(System.currentTimeMillis())/1000;
           }
           // init ulang power up
           initPowerUp();
           
       }
       
       // interval time bertahannya power up
       currPowerUpTime = (System.currentTimeMillis())/1000;
       
       if( (currPowerUpTime - lastPowerUpTime) >= 3 )
       {
           speed = 3;
           istank = false;
           ismagnetis = false;
           lastPowerUpTime = currPowerUpTime;
       }
       
       // interval time buat summon monster, coin, power up lagi :
       currTime = (System.currentTimeMillis())/1000;
       
       if( (currTime - lastTime) >= 1 )
       {
           hitMonster = false;
           hitPowerUp = false;
           lastTime = currTime;
       }
       
       //draw score 
       String tempScore = String.valueOf(score);
       g2d.drawString(tempScore, 100,100);
    }
}
