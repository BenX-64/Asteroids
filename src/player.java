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

    int deg = 0;
    int hp = 100; //self explanatory
    int dps; //damage per shot
    boolean isBoosting = false; //self explanatory

    int x = Main.boardxs/2 - psize/2; //player x and y positions
    int y = Main.boardys/2 - psize/2;

    private int ma; //max acceleration
    private int ax; //x and y components of acceleration
    private int ay;
    private int mv; //max velocity
    private int vx; //x and y components of velocity
    private int vy;

    JLabel icon;
    ImageIcon imageL, imageB;
    String iconPathL, iconPathB;

    
    public player(int mv, int hp, int dps, int ma){
        this.mv = mv;
        this.hp = hp;
        this.dps = dps;
        this.ma = ma;
        String degstr = Integer.toString(deg);
        String iconPathL = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        String iconPathB = new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png";

        ImageIcon imageL = new ImageIcon(iconPathL);
        ImageIcon imageB = new ImageIcon(iconPathB);

        icon = new JLabel(imageL); //hard code for testing stuff
        icon.setBounds(x, y, psize, psize);

    }

    public void move(){

        x+=100;
        y+=100;
        icon.setBounds(x,y,psize,psize);
    }
    
    public void rotate(int deg){
        this.deg += deg;
        if (this.deg >= 360 || this.deg <= 0){
            this.deg = 0;
        }
        String degstr = Integer.toString(this.deg);
        String tempIconPath = isBoosting ? new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png" : new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        ImageIcon tempImage = new ImageIcon(tempIconPath);
        this.icon.setIcon(tempImage);
        icon.setBounds(x,y,psize,psize);
    }
}
