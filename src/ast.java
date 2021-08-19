import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.geom.Area;
public class ast {
    static int astnum = 0;
    private int maxhp; //self explanatory
    private int x = 0;
    private int y = 0; 
    private double vx, vy;
    int mass;
    int size;

    int msp = 7; //max speed possible
    int ms; //max speed
    int deg; //direction of asteroid movement, NOT rotation

    int hp;
    int hp1 = 10;
    int hp2 = 20;
    int hp3 = 100;

    int size1 = 50;
    int size2 = 75;
    int size3 = 100;

    int dmg;
    int dmg1 = 25;
    int dmg2 = 50;
    int dmg3 = 75;


    JLabel icon;
    ImageIcon image;
    String iconPath1, iconPath2, iconPath3, iconPath3b;

    Area hitbox;

    static int numAsts = 0;

    public ast(int mass){
        astnum++;
        this.mass = mass;
        size = 0; //L and w of asteroids of different masses
        if(mass == 1){
            hp = hp1;
            size = size1;
            dmg = dmg1;
        }else if(mass == 2){
            hp = hp2;
            size = size2;
            dmg = dmg2;
        }else{
            hp = hp3;
            size = size3;
            dmg = dmg3;
        }

        //determines the side the asteroid spawns
        int sided = (int)(Math.random()*4+1);
        if(sided == 1){ //left
            this.x = 0;
            this.y = (int)(Math.random()*Main.boardys+0);
        }
        if(sided == 2){ //top
            this.y = 0;
            this.x = (int)(Math.random()*Main.boardxs+0);
        }
        if(sided == 3){ //right
            this.x = Main.boardxs - size;
            this.y = (int)(Math.random()*Main.boardys+0);
        }
        if(sided == 4){ //bottom
            this.x = (int)(Math.random()*Main.boardxs+0);
            this.y = Main.boardys - size;
        }
        ms = (int)(Math.random()*msp+3);
        deg = (int)(Math.random()*360+ 1);

        //change to launch towards player with px and py later
        vx = (ms*Math.sin(Math.toRadians(deg)));
        vy = (ms*Math.cos(Math.toRadians(deg)));
        
        iconPath1 = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid1.png";;
        iconPath2 = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid2.png";
        iconPath3 = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3.png";
        iconPath3b = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3b.png";

        if(size ==1){
            image = new ImageIcon(iconPath1);
        }
        if(size ==2){
            image = new ImageIcon(iconPath2);
        }else{
            image = new ImageIcon(iconPath3);
        }
        this.icon = new JLabel(image);
        this.icon.setBounds(x,y,size,size);
        hitbox = new Area(icon.getBounds());

    }
    public void move(){
        antiOOB();
        x-=this.vx;
        y-=this.vy;
        this.icon.setBounds(x,y,size,size); 
        hitbox = new Area(icon.getBounds());
    }

    public void antiOOB(){  //prevents asteroid from leaving the screen
        if(x+size/2 >= Main.boardxs){
            x = size/2;
        }
        if(x < -size/2){
            x = Main.boardxs - size/2;
        }
        if(y+size/2 > Main.boardys){
            y = -size/2;
        }
        if(y<-size/2){
            y = Main.boardys-size/2;
        }
    }    

}
