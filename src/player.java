import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class player {
    final int psize = 40; //player size as a square

    boolean isDummy; 
    //if it's true it's just gonna be a ship
    //that mimics the real ship's rotation on the main screen.


    int totdeg = 0; //Keeps track of ship's rotation
    int deg = 0;
    int maxhp = 100; //self explanatory
    int hp;
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
    double cs = 0.0; //current speed of the ship
    

    JLabel icon;
    ImageIcon imageL, imageB;
    String iconPathL, iconPathB;

    
    public player(int hp, int dps, int ma, int mt, boolean isDummy){
        this.mt = mt;
        this.maxhp = hp;
        this.hp = this.maxhp;
        this.dps = dps;
        this.ma = ma;
        /*
        i couldn't find a way to easily rotate a jlabel imageicon
        so please don't judge it too harshly
        */
        String degstr = Integer.toString(deg); //Manages icon img paths
        String iconPathL = new File("").getAbsolutePath()+ "/assets/playerl/playerl" + degstr + ".png"; //imageicon shenanigans
        String iconPathB = new File("").getAbsolutePath()+ "/assets/playerb/playerb" + degstr + ".png";

        ImageIcon imageL = new ImageIcon(iconPathL);
        ImageIcon imageB = new ImageIcon(iconPathB);
        this.isDummy = isDummy;
        if(this.isDummy){
            x = 0; 
            y = Main.boardys-2*psize;
        }
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
        double cs2x =Double.valueOf(vx); 
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
