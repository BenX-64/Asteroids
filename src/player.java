import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class player {
    final int psize = 40; //player size as a square

    boolean stoppingSpin = false;

    int totdeg = 0; //Keeps track of ship's rotation
    int deg = 0;
    int hp = 100; //self explanatory
    int dps; //damage per shot

    boolean isBoosting = false; //self explanatory
    int isTurning = 0; //self explanatory, 0 for no, 1 = counter-clockwise, 2 = clockwise
    int x = Main.boardxs/2 - psize/2; //player x and y positions
    int y = Main.boardys/2 - psize/2;

    private int av = 0; //angular velocity
    private int mt; //max torque
    private int ma; //max acceleration
    private int ax; //x and y components of acceleration
    private int ay;
    private int vx = 0; //x and y components of velocity
    private int vy = 0;
    int cs; //current speed of the ship
    

    JLabel icon;
    ImageIcon imageL, imageB;
    String iconPathL, iconPathB;

    
    public player(int hp, int dps, int ma, int mt){
        this.mt = mt;
        this.hp = hp;
        this.dps = dps;
        this.ma = ma;
        String degstr = Integer.toString(deg); //Manages icon img paths
        String iconPathL = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        String iconPathB = new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png";

        ImageIcon imageL = new ImageIcon(iconPathL);
        ImageIcon imageB = new ImageIcon(iconPathB);

        icon = new JLabel(imageL); 
        icon.setBounds(x, y, psize, psize);

    }

    public void move(){

        if(isBoosting){ //Accelerates the ship in the chosen direction
            vx-=ma*Math.sin(Math.toRadians(deg));
            vy-=ma*Math.cos(Math.toRadians(deg));
        }
        x+=vx; //updates x and y positions
        y+=vy;
        
        if(isTurning==1){ //updates ship's rotation
            av -= mt; 
        }else if(isTurning ==2){
            av += mt;
        }
        rotate(av);
        antiOOB(); //prevents ship from exiting board
        icon.setBounds(x,y,psize,psize);
    }
    
    public void rotate(int deg){
        this.totdeg += deg;
        this.deg = this.totdeg/5;
        this.deg*=5;

        //keeps the rotation manageable
        if (this.deg >= 360){
            this.deg = 0;
            this.totdeg = 0;
        }else if(this.deg < 0){
            this.deg = 355;
            this.totdeg = 355;
        }

        String degstr = Integer.toString(this.deg);
        //imageicon shenanigans
        String tempIconPath = isBoosting ? new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png" 
        : new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; 
        
        ImageIcon tempImage = new ImageIcon(tempIconPath);
        this.icon.setIcon(tempImage);
        icon.setBounds(x,y,psize,psize);
    }
    
    public void antiOOB(){  //prevents ship from leaving the screen
        if(x+psize/2 >= Main.boardxs){
            x = psize/2;
        }
        if(x < -psize/2){
            x = Main.boardxs - psize/2;
        }
        if(y+psize/2 > Main.boardys){
            y = -psize/2;
        }
        if(y<-psize/2){
            y = Main.boardys-psize/2;
        }
    }
}
