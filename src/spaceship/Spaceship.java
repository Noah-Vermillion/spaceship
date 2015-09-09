
package spaceship;

import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class Spaceship extends JFrame implements Runnable {
    static final int WINDOW_WIDTH = 420;
    static final int WINDOW_HEIGHT = 445;
    final int XBORDER = 20;
    final int YBORDER = 20;
    final int YTITLE = 25;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    sound zsound = null;
    sound bgSound = null;
    Image outerSpaceImage;

//variables for rocket.
    Image rocketImage;
    int rocketXPos;
    int rocketYPos;
    int rocketYspeed;
    
// variables for missiles
// look at space invaders for idea
    
    Missile missile[];
    
    int Bombxpos;
    int Bombypos;
    boolean Bombactive;
    
//    int currentMissileIndex;
//    int missileNum = 20;
//    int MissileXpos[] = new int [missileNum];
//    int MissileYpos[] = new int [missileNum];
//    boolean missileRight[] = new boolean [missileNum];
//    boolean missileAppear[] = new boolean [missileNum];
    
    int score;
    int highscore;
    int lives;
    boolean gameover;
    
    
// variables for star
    int starNumber = 200;
    int starXpos[];
    int starYpos[];
    int starXspeed;
    
    boolean RocketRight;
    boolean starHit[] = new boolean [starNumber];
    boolean ableHit[] = new boolean [starNumber];
    
    static Spaceship frame;
    public static void main(String[] args) {
        frame = new Spaceship();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Spaceship() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button

// location of the cursor.
                    int xpos = e.getX();
                    int ypos = e.getY();

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                    rocketYspeed ++;
                } else if (e.VK_DOWN == e.getKeyCode()) {
                    rocketYspeed --;
                } else if (e.VK_LEFT == e.getKeyCode()) {
                    starXspeed -- ;
                    
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                    starXspeed ++ ;
                    
                } else if (e.VK_F == e.getKeyCode()) {
                    Bombactive = true;
                }
                else if (e.VK_SPACE == e.getKeyCode())
                {
                   
                    for (int val = 0; val < Missile.missileNum; val++)
                    {
                    missile[Missile.current].Appear = true;
                    missile[Missile.current].Xpos = rocketXPos;
                    missile[Missile.current].Ypos = rocketYPos;
                    missile[Missile.current].Right = RocketRight;
                    Missile.current++;
                    if (Missile.current >= Missile.missileNum)
                        Missile.current = 0;
                     
                    }
                }
              
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }



////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
        //fill border
        g.setColor(Color.black);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);


        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.drawImage(outerSpaceImage,getX(0),getY(0),
                getWidth2(),getHeight2(),this);

        for(int val = 0; val < starNumber; val++)
        {
        drawCircle(getX(starXpos[val]),getYNormal(starYpos[val]),0.0,2.0,2.0);
        }
        
        for(int val = 0; val < missile.length; val++)
        {
            if(missile[val].Appear == true && missile[val].Right == true)
            {
                drawmissile(getX(missile[val].Xpos),getYNormal(missile[val].Ypos),0.0,0.2,0.2);
            }
            if(missile[val].Appear == true && missile[val].Right == false)
            {
                drawmissile(getX(missile[val].Xpos),getYNormal(missile[val].Ypos),0.0,-0.2,0.2);
            }
        }
        if(Bombactive == true)
        {
          drawBomb(getX(Bombxpos),getYNormal(Bombypos),0.0,1.0,1.0 ); 
        }
        if(starXspeed < 0)
       {
          drawRocket(rocketImage,getX(rocketXPos),getYNormal(rocketYPos),0.0,-1.0,-1.0 ); 
       }
        else if(starXspeed > 0 )
       {
           drawRocket(rocketImage,getX(rocketXPos),getYNormal(rocketYPos),0.0,1.0,1.0 );
       }
        else if (starXspeed == 0 && RocketRight == true)
       {
           drawRocket(rocketImage,getX(rocketXPos),getYNormal(rocketYPos),0.0,1.0,1.0 );
       }
        else if (starXspeed == 0 && RocketRight == false)
       {
           drawRocket(rocketImage,getX(rocketXPos),getYNormal(rocketYPos),0.0,-1.0,-1.0 ); 
       }
        
        g.setColor(Color.red);
        g.setFont(new Font("Impact",Font.BOLD,15));
        g.drawString("Score: " + score, 150, 50);
        
        g.setColor(Color.red);
        g.setFont(new Font("Impact",Font.BOLD,15));
        g.drawString("Highscore: " + highscore, 225, 50);
        
        g.setColor(Color.blue);
        g.setFont(new Font("Impact",Font.BOLD,15));
        g.drawString("Lives: " + lives, 10, 50);
        
        if (gameover)
        {
            g.setColor(Color.blue);
            g.setFont(new Font("Impact",Font.BOLD,60));
            g.drawString("GAME OVER", 75, 250);

        }
       
        gOld.drawImage(image, 0, 0, null);
    }
 ////////////////////////////////////////////////////////////////////////////
    public void drawBomb(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.yellow);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
    public void drawCircle(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.yellow);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
    public void drawmissile(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.red);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }


////////////////////////////////////////////////////////////////////////////
    public void drawRocket(Image image,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = rocketImage.getWidth(this);
        int height = rocketImage.getHeight(this);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,this);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

