import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.geom.Area;

public class player {
    final int psize = 40; //player size as a square

    boolean isDummy; 
    //if it's true it's just gonna be a ship
    //that mimics the real ship's rotation on the main screen.


    int totdeg = 0; //Keeps track of ship's rotation
    int deg = 0; //Value used to determine sprite rotation
    int maxhp = 100; //self explanatory
    int hp;
    int dps; //damage per shot

    boolean shooting = false; //stops player from holding space
    boolean thrusting = false; //self explanatory
    int isTurning = 0; //self explanatory, 0 for no, 1 = counter-clockwise, 2 = clockwise
    double x = Main.boardxs/2 - psize/2; //player x and y positions
    double y = Main.boardys/2 - psize/2;

    private int av = 0; //angular velocity
    private double mt; //max torque
    private double ma; //max acceleration
    private int ax; //x and y components of acceleration
    private int ay;
    private double vx = 0; //x and y components of velocity
    private double vy = 0;
    double cs = 0.0; //current speed of the ship
    String degstr;

    JLabel icon;
    ImageIcon imageL, imageB;
    String iconPathL, iconPathB;

    Area hitbox;
    
    public player(int hp, int dps, int ma, double mt, boolean isDummy){
        this.mt = mt;
        this.maxhp = hp;
        this.hp = this.maxhp; 
        this.dps = dps;
        this.ma = ma;
        /*
        i couldn't find a way to easily rotate a jlabel imageicon
        so please don't judge it too harshly
        */
        degstr = Integer.toString(deg); //Manages icon img paths
        iconPathL = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        iconPathB = new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png";

        imageL = new ImageIcon(iconPathL);
        imageB = new ImageIcon(iconPathB);
        this.isDummy = isDummy;
        if(this.isDummy){
            x = 0; 
            y = Main.boardys-2*psize;
            //thrusting = false;
        }
        icon = new JLabel(imageL); 
        icon.setBounds((int)x, (int)y, psize, psize);
        hitbox = new Area(icon.getBounds());

    }
    public void thrustOn(){
        thrusting = true;
    }
    public void thrustOff(){
        thrusting = false;
    }
    public void move(){
        antiOOB();
        if(this.thrusting){ //Accelerates the ship in the chosen direction
            this.vx-=this.ma*Math.sin(Math.toRadians(deg));
            this.vy-=this.ma*Math.cos(Math.toRadians(deg));
        }
        this.x+=this.vx; //updates x and y positions
        this.y+=this.vy;
        
        if(this.isTurning==1){ //updates ship's rotation
            this.av -= this.mt; 
        }else if(this.isTurning ==2){
            this.av += this.mt;
        }
        rotate(av);
        this.icon.setBounds((int)x,(int)y,psize,psize); 
        hitbox = new Area(icon.getBounds());
        cs = Math.sqrt(Math.pow(Math.abs(vx),2) + Math.pow(Math.abs(vy),2));
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
        String tempIconPath = thrusting ? new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png" 
        : new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; 
        
        ImageIcon tempImage = new ImageIcon(tempIconPath);
        this.icon.setIcon(tempImage);
        icon.setBounds((int)x,(int)y,psize,psize);
    }

    public void antiOOB(){  //prevents ship from leaving the screen
        if(x+psize/2 >= Main.boardxs){ //OOB right wall
            x = psize/2;
        }
        if(x < -psize/2){ //OOB left wall
            x = Main.boardxs - psize/2;
        }
        if(y+psize/2 > Main.boardys){ //OOB bottom wall
            y = -psize/2;
        }
        if(y<-psize/2){ //OOB top wall
            y = Main.boardys-psize/2;
        }
    }

    public void dmg(int dmg){
        if(dmg>=hp){ //prevents hp from going negative
            hp = 0;
            return;
        }
        hp-=dmg;
    }

    public void reset(){
        hp = maxhp;
    }
}
