import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.geom.Area;

public class player {
    final int psize = 40; //player size as a square

    boolean isDummy; 
    //if it's true it's just gonna be a ship
    //that mimics the real ship's rotation on the main screen.


    int totdeg = 0; //Keeps track of ship's true rotation, just makes stuff smooth for slow rotations
    int deg = 0; //effective rotation used for sprites, movements, etc.
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
        so i went the nuclear route
        */
        degstr = Integer.toString(deg); //Manages icon img paths
        iconPathL = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        iconPathB = new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png";
        //changes icon for each angle thats an increment of 5
        imageL = new ImageIcon(iconPathL);
        imageB = new ImageIcon(iconPathB);
        this.isDummy = isDummy;
        if(this.isDummy){
            //positions the display on the bottom left
            x = 0; 
            y = Main.boardys-2*psize;
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
        antiOOB(); //anti-out of bounds
        if(this.thrusting){ //Accelerates the ship in the chosen direction
            this.vx-=this.ma*Math.sin(Math.toRadians(deg));
            this.vy-=this.ma*Math.cos(Math.toRadians(deg));
        }
        this.x+=this.vx; //updates x and y positions
        this.y+=this.vy;
        
        //isTurning = 1: counter-clockwise, =2: clockwise, 0: nothing, basically "rotational thrust"
        if(this.isTurning==1){ //updates ship's rotation
            this.av -= this.mt; 
        }else if(this.isTurning ==2){
            this.av += this.mt;
        }
        rotate(av);
        this.icon.setBounds((int)x,(int)y,psize,psize); 
        hitbox = new Area(icon.getBounds()); //updates hitbox position
        cs = Math.sqrt(Math.pow(Math.abs(vx),2) + Math.pow(Math.abs(vy),2)); //updates player speed value
    }
    
    public void rotate(int deg){
        this.totdeg += deg; //deg here is angular velocity
        this.deg = this.totdeg/5; //makes the effective rotation be the closest multiple of 5 of totdeg
        this.deg*=5;

        //keeps the rotation manageable and within range 0 <= rotation < 360
        if (this.deg >= 360){
            this.deg = 0;
            this.totdeg = 0;
        }else if(this.deg < 0){
            this.deg = 355;
            this.totdeg = 355;
        }

        String degstr = Integer.toString(this.deg); //used for sprite changing and stuff
        //imageicon shenanigans, changes icon to thrusting version if its thrusting
        String tempIconPath = thrusting ? new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png" 
        : new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; 
        
        ImageIcon tempImage = new ImageIcon(tempIconPath);
        this.icon.setIcon(tempImage);
        icon.setBounds((int)x,(int)y,psize,psize);
    }

    public void antiOOB(){  //prevents ship from leaving the board
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
        //resets the values back to pre-game state
        av = 0;
        hp = maxhp;
        vx = 0;
        vy = 0;
        
        totdeg = 0;
        deg = 0;
        degstr = Integer.toString(deg);
        icon.setBounds((int)x, (int)y, psize, psize);
        hitbox = new Area(icon.getBounds());
        thrusting = false;

        if(!isDummy){ //stops the display on the bottom left from moving
            x = Main.boardxs/2 - psize/2;
            y = Main.boardys/2 - psize/2;
        }
        String tempIconPath = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + 0 + ".png"; 
        
        ImageIcon tempImage = new ImageIcon(tempIconPath);
        icon.setIcon(tempImage);
        icon.setBounds((int)x, (int)y, psize, psize);

    }
}