//init the location of the rocket to the center.
        rocketXPos = getWidth2()/2;
        rocketYPos = getHeight2()/2;
        RocketRight = true;
        rocketYspeed = 0;
        
        Bombxpos = rocketXPos;
        Bombypos = rocketYPos;
        Bombactive = false;

        score = 0;
        lives = 3;
        gameover = false;
        
        starXpos = new int [starNumber];
        starYpos = new int[starNumber];
        for(int val = 0; val < starNumber; val++)
        {
             starXpos[val] = (int)(Math.random()*getWidth2());
             starYpos[val] = (int)(Math.random()*getHeight2());
             starHit[val] = true;
             ableHit[val] = true;
        } 
        Missile.current = 0;
        missile = new Missile[Missile.missileNum];
        for (int val = 0; val < missile.length; val ++)
        {
             missile[val] = new Missile();
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            readFile();
            outerSpaceImage = Toolkit.getDefaultToolkit().getImage("./outerSpace.jpg");
            rocketImage = Toolkit.getDefaultToolkit().getImage("./rocket.GIF");
            reset();
            bgSound = new sound("starwars.wav");
        }
        
        if (gameover)
            return;
       // ./starwars.wav
       
        if(bgSound.donePlaying)
        {
             bgSound = new sound("starwars.wav");
        }
        
        rocketYPos += rocketYspeed;
        
        if(starXspeed > 0)
            RocketRight = true;
        else if(starXspeed < 0)
            RocketRight = false;
        
        for(int val = 0; val < starNumber; val ++)
        {
        starXpos[val] -= starXspeed;
        
        if (starXpos[val] <= getX())
        {
           starXpos[val] = getWidth2(); 
           starYpos[val] = (int)(Math.random()*getHeight2());
        }
        else if (starXpos[val] >= getWidth2())
        {
           starXpos[val] = getX();
           starYpos[val] = (int)(Math.random()*getHeight2());
        }
        }
        if (rocketYPos >= getHeight2())
        {
            rocketYspeed = 0;
            rocketYPos = getHeight2();
        }
        else if (rocketYPos <= getX())
        {
            rocketYspeed = 0;
            rocketYPos = getX();
        }
        
        if (starXspeed >= 5)
        {
            starXspeed = 5;
        }
        else if (starXspeed <= -5)
        {
            starXspeed = -5;
        }
        
        if (rocketYspeed >= 5)
        {
            rocketYspeed = 5;
        }
        else if (rocketYspeed <= -5)
        {
            rocketYspeed = -5;
        }
        
        if(score > highscore)
            highscore = score;
        
        if (lives <= 0)
            gameover = true;
        
        for (int val = 0; val < starNumber; val ++)
        {
        
        if(rocketYPos-20 <starYpos[val] && 
          rocketYPos+20 > starYpos[val] &&
          rocketXPos-20 < starXpos[val] && 
          rocketXPos+20 > starXpos[val])
        {
            starHit[val] = true;
            if(starHit[val] && ableHit[val])
            {
             zsound = new sound("ouch.wav");
             lives --;
             ableHit[val] = false;
            }
            
        }
            else
            ableHit[val] = true;    
        }
        
        for(int val = 0; val < missile.length ; val++)
        {
            if (missile[val].Appear == true)
            {
                if(missile[val].Right)
                 missile[val].Xpos ++;
                else
                 missile[val].Xpos --;     
                if (missile[val].Xpos > getWidth2())
                     missile[val].Appear = false;
                else if (missile[val].Xpos < getX())
                     missile[val].Appear = false;
            }
        }
        for(int val = 0; val < Missile.missileNum; val++)
        {
            for(int index = 0; index < starNumber; index++)
            {
                 if(missile[val].Appear == true &&
                    missile[val].Xpos + 10 > starXpos[index] &&
                    missile[val].Xpos - 10 < starXpos[index] &&
                    missile[val].Ypos + 10 > starYpos[index] &&
                    missile[val].Ypos - 10 < starYpos[index])
                 {
                    missile[val].Appear = false;
                    score ++;
                    if (RocketRight == true)
                    {
                        starYpos[index] = (int)(Math.random()*getWidth2());
                        starXpos[index] = getWidth2();
                    }
                    else if (RocketRight == false)
                    {
                        starYpos[index] = (int)(Math.random()*getWidth2());
                        starXpos[index] = getX();
                    }
                    
                 }
            }
        }
        
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    
    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
    
    public void readFile() {
        try {
            String inputfile = "info.txt";
            BufferedReader in = new BufferedReader(new FileReader(inputfile));
            String line = in.readLine();
            while (line != null) {
                String newLine = line.toLowerCase();
                if (newLine.startsWith("numstars"))
                {
                    String numStarsString = newLine.substring(9);
                    starNumber = Integer.parseInt(numStarsString.trim());
                }
                
                else if (newLine.startsWith("nummissile"))
                {
                    String numStarsString = newLine.substring(11);
                    Missile.missileNum = Integer.parseInt(numStarsString.trim());
                }
                
                line = in.readLine();
            }
            in.close();
        } catch (IOException ioe) {
        }
    }
}

class sound implements Runnable {
    Thread myThread;
    File soundFile;
    public boolean donePlaying = false;
    sound(String _name)
    {
        soundFile = new File(_name);
        myThread = new Thread(this);
        myThread.start();
    }
    public void run()
    {
        try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat format = ais.getFormat();
    //    System.out.println("Format: " + format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
        source.open(format);
        source.start();
        int read = 0;
        byte[] audioData = new byte[16384];
        while (read > -1){
            read = ais.read(audioData,0,audioData.length);
            if (read >= 0) {
                source.write(audioData,0,read);
            }
        }
        donePlaying = true;

        source.drain();
        source.close();
        }
        catch (Exception exc) {
            System.out.println("error: " + exc.getMessage());
            exc.printStackTrace();
        }
    }

}


class Missile
{
    public static int current = 0;
    public static int missileNum = 20;
    
    public int Xpos;
    public int Ypos;
    public boolean Right;
    public boolean Appear; 
    Missile()
    {
       Appear = false; 
    }
}